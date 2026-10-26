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
import com.partners.hdfils_recolte.data.utils.isValidPhoneNumber
import com.partners.hdfils_recolte.domain.models.ClientAuth
import io.ktor.client.call.body
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val telephone: String = "",
    val avenue: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val attempted: Boolean = false,
)

sealed interface AuthEvent {
    data object OpenHome : AuthEvent
}

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val store = StoreData(app)
    private val api = ClientKtor()
    private val _state = MutableStateFlow(AuthUiState())
    val state = _state.asStateFlow()
    private val _events = MutableSharedFlow<AuthEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun onTelephone(value: String) {
        _state.update {
            it.copy(telephone = value.filter(Char::isDigit).take(10), error = null)
        }
    }

    fun onAvenue(value: String) {
        _state.update { it.copy(avenue = value, error = null) }
    }

    fun submit(isConnected: Boolean) {
        val current = _state.value
        if (current.loading) return
        val error = when {
            !isValidPhoneNumber(current.telephone) ->
                "Ce numéro n'est pas valide en République démocratique du Congo"
            current.avenue.isBlank() -> "Indiquez le nom de l'avenue"
            !isConnected -> "Vous n'êtes pas connecté. Vérifiez votre connexion."
            else -> null
        }
        if (error != null) {
            _state.update { it.copy(attempted = true, error = error) }
            return
        }
        _state.update { it.copy(loading = true, attempted = true, error = null) }
        viewModelScope.launch {
            try {
                val response = api.postData(
                    EndPoint.clientAuth,
                    ClientAuth(
                        telephone = current.telephone.trim(),
                        avenue = current.avenue.trim(),
                    ),
                )
                when (val status = response.status.value) {
                    in 200..299 -> {
                        val body = response.body<ResponseAPI>()
                        val client = body.client
                        if (client == null || (client.id == 0 && client.telephone.isBlank())) {
                            _state.update {
                                it.copy(loading = false, error = "Compte introuvable pour ces informations.")
                            }
                        } else {
                            store.saveSession(
                                client = client,
                                auth = ClientAuth(current.telephone.trim(), current.avenue.trim()),
                            )
                            _state.update { it.copy(loading = false) }
                            _events.emit(AuthEvent.OpenHome)
                        }
                    }
                    in 500..599 -> {
                        _state.update {
                            it.copy(loading = false, error = "Erreur serveur. Réessayez plus tard.")
                        }
                    }
                    in 400..499 -> {
                        val message = runCatching { response.body<ResponseAPIGeneric>().message }
                            .getOrDefault("Connexion refusée.")
                        _state.update { it.copy(loading = false, error = message) }
                    }
                    else -> {
                        _state.update {
                            it.copy(loading = false, error = "Réponse inattendue ($status).")
                        }
                    }
                }
            } catch (error: Exception) {
                Log.e("AUTH", error.message ?: "auth")
                _state.update {
                    it.copy(
                        loading = false,
                        error = "Connexion impossible. Vérifiez vos informations ou le réseau.",
                    )
                }
            }
        }
    }
}
