package com.example.booksrepositoryapp.localization

enum class AppLanguage(
    val code: String,
    val displayName: String
) {
    ENGLISH("en", "English"),
    URDU("ur", "اردو");

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return entries.firstOrNull { it.code == code }
                ?: ENGLISH
        }
    }
}