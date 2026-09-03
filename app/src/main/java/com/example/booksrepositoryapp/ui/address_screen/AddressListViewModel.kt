package com.example.booksrepositoryapp.ui.address_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.data.firebase.firestore.AddressModelFB
import com.example.booksrepositoryapp.data.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AddressListViewModel(application: Application) : AndroidViewModel(application) {
    private val addressRepo = AddressRepository(application)
    private val authRepo = AuthRepository()

    val userId = authRepo.getCurrentUserId() ?: ""

    private val _isFetchingLocation = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    private val _isSaving = MutableStateFlow<Map<String, Boolean>>(emptyMap())

    val addresses: Flow<List<AddressModelFB>> = combine(
        addressRepo.getAddresses(userId),
        _isFetchingLocation,
        _isSaving
    ) { addresses, fetching, saving ->
        addresses.map { address ->
            address.copy(
                isFetchingLocation = fetching[address.id] ?: false,
                isSaving = saving[address.id] ?: false
            )
        }
    }

    val addressCount = addressRepo.getAddressCount(userId)

    fun setFetchingLocation(addressId: String, isFetching: Boolean) {
        _isFetchingLocation.value = _isFetchingLocation.value + (addressId to isFetching)
    }

    fun setSaving(addressId: String, isSaving: Boolean) {
        _isSaving.value = _isSaving.value + (addressId to isSaving)
    }

    fun addAddress(address: AddressModelFB) {
        viewModelScope.launch {
            addressRepo.addAddress(userId, address)
        }
    }

    fun updateAddress(address: AddressModelFB) {
        viewModelScope.launch {
            addressRepo.updateAddress(userId, address)
        }
    }

    fun addEmptyAddress() {
        viewModelScope.launch {
            val address = AddressModelFB(
                house = "",
                street = "",
                area = "",
                city = "",
                postalCode = "",
                country = "",
                fullAddress = "",
                latitude = 0.0,
                longitude = 0.0,
                isSelected = false,
                createdAt = System.currentTimeMillis()
            )
            addressRepo.addAddress(userId, address)
        }
    }

    fun deleteAddress(address: AddressModelFB) {
        viewModelScope.launch {
            addressRepo.deleteAddress(userId, address.id)
        }
    }

    fun deleteAllAddresses() {
        viewModelScope.launch {
            addressRepo.deleteAllAddresses(userId)
        }
    }

    fun updateSelectedAddress(addressId: String) {
        viewModelScope.launch {
            addressRepo.updateSelectedAddress(userId, addressId)
        }
    }
}
