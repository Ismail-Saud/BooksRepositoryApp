package com.example.booksrepositoryapp.domain.model

data class Category (
    val apiValue: String,
)

val categories = listOf(
    Category("fantasy"),
    Category("fiction"),
    Category("horror"),
    Category("non-fiction"),
    Category("classic"),
    Category("crime"),
    Category("sci_fi"),
    Category("drama"),
    Category("young_adult"),
    Category("history"),
)