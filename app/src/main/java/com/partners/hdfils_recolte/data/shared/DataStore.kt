package com.partners.hdfils_recolte.data.shared

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.partners.hdfils_recolte.domain.models.Client
import com.partners.hdfils_recolte.domain.models.ClientAuth
import com.partners.hdfils_recolte.domain.models.TrashClean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreData(private val context: Context) {
    private val gson: Gson = Gson()

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeData")
        val USERNAME = stringPreferencesKey("user_")
        val TRASH = stringPreferencesKey("trash_")
        val CLIENT = stringPreferencesKey("client_auth_")
        val LOGGED_IN = booleanPreferencesKey("logged_in_")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        val raw = preferences[USERNAME]
        val hasClient = !raw.isNullOrBlank() && raw != "null"
        when (val flag = preferences[LOGGED_IN]) {
            null -> hasClient
            else -> flag && hasClient
        }
    }

    val getDataClient: Flow<Client?> = context.dataStore.data.map { preferences ->
        decode(preferences[USERNAME], Client::class.java)
    }

    val getDataTrash: Flow<TrashClean?> = context.dataStore.data.map { preferences ->
        decode(preferences[TRASH], TrashClean::class.java)
    }

    val getDataClientAuth: Flow<ClientAuth?> = context.dataStore.data.map { preferences ->
        decode(preferences[CLIENT], ClientAuth::class.java)
    }

    suspend fun saveSession(client: Client, auth: ClientAuth) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME] = gson.toJson(client)
            preferences[CLIENT] = gson.toJson(auth)
            preferences[LOGGED_IN] = true
        }
    }

    suspend fun saveDataClientAuth(client: ClientAuth?) {
        context.dataStore.edit { preferences ->
            preferences[CLIENT] = gson.toJson(client)
        }
    }

    suspend fun saveDataTrash(trash: TrashClean?) {
        context.dataStore.edit { preferences ->
            preferences[TRASH] = gson.toJson(trash)
        }
    }

    suspend fun saveDataClient(client: Client?) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME] = gson.toJson(client)
        }
    }

    suspend fun setLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LOGGED_IN] = loggedIn
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
            preferences[LOGGED_IN] = false
        }
    }

    suspend fun delete() {
        clearSession()
    }

    private fun <T> decode(raw: String?, type: Class<T>): T? {
        val json = raw?.takeIf { it.isNotBlank() && it != "null" } ?: return null
        return try {
            gson.fromJson(json, type)
        } catch (error: JsonSyntaxException) {
            null
        } catch (error: Exception) {
            null
        }
    }
}
