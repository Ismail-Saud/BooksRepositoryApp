package com.example.booksrepositoryapp.ui.addressScreen

sealed class AddressListEffect {
    object NavigateBack: AddressListEffect()
    data class ShowToast(val message: String): AddressListEffect()
    object ShowDeleteAllConfirmation: AddressListEffect()
    data class ShowDeleteAddressConfirmation(val addressId: String): AddressListEffect()
    object OpenAppSettings: AddressListEffect()
    data class RequestLocation(val addressId: String): AddressListEffect()
}