package com.example.booksrepositoryapp.data.source.remote.retrofit.dto.subjectsApiResponseModels

import kotlinx.serialization.Serializable

@Serializable
data class SubjectApiResponseModel(
    val key: String,
    val name: String,
    val solr_query: String,
    val subject_type: String,
    val work_count: Int,
    val works: List<Work>
)