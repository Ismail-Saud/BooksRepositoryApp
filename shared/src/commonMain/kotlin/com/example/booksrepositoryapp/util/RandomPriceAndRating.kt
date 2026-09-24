package com.example.booksrepositoryapp.util

import kotlin.random.Random
import kotlin.math.round

fun generateRandomAmount(): Double {
    return round(
        Random.nextDouble(15.0, 36.0) * 100
    ) / 100
}

fun generateRandomRating(): Double {
    return round(
        Random.nextDouble(3.0, 4.99) * 100
    ) / 100
}