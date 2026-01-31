package com.fastener.shop.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastener.shop.adapter.CartAdapter
import com.fastener.shop.databinding.ActivityCartBinding
import com.fastener.shop.viewmodel.CartViewModel

/**
 * Экран корзины
 */
class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val viewModel: CartViewModel by viewModels()
    private lateinit var adapter: CartAdapter

    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("USER_ID", 0)

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter(
            cartItems = emptyList(),
            onQuantityChange = { productId, newQuantity ->
                val currentItem = viewModel.cartItems.value?.find { it.product.id == productId }
                if (currentItem != null) {
                    if (newQuantity > currentItem.quantity) {
                        viewModel.increaseQuantity(productId)
                    } else {
                        viewModel.decreaseQuantity(productId)
                    }
                }
            },
            onToggleSelection = { productId ->
                viewModel.toggleSelection(productId)
            },
            onRemove = { productId ->
                viewModel.removeItem(productId)
                Toast.makeText(this, "Товар удален из корзины", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.cartItems.observe(this) { cartItems ->
            adapter.updateCartItems(cartItems)

            if (cartItems.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.tvEmptyCart.visibility = View.VISIBLE
                binding.btnCheckout.isEnabled = false
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.tvEmptyCart.visibility = View.GONE
                binding.btnCheckout.isEnabled = viewModel.getSelectedCount() > 0
            }
        }

        viewModel.selectedTotal.observe(this) { total ->
            binding.tvSelectedTotal.text = "Итого выбранного: ${String.format("%.2f", total)} ₽"
        }

        viewModel.totalAmount.observe(this) { total ->
            binding.tvTotalAmount.text = "Итого в корзине: ${String.format("%.2f", total)} ₽"
        }

        viewModel.orderResult.observe(this) { result ->
            result.onSuccess { order ->
                AlertDialog.Builder(this)
                    .setTitle("Заказ оформлен")
                    .setMessage("Заказ #${order.id} успешно оформлен!\n\nСумма: ${order.total} ₽")
                    .setPositiveButton("OK") { _, _ ->
                        // Можно вернуться в каталог
                    }
                    .show()
            }

            result.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnCheckout.isEnabled = !isLoading && viewModel.getSelectedCount() > 0
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnClear.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Очистить корзину")
                .setMessage("Вы уверены, что хотите очистить корзину?")
                .setPositiveButton("Да") { _, _ ->
                    viewModel.clearCart()
                    Toast.makeText(this, "Корзина очищена", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Отмена", null)
                .show()
        }

        binding.btnCheckout.setOnClickListener {
            if (viewModel.getSelectedCount() == 0) {
                Toast.makeText(this, "Выберите товары для оформления", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Оформить заказ")
                .setMessage("Оформить выбранные товары на сумму ${String.format("%.2f", viewModel.selectedTotal.value ?: 0.0)} ₽?")
                .setPositiveButton("Оформить") { _, _ ->
                    viewModel.checkoutSelected(userId)
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }
}
