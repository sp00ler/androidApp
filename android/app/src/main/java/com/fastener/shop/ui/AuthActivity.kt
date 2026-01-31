package com.fastener.shop.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fastener.shop.databinding.ActivityAuthBinding
import com.fastener.shop.viewmodel.AuthViewModel

/**
 * Экран авторизации и регистрации
 */
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.authResult.observe(this) { result ->
            result.onSuccess { user ->
                Toast.makeText(this, "Добро пожаловать, ${user.login}!", Toast.LENGTH_SHORT).show()

                // Переход в каталог
                val intent = Intent(this, CatalogActivity::class.java)
                intent.putExtra("USER_ID", user.id)
                intent.putExtra("USER_LOGIN", user.login)
                startActivity(intent)
                finish()
            }

            result.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
            binding.btnRegister.isEnabled = !isLoading
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val login = binding.etLogin.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(login, password)
        }

        binding.btnRegister.setOnClickListener {
            val login = binding.etLogin.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.register(login, password)
        }
    }
}
