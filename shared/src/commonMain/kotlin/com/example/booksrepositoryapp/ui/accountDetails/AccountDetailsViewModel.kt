package com.example.booksrepositoryapp.ui.accountDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.Address
import com.example.booksrepositoryapp.domain.model.User
import com.example.booksrepositoryapp.domain.repository.AddressRepository
import com.example.booksrepositoryapp.domain.repository.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AccountDetailsViewModel (
    private val userRepo: UserRepository,
    private val authRepo: AuthRepository,
    addressRepo: AddressRepository
) : ViewModel() {

    val id = authRepo.getCurrentUserId() ?: ""
    private val _userState = MutableStateFlow<AccountDetailsState>(AccountDetailsState.Idle)
    val userState: StateFlow<AccountDetailsState> = _userState
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _effect = Channel<AccountDetailsEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: AccountDetailsEvent) {
        viewModelScope.launch {
            when (event) {
                AccountDetailsEvent.LoadUser -> getUser()
                AccountDetailsEvent.LogoutClicked -> {
                    logout()
                    _effect.send(AccountDetailsEffect.NavigateToLandingPage)
                }
                is AccountDetailsEvent.ProfilePictureSelected -> {
                    saveUserProfilePicture(event.imageUri)
                }
                AccountDetailsEvent.RemoveProfilePictureClicked -> {
                    removeUserProfilePicture()
                }
                AccountDetailsEvent.CameraPermissionDeniedPermanent -> {
                    _effect.send(AccountDetailsEffect.OpenAppSettings)
                }
            }
        }
    }

    fun getUser() {
        val uid = authRepo.getCurrentUserId()
        if (uid == null) {
            _userState.value = AccountDetailsState.Error("User is not logged in")
            return
        }
        viewModelScope.launch {
            _userState.value = AccountDetailsState.Loading
            try {
                val user = userRepo.getUserProfile(uid)
                if (user != null) {
                    _user.value = user
                    _userState.value = AccountDetailsState.Success(user)
                } else {
                    _userState.value = AccountDetailsState.Error("User profile not found")
                }
            } catch (e: Exception) {
                _userState.value = AccountDetailsState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    val selectedAddress: Flow<Address?> = addressRepo.getSelectedAddress(id)
        .catch { emit(null) }

    fun logout() {
        authRepo.logout()
    }

    fun saveUserProfilePicture(imageUri: String) {
        val uid = authRepo.getCurrentUserId() ?: return
        viewModelScope.launch {
            try {
                val oldFileName = _user.value?.profilePicture
                userRepo.saveProfilePicture(uid = uid, imageUri = imageUri, oldFileName = oldFileName)
                getUser()
            } catch (e: Exception) {
                _userState.value = AccountDetailsState.Error(e.message ?: "Failed to save profile picture")
            }
        }
    }

    fun removeUserProfilePicture() {
        val uid = authRepo.getCurrentUserId() ?: return
        viewModelScope.launch {
            try {
                _user.value?.profilePicture?.let { fileName ->
                    userRepo.deleteLocalProfilePicture(fileName)
                }
                userRepo.updateProfilePicture(uid = uid, profilePicture = null)
                getUser()
            } catch (e: Exception) {
                _userState.value = AccountDetailsState.Error(e.message ?: "Failed to remove profile picture")
            }
        }
    }
}
