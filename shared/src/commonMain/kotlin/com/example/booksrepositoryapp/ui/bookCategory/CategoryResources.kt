package com.example.booksrepositoryapp.ui.bookCategory

import booksrepositoryapp.shared.generated.resources.Res
import booksrepositoryapp.shared.generated.resources.category_adventure
import booksrepositoryapp.shared.generated.resources.category_biography
import booksrepositoryapp.shared.generated.resources.category_classic
import booksrepositoryapp.shared.generated.resources.category_crime
import booksrepositoryapp.shared.generated.resources.category_drama
import booksrepositoryapp.shared.generated.resources.category_fantasy
import booksrepositoryapp.shared.generated.resources.category_fiction
import booksrepositoryapp.shared.generated.resources.category_history
import booksrepositoryapp.shared.generated.resources.category_horror
import booksrepositoryapp.shared.generated.resources.category_humor
import booksrepositoryapp.shared.generated.resources.category_juvenile
import booksrepositoryapp.shared.generated.resources.category_literature
import booksrepositoryapp.shared.generated.resources.category_non_fiction
import booksrepositoryapp.shared.generated.resources.category_politics
import booksrepositoryapp.shared.generated.resources.category_satire
import booksrepositoryapp.shared.generated.resources.category_sci_fi
import booksrepositoryapp.shared.generated.resources.category_story
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
        "non_fiction" -> Res.string.category_non_fiction
        "classic" -> Res.string.category_classic
        "crime" -> Res.string.category_crime
        "sci_fi" -> Res.string.category_sci_fi
        "drama" -> Res.string.category_drama
        "young_adult" -> Res.string.category_young_adult
        "history" -> Res.string.category_history
        "biography" -> Res.string.category_biography
        "literature" -> Res.string.category_literature
        "juvenile" -> Res.string.category_juvenile
        "story" -> Res.string.category_story
        "humor" -> Res.string.category_humor
        "politics" -> Res.string.category_politics
        "adventure" -> Res.string.category_adventure
        "satire" -> Res.string.category_satire
        else -> Res.string.category_fantasy
    }
}

fun Category.imageResource(): DrawableResource {
    return when (apiValue) {
        "fantasy" -> Res.drawable.non_fiction_bg
        "fiction" -> Res.drawable.classic_bg
        "horror" -> Res.drawable.fantasy_bg
        "non_fiction" -> Res.drawable.young_adult_bg
        "classic" -> Res.drawable.non_fiction_bg
        "crime" -> Res.drawable.classic_bg
        "sci_fi" -> Res.drawable.fantasy_bg
        "drama" -> Res.drawable.young_adult_bg
        "young_adult" -> Res.drawable.non_fiction_bg
        "history" -> Res.drawable.classic_bg
        "biography" -> Res.drawable.fantasy_bg
        "literature" -> Res.drawable.young_adult_bg
        "juvenile" -> Res.drawable.non_fiction_bg
        "story" -> Res.drawable.classic_bg
        "humor" -> Res.drawable.fantasy_bg
        "politics" -> Res.drawable.young_adult_bg
        "adventure" -> Res.drawable.non_fiction_bg
        "satire" -> Res.drawable.classic_bg
        else -> Res.drawable.fantasy_bg
    }
}