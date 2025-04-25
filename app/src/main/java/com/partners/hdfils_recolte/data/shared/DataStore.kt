package com.partners.hdfils_recolte.data.shared

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
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
    }

    val getData: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USERNAME] ?: ""
        }

    fun isKeyStored(key: Preferences.Key<String>): Flow<Boolean>  =
        context.dataStore.data.map {
                preference -> preference.contains(key)
        }
    val getDataClient: Flow<Client?> = context.dataStore.data
        .map { preferences ->
            val jsonString = preferences[USERNAME] ?: ""
            gson.fromJson(jsonString, Client::class.java)
        }


    val getDataTrash: Flow<TrashClean?> = context.dataStore.data
        .map { preferences ->
            val jsonString = preferences[TRASH] ?: ""
            gson.fromJson(jsonString, TrashClean::class.java)
        }
    val getDataClientAuth: Flow<ClientAuth> = context.dataStore.data
        .map { preferences ->
            val jsonString = preferences[CLIENT] ?: ""
            gson.fromJson(jsonString, ClientAuth::class.java)
        }

    suspend fun delete() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    suspend fun saveDataClientAuth(client:ClientAuth?) {
        context.dataStore.edit { preferences ->
            val jsonString = gson.toJson(client)
            preferences[CLIENT] = jsonString
        }
    }
    suspend fun saveDataTrash(trash:TrashClean?) {
        context.dataStore.edit { preferences ->
            val jsonString = gson.toJson(trash)
            preferences[TRASH] = jsonString
        }
    }
    suspend fun saveDataClient(client:Client?) {
        context.dataStore.edit { preferences ->
            val jsonString = gson.toJson(client)
            preferences[USERNAME] = jsonString
        }
    }

}


