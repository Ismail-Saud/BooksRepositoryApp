package com.example.booksrepositoryapp.ui.auth

import com.example.booksrepositoryapp.data.source.remote.firebase.authentication.AuthRepository
import com.example.booksrepositoryapp.domain.model.User
import com.example.booksrepositoryapp.domain.repository.UserRepository
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedEffect
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedState
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedViewModel
import com.example.booksrepositoryapp.ui.auth.register.RegisterEffect
import com.example.booksrepositoryapp.ui.auth.register.RegisterState
import com.example.booksrepositoryapp.ui.auth.register.RegisterViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthenticationValidationTest {
    @Test
    fun loginRejectsAnInvalidEmailBeforeCallingAuthentication() = runTest {
        val auth = FakeAuthRepository()
        val viewModel = GetStartedViewModel(auth)

        viewModel.login("not-an-email", "password")

        assertEquals(GetStartedState.Error("Enter valid email"), viewModel.getStartedState.value)
        assertEquals(GetStartedEffect.ShowMessage("Enter valid email"), viewModel.effect.first())
        assertEquals(0, auth.loginCalls)
    }

    @Test
    fun loginRejectsShortPasswordsBeforeCallingAuthentication() = runTest {
        val auth = FakeAuthRepository()
        val viewModel = GetStartedViewModel(auth)

        viewModel.login("reader@example.com", "12345")

        assertEquals(GetStartedState.Error("Password must be at least 6 characters"), viewModel.getStartedState.value)
        assertEquals(0, auth.loginCalls)
    }

    @Test
    fun registrationRejectsPasswordsThatDoNotMatchBeforeCallingRepositories() = runTest {
        val auth = FakeAuthRepository()
        val users = FakeUserRepository()
        val viewModel = RegisterViewModel(users, auth)

        viewModel.register("reader", "reader@example.com", "password1", "password2")

        assertEquals(RegisterState.Error("Passwords do not match"), viewModel.registerUser.value)
        assertEquals(RegisterEffect.ShowMessage("Passwords do not match"), viewModel.effect.first())
        assertEquals(0, auth.createCalls)
        assertEquals(0, users.createCalls)
    }

    @Test
    fun registrationRejectsAUsernameWithUnsupportedCharacters() = runTest {
        val auth = FakeAuthRepository()
        val viewModel = RegisterViewModel(FakeUserRepository(), auth)

        viewModel.register("reader name", "reader@example.com", "password1", "password1")

        assertEquals(RegisterState.Error("Enter valid username"), viewModel.registerUser.value)
        assertEquals(0, auth.createCalls)
    }
}

private class FakeAuthRepository : AuthRepository {
    var loginCalls = 0
    var createCalls = 0

    override fun getCurrentUserId(): String? = null
    override suspend fun createUser(username: String, email: String, password: String): Result<String> {
        createCalls++
        return Result.success("id")
    }
    override suspend fun login(email: String, password: String): Result<String> {
        loginCalls++
        return Result.success("id")
    }
    override fun logout() = Unit
}

private class FakeUserRepository : UserRepository {
    var createCalls = 0
    override suspend fun createUserProfile(user: User) { createCalls++ }
    override suspend fun getUserProfile(uid: String): User? = null
    override suspend fun updateProfilePicture(uid: String, profilePicture: String?) = Unit
    override suspend fun saveProfilePicture(uid: String, imageUri: String, oldFileName: String?): String = imageUri
    override suspend fun deleteLocalProfilePicture(fileName: String) = Unit
}
