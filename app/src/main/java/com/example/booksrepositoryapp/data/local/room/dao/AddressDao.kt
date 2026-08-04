package com.example.booksrepositoryapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.booksrepositoryapp.data.local.room.entity.AddressModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {

    @Insert
    suspend fun insertAddress(address: AddressModel)

    @Update
    suspend fun updateAddress(address: AddressModel)

    @Delete
    suspend fun deleteAddress(address: AddressModel)

    @Query("SELECT * FROM address_table")
    fun getAllAddresses(): Flow<List<AddressModel>>

    @Query("SELECT * FROM address_table WHERE userId = :userId")
    fun getAddresses(userId: Int): Flow<List<AddressModel>>

    @Query("DELETE FROM address_table")
    suspend fun deleteAll()
}