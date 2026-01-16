package com.fastener.shop.model

import com.google.gson.annotations.SerializedName

/**
 * Модель пользователя
 */
data class User(
    @SerializedName("id")
    val id: Int,

    @SerializedName("login")
    val login: String
)
