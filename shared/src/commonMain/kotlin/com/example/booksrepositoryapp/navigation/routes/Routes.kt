package com.example.booksrepositoryapp.navigation.routes

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
        val total: String,
    ) : Route

    @Serializable
    data object Success : Route

    @Serializable
    data object AddressList : Route

    @Serializable
    data object Account : Route
}