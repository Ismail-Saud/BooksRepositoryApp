package com.example.booksrepositoryapp.data.repository

import android.content.Context
import com.example.booksrepositoryapp.data.local.room.DatabaseInstance
import com.example.booksrepositoryapp.data.local.room.dao.AddressDao
import com.example.booksrepositoryapp.data.local.room.entity.AddressModel

class AddressRepository(context: Context) {
    private val dao =  DatabaseInstance.getDatabase(context).AddressDao()

    fun getAddresses() = dao.getAllAddresses()

    suspend fun addAddress(address: AddressModel) {
        dao.insertAddress(address)
    }

    suspend fun updateAddress(address: AddressModel) {
        dao.updateAddress(address)
    }

    suspend fun deleteAddress() {
        dao.deleteAll()
    }

    fun getAddresses(userId: Int) {
        dao.getAddresses(userId)
    }

}