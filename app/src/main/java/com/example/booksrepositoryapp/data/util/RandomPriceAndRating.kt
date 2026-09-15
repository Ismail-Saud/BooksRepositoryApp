package com.example.booksrepositoryapp.data.util

import java.util.Locale
import kotlin.random.Random

fun generateRandomAmount(): Double {
    return String.format(
        Locale.US,
        "%.2f",
        Random.nextDouble(15.0, 36.0)
    ).toDouble()
}

fun generateRandomRating(): Double {
    return String.format(
        Locale.US,
        "%.2f",
        Random.nextDouble(3.0, 4.99)
    ).toDouble()
}