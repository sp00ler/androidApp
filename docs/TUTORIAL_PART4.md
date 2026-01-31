# 📚 Подробная инструкция - ЧАСТЬ 4: Запуск приложения

Финальная часть! Создадим ViewModels, Activities, Adapters и запустим приложение!

---

## 🎯 ЧАСТЬ 10: Создание ViewModels (Логика экранов)

ViewModel - это "мозг" экрана. Он содержит всю логику и данные.

**Аналогия**: Если Activity - это человек, то ViewModel - его мозг. Мозг думает, принимает решения, а человек просто показывает результат.

### 10.1 AuthViewModel.kt (Логика авторизации)

В папке **viewmodel** создай **AuthViewModel.kt**:

```kotlin
package com.fastener.shop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fastener.shop.model.User
import com.fastener.shop.repository.ShopRepository
import kotlinx.coroutines.launch

/**
 * ViewModel для экрана авторизации
 */
class AuthViewModel : ViewModel() {

    private val repository = ShopRepository()

    // Результат авторизации
    private val _authResult = MutableLiveData<Result<User>>()
    val authResult: LiveData<Result<User>> = _authResult

    // Индикатор загрузки
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Регистрация нового пользователя
     */
    fun register(login: String, password: String) {
        // Проверка полей
        if (login.isBlank() || password.isBlank()) {
            _authResult.value = Result.failure(Exception("Заполните все поля"))
            return
        }

        if (login.length < 3) {
            _authResult.value = Result.failure(Exception("Логин минимум 3 символа"))
            return
        }

        if (password.length < 4) {
            _authResult.value = Result.failure(Exception("Пароль минимум 4 символа"))
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            val result = repository.register(login, password)
            _isLoading.value = false
            _authResult.value = result
        }
    }

    /**
     * Вход пользователя
     */
    fun login(login: String, password: String) {
        if (login.isBlank() || password.isBlank()) {
            _authResult.value = Result.failure(Exception("Заполните все поля"))
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            val result = repository.login(login, password)
            _isLoading.value = false
            _authResult.value = result
        }
    }
}
```

**Что делает ViewModel?**
1. Проверяет данные (не пустые ли поля?)
2. Показывает крутилку загрузки
3. Отправляет запрос на сервер
4. Возвращает результат (успех или ошибка)

---

### 10.2 CatalogViewModel.kt (Логика каталога)

```kotlin
package com.fastener.shop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fastener.shop.model.Product
import com.fastener.shop.repository.CartRepository
import com.fastener.shop.repository.ShopRepository
import kotlinx.coroutines.launch

/**
 * ViewModel для экрана каталога
 */
class CatalogViewModel : ViewModel() {

    private val shopRepository = ShopRepository()
    private val cartRepository = CartRepository

    // Список товаров
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    // Количество товаров в корзине
    private val _cartItemCount = MutableLiveData<Int>()
    val cartItemCount: LiveData<Int> = _cartItemCount

    // Индикатор загрузки
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Ошибка
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadProducts()
        updateCartCount()
    }

    /**
     * Загрузить товары с сервера
     */
    fun loadProducts() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = shopRepository.getProducts()

            _isLoading.value = false

            result.onSuccess { productList ->
                _products.value = productList
                _error.value = null
            }

            result.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }

    /**
     * Добавить товар в корзину
     */
    fun addToCart(product: Product) {
        cartRepository.addToCart(product)
        updateCartCount()
    }

    /**
     * Обновить счётчик корзины
     */
    fun updateCartCount() {
        _cartItemCount.value = cartRepository.getItemCount()
    }
}
```

---

### 10.3 CartViewModel.kt (Логика корзины)

```kotlin
package com.fastener.shop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fastener.shop.model.CartItem
import com.fastener.shop.model.OrderInfo
import com.fastener.shop.repository.CartRepository
import com.fastener.shop.repository.ShopRepository
import kotlinx.coroutines.launch

/**
 * ViewModel для экрана корзины
 */
class CartViewModel : ViewModel() {

    private val shopRepository = ShopRepository()
    private val cartRepository = CartRepository

    // Товары в корзине
    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    // Сумма выбранных товаров
    private val _selectedTotal = MutableLiveData<Double>()
    val selectedTotal: LiveData<Double> = _selectedTotal

    // Общая сумма
    private val _totalAmount = MutableLiveData<Double>()
    val totalAmount: LiveData<Double> = _totalAmount

    // Результат заказа
    private val _orderResult = MutableLiveData<Result<OrderInfo>>()
    val orderResult: LiveData<Result<OrderInfo>> = _orderResult

    // Индикатор загрузки
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        updateCart()
    }

    /**
     * Обновить данные корзины
     */
    fun updateCart() {
        _cartItems.value = cartRepository.getCartItems()
        _selectedTotal.value = cartRepository.getSelectedTotal()
        _totalAmount.value = cartRepository.getTotalAmount()
    }

    /**
     * Увеличить количество товара
     */
    fun increaseQuantity(productId: Int) {
        val item = cartRepository.getCartItems().find { it.product.id == productId }
        item?.let {
            cartRepository.updateQuantity(productId, it.quantity + 1)
            updateCart()
        }
    }

    /**
     * Уменьшить количество товара
     */
    fun decreaseQuantity(productId: Int) {
        val item = cartRepository.getCartItems().find { it.product.id == productId }
        item?.let {
            if (it.quantity > 1) {
                cartRepository.updateQuantity(productId, it.quantity - 1)
                updateCart()
            }
        }
    }

    /**
     * Переключить выбор товара (галочка)
     */
    fun toggleSelection(productId: Int) {
        cartRepository.toggleSelection(productId)
        updateCart()
    }

    /**
     * Удалить товар из корзины
     */
    fun removeItem(productId: Int) {
        cartRepository.removeFromCart(productId)
        updateCart()
    }

    /**
     * Очистить корзину
     */
    fun clearCart() {
        cartRepository.clearCart()
        updateCart()
    }

    /**
     * Оформить выбранные товары
     */
    fun checkoutSelected(userId: Int) {
        val selectedItems = cartRepository.getCartItems().filter { it.isSelected }

        if (selectedItems.isEmpty()) {
            _orderResult.value = Result.failure(Exception("Выберите товары"))
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            val result = shopRepository.createOrder(userId, selectedItems)
            _isLoading.value = false

            if (result.isSuccess) {
                // Удаляем оформленные товары
                cartRepository.removeSelectedItems()
                updateCart()
            }

            _orderResult.value = result
        }
    }

    /**
     * Получить количество выбранных товаров
     */
    fun getSelectedCount(): Int {
        return cartRepository.getSelectedCount()
    }
}
```

---

## 🎨 ЧАСТЬ 11: Создание Adapters (Для списков)

Adapter - это "переводчик" между данными и RecyclerView.

**Аналогия**: У тебя есть коробка игрушек (данные) и полка (RecyclerView). Adapter берёт каждую игрушку из коробки и ставит на полку.

### 11.1 ProductAdapter.kt (Адаптер для каталога)

В папке **adapter** создай **ProductAdapter.kt**:

```kotlin
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
        val btnAddToCart: Button = view.findViewById(R.id.btnAddToCart)
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
        holder.stockText.text = "Остаток: ${product.stockQty} шт."

        // Установка изображения
        val imageResId = when (product.imageKey) {
            "anker_10x100" -> R.drawable.anker_10x100
            "bolt_m8x40" -> R.drawable.bolt_m8x40
            "samorez_4x16" -> R.drawable.samorez_4x16
            else -> R.drawable.ic_product_placeholder
        }
        holder.imageView.setImageResource(imageResId)

        // Обработчик кнопки "В корзину"
        holder.btnAddToCart.setOnClickListener {
            onAddToCart(product)
        }
    }

    override fun getItemCount(): Int = products.size

    /**
     * Обновить список товаров
     */
    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
```

---

### 11.2 CartAdapter.kt (Адаптер для корзины)

```kotlin
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
     * Обновить список товаров в корзине
     */
    fun updateCartItems(newCartItems: List<CartItem>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }
}
```

---

## 🚀 ЧАСТЬ 12: Создание Activities (Экраны приложения)

Activities - это сами экраны приложения.

### 12.1 AuthActivity.kt (Экран авторизации)

В папке **ui** создай **AuthActivity.kt**:

```kotlin
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
 * Экран авторизации
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
        // Наблюдаем за результатом авторизации
        viewModel.authResult.observe(this) { result ->
            result.onSuccess { user ->
                Toast.makeText(this, "Добро пожаловать, ${user.login}!", Toast.LENGTH_SHORT).show()

                // Переходим в каталог
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

        // Наблюдаем за индикатором загрузки
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
            binding.btnRegister.isEnabled = !isLoading
        }
    }

    private fun setupListeners() {
        // Кнопка "Войти"
        binding.btnLogin.setOnClickListener {
            val login = binding.etLogin.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(login, password)
        }

        // Кнопка "Регистрация"
        binding.btnRegister.setOnClickListener {
            val login = binding.etLogin.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.register(login, password)
        }
    }
}
```

---

### 12.2 CatalogActivity.kt (Экран каталога)

```kotlin
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("USER_ID", 0)

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateCartCount()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(
            products = emptyList(),
            onAddToCart = { product ->
                viewModel.addToCart(product)
                Toast.makeText(this, "${product.name} добавлен в корзину", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        // Наблюдаем за списком товаров
        viewModel.products.observe(this) { products ->
            adapter.updateProducts(products)
        }

        // Наблюдаем за счётчиком корзины
        viewModel.cartItemCount.observe(this) { count ->
            binding.tvCartCount.text = "В корзине: $count"
        }

        // Наблюдаем за индикатором загрузки
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        // Наблюдаем за ошибками
        viewModel.error.observe(this) { error ->
            if (error != null) {
                binding.tvError.text = error
                binding.tvError.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.tvError.visibility = View.GONE
            }
        }
    }

    private fun setupListeners() {
        // Кнопка "Обновить"
        binding.btnRefresh.setOnClickListener {
            viewModel.loadProducts()
        }

        // Кнопка "Корзина"
        binding.btnGoToCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
    }
}
```

---

### 12.3 CartActivity.kt (Экран корзины)

```kotlin
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
                Toast.makeText(this, "Товар удалён", Toast.LENGTH_SHORT).show()
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
                    .setTitle("Заказ оформлен!")
                    .setMessage("Заказ №${order.id}\nСумма: ${order.total} ₽")
                    .setPositiveButton("OK", null)
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
                .setTitle("Очистить корзину?")
                .setMessage("Все товары будут удалены")
                .setPositiveButton("Да") { _, _ ->
                    viewModel.clearCart()
                    Toast.makeText(this, "Корзина очищена", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Отмена", null)
                .show()
        }

        binding.btnCheckout.setOnClickListener {
            if (viewModel.getSelectedCount() == 0) {
                Toast.makeText(this, "Выберите товары", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Оформить заказ?")
                .setMessage("Сумма: ${String.format("%.2f", viewModel.selectedTotal.value ?: 0.0)} ₽")
                .setPositiveButton("Да") { _, _ ->
                    viewModel.checkoutSelected(userId)
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }
}
```

---

## ⚙️ ЧАСТЬ 13: Настройка AndroidManifest.xml

Теперь нужно зарегистрировать все экраны в главном файле.

Открой **app/src/main/AndroidManifest.xml** и замени содержимое на:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.fastener.shop">

    <!-- Разрешение для интернета -->
    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Material3.Light">

        <!-- Экран авторизации (стартовый) -->
        <activity
            android:name=".ui.AuthActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Экран каталога -->
        <activity
            android:name=".ui.CatalogActivity"
            android:exported="false" />

        <!-- Экран корзины -->
        <activity
            android:name=".ui.CartActivity"
            android:exported="false" />

    </application>

</manifest>
```

---

## 🎉 ЧАСТЬ 14: ЗАПУСК ПРИЛОЖЕНИЯ!

Всё готово! Запускаем приложение!

### Шаг 1: Убедись что сервер запущен

В окне командной строки должен быть запущен Flask сервер:

```
🚀 Сервер запускается на http://0.0.0.0:5000
 * Running on http://127.0.0.1:5000
```

### Шаг 2: Создай эмулятор (если ещё нет)

В Android Studio:
1. **Tools → Device Manager**
2. **Create Device**
3. Выбери любое устройство (например, **Pixel 6**)
4. Выбери **system image** (например, **API 30** или **API 33**)
5. Нажми **Finish**

### Шаг 3: Запусти эмулятор

В Device Manager нажми **▶️** (Play) рядом с твоим устройством.

Эмулятор запустится через 30-60 секунд.

### Шаг 4: Запусти приложение

В Android Studio нажми зелёную кнопку **▶️ Run** (или Shift+F10).

Приложение установится на эмулятор и запустится!

---

## 🧪 ЧАСТЬ 15: Тестирование

### Тест 1: Регистрация

1. Открой приложение
2. Введи логин: `test`
3. Введи пароль: `1234`
4. Нажми **"Регистрация"**

**Ожидаемый результат**: Появится сообщение "Добро пожаловать, test!" и откроется каталог.

---

### Тест 2: Просмотр каталога

1. Должны увидеть 3 товара (если ты добавил их в БД через pgAdmin)
2. У каждого товара есть картинка, название, цена, остаток
3. Кнопка "В корзину"

**Если товаров нет**: Вернись в pgAdmin и добавь товары (смотри предыдущие инструкции).

---

### Тест 3: Добавление в корзину

1. Нажми **"В корзину"** на любом товаре
2. Должно появиться сообщение "Товар добавлен в корзину"
3. Счётчик сверху изменится: "В корзине: 1"

---

### Тест 4: Открытие корзины

1. Нажми кнопку **"Корзина"** (вверху справа)
2. Откроется экран корзины
3. Увидишь добавленный товар с галочкой

---

### Тест 5: Изменение количества

1. Нажми **"+"** возле товара
2. Количество увеличится
3. "Итого выбранного" пересчитается
4. Нажми **"-"** - количество уменьшится

---

### Тест 6: Оформление заказа

1. Убедись что галочка стоит на товаре
2. Нажми **"Оформить выбранное"**
3. Подтверди в диалоге
4. Должно появиться: "Заказ оформлён! Заказ №1, Сумма: ..."
5. Оформленный товар исчезнет из корзины

---

## 🎊 ПОЗДРАВЛЯЮ!

Ты создал полноценное мобильное приложение с:
- ✅ Авторизацией и регистрацией
- ✅ Каталогом товаров
- ✅ Корзиной с выбором товаров
- ✅ Оформлением заказов
- ✅ Клиент-серверной архитектурой
- ✅ Базой данных PostgreSQL

---

## 🐛 Частые проблемы

### Приложение падает при запуске

**Проверь**:
1. Все файлы созданы?
2. Нет ошибок компиляции в Android Studio?
3. Запущен ли сервер?

**Решение**: Посмотри Logcat в Android Studio (View → Tool Windows → Logcat) и найди ошибку.

---

### "Сервер недоступен"

**Проверь**:
1. Сервер запущен?
2. В RetrofitClient.kt адрес `10.0.2.2:5000`?
3. В .env правильный пароль от БД?

---

### Товары не отображаются

**Проверь**:
1. Открой http://localhost:5000/products в браузере
2. Должен увидеть JSON с товарами
3. Если пусто - добавь товары через pgAdmin

---

## 🚀 Что дальше?

Можешь улучшить приложение:

1. **Добавить реальные фотографии товаров**
   - Замени XML файлы в `drawable/` на JPG/PNG

2. **Добавить больше товаров**
   - Через pgAdmin добавь в таблицу `products`

3. **Изменить дизайн**
   - Измени цвета в `colors.xml`
   - Измени layouts

4. **Добавить новые функции**
   - История заказов
   - Поиск товаров
   - Профиль пользователя

---

**Молодец! Ты создал настоящее приложение!** 🎉
