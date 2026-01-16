package com.fastener.shop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.fastener.shop.R
import com.fastener.shop.model.CartItem

/**
 * Адаптер для списка товаров в корзине
 */
class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onQuantityChange: (Int, Int) -> Unit,
    private val onToggleSelection: (Int) -> Unit,
    private val onRemove: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkbox: CheckBox = view.findViewById(R.id.checkboxSelect)
        val imageView: ImageView = view.findViewById(R.id.cartItemImage)
        val nameText: TextView = view.findViewById(R.id.cartItemName)
        val priceText: TextView = view.findViewById(R.id.cartItemPrice)
        val quantityText: TextView = view.findViewById(R.id.tvQuantity)
        val btnMinus: Button = view.findViewById(R.id.btnMinus)
        val btnPlus: Button = view.findViewById(R.id.btnPlus)
        val totalText: TextView = view.findViewById(R.id.cartItemTotal)
        val btnRemove: ImageButton = view.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val product = cartItem.product

        holder.checkbox.isChecked = cartItem.isSelected
        holder.nameText.text = product.name
        holder.priceText.text = "${product.price} ₽"
        holder.quantityText.text = cartItem.quantity.toString()
        holder.totalText.text = "Итого: ${String.format("%.2f", cartItem.getTotalPrice())} ₽"

        // Установка изображения
        val imageResId = when (product.imageKey) {
            "anker_10x100" -> R.drawable.anker_10x100
            "bolt_m8x40" -> R.drawable.bolt_m8x40
            "samorez_4x16" -> R.drawable.samorez_4x16
            else -> R.drawable.ic_product_placeholder
        }
        holder.imageView.setImageResource(imageResId)

        // Обработчики
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (cartItem.isSelected != isChecked) {
                onToggleSelection(product.id)
            }
        }

        holder.btnPlus.setOnClickListener {
            onQuantityChange(product.id, cartItem.quantity + 1)
        }

        holder.btnMinus.setOnClickListener {
            if (cartItem.quantity > 1) {
                onQuantityChange(product.id, cartItem.quantity - 1)
            }
        }

        holder.btnRemove.setOnClickListener {
            onRemove(product.id)
        }
    }

    override fun getItemCount(): Int = cartItems.size

    /**
     * Обновление списка товаров в корзине
     */
    fun updateCartItems(newCartItems: List<CartItem>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }
}
