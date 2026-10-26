package com.example.booksrepositoryapp.util

import kotlin.test.Test
import kotlin.test.assertTrue

class RandomPriceAndRatingTest {
    @Test
    fun generatedAmountsStayWithinSupportedRange() {
        repeat(100) {
            val amount = generateRandomAmount()
            assertTrue(amount >= 15.0, "amount=$amount")
            assertTrue(amount <= 36.0, "amount=$amount")
        }
    }

    @Test
    fun generatedRatingsStayWithinSupportedRange() {
        repeat(100) {
            val rating = generateRandomRating()
            assertTrue(rating >= 3.0, "rating=$rating")
            assertTrue(rating <= 4.99, "rating=$rating")
        }
    }
}
