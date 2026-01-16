package com.fastener.shop.model

import com.google.gson.annotations.SerializedName

/**
 * Базовый ответ API
 */
data class ApiResponse<T>(
    @SerializedName("ok")
    val ok: Boolean,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: T? = null
)

/**
 * Ответ с списком товаров
 */
data class ProductsResponse(
    @SerializedName("ok")
    val ok: Boolean,

    @SerializedName("products")
    val products: List<Product>,

    @SerializedName("error")
    val error: String? = null
)

/**
 * Ответ авторизации
 */
data class AuthResponse(
    @SerializedName("ok")
    val ok: Boolean,

    @SerializedName("user")
    val user: User? = null,

    @SerializedName("token")
    val token: String? = null,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("message")
    val message: String? = null
)

/**
 * Ответ создания заказа
 */
data class OrderResponse(
    @SerializedName("ok")
    val ok: Boolean,

    @SerializedName("order")
    val order: OrderInfo? = null,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("message")
    val message: String? = null
)

data class OrderInfo(
    @SerializedName("id")
    val id: Int,

    @SerializedName("total")
    val total: String,

    @SerializedName("created_at")
    val createdAt: String
)
