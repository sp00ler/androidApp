# 📚 Подробная инструкция - ЧАСТЬ 3: Android приложение

Это самая интересная часть! Мы создадим красивое приложение для телефона.

---

## 📱 ЧАСТЬ 5: Создание Android приложения

### 5.1 Создаём новый проект в Android Studio

**Шаг 1**: Запусти Android Studio

**Шаг 2**: Нажми **"New Project"** (Новый проект)

**Шаг 3**: Выбери **"Empty Views Activity"**
- Это пустой проект с одним экраном
- Нажми "Next"

**Шаг 4**: Заполни настройки проекта:

```
Name: Fastener Shop
Package name: com.fastener.shop
Save location: FastenerShop/android
Language: Kotlin
Minimum SDK: API 24 ("Nougat"; Android 7.0)
```

**Объяснение**:
- **Name** - название приложения (будет видно на телефоне)
- **Package name** - уникальное имя пакета (как адрес дома)
- **Save location** - где сохранить проект
- **Language** - язык программирования (Kotlin - современный и удобный)
- **Minimum SDK** - на каких версиях Android будет работать

**Шаг 5**: Нажми **"Finish"**

**Шаг 6**: Подожди 3-5 минут, пока Android Studio создаст проект

---

### 5.2 Понимаем структуру проекта

После создания слева увидишь дерево файлов. Переключи вид на **"Project"** (вверху слева).

```
android/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/fastener/shop/  ← Здесь весь код на Kotlin
│   │       ├── res/                      ← Ресурсы (картинки, layouts)
│   │       │   ├── layout/              ← XML файлы экранов
│   │       │   ├── drawable/            ← Картинки
│   │       │   ├── values/              ← Цвета, строки
│   │       │   └── ...
│   │       └── AndroidManifest.xml      ← "Паспорт" приложения
│   └── build.gradle.kts                 ← Зависимости
└── ...
```

**Простыми словами**:
- **java/com/fastener/shop/** - код (логика приложения)
  - Как мозг человека
- **res/layout/** - внешний вид экранов
  - Как одежда человека
- **res/drawable/** - картинки
- **res/values/** - цвета, тексты
- **AndroidManifest.xml** - главный файл настроек
  - Как паспорт с информацией о приложении

---

### 5.3 Добавляем библиотеки

**Что делаем?** Добавляем внешние библиотеки (как устанавливать моды в игру).

Найди файл **app/build.gradle.kts** и открой его.

Найди секцию `dependencies` (в конце файла) и добавь:

```kotlin
dependencies {
    // Уже есть:
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // ДОБАВЬ ЭТИ СТРОКИ:

    // Retrofit - для общения с сервером
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // ViewModel и LiveData - для MVVM архитектуры
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")

    // Coroutines - для асинхронной работы
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // RecyclerView - для списков
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // CardView - для красивых карточек
    implementation("androidx.cardview:cardview:1.0.0")
}
```

**Что это за библиотеки?**

- **Retrofit** - "почтальон", который отправляет запросы на сервер
- **ViewModel/LiveData** - помогают организовать код красиво (архитектура MVVM)
- **Coroutines** - позволяют делать несколько дел одновременно (чтобы приложение не зависало)
- **RecyclerView** - для создания списков (каталог товаров, корзина)
- **CardView** - для красивых карточек товаров

После добавления вверху появится панель **"Sync Now"** - нажми её!

Android Studio скачает все библиотеки (это займёт 2-3 минуты).

---

### 5.4 Включаем ViewBinding

**Что это?** ViewBinding - штука, которая упрощает доступ к кнопкам, текстам и другим элементам на экране.

**Без ViewBinding** (старый способ):
```kotlin
val button = findViewById<Button>(R.id.myButton)
```

**С ViewBinding** (новый способ):
```kotlin
binding.myButton.setOnClickListener { ... }
```

В том же файле **build.gradle.kts** найди секцию `android` и добавь:

```kotlin
android {
    ...

    buildFeatures {
        viewBinding = true
    }
}
```

Снова нажми **"Sync Now"**.

---

### 5.5 Добавляем разрешение для интернета

Приложению нужно разрешение для доступа в интернет (чтобы общаться с сервером).

Открой файл **app/src/main/AndroidManifest.xml**

Добавь строку **перед** тегом `<application>`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.fastener.shop">

    <!-- ДОБАВЬ ЭТУ СТРОКУ: -->
    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        ...
```

**Сохрани** файл (Ctrl+S или Cmd+S).

---

### 5.6 Создаём структуру папок

Теперь создадим папки для кода. В Android Studio это называется **packages**.

Слева в дереве файлов найди: **app → src → main → java → com.fastener.shop**

Правой кнопкой мыши по **com.fastener.shop** → **New → Package**

Создай такие пакеты (по одному):
- `model`
- `network`
- `repository`
- `viewmodel`
- `ui`
- `adapter`

**Как создать**:
1. Правый клик на `com.fastener.shop`
2. New → Package
3. Введи название: `model`
4. Enter
5. Повтори для остальных

В итоге должна получиться такая структура:

```
com.fastener.shop
├── adapter
├── model
├── network
├── repository
├── ui
└── viewmodel
```

---

### 5.7 Создаём цвета и стили

Откроем файлы в **res/values/** и настроим цвета.

#### Файл colors.xml

Открой **app/src/main/res/values/colors.xml**:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Основные цвета приложения -->
    <color name="purple_200">#FFBB86FC</color>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    <color name="teal_200">#FF03DAC5</color>
    <color name="teal_700">#FF018786</color>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>

    <!-- Наши цвета -->
    <color name="colorPrimary">#FF2196F3</color>      <!-- Синий -->
    <color name="colorPrimaryDark">#FF1976D2</color>  <!-- Тёмно-синий -->
    <color name="colorAccent">#FFFF5722</color>       <!-- Оранжевый -->
    <color name="grey_background">#FFF5F5F5</color>   <!-- Светло-серый фон -->
    <color name="grey_light">#FFEEEEEE</color>
    <color name="text_dark">#FF212121</color>
    <color name="text_light">#FF757575</color>
</resources>
```

#### Файл strings.xml

Открой **app/src/main/res/values/strings.xml**:

```xml
<resources>
    <string name="app_name">Магазин крепежа</string>

    <!-- Авторизация -->
    <string name="login">Логин</string>
    <string name="password">Пароль</string>
    <string name="register">Регистрация</string>
    <string name="sign_in">Войти</string>

    <!-- Каталог -->
    <string name="catalog">Каталог</string>
    <string name="add_to_cart">В корзину</string>
    <string name="in_cart">В корзине:</string>
    <string name="refresh">Обновить</string>

    <!-- Корзина -->
    <string name="cart">Корзина</string>
    <string name="empty_cart">Корзина пуста</string>
    <string name="clear_cart">Очистить</string>
    <string name="checkout">Оформить выбранное</string>
    <string name="total_selected">Итого выбранного:</string>
    <string name="total_cart">Итого в корзине:</string>
    <string name="back">Назад</string>

    <!-- Общие -->
    <string name="price_format">%s ₽</string>
    <string name="stock_format">Остаток: %d шт.</string>
    <string name="error">Ошибка</string>
    <string name="ok">OK</string>
</resources>
```

---

### 5.8 Создаём изображения для товаров

Нам нужны картинки для товаров. Создадим простые цветные блоки (placeholders).

Правой кнопкой по **res/drawable** → **New → Drawable Resource File**

#### 1. anker_10x100.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#FFD700" />
    <corners android:radius="8dp" />
</shape>
```

#### 2. bolt_m8x40.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#87CEEB" />
    <corners android:radius="8dp" />
</shape>
```

#### 3. samorez_4x16.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#C0C0C0" />
    <corners android:radius="8dp" />
</shape>
```

#### 4. ic_product_placeholder.xml (по умолчанию)

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#CCCCCC" />
    <corners android:radius="8dp" />
</shape>
```

**Что мы сделали?** Создали 4 цветных квадрата (золотой для анкера, голубой для болта, серебряный для самореза).

Позже можно заменить их на настоящие фотографии!

---

## 🎨 ЧАСТЬ 6: Создание экранов (Layouts)

Layouts - это XML файлы, которые описывают внешний вид экрана.

**Аналогия**: Если приложение - это дом, то layout - это чертёж комнаты (где стоят диван, стол, телевизор).

---

### 6.1 Экран авторизации (activity_auth.xml)

Правой кнопкой по **res/layout** → **New → Layout Resource File**
- Имя: `activity_auth.xml`
- Root element: `LinearLayout`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp"
    android:gravity="center"
    android:background="@color/white">

    <!-- Заголовок -->
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Магазин крепежа"
        android:textSize="28sp"
        android:textStyle="bold"
        android:textColor="@color/colorPrimary"
        android:layout_marginBottom="40dp" />

    <!-- Поле логина -->
    <com.google.android.material.textfield.TextInputLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="16dp">

        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/etLogin"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="@string/login"
            android:inputType="text" />
    </com.google.android.material.textfield.TextInputLayout>

    <!-- Поле пароля -->
    <com.google.android.material.textfield.TextInputLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="24dp">

        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/etPassword"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="@string/password"
            android:inputType="textPassword" />
    </com.google.android.material.textfield.TextInputLayout>

    <!-- Кнопка входа -->
    <com.google.android.material.button.MaterialButton
        android:id="@+id/btnLogin"
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:text="@string/sign_in"
        android:textSize="16sp"
        android:layout_marginBottom="12dp" />

    <!-- Кнопка регистрации -->
    <com.google.android.material.button.MaterialButton
        android:id="@+id/btnRegister"
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:text="@string/register"
        android:textSize="16sp"
        style="@style/Widget.Material3.Button.OutlinedButton" />

    <!-- Индикатор загрузки -->
    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:visibility="gone" />

</LinearLayout>
```

**Что здесь?**
- `TextView` - текст "Магазин крепежа"
- `TextInputEditText` - поле для ввода логина и пароля
- `MaterialButton` - кнопки "Войти" и "Регистрация"
- `ProgressBar` - крутящийся кружок (показывается при загрузке)

---

### 6.2 Карточка товара (item_product.xml)

Это как выглядит один товар в списке.

Создай файл **item_product.xml**:

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:cardCornerRadius="12dp"
    app:cardElevation="4dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="16dp">

        <!-- Картинка товара -->
        <ImageView
            android:id="@+id/productImage"
            android:layout_width="80dp"
            android:layout_height="80dp"
            android:scaleType="centerCrop"
            android:contentDescription="Изображение товара" />

        <!-- Информация о товаре -->
        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical"
            android:layout_marginStart="16dp">

            <!-- Название -->
            <TextView
                android:id="@+id/productName"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Название товара"
                android:textSize="16sp"
                android:textStyle="bold"
                android:textColor="@color/text_dark" />

            <!-- Цена -->
            <TextView
                android:id="@+id/productPrice"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="0.00 ₽"
                android:textSize="18sp"
                android:textColor="@color/colorPrimary"
                android:textStyle="bold"
                android:layout_marginTop="8dp" />

            <!-- Остаток -->
            <TextView
                android:id="@+id/productStock"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Остаток: 0 шт."
                android:textSize="14sp"
                android:textColor="@color/text_light"
                android:layout_marginTop="4dp" />

            <!-- Кнопка "В корзину" -->
            <Button
                android:id="@+id/btnAddToCart"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/add_to_cart"
                android:layout_marginTop="8dp" />

        </LinearLayout>

    </LinearLayout>

</androidx.cardview.widget.CardView>
```

---

**Подсказка**: Инструкция получается очень большой. Рекомендую создать краткую шпаргалку в файле **QUICK_START.md** с основными командами. Хочешь?

Или продолжить с остальными layouts и кодом?

Скажи, и я продолжу! 😊
