package com.example.booksrepositoryapp.localization

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LanguagePreferences(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val LANGUAGE_KEY = stringPreferencesKey("app_language")
    }

    val language: Flow<String?> = dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY]
    }

    suspend fun saveLanguage(language: AppLanguage) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language.code
        }
    }
}