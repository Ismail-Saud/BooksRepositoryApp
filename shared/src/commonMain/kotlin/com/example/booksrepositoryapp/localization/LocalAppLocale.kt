package com.example.booksrepositoryapp.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection

var customAppLocale by mutableStateOf<String?>(null)
    private set

fun changeAppLanguage(language: String) {
    customAppLocale = language
}

expect object LocalAppLocale {
    val current: String
        @Composable get

    @Composable
    infix fun provides(value: String?): ProvidedValue<*>
}

@Composable
fun AppEnvironment(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAppLocale provides customAppLocale
    ) {
        key(customAppLocale) {
            val language = customAppLocale
                ?: LocalAppLocale.current

            val direction = if (
                language.startsWith("ur", ignoreCase = true)
            ) {
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }

            CompositionLocalProvider(
                LocalLayoutDirection provides direction
            ) {
                content()
            }
        }
    }
}