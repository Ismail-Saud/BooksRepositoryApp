package com.example.booksrepositoryapp.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.booksrepositoryapp.domain.model.Address
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

/**
 * Enable this test only when the Firebase Authentication and Firestore emulators
 * are running and the instrumented app has Firebase configuration available.
 */
@Ignore("Requires Firebase emulators on the host machine.")
@RunWith(AndroidJUnit4::class)
class FirebaseEmulatorIntegrationTest {
    private lateinit var repository: AddressRepositoryImpl

    @Before
    fun setUp() {
        FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099)
        val firestore = FirebaseFirestore.getInstance().apply {
            useEmulator("10.0.2.2", 8080)
        }
        repository = AddressRepositoryImpl(firestore)
    }

    @Test
    fun savesAndReadsAnAddress() = runTest {
        val userId = "integration-test-user"
        val address = Address(house = "42", city = "Lahore")

        repository.addAddress(userId, address)

        val addresses = withTimeout(5_000) {
            repository.getAddresses(userId).first { it.isNotEmpty() }
        }
        assertEquals("42", addresses.single().house)
        assertEquals("Lahore", addresses.single().city)
    }
}
