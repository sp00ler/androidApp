# 📁 Полная структура проекта "Магазин крепежа"

Детальное описание всех файлов проекта с их назначением.

---

## 📂 Корневая папка

```
androidApp/
├── .git/                  # Git репозиторий
├── .gitignore            # Файлы игнорируемые git
├── README.md             # Главная документация проекта
├── cadabra               # Служебный файл
├── android/              # Android приложение
├── server/               # Flask сервер
├── database/             # SQL скрипты
└── docs/                 # Документация
```

---

## 🗄️ database/ - База данных

| Файл | Назначение |
|------|------------|
| `schema.sql` | **Схема БД** - создаёт 4 таблицы (users, products, orders, order_items) с индексами и связями |
| `seed_data.sql` | **Начальные данные** - добавляет 3 товара в БД для демонстрации |
| `init_db.sh` | **Скрипт инициализации** (Linux/macOS) - автоматически создаёт и наполняет БД |
| `init_db.bat` | **Скрипт инициализации** (Windows) - то же самое для Windows |

**Таблицы в БД**:
- `users` - пользователи (id, login, password_hash, created_at)
- `products` - товары (id, name, price, stock_qty, image_key, created_at)
- `orders` - заказы (id, user_id, created_at, total)
- `order_items` - позиции в заказах (id, order_id, product_id, qty, price_at_purchase)

---

## 🐍 server/ - Flask сервер

| Файл | Размер | Назначение |
|------|--------|------------|
| `app.py` | ~300 строк | **Главный файл сервера** - 5 REST API endpoints (health, products, register, login, orders) |
| `config.py` | ~25 строк | **Конфигурация** - читает настройки из .env (БД, Flask) |
| `database.py` | ~50 строк | **Работа с PostgreSQL** - функции подключения и выполнения SQL запросов |
| `requirements.txt` | 5 строк | **Зависимости Python** - список библиотек для установки через pip |
| `.env.example` | 12 строк | **Пример настроек** - шаблон для создания .env файла |
| `.env` | - | **Настройки** (не в git) - пароли, ключи, параметры БД |
| `run_server.sh` | ~5 строк | **Скрипт запуска** (Linux/macOS) - активирует venv и запускает сервер |
| `run_server.bat` | ~5 строк | **Скрипт запуска** (Windows) |
| `venv/` | - | **Виртуальная среда Python** (не в git) - изолированные библиотеки проекта |

### REST API Endpoints

| Метод | URL | Описание | Тело запроса | Ответ |
|-------|-----|----------|--------------|-------|
| GET | `/health` | Проверка сервера | - | `{"status": "ok"}` |
| GET | `/products` | Список товаров | - | `{"ok": true, "products": [...]}` |
| POST | `/auth/register` | Регистрация | `{"login": "...", "password": "..."}` | `{"ok": true, "user": {...}}` |
| POST | `/auth/login` | Вход | `{"login": "...", "password": "..."}` | `{"ok": true, "user": {...}, "token": "..."}` |
| POST | `/orders` | Создание заказа | `{"user_id": 1, "items": [...]}` | `{"ok": true, "order": {...}}` |

---

## 📱 android/ - Android приложение

### Конфигурационные файлы

| Файл | Назначение |
|------|------------|
| `settings.gradle.kts` | Настройки Gradle проекта |
| `build.gradle.kts` | Настройки сборки проекта |
| `gradle.properties` | Свойства Gradle (настройки JVM, AndroidX) |
| `app/build.gradle.kts` | **Зависимости приложения** - список библиотек (Retrofit, ViewModel, Coroutines и т.д.) |
| `app/proguard-rules.pro` | Правила обфускации кода для релиза |

### AndroidManifest.xml

**Файл**: `app/src/main/AndroidManifest.xml`

**Назначение**: "Паспорт" приложения - регистрирует:
- Разрешения (INTERNET)
- Экраны (AuthActivity, CatalogActivity, CartActivity)
- Стартовый экран (AuthActivity)

---

### 📦 app/src/main/java/com/fastener/shop/

#### 1. model/ - Модели данных (7 файлов)

| Файл | Класс | Назначение |
|------|-------|------------|
| `Product.kt` | `Product` | Модель товара (id, name, price, stockQty, imageKey) |
| `User.kt` | `User` | Модель пользователя (id, login) |
| `CartItem.kt` | `CartItem` | Товар в корзине (product, quantity, isSelected) + метод `getTotalPrice()` |
| `ApiResponse.kt` | `ProductsResponse` | Ответ сервера со списком товаров |
| | `AuthResponse` | Ответ при авторизации (ok, user, token, error) |
| | `OrderResponse` | Ответ при создании заказа |
| | `OrderInfo` | Информация о заказе (id, total, createdAt) |

---

#### 2. network/ - Сетевой слой (2 файла)

| Файл | Описание |
|------|----------|
| `ApiService.kt` | **API интерфейс** - описание всех HTTP запросов к серверу (getProducts, register, login, createOrder) |
| `RetrofitClient.kt` | **HTTP клиент** - настройка Retrofit, OkHttp, логирование. Адрес сервера: `http://10.0.2.2:5000/` |

**Технологии**: Retrofit 2, OkHttp, Gson

---

#### 3. repository/ - Хранилища данных (2 файла)

| Файл | Класс | Назначение |
|------|-------|------------|
| `ShopRepository.kt` | `ShopRepository` | **Работа с сервером** - методы register, login, getProducts, createOrder. Обрабатывает ошибки. |
| `CartRepository.kt` | `CartRepository` (object) | **Корзина в памяти** - хранит CartItem'ы, методы добавления/удаления/обновления товаров |

**Паттерн**: Repository (слой абстракции между UI и данными)

---

#### 4. viewmodel/ - Логика экранов (3 файла)

| Файл | Класс | Экран | Логика |
|------|-------|-------|--------|
| `AuthViewModel.kt` | `AuthViewModel` | Авторизация | Валидация полей (логин ≥3, пароль ≥4), вызов register/login, обработка результата |
| `CatalogViewModel.kt` | `CatalogViewModel` | Каталог | Загрузка товаров с сервера, добавление в корзину, обновление счётчика корзины |
| `CartViewModel.kt` | `CartViewModel` | Корзина | Управление количеством, выбор товаров (чекбоксы), расчёт сумм, оформление заказа |

**Технологии**: ViewModel, LiveData, Coroutines

---

#### 5. ui/ - Экраны приложения (3 файла)

| Файл | Класс | Описание | Размер |
|------|-------|----------|--------|
| `AuthActivity.kt` | `AuthActivity` | **Экран авторизации** - поля логин/пароль, кнопки "Войти"/"Регистрация" | ~100 строк |
| `CatalogActivity.kt` | `CatalogActivity` | **Экран каталога** - список товаров (RecyclerView), кнопки "Обновить"/"Корзина", счётчик корзины | ~120 строк |
| `CartActivity.kt` | `CartActivity` | **Экран корзины** - список с чекбоксами, кнопки +/-, удаление, очистка, оформление | ~150 строк |

**Паттерн**: MVVM (Activity наблюдает за ViewModel через LiveData)

---

#### 6. adapter/ - Адаптеры для RecyclerView (2 файла)

| Файл | Класс | Назначение | Используется в |
|------|-------|------------|----------------|
| `ProductAdapter.kt` | `ProductAdapter` | Отображение товаров в каталоге | CatalogActivity |
| `CartAdapter.kt` | `CartAdapter` | Отображение товаров в корзине (с чекбоксами и кнопками +/-) | CartActivity |

**Паттерн**: Adapter (связывает данные с RecyclerView)

---

### 🎨 app/src/main/res/ - Ресурсы

#### res/layout/ - Экраны и компоненты (5 файлов)

| Файл | Назначение | Элементы |
|------|------------|----------|
| `activity_auth.xml` | Экран авторизации | Заголовок, поля ввода (логин, пароль), кнопки "Войти"/"Регистрация", ProgressBar |
| `activity_catalog.xml` | Экран каталога | Шапка с кнопками, счётчик корзины, RecyclerView, ProgressBar, сообщение об ошибке |
| `activity_cart.xml` | Экран корзины | Шапка, RecyclerView, итоговые суммы, кнопка "Оформить", сообщение "Корзина пуста" |
| `item_product.xml` | Карточка товара в каталоге | Картинка (80x80dp), название, цена, остаток, кнопка "В корзину" |
| `item_cart.xml` | Карточка товара в корзине | Чекбокс, картинка, название, цена, кнопки +/-, количество, итого, кнопка удаления |

---

#### res/drawable/ - Изображения (4 файла)

| Файл | Цвет | Товар |
|------|------|-------|
| `anker_10x100.xml` | Золотой (#FFD700) | Анкер 10x100 мм |
| `bolt_m8x40.xml` | Голубой (#87CEEB) | Болт М8x40 |
| `samorez_4x16.xml` | Серебряный (#C0C0C0) | Саморез 4x16 мм |
| `ic_product_placeholder.xml` | Серый (#CCCCCC) | Placeholder по умолчанию |

**Примечание**: Это XML shapes - цветные прямоугольники. Можно заменить на реальные фото (JPG/PNG).

---

#### res/values/ - Значения (3 файла)

**colors.xml** (10 цветов):
- `colorPrimary` - Синий (#2196F3)
- `colorPrimaryDark` - Тёмно-синий (#1976D2)
- `colorAccent` - Оранжевый (#FF5722)
- `grey_background`, `grey_light`, `text_dark`, `text_light`

**strings.xml** (20+ строк):
- Названия экранов, кнопок
- Сообщения об ошибках
- Форматы текста

**themes.xml**:
- Material Design 3 тема

---

## 📚 docs/ - Документация

| Файл | Размер | Для кого | Описание |
|------|--------|----------|----------|
| `QUICK_START.md` | 300 строк | Опытные | Быстрый старт за 5 минут, команды, частые проблемы |
| `TUTORIAL_PART1.md` | 500 строк | Новички | Установка программ, создание БД |
| `TUTORIAL_PART2.md` | 650 строк | Новички | Создание Flask сервера |
| `TUTORIAL_PART3.md` | 535 строк | Новички | Android: структура, layouts, resources |
| `TUTORIAL_PART3_CONTINUED.md` | 600 строк | Новички | Android: модели, network, repository |
| `TUTORIAL_PART4.md` | 767 строк | Новички | Android: ViewModels, Activities, запуск |
| `API_DOCUMENTATION.md` | 400 строк | Все | Полное описание REST API с примерами |
| `DATABASE_SCHEMA.md` | 500 строк | Все | Схема БД, таблицы, связи, SQL запросы |
| `PROJECT_STRUCTURE.md` | Этот файл | Все | Полное описание структуры проекта |

**Итого**: 4500+ строк документации на русском языке!

---

## 📊 Статистика проекта

### Код

| Компонент | Файлов | Строк кода | Язык |
|-----------|--------|------------|------|
| **Android** | 22 | ~2000 | Kotlin + XML |
| - Models | 7 | 150 | Kotlin |
| - Network | 2 | 100 | Kotlin |
| - Repository | 2 | 200 | Kotlin |
| - ViewModel | 3 | 350 | Kotlin |
| - UI (Activities) | 3 | 370 | Kotlin |
| - Adapters | 2 | 200 | Kotlin |
| - Layouts | 5 | 600 | XML |
| **Server (Flask)** | 3 | 375 | Python |
| **Database** | 2 | 100 | SQL |
| **Конфигурация** | 8 | 80 | Различные |
| **ИТОГО** | **35** | **~2555** | - |

### Документация

| Тип | Файлов | Строк | Символов |
|-----|--------|-------|----------|
| README | 1 | 350 | ~15 KB |
| Туториалы | 5 | 3050 | ~95 KB |
| Техническая документация | 3 | 1300 | ~45 KB |
| **ИТОГО** | **9** | **4700** | **~155 KB** |

---

## 🎯 Архитектурные паттерны

### Android (MVVM)

```
┌─────────────┐
│   Activity  │  ← UI (отображение)
└──────┬──────┘
       │ наблюдает (LiveData)
       ▼
┌─────────────┐
│  ViewModel  │  ← Логика экрана
└──────┬──────┘
       │ использует
       ▼
┌─────────────┐
│ Repository  │  ← Доступ к данным
└──────┬──────┘
       │
       ├──────────┐
       ▼          ▼
  ┌────────┐  ┌────────┐
  │ Network│  │ Memory │
  │ (API)  │  │ (Cache)│
  └────────┘  └────────┘
```

### Server (Flask)

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ HTTP Request
       ▼
┌─────────────┐
│  Flask App  │  ← REST API
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  Database   │  ← PostgreSQL
│   Layer     │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ PostgreSQL  │
└─────────────┘
```

---

## 🔗 Связи между компонентами

### Полный цикл запроса

```
1. USER нажимает кнопку в Activity
   ↓
2. Activity вызывает метод ViewModel
   ↓
3. ViewModel вызывает Repository
   ↓
4. Repository использует Retrofit API
   ↓
5. Retrofit отправляет HTTP запрос на Flask сервер
   ↓
6. Flask обрабатывает запрос, обращается к PostgreSQL
   ↓
7. PostgreSQL возвращает данные
   ↓
8. Flask возвращает JSON ответ
   ↓
9. Retrofit преобразует JSON в Kotlin объекты
   ↓
10. Repository возвращает Result<T> в ViewModel
   ↓
11. ViewModel обновляет LiveData
   ↓
12. Activity наблюдает за LiveData и обновляет UI
   ↓
13. USER видит результат на экране
```

---

## 🛠 Технологии по слоям

### Presentation Layer (UI)
- Activities (Android)
- ViewBinding
- RecyclerView + Adapters
- Material Design 3

### Business Logic Layer
- ViewModels
- LiveData
- Kotlin Coroutines

### Data Layer
- Repository Pattern
- Retrofit 2 (HTTP клиент)
- Gson (JSON парсинг)

### Server Layer
- Flask (REST API)
- Python 3
- Werkzeug (безопасность паролей)

### Data Storage Layer
- PostgreSQL (реляционная БД)
- psycopg2 (драйвер для Python)

---

## 📝 Ключевые файлы для изучения

Если хочешь понять как работает проект, начни с этих файлов:

1. **server/app.py** - вся логика сервера в одном файле
2. **database/schema.sql** - структура базы данных
3. **android/.../network/RetrofitClient.kt** - настройка HTTP клиента
4. **android/.../ui/CatalogActivity.kt** - пример MVVM
5. **android/.../adapter/ProductAdapter.kt** - как работает RecyclerView

---

## 🎓 Для новичков

**Начни с**:
1. Прочитай `docs/QUICK_START.md`
2. Пройди туториалы по порядку (PART1 → PART2 → PART3 → PART3_CONTINUED → PART4)
3. Запусти проект
4. Экспериментируй: меняй цвета, тексты, добавляй товары

**Для продвинутых**:
1. Изучи `docs/API_DOCUMENTATION.md`
2. Изучи `docs/DATABASE_SCHEMA.md`
3. Модифицируй код под свои нужды

---

**Успехов в изучении! 🚀**
