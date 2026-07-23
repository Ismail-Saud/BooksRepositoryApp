package com.example.booksrepositoryapp.data.api.models.bookDetailsResponse

data class BookDetailsResponse(
    val authors: List<Author>,
    val cover_edition: CoverEdition,
    val covers: List<Int>,
    val created: Created,
    val description: String,
    val dewey_number: List<String>,
    val excerpts: List<Excerpt>,
    val first_publish_date: String,
    val identifiers: Identifiers,
    val key: String,
    val last_modified: LastModified,
    val latest_revision: Int,
    val lc_classifications: List<String>,
    val links: List<Link>,
    val location: String,
    val revision: Int,
    val subject_people: List<String>,
    val subject_places: List<String>,
    val subject_times: List<String>,
    val subjects: List<String>,
    val title: String,
    val type: TypeXX
)