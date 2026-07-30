package com.example.booksrepositoryapp.data.api.models

import com.example.booksrepositoryapp.R

data class Category (
    val title: String,
    val apiValue: String,
    val imgSrc: Int
)

val categories = listOf(
    Category("Fantasy", "fantasy", R.drawable.fantasy_bg),
    Category("Fiction","fiction", R.drawable.non_fiction_bg),
    Category("Horror","horror", R.drawable.horror_bg),
    Category("Non Fiction","non-fiction", R.drawable.non_fiction_bg),
    Category("Classic","classic", R.drawable.classic_bg),
    Category("Crime","crime", R.drawable.crime_bg),
    Category("Sci-fi","sci_fi", R.drawable.sci_fi_bg),
    Category("Drama","drama", R.drawable.drama_bg),
    Category("Young Adult","young_adult", R.drawable.young_adult_bg),
    Category("History","history", R.drawable.young_adult_bg),
)