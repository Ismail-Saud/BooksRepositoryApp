package com.example.booksrepositoryapp.ui.addressScreen

import com.example.booksrepositoryapp.data.source.local.uiModels.AddressUiModel

sealed class AddressListEvent {
    object BackClick: AddressListEvent()
    data class AddAddress(val currentCount: Int, val maxAllowed: Int): AddressListEvent()
    object DeleteAllAddresses: AddressListEvent()
    data class DeleteAddress(val addressId: String): AddressListEvent()
    data class ConfirmDeleteAddress(val addressId: String): AddressListEvent()
    object ConfirmDeleteAllAddresses: AddressListEvent()
    data class GetLocation(val address: AddressUiModel): AddressListEvent()
    data class SaveAddress(val address: AddressUiModel, val fullAddress: String): AddressListEvent()
    data class LocationReceived(val addressId: String, val latitude: Double, val longitude: Double): AddressListEvent()
}