package com.example.booksrepositoryapp.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.Address
import com.example.booksrepositoryapp.domain.model.Cart
import com.example.booksrepositoryapp.domain.repository.AddressRepository
import com.example.booksrepositoryapp.domain.repository.CartRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class CheckoutViewModel(
    private val authRepo: AuthRepository,
    private val addressRepo: AddressRepository,
    private val cartRepo: CartRepository,
    private val shippingFee: Double,
) : ViewModel() {

    private val userId: String
        get() = authRepo.getCurrentUserId().orEmpty()

    private val _checkoutState =
        MutableStateFlow<CheckoutState>(CheckoutState.Loading)

    val checkoutState: StateFlow<CheckoutState> =
        _checkoutState.asStateFlow()

    private val _effect = Channel<CheckoutEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val _selectedAddress = MutableStateFlow<Address?>(null)

    private val _cartTotal = MutableStateFlow(0.0)

    private var addressLoaded = false
    private var cartLoaded = false

    private val creditCardNumberRegex =
        Regex("^(\\d{4}\\s?){3}\\d{4}$")

    private val creditCardNameHolderRegex =
        Regex("^[A-Za-z ]{2,50}$")

    private val creditCardExpiryRegex =
        Regex("^(0[1-9]|1[0-2])/\\d{2}$")

    private val creditCardCVVRegex =
        Regex("^\\d{3,4}$")

    init {
        observeSelectedAddress()
        observeCart()
    }

    fun onEvent(event: CheckoutEvent) {
        when (event) {
            CheckoutEvent.BackClick -> {
                viewModelScope.launch {
                    _effect.send(CheckoutEffect.NavigateBack)
                }
            }

            CheckoutEvent.SelectedAddress -> {
                viewModelScope.launch {
                    _effect.send(
                        CheckoutEffect.NavigateToAddressList
                    )
                }
            }

            is CheckoutEvent.PayClicked -> {
                processPayment()
            }
        }
    }

    private fun observeSelectedAddress() {
        if (userId.isBlank()) {
            _checkoutState.value =
                CheckoutState.Error("User is not logged in")
            return
        }

        viewModelScope.launch {
            addressRepo.getSelectedAddress(userId)
                .catch { error ->
                    if (_checkoutState.value !is CheckoutState.ProcessingPayment) {
                        _checkoutState.value = CheckoutState.Error(
                            error.message ?: "Failed to load selected address"
                        )
                    }
                }
                .collectLatest { address ->
                    if (_checkoutState.value is CheckoutState.ProcessingPayment) {
                        return@collectLatest
                    }

                    _selectedAddress.value = address
                    addressLoaded = true
                    updateCheckoutState()
                }
        }
    }

    private fun observeCart() {
        if (userId.isBlank()) {
            _checkoutState.value =
                CheckoutState.Error("User is not logged in")
            return
        }

        viewModelScope.launch {
            cartRepo.getCart(userId)
                .catch { error ->
                    if (_checkoutState.value !is CheckoutState.ProcessingPayment) {
                        _checkoutState.value = CheckoutState.Error(
                            error.message ?: "Failed to load cart"
                        )
                    }
                }
                .collectLatest { cartItems ->
                    _cartTotal.value = calculateTotal(cartItems)
                    cartLoaded = true
                    updateCheckoutState()
                }
        }
    }

    private fun calculateTotal(cartItems: List<Cart>): Double {
        val subtotal = cartItems.sumOf { cart ->
            cart.price * cart.quantity
        }

        val applicableShippingFee = if (cartItems.isNotEmpty()) shippingFee else 0.0
        return subtotal + applicableShippingFee
    }

    private fun updateCheckoutState() {
        if (_checkoutState.value is CheckoutState.ProcessingPayment) {
            return
        }
        if (addressLoaded && cartLoaded) {
            _checkoutState.value = CheckoutState.Success(
                address = _selectedAddress.value,
                total = _cartTotal.value,
            )
        }
    }

    private fun processPayment() {
        if (_checkoutState.value !is CheckoutState.Success) return

        if (_checkoutState.value is CheckoutState.ProcessingPayment) return

        viewModelScope.launch {
            _checkoutState.value = CheckoutState.ProcessingPayment

            try {
                delay(2000.milliseconds)
                cartRepo.clearCart(userId)
                _effect.send(
                    CheckoutEffect.NavigateToSuccess
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _effect.send(
                    CheckoutEffect.ShowError(
                        e.message ?: "Payment failed"
                    )
                )
                updateCheckoutState()
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
}