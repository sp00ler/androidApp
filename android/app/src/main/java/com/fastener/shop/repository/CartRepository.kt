package com.fastener.shop.repository

import com.fastener.shop.model.CartItem
import com.fastener.shop.model.Product

/**
 * Repository для управления корзиной (in-memory)
 */
object CartRepository {

    private val cartItems = mutableListOf<CartItem>()

    /**
     * Получение всех товаров в корзине
     */
    fun getCartItems(): List<CartItem> {
        return cartItems.toList()
    }

    /**
     * Добавление товара в корзину
     */
    fun addToCart(product: Product, quantity: Int = 1) {
        val existingItem = cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            cartItems.add(CartItem(product, quantity, true))
        }
    }

    /**
     * Удаление товара из корзины
     */
    fun removeFromCart(productId: Int) {
        cartItems.removeAll { it.product.id == productId }
    }

    /**
     * Обновление количества товара
     */
    fun updateQuantity(productId: Int, quantity: Int) {
        val item = cartItems.find { it.product.id == productId }
        if (item != null && quantity > 0) {
            item.quantity = quantity
        }
    }

    /**
     * Переключение выбора товара
     */
    fun toggleSelection(productId: Int) {
        val item = cartItems.find { it.product.id == productId }
        item?.let {
            it.isSelected = !it.isSelected
        }
    }

    /**
     * Удаление выбранных товаров
     */
    fun removeSelectedItems() {
        cartItems.removeAll { it.isSelected }
    }

    /**
     * Очистка корзины
     */
    fun clearCart() {
        cartItems.clear()
    }

    /**
     * Получение общей суммы выбранных товаров
     */
    fun getSelectedTotal(): Double {
        return cartItems.filter { it.isSelected }.sumOf { it.getTotalPrice() }
    }

    /**
     * Получение общей суммы всех товаров
     */
    fun getTotalAmount(): Double {
        return cartItems.sumOf { it.getTotalPrice() }
    }

    /**
     * Получение количества товаров в корзине
     */
    fun getItemCount(): Int {
        return cartItems.size
    }

    /**
     * Получение количества выбранных товаров
     */
    fun getSelectedCount(): Int {
        return cartItems.count { it.isSelected }
    }
}
