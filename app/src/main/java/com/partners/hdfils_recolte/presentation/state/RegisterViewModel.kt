package com.partners.hdfils_recolte.presentation.state

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.partners.hdfils_recolte.data.remote.ClientKtor
import com.partners.hdfils_recolte.data.remote.ResponseAPI
import com.partners.hdfils_recolte.data.remote.ResponseAPIGeneric
import com.partners.hdfils_recolte.data.shared.StoreData
import com.partners.hdfils_recolte.data.utils.EndPoint
import com.partners.hdfils_recolte.domain.models.ClientAuth
import com.partners.hdfils_recolte.domain.models.ClientCreate
import com.partners.hdfils_recolte.domain.models.addresses
import com.partners.hdfils_recolte.domain.subscription.RegisterDraft
import com.partners.hdfils_recolte.domain.subscription.validateRegister
import io.ktor.client.call.body
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val draft: RegisterDraft = RegisterDraft(),
    val resetKey: Int = 0,
    val loading: Boolean = false,
    val error: String? = null,
)

sealed interface RegisterEvent {
    data object OpenHome : RegisterEvent
    data class OpenLogin(val message: String) : RegisterEvent
}

class RegisterViewModel(app: Application) : AndroidViewModel(app) {
    private val store = StoreData(app)
    private val api = ClientKtor()
    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()
    private val _events = MutableSharedFlow<RegisterEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun update(block: RegisterDraft.() -> RegisterDraft) {
        _state.update { it.copy(draft = it.draft.block(), error = null) }
    }

    fun onNom(value: String) = update { copy(nom = value) }
    fun onPrenom(value: String) = update { copy(prenom = value) }
    fun onTelephone(value: String) = update { copy(telephone = value.filter(Char::isDigit).take(10)) }
    fun onAvenue(value: String) = update { copy(avenue = value) }
    fun onQuartier(value: String) = update { copy(quartier = value) }
    fun onNumero(value: String) = update { copy(numeroParcelle = value) }
    fun onMontant(value: String) = update { copy(montant = value.filter(Char::isDigit).take(9)) }
    fun onDevise(value: String) = update { copy(devise = value) }
    fun onTranches(count: Int) = update { copy(nombreTranches = count) }
    fun onCommune(id: Int) = update { copy(communeId = id) }

    fun submit(isConnected: Boolean) {
        val current = _state.value
        if (current.loading) return
        if (!isConnected) {
            _state.update { it.copy(error = "Vous n'êtes pas connecté. Vérifiez votre connexion.") }
            return
        }
        val error = validateRegister(current.draft)
        if (error != null) {
            _state.update { it.copy(error = error) }
            return
        }
        val draft = current.draft
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                val created = ClientCreate(
                    nom = draft.nom.trim(),
                    prenom = draft.prenom.trim(),
                    telephone = draft.telephone.trim(),
                    commune_id = draft.communeId,
                    avenue = draft.avenue.trim(),
                    quartier = draft.quartier.trim(),
                    numero_parcelle = draft.numeroParcelle.trim(),
                    montant_souscription = draft.montant.toInt(),
                    devise = draft.devise,
                    nombre_tranches = draft.nombreTranches,
                )
                val response = api.postData(EndPoint.clientCreate, created)
                when (val status = response.status.value) {
                    in 200..299 -> {
                        val client = runCatching { response.body<ResponseAPI>().client }.getOrNull()
                        if (client != null && (client.id != 0 || client.telephone.isNotBlank())) {
                            val avenue = client.addresses().firstOrNull()?.avenue?.ifBlank { null }
                                ?: draft.avenue.trim()
                            store.saveSession(
                                client = client,
                                auth = ClientAuth(
                                    telephone = client.telephone.ifBlank { draft.telephone.trim() },
                                    avenue = avenue,
                                ),
                            )
                            _state.update { it.copy(loading = false) }
                            _events.emit(RegisterEvent.OpenHome)
                        } else {
                            _state.update { RegisterUiState(resetKey = current.resetKey + 1) }
                            _events.emit(
                                RegisterEvent.OpenLogin(
                                    "Compte créé. Connectez-vous avec votre téléphone et votre avenue.",
                                ),
                            )
                        }
                    }
                    in 500..599 -> {
                        _state.update { it.copy(loading = false, error = "Erreur serveur. Réessayez plus tard.") }
                    }
                    in 400..499 -> {
                        val message = runCatching { response.body<ResponseAPIGeneric>().message }
                            .getOrDefault("Création refusée.")
                        _state.update { it.copy(loading = false, error = message) }
                    }
                    else -> {
                        _state.update { it.copy(loading = false, error = "Réponse inattendue ($status).") }
                    }
                }
            } catch (error: Exception) {
                Log.e("REGISTER", error.message ?: "register")
                _state.update {
                    it.copy(loading = false, error = "Enregistrement impossible. Réessayez.")
                }
            }
        }
    }
}
