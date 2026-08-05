package com.example.booksrepositoryapp.data.repository

import android.content.Context
import com.example.booksrepositoryapp.data.local.room.DatabaseInstance
import com.example.booksrepositoryapp.data.local.room.dao.AddressDao
import com.example.booksrepositoryapp.data.local.room.entity.AddressModel
import kotlinx.coroutines.flow.Flow

class AddressRepository(context: Context) {
    private val dao =  DatabaseInstance.getDatabase(context).AddressDao()

    fun getAddresses() = dao.getAllAddresses()

    suspend fun addAddress(address: AddressModel) {
        dao.insertAddress(address)
    }

    suspend fun updateAddress(address: AddressModel) {
        dao.updateAddress(address)
    }

    suspend fun deleteAddress(address: AddressModel) {
        dao.deleteAddress(address)
    }

    suspend fun deleteAllAddresses() {
        dao.deleteAll()
    }
    suspend fun updateSelectedAddress(userId: Int, addressId: Int) {
        dao.updateSelectedAddress(userId, addressId)
    }

    fun getSelectedAddress(userId: Int): Flow<AddressModel?> {
        return dao.getSelectedAddress(userId)
    }
}