package com.example.booksrepositoryapp.ui.landingPage

import booksrepositoryapp.shared.generated.resources.Res
import booksrepositoryapp.shared.generated.resources.app_logo
import booksrepositoryapp.shared.generated.resources.get_started
import booksrepositoryapp.shared.generated.resources.ic_launcher_foreground
import booksrepositoryapp.shared.generated.resources.landing_bg
import booksrepositoryapp.shared.generated.resources.landing_description
import booksrepositoryapp.shared.generated.resources.register
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

object LandingPageResources {
    val landingBg: DrawableResource get() = Res.drawable.landing_bg
    val appLogoImage: DrawableResource get() = Res.drawable.ic_launcher_foreground
    val appLogoTitle: StringResource get() = Res.string.app_logo
    val landingDescription: StringResource get() = Res.string.landing_description
    val getStarted: StringResource get() = Res.string.get_started
    val register: StringResource get() = Res.string.register
}
