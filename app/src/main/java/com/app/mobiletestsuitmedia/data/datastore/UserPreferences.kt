package com.app.mobiletestsuitmedia.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveUser(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME] = name
        }
    }

    suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = username
        }
    }

    fun getSaveUser(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[NAME] ?: ""
        }
    }

    fun getSaveUsername():Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USERNAME] ?: ""
        }
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val NAME = stringPreferencesKey("name")
        private val USERNAME = stringPreferencesKey("username")

        fun getInstance(context: Context): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val dataStore = context.datastore
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}