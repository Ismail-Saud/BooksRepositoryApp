package com.example.booksrepositoryapp.ui.checkout

import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.Address
import com.example.booksrepositoryapp.domain.model.Cart
import com.example.booksrepositoryapp.domain.repository.AddressRepository
import com.example.booksrepositoryapp.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CheckoutViewModelTest {
    private val viewModel = CheckoutViewModel(
        authRepo = LoggedOutAuthRepository(),
        addressRepo = EmptyAddressRepository(),
        cartRepo = EmptyCartRepository(),
        shippingFee = 5.0,
    )

    @Test
    fun acceptsSupportedPaymentFieldFormats() {
        assertTrue(viewModel.isValidCardNumber("4242 4242 4242 4242"))
        assertTrue(viewModel.isValidCardHolderName("Ada Lovelace"))
        assertTrue(viewModel.isValidExpiryDate("12/30"))
        assertTrue(viewModel.isValidCVV("1234"))
    }

    @Test
    fun rejectsMalformedPaymentFieldFormats() {
        assertFalse(viewModel.isValidCardNumber("4242-4242-4242-4242"))
        assertFalse(viewModel.isValidCardHolderName("A"))
        assertFalse(viewModel.isValidExpiryDate("13/30"))
        assertFalse(viewModel.isValidCVV("12"))
    }
}

private class LoggedOutAuthRepository : AuthRepository {
    override fun getCurrentUserId(): String? = null
    override suspend fun createUser(username: String, email: String, password: String) = Result.success("id")
    override suspend fun login(email: String, password: String) = Result.success("id")
    override fun logout() = Unit
}

private class EmptyAddressRepository : AddressRepository {
    override fun getAddresses(userId: String): Flow<List<Address>> = flowOf(emptyList())
    override suspend fun addAddress(userId: String, address: Address) = Unit
    override suspend fun updateAddress(userId: String, address: Address) = Unit
    override suspend fun deleteAddress(userId: String, addressId: String) = Unit
    override suspend fun deleteAllAddresses(userId: String) = Unit
    override suspend fun updateSelectedAddress(userId: String, addressId: String) = Unit
    override fun getSelectedAddress(userId: String): Flow<Address?> = flowOf(null)
    override fun getAddressCount(userId: String): Flow<Int> = flowOf(0)
}

private class EmptyCartRepository : CartRepository {
    override suspend fun insertCartItem(userId: String, cart: Cart) = Unit
    override fun getCart(userId: String?): Flow<List<Cart>> = flowOf(emptyList())
    override suspend fun updateCartItem(userId: String, bookId: String, quantity: Int) = Unit
    override suspend fun deleteCartItem(userId: String, bookId: String) = Unit
    override suspend fun clearCart(userId: String) = Unit
}
