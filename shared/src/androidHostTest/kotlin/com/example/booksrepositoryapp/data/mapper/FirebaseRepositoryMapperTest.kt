package com.example.booksrepositoryapp.data.mapper

import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.UserProfile
import com.example.booksrepositoryapp.data.source.remote.firebase.firestore.AddressModelFB
import com.example.booksrepositoryapp.data.source.remote.firebase.firestore.CartModelFB
import com.example.booksrepositoryapp.domain.model.Address
import com.example.booksrepositoryapp.domain.model.Cart
import com.example.booksrepositoryapp.domain.model.User
import kotlin.test.Test
import kotlin.test.assertEquals

class FirebaseRepositoryMapperTest {
    @Test
    fun cartRoundTripPreservesEveryRepositoryField() {
        val cart = Cart("work-1", "Dune", "Frank Herbert", 25.0, 12, "sci_fi", 2)

        assertEquals(cart, cart.toFirestore().toDomain())
        assertEquals("work-1", CartModelFB(workId = "work-1").toDomain().bookId)
    }

    @Test
    fun addressRoundTripPreservesEveryRepositoryField() {
        val address = Address(
            id = "address-1", house = "42", street = "Main", area = "Central", city = "Lahore",
            postalCode = "54000", country = "Pakistan", fullAddress = "42 Main", latitude = 31.5,
            longitude = 74.3, isSelected = true, createdAt = 1234L,
        )

        assertEquals(address, address.toFirestore().toDomain())
        assertEquals("address-1", AddressModelFB(id = "address-1").toDomain().id)
    }

    @Test
    fun userRoundTripPreservesProfilePictureAndIdentity() {
        val user = User("user-1", "reader", "reader@example.com", "profile.jpg")

        assertEquals(user, user.toProfile().toDomain())
        assertEquals("user-1", UserProfile(uid = "user-1").toDomain().id)
    }
}
