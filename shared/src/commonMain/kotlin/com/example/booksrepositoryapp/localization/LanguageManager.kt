package com.example.booksrepositoryapp.localization

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LanguageManager(
    private val preferences: LanguagePreferences
) {
    private val scope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main)

    suspend fun loadSavedLanguage(): AppLanguage? {
        return preferences.language.first()?.let {
            AppLanguage.fromCode(it)
        }
    }

    fun saveLanguage(language: AppLanguage) {
        scope.launch {
            preferences.saveLanguage(language)
        }
    }
}