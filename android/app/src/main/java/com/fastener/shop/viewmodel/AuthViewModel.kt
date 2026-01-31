package com.fastener.shop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fastener.shop.model.User
import com.fastener.shop.repository.ShopRepository
import kotlinx.coroutines.launch

/**
 * ViewModel для экрана авторизации
 */
class AuthViewModel : ViewModel() {

    private val repository = ShopRepository()

    private val _authResult = MutableLiveData<Result<User>>()
    val authResult: LiveData<Result<User>> = _authResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Вход пользователя
     */
    fun login(login: String, password: String) {
        // Валидация
        if (login.isBlank() || password.isBlank()) {
            _authResult.value = Result.failure(Exception("Заполните все поля"))
            return
        }

        if (password.length < 4) {
            _authResult.value = Result.failure(Exception("Пароль должен быть не короче 4 символов"))
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            val result = repository.login(login, password)
            _authResult.value = result
            _isLoading.value = false
        }
    }

    /**
     * Регистрация пользователя
     */
    fun register(login: String, password: String) {
        // Валидация
        if (login.isBlank() || password.isBlank()) {
            _authResult.value = Result.failure(Exception("Заполните все поля"))
            return
        }

        if (login.length < 3) {
            _authResult.value = Result.failure(Exception("Логин должен быть не короче 3 символов"))
            return
        }

        if (password.length < 4) {
            _authResult.value = Result.failure(Exception("Пароль должен быть не короче 4 символов"))
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            val result = repository.register(login, password)
            _authResult.value = result
            _isLoading.value = false
        }
    }
}
