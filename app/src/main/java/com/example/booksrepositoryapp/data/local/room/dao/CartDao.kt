package com.example.booksrepositoryapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.booksrepositoryapp.data.local.room.entity.CartModel
import com.example.booksrepositoryapp.data.local.uiModels.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert
    suspend fun insert (cart: CartModel)

    @Delete
    suspend fun delete (cart: CartModel)

    @Update
    suspend fun update (cart: CartModel)

    @Query("""
    SELECT 
        c.cartId AS cartId,
        c.workId AS bookId,
        c.id AS userId,
        b.title AS title,
        b.category AS category,
        b.author AS author,
        b.price AS price,
        b.coverId AS coverId,
        c.quantity AS quantity
    FROM cart c
    INNER JOIN book_details b ON c.workId = b.workId
    WHERE c.id = :userId
""")
    fun getCart(userId: Int): Flow<List<CartItem>>

    @Query("""
        SELECT * FROM cart 
        WHERE id = :userId
        AND workId = :bookId
        LIMIT 1
    """)
    suspend fun getCartEntity (userId: Int, bookId: String): CartModel?
}