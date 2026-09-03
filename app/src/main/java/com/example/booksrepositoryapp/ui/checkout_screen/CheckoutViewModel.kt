package com.example.booksrepositoryapp.ui.checkout_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.data.firebase.firestore.AddressModelFB
import com.example.booksrepositoryapp.data.repository.AddressRepository
import com.example.booksrepositoryapp.data.repository.CartRepository
import com.example.booksrepositoryapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo = UserRepository.getInstance(application)
    private val authRepo = AuthRepository()
    private val addressRepo = AddressRepository(application)
    private val cartRepo = CartRepository(application)

    val userId = authRepo.getCurrentUserId() ?: ""
    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Loading)

    val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()

    private val _selectedAddress = MutableStateFlow<AddressModelFB?>(null)
    private val creditCardNumberRegex = Regex("^(\\d{4}\\s?){3}\\d{4}$")
    private val creditCardNameHolderRegex = Regex("^[A-Za-z ]{2,50}$")
    private val creditCardExpiryRegex = Regex("^(0[1-9]|1[0-2])/\\d{2}$")
    private val creditCardCVVRegex = Regex("^\\d{3,4}$")

    init {
        getSelectedAddress()
    }

    private fun getSelectedAddress() {
        viewModelScope.launch {
            addressRepo.getSelectedAddress(userId)
                .catch { error ->
                    _checkoutState.value = CheckoutState.Error(error.message ?: "Something went wrong")
                }
                .collectLatest { address ->
                    _selectedAddress.value = address
                    if (address == null) {
                        _checkoutState.value = CheckoutState.Idle
                    } else {
                        _checkoutState.value = CheckoutState.Success(address)
                    }
                }
        }
    }

    fun isValidCardNumber(cardNumber: String): Boolean {
        return creditCardNumberRegex.matches(cardNumber.trim())
    }

    fun isValidCardHolderName(holderName: String): Boolean {
        return creditCardNameHolderRegex.matches(holderName.trim())
    }

    fun isValidExpiryDate(expiryDate: String): Boolean {
        return creditCardExpiryRegex.matches(expiryDate.trim())
    }

    fun isValidCVV(cvv: String): Boolean {
        return creditCardCVVRegex.matches(cvv.trim())
    }

    fun clearCart() {
        viewModelScope.launch {
            try {
                cartRepo.clearCart(userId)
            } catch (e: Exception) {
                _checkoutState.value = CheckoutState.Error(e.message ?: "Unable to clear cart")
            }
        }
    }
}
