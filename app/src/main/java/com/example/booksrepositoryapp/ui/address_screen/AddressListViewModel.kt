package com.example.booksrepositoryapp.ui.address_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.local.room.entity.AddressModel
import com.example.booksrepositoryapp.data.repository.AddressRepository
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AddressListViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepo = UserRepository.getInstance(application)
    private val addressRepo = AddressRepository(application)

    val userId = userRepo.getSavedUser()?.toInt() ?: 1

    val addresses: Flow<List<AddressModel>> = addressRepo.getAddresses().map { addresses ->
            addresses.filter {
                it.userId == userId
            }
        }

    fun addAddress(address: AddressModel) {
        viewModelScope.launch {
            addressRepo.addAddress(address)
        }
    }

    fun updateAddress(address: AddressModel) {
        viewModelScope.launch {
            addressRepo.updateAddress(address)
        }
    }

    fun deleteAddress(address: AddressModel) {
        viewModelScope.launch {
            addressRepo.deleteAddress(address)
        }
    }

    fun deleteAllAddresses() {
        viewModelScope.launch {
            addressRepo.deleteAllAddresses()
        }
    }

    fun updateSelectedAddress(addressId: Int) {
        viewModelScope.launch {
            addressRepo.updateSelectedAddress(userId, addressId)
        }
    }
}