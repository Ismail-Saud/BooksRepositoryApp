package com.example.booksrepositoryapp.domain.model

data class Category (
    val title: String,
    val apiValue: String,
    val imageName: String
)

val categories = listOf(
    Category("Fantasy", "fantasy", "fantasy_bg"),
    Category("Fiction","fiction", "non_fiction_bg"),
    Category("Horror","horror", "horror_bg"),
    Category("Non Fiction","non-fiction", "non_fiction_bg"),
    Category("Classic","classic", "classic_bg"),
    Category("Crime","crime", "crime_bg"),
    Category("Sci-fi","sci_fi", "sci_fi_bg"),
    Category("Drama","drama", "drama_bg"),
    Category("Young Adult","young_adult", "young_adult_bg"),
    Category("History","history", "young_adult_bg"),
)