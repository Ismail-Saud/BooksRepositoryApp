package com.example.booksrepositoryapp.ui.bookCategory

import booksrepositoryapp.shared.generated.resources.Res
import booksrepositoryapp.shared.generated.resources.category_classic
import booksrepositoryapp.shared.generated.resources.category_crime
import booksrepositoryapp.shared.generated.resources.category_drama
import booksrepositoryapp.shared.generated.resources.category_fantasy
import booksrepositoryapp.shared.generated.resources.category_fiction
import booksrepositoryapp.shared.generated.resources.category_history
import booksrepositoryapp.shared.generated.resources.category_horror
import booksrepositoryapp.shared.generated.resources.category_non_fiction
import booksrepositoryapp.shared.generated.resources.category_sci_fi
import booksrepositoryapp.shared.generated.resources.category_young_adult
import booksrepositoryapp.shared.generated.resources.classic_bg
import booksrepositoryapp.shared.generated.resources.crime_bg
import booksrepositoryapp.shared.generated.resources.drama_bg
import booksrepositoryapp.shared.generated.resources.fantasy_bg
import booksrepositoryapp.shared.generated.resources.horror_bg
import booksrepositoryapp.shared.generated.resources.non_fiction_bg
import booksrepositoryapp.shared.generated.resources.sci_fi_bg
import booksrepositoryapp.shared.generated.resources.young_adult_bg
import com.example.booksrepositoryapp.domain.model.Category
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource


fun Category.titleResource(): StringResource {
    return when (apiValue) {
        "fantasy" -> Res.string.category_fantasy
        "fiction" -> Res.string.category_fiction
        "horror" -> Res.string.category_horror
        "non-fiction" -> Res.string.category_non_fiction
        "classic" -> Res.string.category_classic
        "crime" -> Res.string.category_crime
        "sci_fi" -> Res.string.category_sci_fi
        "drama" -> Res.string.category_drama
        "young_adult" -> Res.string.category_young_adult
        "history" -> Res.string.category_history
        else -> Res.string.category_fantasy
    }
}

fun Category.imageResource(): DrawableResource {
    return when (apiValue) {
        "fantasy" -> Res.drawable.fantasy_bg
        "fiction" -> Res.drawable.fantasy_bg
        "horror" -> Res.drawable.horror_bg
        "non_fiction" -> Res.drawable.non_fiction_bg
        "classic" -> Res.drawable.classic_bg
        "crime" -> Res.drawable.crime_bg
        "sci_fi" -> Res.drawable.sci_fi_bg
        "drama" -> Res.drawable.drama_bg
        "young_adult" -> Res.drawable.young_adult_bg
        "history" -> Res.drawable.classic_bg
        else -> Res.drawable.fantasy_bg
    }
}