package com.example.booksrepositoryapp.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual object LocalAppLocale {
    private val localeState = staticCompositionLocalOf {
        NSLocale.currentLocale.languageCode ?: "en"
    }

    actual val current: String
        @Composable get() = localeState.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        return localeState.provides(value ?: localeState.current)
    }
}
