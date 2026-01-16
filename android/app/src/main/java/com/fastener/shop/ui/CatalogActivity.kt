package com.fastener.shop.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastener.shop.adapter.ProductAdapter
import com.fastener.shop.databinding.ActivityCatalogBinding
import com.fastener.shop.viewmodel.CatalogViewModel

/**
 * Экран каталога товаров
 */
class CatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogBinding
    private val viewModel: CatalogViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    private var userId: Int = 0
    private var userLogin: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("USER_ID", 0)
        userLogin = intent.getStringExtra("USER_LOGIN") ?: ""

        setupRecyclerView()
        setupObservers()
        setupListeners()

        binding.tvWelcome.text = "Добро пожаловать, $userLogin"
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateCartCount()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(emptyList()) { product ->
            viewModel.addToCart(product)
            Toast.makeText(this, "${product.name} добавлен в корзину", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.products.observe(this) { products ->
            adapter.updateProducts(products)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }

        viewModel.cartCount.observe(this) { count ->
            binding.tvCartCount.text = "В корзине: $count"
        }
    }

    private fun setupListeners() {
        binding.btnCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("USER_LOGIN", userLogin)
            startActivity(intent)
        }

        binding.btnRefresh.setOnClickListener {
            viewModel.loadProducts()
        }
    }
}
