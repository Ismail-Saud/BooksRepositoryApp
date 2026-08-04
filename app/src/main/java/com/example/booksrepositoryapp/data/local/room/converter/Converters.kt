package com.example.booksrepositoryapp.data.local.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromList(list: List<String>): String = Gson().toJson(list)

    @TypeConverter
    fun toList(value: String): List<String> = Gson().fromJson(value, object : TypeToken<List<String>>() {}.type)
}