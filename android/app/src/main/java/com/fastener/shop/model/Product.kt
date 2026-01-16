package com.fastener.shop.model

import com.google.gson.annotations.SerializedName

/**
 * Модель товара
 */
data class Product(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("stock_qty")
    val stockQty: Int,

    @SerializedName("image_key")
    val imageKey: String?
)
