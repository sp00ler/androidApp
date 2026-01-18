package com.fastener.shop.network

import com.fastener.shop.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * REST API интерфейс
 */
interface ApiService {

    /**
     * Получение списка товаров
     */
    @GET("products")
    suspend fun getProducts(): Response<ProductsResponse>

    /**
     * Регистрация пользователя
     */
    @POST("auth/register")
    suspend fun register(@Body request: AuthRequest): Response<AuthResponse>

    /**
     * Авторизация пользователя
     */
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    /**
     * Оформление заказа
     */
    @POST("orders")
    suspend fun createOrder(@Body request: OrderRequest): Response<OrderResponse>

    /**
     * Проверка работоспособности сервера
     */
    @GET("health")
    suspend fun healthCheck(): Response<HealthResponse>
}

/**
 * Запрос авторизации/регистрации
 */
data class AuthRequest(
    val login: String,
    val password: String
)

/**
 * Запрос создания заказа
 */
data class OrderRequest(
    val user_id: Int,
    val items: @JvmSuppressWildcards List<OrderItemRequest>
)

data class OrderItemRequest(
    val product_id: Int,
    val qty: Int
)

/**
 * Ответ health check
 */
data class HealthResponse(
    val status: String,
    val timestamp: String
)
