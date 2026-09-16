package com.example.booksrepositoryapp.ui.addressScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.source.local.uiModels.AddressUiModel
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.Address
import com.example.booksrepositoryapp.domain.repository.AddressRepository
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressListViewModel @Inject constructor(
    private val addressRepo: AddressRepository,
    authRepo: AuthRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    val userId = authRepo.getCurrentUserId() ?: ""
    private val _isFetchingLocation = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    private val _isSaving = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    private val _effect = Channel<AddressListEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: AddressListEvent) {
        viewModelScope.launch {
            when (event) {
                is AddressListEvent.BackClick -> {
                    viewModelScope.launch {
                        _effect.send(AddressListEffect.NavigateBack)
                    }
                }
                is AddressListEvent.AddAddress -> {
                    if (event.currentCount < event.maxAllowed) {
                        addEmptyAddress()
                    } else {
                        _effect.send(AddressListEffect.ShowToast("Maximum address limit reached"))
                    }
                }
                is AddressListEvent.DeleteAddress -> {
                    _effect.send(AddressListEffect.ShowDeleteAddressConfirmation(event.addressId))
                }
                is AddressListEvent.DeleteAllAddresses -> {
                    _effect.send(AddressListEffect.ShowDeleteAllConfirmation)
                }
                is AddressListEvent.ConfirmDeleteAddress -> {
                    deleteAddress(event.addressId)
                }
                is AddressListEvent.ConfirmDeleteAllAddresses -> {
                    deleteAllAddresses()
                }
                is AddressListEvent.GetLocation -> {
                    _effect.send(AddressListEffect.RequestLocation(event.address.address.id))
                }
                is AddressListEvent.SaveAddress -> {
                    saveAddress(event.address.address, event.fullAddress)
                }
                is AddressListEvent.LocationReceived -> {
                    fetchAddressFromLocation(event.addressId, event.latitude, event.longitude)
                }
            }
        }
    }

    private fun saveAddress(address: Address, fullAddress: String) {
        viewModelScope.launch {
            setSaving(address.id, true)
            val updatedAddress = address.copy(fullAddress = fullAddress)
            addressRepo.updateAddress(userId, updatedAddress)
            setSaving(address.id, false)
            _effect.send(AddressListEffect.ShowToast("Address saved successfully"))
        }
    }

    private fun fetchAddressFromLocation(addressId: String, lat: Double, lng: Double) {
        viewModelScope.launch {
            setFetchingLocation(addressId, true)
            try {
                val geocoder = android.location.Geocoder(getApplication(appContext))
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val fullAddress = address.getAddressLine(0) ?: ""
                    val list = addressRepo.getAddresses(userId).first()
                    list.find { it.id == addressId }?.let { original ->
                        val updated = original.copy(
                            fullAddress = fullAddress,
                            latitude = lat,
                            longitude = lng
                        )
                        addressRepo.updateAddress(userId, updated)
                    }
                }
            } catch (e: Exception) {
                _effect.send(AddressListEffect.ShowToast("Failed to fetch address: ${e.message}"))
            } finally {
                setFetchingLocation(addressId, false)
            }
        }
    }

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
        _isFetchingLocation.value += (addressId to isFetching)
    }

    fun setSaving(addressId: String, isSaving: Boolean) {
        _isSaving.value += (addressId to isSaving)
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
}
