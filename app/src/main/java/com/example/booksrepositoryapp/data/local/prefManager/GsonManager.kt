package com.example.booksrepositoryapp.data.local.prefManager

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonManager {

    private val gson = Gson()
    fun <T> toJson(data: T): String {
        return gson.toJson(data)
    }
    fun <T> fromJson(json: String?, classOfT: Class<T>): T? {
        if (json.isNullOrEmpty()) return null

        return try {
            gson.fromJson(json, classOfT)
        } catch (e: Exception) {
            null
        }
    }

    internal inline fun <reified T> fromJsonList(json: String?): MutableList<T> {
        if (json.isNullOrEmpty()) return mutableListOf()

        return try {
            val type = object : TypeToken<MutableList<T>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            mutableListOf()
        }
    }
}