package com.example.pbptugas10.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pbptugas10.firebase.FirebaseAuthManager
import com.example.pbptugas10.storage.MMKVManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val email: String = "",
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState
    
    init {
        checkLoginStatus()
    }
    
    private fun checkLoginStatus() {
        val isLoggedIn = FirebaseAuthManager.isLoggedIn()
        val email = MMKVManager.getEmail() ?: ""
        _authState.value = AuthState(isLoggedIn = isLoggedIn, email = email)
    }
    
    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)
            val result = FirebaseAuthManager.register(email, password, name)
            result.onSuccess {
                _authState.value = AuthState(
                    isLoading = false,
                    isLoggedIn = true,
                    email = email
                )
            }
            result.onFailure { exception ->
                _authState.value = AuthState(
                    isLoading = false,
                    error = exception.message ?: "Registration failed"
                )
            }
        }
    }
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)
            val result = FirebaseAuthManager.login(email, password)
            result.onSuccess {
                _authState.value = AuthState(
                    isLoading = false,
                    isLoggedIn = true,
                    email = email
                )
            }
            result.onFailure { exception ->
                _authState.value = AuthState(
                    isLoading = false,
                    error = exception.message ?: "Login failed"
                )
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)
            val result = FirebaseAuthManager.logout()
            result.onSuccess {
                _authState.value = AuthState(isLoading = false)
            }
            result.onFailure { exception ->
                _authState.value = AuthState(
                    isLoading = false,
                    error = exception.message ?: "Logout failed"
                )
            }
        }
    }
    
    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
}
