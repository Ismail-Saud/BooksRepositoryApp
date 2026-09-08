package com.example.booksrepositoryapp.ui.addressScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.AddressRepositoryImpl
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AddressListViewModel(application: Application) : AndroidViewModel(application) {
    private val addressRepo = AddressRepositoryImpl()
    private val authRepo = AuthRepository()

    val userId = authRepo.getCurrentUserId() ?: ""

    private val _isFetchingLocation = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    private val _isSaving = MutableStateFlow<Map<String, Boolean>>(emptyMap())

    val addresses: Flow<List<AddressUiModel>> = combine(
        addressRepo.getAddresses(userId),
        _isFetchingLocation,
        _isSaving
    ) { addresses, fetching, saving ->
        addresses.map { address ->
            AddressUiModel(
                address = address,
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

    fun addAddress(address: Address) {
        viewModelScope.launch {
            addressRepo.addAddress(userId, address)
        }
    }

    fun updateAddress(address: Address) {
        viewModelScope.launch {
            addressRepo.updateAddress(userId, address)
        }
    }

    fun addEmptyAddress() {
        viewModelScope.launch {
            val address = Address(
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

    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            addressRepo.deleteAddress(userId, addressId)
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
