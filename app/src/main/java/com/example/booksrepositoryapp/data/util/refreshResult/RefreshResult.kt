package com.example.booksrepositoryapp.data.util.refreshResult

sealed class RefreshResult {
    object Success : RefreshResult()
    object Offline : RefreshResult()
    data class Error(val message: String) : RefreshResult()
}