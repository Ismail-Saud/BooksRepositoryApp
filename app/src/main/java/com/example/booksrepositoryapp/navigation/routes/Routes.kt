package com.example.booksrepositoryapp.navigation.routes

sealed class Routes(val route: String) {
    object LandingPage : Routes("landing_page")
    object GetStarted : Routes("get_started")
    object Register : Routes("register")
    object BooksCategory : Routes("books")
    object BooksList : Routes("books_list/{apiValue}/{title}") {
        fun createRoute(
            apiValue: String,
            title: String
        ): String {
            return "books_list/$apiValue/$title"
        }
    }
    object BookDetails : Routes("book_details/{workId}") {
        fun createRoute(
            workId: String
        ): String {
            return "book_details/$workId"
        }
    }
    object AddToCart : Routes("add_to_cart")
    object Checkout : Routes("checkout/{total}") {
        fun createRoute(
            total: Double
        ): String {
            return "checkout/$total"
        }
    }
    object Success : Routes("success")
    object AddressList : Routes("address_list")
    object Account : Routes("account")
}