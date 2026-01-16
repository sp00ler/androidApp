package com.fastener.shop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fastener.shop.model.CartItem
import com.fastener.shop.model.OrderInfo
import com.fastener.shop.repository.CartRepository
import com.fastener.shop.repository.ShopRepository
import kotlinx.coroutines.launch

/**
 * ViewModel для экрана корзины
 */
class CartViewModel : ViewModel() {

    private val shopRepository = ShopRepository()
    private val cartRepository = CartRepository

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _selectedTotal = MutableLiveData<Double>()
    val selectedTotal: LiveData<Double> = _selectedTotal

    private val _totalAmount = MutableLiveData<Double>()
    val totalAmount: LiveData<Double> = _totalAmount

    private val _orderResult = MutableLiveData<Result<OrderInfo>>()
    val orderResult: LiveData<Result<OrderInfo>> = _orderResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        updateCart()
    }

    /**
     * Обновление данных корзины
     */
    fun updateCart() {
        _cartItems.value = cartRepository.getCartItems()
        _selectedTotal.value = cartRepository.getSelectedTotal()
        _totalAmount.value = cartRepository.getTotalAmount()
    }

    /**
     * Увеличение количества товара
     */
    fun increaseQuantity(productId: Int) {
        val item = cartRepository.getCartItems().find { it.product.id == productId }
        item?.let {
            cartRepository.updateQuantity(productId, it.quantity + 1)
            updateCart()
        }
    }

    /**
     * Уменьшение количества товара
     */
    fun decreaseQuantity(productId: Int) {
        val item = cartRepository.getCartItems().find { it.product.id == productId }
        item?.let {
            if (it.quantity > 1) {
                cartRepository.updateQuantity(productId, it.quantity - 1)
                updateCart()
            }
        }
    }

    /**
     * Переключение выбора товара
     */
    fun toggleSelection(productId: Int) {
        cartRepository.toggleSelection(productId)
        updateCart()
    }

    /**
     * Удаление товара из корзины
     */
    fun removeItem(productId: Int) {
        cartRepository.removeFromCart(productId)
        updateCart()
    }

    /**
     * Очистка корзины
     */
    fun clearCart() {
        cartRepository.clearCart()
        updateCart()
    }

    /**
     * Оформление выбранных товаров
     */
    fun checkoutSelected(userId: Int) {
        val selectedItems = cartRepository.getCartItems().filter { it.isSelected }

        if (selectedItems.isEmpty()) {
            _orderResult.value = Result.failure(Exception("Не выбраны товары для оформления"))
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            val result = shopRepository.createOrder(userId, selectedItems)
            _isLoading.value = false

            if (result.isSuccess) {
                // Удаляем оформленные товары из корзины
                cartRepository.removeSelectedItems()
                updateCart()
            }

            _orderResult.value = result
        }
    }

    /**
     * Получение количества выбранных товаров
     */
    fun getSelectedCount(): Int {
        return cartRepository.getSelectedCount()
    }
}
