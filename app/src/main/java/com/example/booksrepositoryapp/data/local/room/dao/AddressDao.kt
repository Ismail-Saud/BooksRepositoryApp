package com.example.booksrepositoryapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
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

    // Make every address of the user unselected
    @Query("UPDATE address_table SET isSelected = 0 WHERE userId = :userId")
    suspend fun clearSelection(userId: Int)

    // Select a specific address
    @Query("UPDATE address_table SET isSelected = 1 WHERE id = :addressId")
    suspend fun selectAddress(addressId: Int)

    // Update selection atomically
    @Transaction
    suspend fun updateSelectedAddress(userId: Int, addressId: Int) {
        clearSelection(userId)
        selectAddress(addressId)
    }

    @Query("""SELECT * FROM address_table WHERE userId = :userId AND isSelected = 1 LIMIT 1""")
    fun getSelectedAddress(userId: Int): Flow<AddressModel?>
}