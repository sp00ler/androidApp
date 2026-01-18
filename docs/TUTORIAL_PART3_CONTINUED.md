# 📚 Подробная инструкция - ЧАСТЬ 3: Android приложение (ПРОДОЛЖЕНИЕ)

Продолжаем создание layouts и кода!

---

### 6.3 Экран каталога (activity_catalog.xml)

Создай файл **activity_catalog.xml**:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="@color/grey_background">

    <!-- Шапка -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="16dp"
        android:background="@color/colorPrimary"
        android:gravity="center_vertical">

        <TextView
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="@string/catalog"
            android:textSize="24sp"
            android:textStyle="bold"
            android:textColor="@color/white" />

        <Button
            android:id="@+id/btnRefresh"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/refresh"
            style="@style/Widget.Material3.Button.TextButton" />

        <Button
            android:id="@+id/btnGoToCart"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/cart"
            style="@style/Widget.Material3.Button.TextButton" />
    </LinearLayout>

    <!-- Счётчик корзины -->
    <TextView
        android:id="@+id/tvCartCount"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="В корзине: 0"
        android:textSize="16sp"
        android:padding="12dp"
        android:background="@color/grey_light" />

    <!-- Список товаров -->
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:padding="8dp" />

    <!-- Индикатор загрузки -->
    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:visibility="gone" />

    <!-- Сообщение об ошибке -->
    <TextView
        android:id="@+id/tvError"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Ошибка загрузки товаров"
        android:textSize="16sp"
        android:gravity="center"
        android:padding="24dp"
        android:visibility="gone" />

</LinearLayout>
```

**Что здесь?**
- **Шапка** с кнопками "Обновить" и "Корзина"
- **Счётчик** показывает сколько товаров в корзине
- **RecyclerView** - список товаров
- **ProgressBar** - показывается пока загружаются товары
- **tvError** - показывается если ошибка

---

### 6.4 Экран корзины (activity_cart.xml)

Создай файл **activity_cart.xml**:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="@color/grey_background">

    <!-- Шапка -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="16dp"
        android:background="@color/colorPrimary"
        android:gravity="center_vertical">

        <Button
            android:id="@+id/btnBack"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/back"
            style="@style/Widget.Material3.Button.TextButton" />

        <TextView
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="@string/cart"
            android:textSize="24sp"
            android:textStyle="bold"
            android:textColor="@color/white"
            android:gravity="center" />

        <Button
            android:id="@+id/btnClear"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/clear_cart"
            style="@style/Widget.Material3.Button.TextButton" />
    </LinearLayout>

    <!-- Список товаров в корзине -->
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:padding="8dp" />

    <!-- Сообщение "Корзина пуста" -->
    <TextView
        android:id="@+id/tvEmptyCart"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:text="@string/empty_cart"
        android:textSize="18sp"
        android:gravity="center"
        android:visibility="gone" />

    <!-- Итоги и кнопка оформления -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp"
        android:background="@color/white">

        <!-- Итого выбранного -->
        <TextView
            android:id="@+id/tvSelectedTotal"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Итого выбранного: 0.00 ₽"
            android:textSize="18sp"
            android:textStyle="bold"
            android:textColor="@color/colorPrimary" />

        <!-- Итого в корзине -->
        <TextView
            android:id="@+id/tvTotalAmount"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Итого в корзине: 0.00 ₽"
            android:textSize="16sp"
            android:layout_marginTop="8dp" />

        <!-- Кнопка оформления -->
        <Button
            android:id="@+id/btnCheckout"
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:text="@string/checkout"
            android:textSize="16sp"
            android:layout_marginTop="16dp" />

        <!-- Индикатор загрузки -->
        <ProgressBar
            android:id="@+id/progressBar"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:layout_marginTop="16dp"
            android:visibility="gone" />
    </LinearLayout>

</LinearLayout>
```

---

## 💻 ЧАСТЬ 7: Создание моделей данных

Модели - это классы, которые описывают объекты (Товар, Пользователь, Корзина).

### 7.1 Product.kt (Товар)

Правый клик на папку **model** → New → Kotlin Class/File → назови **Product**

```kotlin
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
```

**Простыми словами**: Это шаблон для товара. У каждого товара есть номер, название, цена, количество и картинка.

---

### 7.2 User.kt (Пользователь)

```kotlin
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
```

---

### 7.3 CartItem.kt (Товар в корзине)

```kotlin
package com.fastener.shop.model

/**
 * Товар в корзине
 * Это обычный товар + количество + галочка "выбран"
 */
data class CartItem(
    val product: Product,           // Сам товар
    var quantity: Int = 1,          // Сколько штук
    var isSelected: Boolean = true  // Выбран ли (галочка)
) {
    /**
     * Считает общую цену за этот товар
     * (цена × количество)
     */
    fun getTotalPrice(): Double {
        return product.price * quantity
    }
}
```

**Аналогия**: Представь что ты в магазине с корзиной. CartItem - это товар В корзине. У него есть количество (сколько штук ты взял) и галочка (будешь ли покупать).

---

### 7.4 ApiResponse.kt (Ответы от сервера)

```kotlin
package com.fastener.shop.model

import com.google.gson.annotations.SerializedName

/**
 * Ответ от сервера со списком товаров
 */
data class ProductsResponse(
    @SerializedName("ok")
    val ok: Boolean,

    @SerializedName("products")
    val products: List<Product>,

    @SerializedName("error")
    val error: String? = null
)

/**
 * Ответ при авторизации
 */
data class AuthResponse(
    @SerializedName("ok")
    val ok: Boolean,

    @SerializedName("user")
    val user: User? = null,

    @SerializedName("token")
    val token: String? = null,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("message")
    val message: String? = null
)

/**
 * Ответ при создании заказа
 */
data class OrderResponse(
    @SerializedName("ok")
    val ok: Boolean,

    @SerializedName("order")
    val order: OrderInfo? = null,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("message")
    val message: String? = null
)

/**
 * Информация о заказе
 */
data class OrderInfo(
    @SerializedName("id")
    val id: Int,

    @SerializedName("total")
    val total: String,

    @SerializedName("created_at")
    val createdAt: String
)
```

**Простыми словами**: Эти классы описывают, какие ответы мы получаем от сервера.

---

## 🌐 ЧАСТЬ 8: Создание кода для общения с сервером

### 8.1 ApiService.kt (API методы)

В папке **network** создай **ApiService.kt**:

```kotlin
package com.fastener.shop.network

import com.fastener.shop.model.AuthResponse
import com.fastener.shop.model.OrderResponse
import com.fastener.shop.model.ProductsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * API сервис - все запросы к серверу
 */
interface ApiService {

    /**
     * Получить список товаров
     * GET /products
     */
    @GET("products")
    suspend fun getProducts(): ProductsResponse

    /**
     * Регистрация пользователя
     * POST /auth/register
     */
    @POST("auth/register")
    suspend fun register(@Body body: Map<String, String>): AuthResponse

    /**
     * Вход пользователя
     * POST /auth/login
     */
    @POST("auth/login")
    suspend fun login(@Body body: Map<String, String>): AuthResponse

    /**
     * Создать заказ
     * POST /orders
     */
    @POST("orders")
    suspend fun createOrder(@Body body: Map<String, Any>): OrderResponse
}
```

**Что это?** Это контракт с сервером - список всех запросов, которые мы можем отправить.

`suspend` = асинхронная функция (не блокирует приложение)

---

### 8.2 RetrofitClient.kt (HTTP клиент)

```kotlin
package com.fastener.shop.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Клиент для общения с сервером
 */
object RetrofitClient {

    // ВАЖНО! Для эмулятора используем 10.0.2.2 вместо localhost
    private const val BASE_URL = "http://10.0.2.2:5000/"

    // Логирование запросов (для отладки)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // HTTP клиент
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Retrofit - библиотека для работы с API
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // API сервис
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
```

**ВАЖНО**: `10.0.2.2` - специальный адрес для эмулятора. Это означает "localhost на моём компьютере".

---

## 📦 ЧАСТЬ 9: Создание Repository (Хранилищ данных)

Repository - это слой между сервером и UI. Он упрощает получение данных.

### 9.1 ShopRepository.kt

В папке **repository** создай **ShopRepository.kt**:

```kotlin
package com.fastener.shop.repository

import com.fastener.shop.model.CartItem
import com.fastener.shop.model.OrderInfo
import com.fastener.shop.model.Product
import com.fastener.shop.model.User
import com.fastener.shop.network.RetrofitClient

/**
 * Репозиторий магазина
 * Отвечает за общение с сервером
 */
class ShopRepository {

    private val api = RetrofitClient.apiService

    /**
     * Регистрация пользователя
     */
    suspend fun register(login: String, password: String): Result<User> {
        return try {
            val response = api.register(mapOf(
                "login" to login,
                "password" to password
            ))

            if (response.ok && response.user != null) {
                Result.success(response.user)
            } else {
                Result.failure(Exception(response.error ?: "Ошибка регистрации"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сервер недоступен: ${e.message}"))
        }
    }

    /**
     * Вход пользователя
     */
    suspend fun login(login: String, password: String): Result<User> {
        return try {
            val response = api.login(mapOf(
                "login" to login,
                "password" to password
            ))

            if (response.ok && response.user != null) {
                Result.success(response.user)
            } else {
                Result.failure(Exception(response.error ?: "Ошибка входа"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сервер недоступен: ${e.message}"))
        }
    }

    /**
     * Получить список товаров
     */
    suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = api.getProducts()

            if (response.ok) {
                Result.success(response.products)
            } else {
                Result.failure(Exception(response.error ?: "Ошибка загрузки"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сервер недоступен: ${e.message}"))
        }
    }

    /**
     * Создать заказ
     */
    suspend fun createOrder(userId: Int, cartItems: List<CartItem>): Result<OrderInfo> {
        return try {
            val items = cartItems.map { cartItem ->
                mapOf(
                    "product_id" to cartItem.product.id,
                    "qty" to cartItem.quantity
                )
            }

            val response = api.createOrder(mapOf(
                "user_id" to userId,
                "items" to items
            ))

            if (response.ok && response.order != null) {
                Result.success(response.order)
            } else {
                Result.failure(Exception(response.error ?: "Ошибка заказа"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Сервер недоступен: ${e.message}"))
        }
    }
}
```

---

### 9.2 CartRepository.kt (Корзина)

```kotlin
package com.fastener.shop.repository

import com.fastener.shop.model.CartItem
import com.fastener.shop.model.Product

/**
 * Репозиторий корзины
 * Хранит товары в памяти приложения
 */
object CartRepository {

    // Список товаров в корзине
    private val cartItems = mutableListOf<CartItem>()

    /**
     * Получить все товары в корзине
     */
    fun getCartItems(): List<CartItem> {
        return cartItems.toList()
    }

    /**
     * Добавить товар в корзину
     */
    fun addToCart(product: Product, quantity: Int = 1) {
        // Проверяем, есть ли уже этот товар
        val existingItem = cartItems.find { it.product.id == product.id }

        if (existingItem != null) {
            // Если есть - увеличиваем количество
            existingItem.quantity += quantity
        } else {
            // Если нет - добавляем новый
            cartItems.add(CartItem(product, quantity, true))
        }
    }

    /**
     * Удалить товар из корзины
     */
    fun removeFromCart(productId: Int) {
        cartItems.removeAll { it.product.id == productId }
    }

    /**
     * Обновить количество товара
     */
    fun updateQuantity(productId: Int, quantity: Int) {
        val item = cartItems.find { it.product.id == productId }
        if (item != null && quantity > 0) {
            item.quantity = quantity
        }
    }

    /**
     * Переключить выбор товара (галочка)
     */
    fun toggleSelection(productId: Int) {
        val item = cartItems.find { it.product.id == productId }
        item?.let {
            it.isSelected = !it.isSelected
        }
    }

    /**
     * Удалить выбранные товары
     */
    fun removeSelectedItems() {
        cartItems.removeAll { it.isSelected }
    }

    /**
     * Очистить всю корзину
     */
    fun clearCart() {
        cartItems.clear()
    }

    /**
     * Получить сумму выбранных товаров
     */
    fun getSelectedTotal(): Double {
        return cartItems
            .filter { it.isSelected }
            .sumOf { it.getTotalPrice() }
    }

    /**
     * Получить общую сумму
     */
    fun getTotalAmount(): Double {
        return cartItems.sumOf { it.getTotalPrice() }
    }

    /**
     * Получить количество товаров
     */
    fun getItemCount(): Int {
        return cartItems.size
    }

    /**
     * Получить количество выбранных товаров
     */
    fun getSelectedCount(): Int {
        return cartItems.count { it.isSelected }
    }
}
```

**Простыми словами**: CartRepository - это корзина в памяти приложения. Она хранит товары пока приложение работает.

---

**Продолжение в следующем файле TUTORIAL_PART3_CONTINUED.md...**

В следующей части:
- ViewModels (логика экранов)
- Activities (сами экраны)
- Adapters (для RecyclerView)
- Запуск приложения
