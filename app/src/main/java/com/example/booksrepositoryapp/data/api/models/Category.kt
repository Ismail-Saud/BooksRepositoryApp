package com.example.booksrepositoryapp.data.api.models

data class Category (
    val title: String,
    val apiValue: String
)

val categories = listOf(
    Category("Fantasy", "fantasy" ),
    Category("Fiction","fiction"),
    Category("Horror","horror"),
    Category("Non Fiction","non-fiction"),
    Category("Classic","classic"),
    Category("Crime","crime"),
    Category("Sci-fi","sci_fi"),
    Category("Drama","drama"),
    Category("Young Adult","young_adult"),
)