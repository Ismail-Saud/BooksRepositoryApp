package com.example.booksrepositoryapp.ui.address_screen

sealed class AddressListState {
    object Idle: AddressListState()
    object Loading: AddressListState()
    data class Error(val message: String): AddressListState()
    object Success: AddressListState()
}