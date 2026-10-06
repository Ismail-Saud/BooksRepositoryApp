package com.example.booksrepositoryapp.domain.model

data class Category (
    val apiValue: String,
)

val categories = listOf(
    Category("fantasy"),
    Category("fiction"),
    Category("horror"),
    Category("non_fiction"),
    Category("classic"),
    Category("crime"),
    Category("sci_fi"),
    Category("drama"),
    Category("young_adult"),
    Category("history"),
    Category("biography"),
    Category("literature"),
    Category("juvenile"),
    Category("story"),
    Category("humor"),
    Category("politics"),
    Category("adventure"),
    Category("satire"),
)