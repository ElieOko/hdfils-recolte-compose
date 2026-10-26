package com.partners.hdfils_recolte.presentation.state

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.partners.hdfils_recolte.data.remote.ClientKtor
import com.partners.hdfils_recolte.data.remote.ResponseAPIGeneric
import com.partners.hdfils_recolte.data.remote.ResponseAPITrash
import com.partners.hdfils_recolte.data.shared.StoreData
import com.partners.hdfils_recolte.data.utils.EndPoint
import com.partners.hdfils_recolte.domain.models.Client
import com.partners.hdfils_recolte.domain.models.Trash
import com.partners.hdfils_recolte.domain.models.TrashClean
import io.ktor.client.call.body
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppDialog(
    val title: String,
    val message: String,
    val confirmLabel: String = "Ok",
    val kind: DialogKind = DialogKind.Info,
)

enum class DialogKind {
    Info,
    ConfirmSignOut,
}

data class HomeUiState(
    val client: Client? = null,
    val trash: TrashClean? = null,
    val loading: Boolean = false,
    val ready: Boolean = false,
    val dialog: AppDialog? = null,
)

sealed interface HomeEvent {
    data object SignedOut : HomeEvent
}

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    private val store = StoreData(app)
    private val api = ClientKtor()
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()
    private val _events = MutableSharedFlow<HomeEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            store.getDataClient.collect { client ->
                _state.update { it.copy(client = client, ready = true) }
            }
        }
        viewModelScope.launch {
            store.getDataTrash.collect { trash ->
                _state.update { it.copy(trash = trash) }
            }
        }
    }

    fun dismissDialog() {
        _state.update { it.copy(dialog = null) }
    }

    fun askSignOut() {
        _state.update {
            it.copy(
                dialog = AppDialog(
                    title = "Déconnexion",
                    message = "Voulez-vous vraiment vous déconnecter ?",
                    confirmLabel = "Se déconnecter",
                    kind = DialogKind.ConfirmSignOut,
                ),
            )
        }
    }

    fun onDialogConfirm() {
        when (_state.value.dialog?.kind) {
            DialogKind.ConfirmSignOut -> signOut()
            else -> dismissDialog()
        }
    }

    fun refresh(isConnected: Boolean) {
        val client = _state.value.client ?: return
        if (!ensureOnline(isConnected)) return
        launchRequest {
            val response = api.getData("${EndPoint.trashAction}/${client.id}")
            handleTrashResponse(response.status.value, response, successTitle = "Succès") { _ ->
                "Mise à jour effectuée avec succès"
            }
        }
    }

    fun requestEmpty(isConnected: Boolean) {
        val current = _state.value
        val client = current.client ?: return
        if (current.trash?.state_trash_id == 2) {
            showInfo("Information", "Votre poubelle est en cours de nettoyage.")
            return
        }
        if (!ensureOnline(isConnected)) return
        launchRequest {
            val response = api.postData(
                EndPoint.trashAction,
                Trash(client_id = client.id, state_trash_id = 3),
            )
            handleTrashResponse(response.status.value, response, successTitle = "Succès") { body ->
                body.message.ifBlank { "Demande de vidage envoyée." }
            }
        }
    }

    fun validateClean(isConnected: Boolean) {
        val current = _state.value
        val client = current.client ?: return
        if (current.trash == null) return
        if (current.trash.state_trash_id == 2) {
            showInfo(
                "Information",
                "La validation de la poubelle est faite et gérée par notre équipe.",
            )
            return
        }
        if (!ensureOnline(isConnected)) return
        launchRequest {
            val response = api.postData(
                "${EndPoint.trashClean}${client.id}",
                Trash(client_id = client.id, state_trash_id = 2),
            )
            handleTrashResponse(response.status.value, response, successTitle = "Succès") { body ->
                body.message.ifBlank { "Validation enregistrée." }
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            store.clearSession()
            _state.update { it.copy(dialog = null, client = null) }
            _events.emit(HomeEvent.SignedOut)
        }
    }

    private fun ensureOnline(isConnected: Boolean): Boolean {
        if (isConnected) return true
        showInfo(
            "Problème de connexion",
            "Vous n'êtes pas connecté. Vérifiez votre connexion.",
        )
        return false
    }

    private fun showInfo(title: String, message: String) {
        _state.update { it.copy(dialog = AppDialog(title = title, message = message)) }
    }

    private fun launchRequest(block: suspend () -> Unit) {
        if (_state.value.loading) return
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                block()
            } catch (error: Exception) {
                Log.e("HOME", error.message ?: "home")
                _state.update {
                    it.copy(
                        loading = false,
                        dialog = AppDialog(
                            title = "Erreur",
                            message = "Action impossible. Réessayez.",
                        ),
                    )
                }
            }
        }
    }

    private suspend fun handleTrashResponse(
        status: Int,
        response: io.ktor.client.statement.HttpResponse,
        successTitle: String,
        successMessage: (ResponseAPITrash) -> String,
    ) {
        when (status) {
            in 200..299 -> {
                val body = response.body<ResponseAPITrash>()
                store.saveDataTrash(
                    TrashClean(
                        id = body.data.id,
                        state_trash_id = body.data.state_trash_id,
                        client_id = body.data.client_id,
                        created_at = body.data.created_at,
                    ),
                )
                _state.update {
                    it.copy(
                        loading = false,
                        dialog = AppDialog(title = successTitle, message = successMessage(body)),
                    )
                }
            }
            in 500..599 -> {
                _state.update {
                    it.copy(
                        loading = false,
                        dialog = AppDialog(
                            title = "Erreur",
                            message = "Erreur serveur. Réessayez plus tard.",
                        ),
                    )
                }
            }
            in 400..499 -> {
                val message = runCatching { response.body<ResponseAPIGeneric>().message }
                    .getOrDefault("Action refusée.")
                _state.update {
                    it.copy(
                        loading = false,
                        dialog = AppDialog(title = "Information", message = message),
                    )
                }
            }
            else -> {
                _state.update {
                    it.copy(
                        loading = false,
                        dialog = AppDialog(title = "Erreur", message = "Réponse inattendue ($status)."),
                    )
                }
            }
        }
    }
}
