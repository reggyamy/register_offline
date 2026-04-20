package com.reggya.registeroffline.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import com.reggya.registeroffline.data.local.preferences.AuthPreferences
import com.reggya.registeroffline.domain.model.Profile
import com.reggya.registeroffline.domain.usecase.GetProfileUseCase
import com.reggya.registeroffline.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.reggya.registeroffline.domain.model.Result
import com.reggya.registeroffline.domain.usecase.IsLoggedInUseCase
import com.reggya.registeroffline.domain.usecase.LogoutUseCase
import com.reggya.registeroffline.presentation.utils.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


@HiltViewModel
class UserViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<String>>(UiState.Loading)
    val loginState: StateFlow<UiState<String>?> = _loginState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()
    private val _profile = MutableStateFlow<UiState<Profile>>(UiState.Loading)
    val profile: StateFlow<UiState<Profile>> = _profile.asStateFlow()

    init {
        isLoggedIn()
        getProfile()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email, password).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _loginState.value = UiState.Success("Login berhasil")
                    }
                    is Result.Error -> {
                        _loginState.value = UiState.Error("Login gagal: ${result.message}")
                    }
                    is Result.Loading -> {
                        _loginState.value = UiState.Loading
                    }
                }
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            getProfileUseCase().collect { result ->
                when (result) {
                    is Result.Success -> { _profile.value = UiState.Success(result.data) }
                    is Result.Error -> { _profile.value = UiState.Error(result.message ?: "Unknown error") }
                    is Result.Loading -> { _profile.value = UiState.Loading }
                }
            }

        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _profile.value = UiState.Loading
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            logoutUseCase()
        }
    }

     private fun isLoggedIn() {
        viewModelScope.launch {
            delay(500)
            isLoggedInUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _isLoggedIn.value = result.data
                    }

                    is Result.Error -> {
                        _isLoggedIn.value = false
                    }

                    is Result.Loading -> {
                        _isLoggedIn.value = null
                    }
                }
            }
        }
    }
}