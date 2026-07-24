package com.example.booksrepositoryapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.booksrepositoryapp.data.local.room.entity.UserModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert (user: UserModel)

    @Query("""SELECT EXISTS(SELECT 1 FROM userTable WHERE email = :email)""")
    suspend fun doesEmailExists (email: String) : Boolean

    @Query("""SELECT * FROM userTable WHERE email = :email AND password = :password LIMIT 1""")
    suspend fun loginUser (email: String, password: String) : UserModel?

}