package com.fastener.shop.model

/**
 * Модель товара в корзине
 */
data class CartItem(
    val product: Product,
    var quantity: Int = 1,
    var isSelected: Boolean = true
) {
    /**
     * Общая стоимость позиции
     */
    fun getTotalPrice(): Double {
        return product.price * quantity
    }
}
