# Мобильное приложение "Магазин крепежа"

Мобильное приложение для Android с клиент-серверной архитектурой для демонстрации процесса покупки крепежных изделий.

## 📋 Технологический стек

### Android (Клиент)
- **Язык**: Kotlin
- **Архитектура**: MVVM (Model-View-ViewModel)
- **UI**: ViewBinding, RecyclerView, Material Design
- **Сеть**: Retrofit 2, OkHttp
- **Асинхронность**: Kotlin Coroutines
- **Минимальная версия Android**: API 24 (Android 7.0)

### Backend (Сервер)
- **Язык**: Python 3.x
- **Фреймворк**: Flask
- **База данных**: PostgreSQL
- **Библиотеки**: psycopg2, Flask-CORS, werkzeug

## 📚 Документация и обучение

### Быстрый старт
- **[QUICK_START.md](docs/QUICK_START.md)** - Запуск за 5 минут (для опытных разработчиков)

### Подробные туториалы для начинающих
Пошаговые инструкции с объяснениями "для 12-летнего ребёнка":

1. **[TUTORIAL_PART1.md](docs/TUTORIAL_PART1.md)** - Установка программ и создание БД
   - Установка PostgreSQL, Python, Android Studio, Git
   - Создание структуры проекта
   - Создание базы данных с таблицами
   - Добавление начальных данных

2. **[TUTORIAL_PART2.md](docs/TUTORIAL_PART2.md)** - Создание сервера Flask
   - Настройка виртуальной среды Python
   - Создание REST API с 5 endpoints
   - Подключение к PostgreSQL
   - Запуск и тестирование сервера

3. **[TUTORIAL_PART3.md](docs/TUTORIAL_PART3.md)** - Создание Android приложения (часть 1)
   - Создание проекта в Android Studio
   - Добавление библиотек (Retrofit, ViewModel, Coroutines)
   - Создание структуры пакетов
   - Настройка цветов и ресурсов
   - Создание layouts (экраны авторизации, каталога)

4. **[TUTORIAL_PART3_CONTINUED.md](docs/TUTORIAL_PART3_CONTINUED.md)** - Android приложение (часть 2)
   - Создание моделей данных (Product, User, CartItem)
   - Настройка сетевого слоя (Retrofit, API)
   - Создание Repository (хранилищ данных)
   - Layout для корзины

5. **[TUTORIAL_PART4.md](docs/TUTORIAL_PART4.md)** - Запуск и тестирование
   - Создание ViewModels (логика экранов)
   - Создание Activities (сами экраны)
   - Создание Adapters (для списков)
   - Настройка AndroidManifest
   - Запуск на эмуляторе
   - Полное тестирование приложения

### Техническая документация
- **[API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md)** - Полное описание REST API
- **[DATABASE_SCHEMA.md](docs/DATABASE_SCHEMA.md)** - Схема базы данных с диаграммами

## 🏗 Структура проекта

```
androidApp/
├── android/              # Android приложение
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/com/fastener/shop/
│   │   │   │   ├── adapter/      # RecyclerView адаптеры
│   │   │   │   ├── model/        # Модели данных
│   │   │   │   ├── network/      # Retrofit API
│   │   │   │   ├── repository/   # Репозитории (доступ к данным)
│   │   │   │   ├── ui/           # Activities (экраны)
│   │   │   │   └── viewmodel/    # ViewModels
│   │   │   ├── res/              # Ресурсы (layouts, drawables, values)
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle.kts
│   └── settings.gradle.kts
├── server/               # Flask сервер
│   ├── app.py           # Основное приложение
│   ├── config.py        # Конфигурация
│   ├── database.py      # Работа с БД
│   ├── requirements.txt # Зависимости Python
│   └── .env.example     # Пример переменных окружения
├── database/            # SQL схемы и скрипты
│   ├── schema.sql       # Схема БД
│   ├── seed_data.sql    # Начальные данные
│   └── init_db.sh       # Скрипт инициализации
└── README.md            # Этот файл
```

## 🚀 Установка и запуск

### 1. Предварительные требования

Убедитесь, что у вас установлены:
- **PostgreSQL** (версия 12 или выше)
- **Python 3.8+**
- **Android Studio** (последняя версия)
- **Git**

### 2. Клонирование репозитория

```bash
git clone <repository-url>
cd androidApp
```

### 3. Настройка базы данных PostgreSQL

#### 3.1. Запуск PostgreSQL

Убедитесь, что PostgreSQL запущен:

```bash
# Linux
sudo systemctl start postgresql
sudo systemctl status postgresql

# macOS
brew services start postgresql

# Windows
# Запустите через Services или pgAdmin
```

#### 3.2. Инициализация базы данных

```bash
cd database

# Сделать скрипт исполняемым (Linux/macOS)
chmod +x init_db.sh

# Запустить инициализацию
./init_db.sh

# Или вручную:
psql -U postgres -c "CREATE DATABASE fastener_shop;"
psql -U postgres -d fastener_shop -f schema.sql
psql -U postgres -d fastener_shop -f seed_data.sql
```

### 4. Настройка и запуск Flask сервера

#### 4.1. Создание виртуального окружения

```bash
cd ../server

# Создание виртуального окружения
python3 -m venv venv

# Активация
# Linux/macOS:
source venv/bin/activate
# Windows:
venv\Scripts\activate
```

#### 4.2. Установка зависимостей

```bash
pip install -r requirements.txt
```

#### 4.3. Настройка переменных окружения

```bash
# Скопировать пример конфигурации
cp .env.example .env

# Отредактировать .env при необходимости
nano .env
```

Содержимое `.env`:
```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=fastener_shop
DB_USER=postgres
DB_PASSWORD=postgres

SECRET_KEY=your-secret-key-here
DEBUG=True
HOST=0.0.0.0
PORT=5000
```

#### 4.4. Запуск сервера

```bash
python app.py
```

Сервер запустится на `http://0.0.0.0:5000`

Проверьте работу сервера:
```bash
curl http://localhost:5000/health
# Ответ: {"status": "ok", "timestamp": "..."}
```

### 5. Настройка и запуск Android приложения

#### 5.1. Открытие проекта в Android Studio

1. Откройте **Android Studio**
2. Выберите **File → Open**
3. Откройте папку `androidApp/android`
4. Дождитесь синхронизации Gradle (первый раз может занять несколько минут)

#### 5.2. Добавление изображений товаров (опционально)

По умолчанию используются цветные placeholder'ы. Для добавления реальных изображений:

1. Поместите изображения товаров в папку:
   ```
   android/app/src/main/res/drawable/
   ```

2. Изображения должны быть названы:
   - `anker_10x100.jpg` (или .png) - Анкер 10x100 мм
   - `bolt_m8x40.jpg` (или .png) - Болт М8x40
   - `samorez_4x16.jpg` (или .png) - Саморез 4x16 мм

3. Удалите соответствующие XML файлы placeholder'ов

#### 5.3. Запуск приложения

1. **Создайте виртуальное устройство (эмулятор)**:
   - Tools → Device Manager → Create Device
   - Выберите любое устройство (например, Pixel 6)
   - Выберите system image (API 30 или выше)
   - Запустите эмулятор

2. **Запустите приложение**:
   - Нажмите зеленую кнопку "Run" (▶️) или Shift+F10
   - Выберите запущенный эмулятор
   - Дождитесь установки приложения

#### 5.4. Важно: адрес сервера для эмулятора

В Android эмуляторе `localhost` не работает. Используется специальный адрес:
- **10.0.2.2** - для доступа к localhost хост-машины

Это уже настроено в коде:
```kotlin
// RetrofitClient.kt
private const val BASE_URL = "http://10.0.2.2:5000/"
```

Для физического устройства замените на:
```kotlin
private const val BASE_URL = "http://<IP-адрес-вашего-компьютера>:5000/"
```

## 📱 Функционал приложения

### 1. Экран авторизации
- Регистрация нового пользователя
- Вход существующего пользователя
- Валидация полей (логин >= 3 символа, пароль >= 4 символа)

### 2. Экран каталога
- Отображение списка товаров (название, цена, остаток, изображение)
- Добавление товаров в корзину
- Счетчик товаров в корзине
- Переход в корзину
- Обновление каталога

### 3. Экран корзины
- Отображение товаров в корзине
- Чекбокс для выбора товаров
- Изменение количества (+/-)
- Удаление товара
- Отображение итоговых сумм:
  - Итого выбранного
  - Итого в корзине
- Очистка корзины
- Оформление выбранных товаров

### 4. Оформление заказа
- Создание заказа на сервере
- Обновление остатков товаров
- Удаление оформленных товаров из корзины

## 🔌 REST API Endpoints

### Health Check
```
GET /health
Response: {"status": "ok", "timestamp": "..."}
```

### Получение товаров
```
GET /products
Response: {
  "ok": true,
  "products": [
    {
      "id": 1,
      "name": "Анкер 10x100 мм",
      "price": 45.50,
      "stock_qty": 150,
      "image_key": "anker_10x100"
    }
  ]
}
```

### Регистрация
```
POST /auth/register
Body: {"login": "user@example.com", "password": "password123"}
Response: {
  "ok": true,
  "user": {"id": 1, "login": "user@example.com"},
  "message": "Регистрация успешна"
}
```

### Авторизация
```
POST /auth/login
Body: {"login": "user@example.com", "password": "password123"}
Response: {
  "ok": true,
  "user": {"id": 1, "login": "user@example.com"},
  "token": "demo-token-1"
}
```

### Оформление заказа
```
POST /orders
Body: {
  "user_id": 1,
  "items": [
    {"product_id": 1, "qty": 2},
    {"product_id": 3, "qty": 5}
  ]
}
Response: {
  "ok": true,
  "order": {
    "id": 1,
    "total": "103.50",
    "created_at": "2026-01-16T12:00:00"
  }
}
```

## 🗄 Схема базы данных

### Таблица `users`
| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL | Первичный ключ |
| login | VARCHAR(255) | Логин (уникальный) |
| password_hash | VARCHAR(255) | Хэш пароля |
| created_at | TIMESTAMP | Дата регистрации |

### Таблица `products`
| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL | Первичный ключ |
| name | VARCHAR(255) | Название товара |
| price | NUMERIC(10,2) | Цена |
| stock_qty | INTEGER | Остаток на складе |
| image_key | VARCHAR(100) | Ключ изображения |
| created_at | TIMESTAMP | Дата добавления |

### Таблица `orders`
| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL | Первичный ключ |
| user_id | INTEGER | FK на users |
| created_at | TIMESTAMP | Дата создания |
| total | NUMERIC(10,2) | Общая сумма |

### Таблица `order_items`
| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL | Первичный ключ |
| order_id | INTEGER | FK на orders |
| product_id | INTEGER | FK на products |
| qty | INTEGER | Количество |
| price_at_purchase | NUMERIC(10,2) | Цена на момент покупки |

## 🧪 Тестирование

### Базовые тест-кейсы

1. **Регистрация и вход**
   - Зарегистрировать нового пользователя
   - Войти под созданным пользователем
   - Проверить переход в каталог

2. **Каталог товаров**
   - Проверить отображение 3 товаров
   - Добавить товары в корзину
   - Проверить обновление счетчика

3. **Корзина**
   - Открыть корзину
   - Изменить количество товаров
   - Проверить расчет итоговых сумм
   - Выбрать/снять выбор товаров
   - Проверить обновление "Итого выбранного"

4. **Оформление заказа**
   - Выбрать несколько товаров
   - Оформить выбранные
   - Проверить успешное создание заказа
   - Проверить удаление оформленных товаров из корзины
   - Проверить, что невыбранные товары остались

5. **Обработка ошибок**
   - Остановить Flask сервер
   - Попытаться загрузить каталог
   - Проверить отображение сообщения об ошибке

## 🐛 Устранение неполадок

### Проблема: "Сервер недоступен"

**Решение**:
1. Проверьте, что Flask сервер запущен: `curl http://localhost:5000/health`
2. Убедитесь, что используется адрес `10.0.2.2` для эмулятора
3. Проверьте firewall (должен пропускать порт 5000)

### Проблема: Ошибка подключения к PostgreSQL

**Решение**:
1. Проверьте статус PostgreSQL: `sudo systemctl status postgresql`
2. Проверьте параметры в `.env` (DB_HOST, DB_PORT, DB_USER, DB_PASSWORD)
3. Проверьте существование базы: `psql -U postgres -l`

### Проблема: Gradle sync failed

**Решение**:
1. Проверьте подключение к интернету
2. Очистите кэш: File → Invalidate Caches / Restart
3. Удалите папки `.gradle` и `.idea`, откройте проект заново

### Проблема: Изображения не отображаются

**Решение**:
1. Убедитесь, что файлы изображений находятся в правильной папке
2. Имена файлов должны быть в нижнем регистре
3. Пересоберите проект: Build → Rebuild Project

## 📚 Дополнительная информация

### Начальные данные в БД

После инициализации в БД будут созданы:
- **Пользователи**: demo@example.com, test_user (пароли нужно создать через регистрацию)
- **Товары**:
  - Анкер 10x100 мм - 45.50 ₽ (150 шт.)
  - Болт М8x40 оцинкованный - 12.30 ₽ (500 шт.)
  - Саморез 4x16 мм - 2.50 ₽ (1000 шт.)

### Архитектура MVVM

```
UI (Activity) → ViewModel → Repository → Network/Database
     ↑              ↓
  LiveData    Business Logic
```

- **View (Activity)**: Отображает UI и обрабатывает пользовательский ввод
- **ViewModel**: Содержит бизнес-логику и управляет состоянием UI
- **Repository**: Абстракция для доступа к данным (API, БД)
- **Network**: Retrofit для HTTP запросов

## 📝 Лицензия

Этот проект создан в учебных целях для курсовой работы.

## 👥 Авторы

Разработано в рамках курсовой работы по теме "Разработка мобильного приложения для магазина крепежа".

---

**Дата создания**: 2026-01-16
**Версия**: 1.0
