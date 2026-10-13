package com.example.booksrepositoryapp.navigation.routes

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object LandingPage : Route

    @Serializable
    data object GetStarted : Route

    @Serializable
    data object Register : Route

    @Serializable
    data object BooksCategory : Route

    @Serializable
    data class BooksList(
        val apiValue: String,
        val title: String,
    ) : Route

    @Serializable
    data class BookDetails(
        val workId: String,
    ) : Route

    @Serializable
    data object AddToCart : Route

    @Serializable
    data class Checkout(
        val total: Double,
    ) : Route

    @Serializable
    data object Success : Route

    @Serializable
    data object AddressList : Route

    @Serializable
    data object Account : Route
}

val DoubleNavType = object : NavType<Double>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Double {
        return bundle.getDouble(key)
    }

    override fun parseValue(value: String): Double {
        return value.toDouble()
    }

    override fun serializeAsValue(value: Double): String {
        return value.toString()
    }

    override fun put(bundle: Bundle, key: String, value: Double) {
        bundle.putDouble(key, value)
    }
}