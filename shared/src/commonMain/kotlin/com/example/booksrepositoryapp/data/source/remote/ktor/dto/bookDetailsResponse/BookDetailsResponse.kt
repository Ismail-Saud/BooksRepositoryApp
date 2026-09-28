package com.example.booksrepositoryapp.data.source.remote.ktor.dto.bookDetailsResponse

import kotlinx.serialization.Serializable

@Serializable
data class BookDetailsResponse(
    val authors: List<Author>? = null,
    val cover_edition: CoverEdition? = null,
    val covers: List<Int>? = null,
    val created: Created? = null,
    @Serializable(with = DescriptionSerializer::class)
    val description: String? = null,
    val dewey_number: List<String>? = null,
    val excerpts: List<Excerpt>? = null,
    val first_publish_date: String? = null,
    val identifiers: Identifiers? = null,
    val key: String? = null,
    val last_modified: LastModified? = null,
    val latest_revision: Int? = null,
    val lc_classifications: List<String>? = null,
    val links: List<Link>? = null,
    val location: String? = null,
    val revision: Int? = null,
    val subject_people: List<String>? = null,
    val subject_places: List<String>? = null,
    val subject_times: List<String>? = null,
    val subjects: List<String>? = null,
    val title: String? = null,
    val type: TypeXX? = null
)
