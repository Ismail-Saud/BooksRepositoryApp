package com.example.booksrepositoryapp.ui.addressScreen

import com.example.booksrepositoryapp.domain.model.Address

data class AddressUiModel(
    val address: Address,
    val isFetchingLocation: Boolean = false,
    val isSaving: Boolean = false
) {
    val id: String get() = address.id
}
