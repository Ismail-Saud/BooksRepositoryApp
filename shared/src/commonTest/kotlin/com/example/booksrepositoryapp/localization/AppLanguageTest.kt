package com.example.booksrepositoryapp.localization

import kotlin.test.Test
import kotlin.test.assertEquals

class AppLanguageTest {
    @Test
    fun fromCodeReturnsMatchingLanguage() {
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromCode("en"))
        assertEquals(AppLanguage.URDU, AppLanguage.fromCode("ur"))
    }

    @Test
    fun fromCodeFallsBackToEnglishForUnknownOrMissingCode() {
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromCode(null))
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromCode("fr"))
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromCode(""))
    }
}
