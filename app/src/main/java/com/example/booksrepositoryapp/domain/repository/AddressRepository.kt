package com.example.booksrepositoryapp.domain.repository

import com.example.booksrepositoryapp.domain.model.Address
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    fun getAddresses(userId: String): Flow<List<Address>>
    suspend fun addAddress(userId: String, address: Address)
    suspend fun updateAddress(userId: String, address: Address)
    suspend fun deleteAddress(userId: String, addressId: String)
    suspend fun deleteAllAddresses(userId: String)
    suspend fun updateSelectedAddress(userId: String, addressId: String)
    fun getSelectedAddress(userId: String): Flow<Address?>
    fun getAddressCount(userId: String): Flow<Int>
}
