package com.example.booksrepositoryapp.data.source.remote.retrofit.dto.bookDetailsResponse

import kotlinx.serialization.Serializable

@Serializable
data class Identifiers(
    val wikidata: List<String>
)