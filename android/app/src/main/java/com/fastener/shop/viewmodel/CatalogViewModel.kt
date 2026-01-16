package com.fastener.shop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fastener.shop.model.Product
import com.fastener.shop.repository.CartRepository
import com.fastener.shop.repository.ShopRepository
import kotlinx.coroutines.launch

/**
 * ViewModel для экрана каталога
 */
class CatalogViewModel : ViewModel() {

    private val shopRepository = ShopRepository()
    private val cartRepository = CartRepository

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _cartCount = MutableLiveData<Int>()
    val cartCount: LiveData<Int> = _cartCount

    init {
        loadProducts()
        updateCartCount()
    }

    /**
     * Загрузка товаров
     */
    fun loadProducts() {
        _isLoading.value = true

        viewModelScope.launch {
            val result = shopRepository.getProducts()
            _isLoading.value = false

            if (result.isSuccess) {
                _products.value = result.getOrNull()
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Ошибка загрузки товаров"
            }
        }
    }

    /**
     * Добавление товара в корзину
     */
    fun addToCart(product: Product) {
        cartRepository.addToCart(product, 1)
        updateCartCount()
    }

    /**
     * Обновление счетчика корзины
     */
    fun updateCartCount() {
        _cartCount.value = cartRepository.getItemCount()
    }
}
