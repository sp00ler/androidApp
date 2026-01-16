package com.fastener.shop.repository

import com.fastener.shop.model.*
import com.fastener.shop.network.*

/**
 * Repository для работы с данными приложения
 */
class ShopRepository {

    private val apiService = RetrofitClient.apiService

    /**
     * Получение списка товаров
     */
    suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = apiService.getProducts()
            if (response.isSuccessful && response.body()?.ok == true) {
                Result.success(response.body()!!.products)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Ошибка загрузки товаров"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сервер недоступен: ${e.message}"))
        }
    }

    /**
     * Регистрация пользователя
     */
    suspend fun register(login: String, password: String): Result<User> {
        return try {
            val request = AuthRequest(login, password)
            val response = apiService.register(request)

            if (response.isSuccessful && response.body()?.ok == true) {
                val user = response.body()!!.user
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Ошибка регистрации"))
                }
            } else {
                Result.failure(Exception(response.body()?.error ?: "Ошибка регистрации"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сервер недоступен: ${e.message}"))
        }
    }

    /**
     * Авторизация пользователя
     */
    suspend fun login(login: String, password: String): Result<User> {
        return try {
            val request = AuthRequest(login, password)
            val response = apiService.login(request)

            if (response.isSuccessful && response.body()?.ok == true) {
                val user = response.body()!!.user
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Ошибка авторизации"))
                }
            } else {
                Result.failure(Exception(response.body()?.error ?: "Ошибка авторизации"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сервер недоступен: ${e.message}"))
        }
    }

    /**
     * Оформление заказа
     */
    suspend fun createOrder(userId: Int, items: List<CartItem>): Result<OrderInfo> {
        return try {
            val orderItems = items
                .filter { it.isSelected }
                .map { OrderItemRequest(it.product.id, it.quantity) }

            if (orderItems.isEmpty()) {
                return Result.failure(Exception("Не выбраны товары для оформления"))
            }

            val request = OrderRequest(userId, orderItems)
            val response = apiService.createOrder(request)

            if (response.isSuccessful && response.body()?.ok == true) {
                val order = response.body()!!.order
                if (order != null) {
                    Result.success(order)
                } else {
                    Result.failure(Exception("Ошибка оформления заказа"))
                }
            } else {
                Result.failure(Exception(response.body()?.error ?: "Ошибка оформления заказа"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сервер недоступен: ${e.message}"))
        }
    }

    /**
     * Проверка доступности сервера
     */
    suspend fun checkHealth(): Result<Boolean> {
        return try {
            val response = apiService.healthCheck()
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Сервер недоступен"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сервер недоступен: ${e.message}"))
        }
    }
}
