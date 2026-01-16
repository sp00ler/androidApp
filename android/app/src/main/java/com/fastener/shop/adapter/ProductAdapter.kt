package com.fastener.shop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fastener.shop.R
import com.fastener.shop.model.Product

/**
 * Адаптер для списка товаров в каталоге
 */
class ProductAdapter(
    private var products: List<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.productImage)
        val nameText: TextView = view.findViewById(R.id.productName)
        val priceText: TextView = view.findViewById(R.id.productPrice)
        val stockText: TextView = view.findViewById(R.id.productStock)
        val addButton: Button = view.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.nameText.text = product.name
        holder.priceText.text = "${product.price} ₽"
        holder.stockText.text = "В наличии: ${product.stockQty} шт."

        // Установка изображения
        val imageResId = when (product.imageKey) {
            "anker_10x100" -> R.drawable.anker_10x100
            "bolt_m8x40" -> R.drawable.bolt_m8x40
            "samorez_4x16" -> R.drawable.samorez_4x16
            else -> R.drawable.ic_product_placeholder
        }
        holder.imageView.setImageResource(imageResId)

        holder.addButton.setOnClickListener {
            onAddToCart(product)
        }
    }

    override fun getItemCount(): Int = products.size

    /**
     * Обновление списка товаров
     */
    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
