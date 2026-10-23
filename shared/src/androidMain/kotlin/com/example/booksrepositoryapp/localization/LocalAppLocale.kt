package com.example.booksrepositoryapp.localization

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import java.util.Locale

actual object LocalAppLocale {

    private var defaultLocale: Locale? = null

    private val localeState = staticCompositionLocalOf {
        Locale.getDefault().toLanguageTag()
    }

    actual val current: String
        @Composable get() = localeState.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val configuration = Configuration(
            LocalConfiguration.current
        )

        if (defaultLocale == null) {
            defaultLocale = Locale.getDefault()
        }

        val locale = value?.let(Locale::forLanguageTag)
            ?: defaultLocale!!

        Locale.setDefault(locale)
        configuration.setLocale(locale)

        val resources = LocalResources.current
        resources.updateConfiguration(
            configuration,
            resources.displayMetrics
        )

        return arrayOf(
            LocalConfiguration provides configuration,
            localeState provides locale.toLanguageTag()
        ).let {
            androidx.compose.runtime.CompositionLocalProvider(
                values = it,
                content = {}
            )
            localeState.provides(locale.toLanguageTag())
        }
    }
}