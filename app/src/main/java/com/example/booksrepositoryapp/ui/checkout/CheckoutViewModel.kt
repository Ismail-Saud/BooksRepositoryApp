package com.example.booksrepositoryapp.ui.checkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.repository.AddressRepositoryImpl
import com.example.booksrepositoryapp.data.repository.CartRepositoryImpl
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.Address
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepo = AuthRepository()
    private val addressRepo = AddressRepositoryImpl()
    private val cartRepo = CartRepositoryImpl()
    val userId = authRepo.getCurrentUserId() ?: ""
    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Loading)
    val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()
    private val _selectedAddress = MutableStateFlow<Address?>(null)
    private val creditCardNumberRegex = Regex("^(\\d{4}\\s?){3}\\d{4}$")
    private val creditCardNameHolderRegex = Regex("^[A-Za-z ]{2,50}$")
    private val creditCardExpiryRegex = Regex("^(0[1-9]|1[0-2])/\\d{2}$")
    private val creditCardCVVRegex = Regex("^\\d{3,4}$")
    private val _effect = Channel<CheckoutEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        getSelectedAddress()
    }

    fun onEvent (event: CheckoutEvent) {
        when (event) {
            CheckoutEvent.BackClick -> {
                viewModelScope.launch {
                    _effect.send(CheckoutEffect.NavigateBack)
                }
            }
            CheckoutEvent.SelectedAddress -> {
                viewModelScope.launch {
                    _effect.send(CheckoutEffect.NavigateToAddressList)
                }
            }
            is CheckoutEvent.PayClicked -> {
                processPayment(event.total)
            }
        }
    }

    private fun processPayment(total: Double) {
        viewModelScope.launch {
            try {
                cartRepo.clearCart(userId)
                _effect.send(CheckoutEffect.NavigateToSuccess)
            } catch (e: Exception) {
                _effect.send(CheckoutEffect.ShowError(e.message?: "Error Occurred"))
            }
        }
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
                        // Assuming CheckoutState.Success takes Address domain model
                        // We might need to update CheckoutState too
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
