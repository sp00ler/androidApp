# ⚡ Быстрый старт - Магазин крепежа

Это краткая инструкция для тех, кто хочет **быстро запустить** проект. Для подробных объяснений смотри TUTORIAL_PART1.md, TUTORIAL_PART2.md, TUTORIAL_PART3.md.

---

## 📋 Что нужно установить?

### 1. PostgreSQL
- **Windows**: https://www.postgresql.org/download/windows/
- **macOS**: `brew install postgresql@15`
- **Linux**: `sudo apt install postgresql`

Запомни пароль! (например: `postgres`)

### 2. Python 3.8+
- **Windows**: https://www.python.org/downloads/ (не забудь поставить галочку "Add Python to PATH")
- **macOS/Linux**: Уже установлен

### 3. Android Studio
- Скачай с https://developer.android.com/studio
- Установи (займёт 10-20 минут)

---

## 🚀 Запуск за 5 минут

### Шаг 1: База данных

```bash
# Перейди в папку database
cd database

# Windows:
init_db.bat

# macOS/Linux:
chmod +x init_db.sh
./init_db.sh
```

Должно появиться "Готово!"

---

### Шаг 2: Сервер

```bash
# Перейди в папку server
cd ../server

# Создай виртуальную среду и установи зависимости
# Windows:
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt

# macOS/Linux:
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

Создай файл `.env` (скопируй из `.env.example`):
```bash
cp .env.example .env
```

Открой `.env` и **измени пароль** если нужно:
```
DB_PASSWORD=postgres   ← Твой пароль от PostgreSQL
```

Запусти сервер:
```bash
python app.py
```

**Проверь**: Открой http://localhost:5000/health в браузере
- Должен увидеть: `{"status": "ok", ...}`

**НЕ ЗАКРЫВАЙ** окно с сервером!

---

### Шаг 3: Android приложение

1. **Открой Android Studio**
2. **File → Open** → выбери папку `android`
3. Дождись синхронизации Gradle (2-3 минуты)
4. **Tools → Device Manager** → создай эмулятор (например, Pixel 6, API 30)
5. Запусти эмулятор
6. Нажми зелёную кнопку **Run** (▶️)

Приложение установится на эмулятор!

---

## 🎮 Как пользоваться приложением?

### 1. Регистрация
- Открой приложение
- Введи логин (например: `test`)
- Введи пароль (например: `1234`)
- Нажми **"Регистрация"**

### 2. Вход
- Введи те же логин и пароль
- Нажми **"Войти"**

### 3. Каталог товаров
- Увидишь 3 товара
- Нажми **"В корзину"** на любом товаре
- Счётчик корзины увеличится

### 4. Корзина
- Нажми **"Перейти в корзину"**
- Поставь галочки на товарах, которые хочешь купить
- Нажми **"+"** или **"-"** чтобы изменить количество
- Нажми **"Оформить выбранное"**
- Готово! Заказ создан 🎉

---

## 🐛 Проблемы?

### Сервер недоступен
**Проблема**: Приложение показывает "Сервер недоступен"

**Решение**:
1. Проверь, что сервер запущен (окно не закрыто)
2. Открой http://localhost:5000/health в браузере
3. Если не открывается - перезапусти сервер

### Ошибка подключения к БД
**Проблема**: Сервер не запускается, ошибка PostgreSQL

**Решение**:
1. Проверь, что PostgreSQL запущен:
   - Windows: Пуск → Services → найди PostgreSQL
   - macOS: `brew services list`
   - Linux: `sudo systemctl status postgresql`
2. Проверь пароль в `.env`

### Gradle sync failed
**Проблема**: Android Studio не может синхронизировать Gradle

**Решение**:
1. File → Invalidate Caches / Restart
2. Удали папки `.gradle` и `.idea`
3. Открой проект заново

---

## 📁 Структура проекта

```
FastenerShop/
├── android/          # Android приложение
│   ├── app/src/main/
│   │   ├── java/com/fastener/shop/  # Код Kotlin
│   │   └── res/                      # Ресурсы (layouts, images)
│   └── build.gradle.kts
├── server/           # Flask сервер
│   ├── app.py       # Главный файл
│   ├── database.py  # Работа с БД
│   ├── config.py    # Настройки
│   └── .env         # Пароли (не коммитится в git)
├── database/         # SQL скрипты
│   ├── schema.sql   # Схема БД
│   ├── seed_data.sql # Начальные данные
│   └── init_db.sh   # Скрипт установки
└── docs/            # Документация
    ├── QUICK_START.md  # Это файл!
    ├── TUTORIAL_PART1.md
    ├── TUTORIAL_PART2.md
    └── TUTORIAL_PART3.md
```

---

## 🎯 Основные команды

### База данных
```bash
# Инициализация
cd database && ./init_db.sh

# Подключение к БД
psql -U postgres -d fastener_shop

# Просмотр товаров
psql -U postgres -d fastener_shop -c "SELECT * FROM products;"
```

### Сервер
```bash
# Активировать venv
source venv/bin/activate  # Linux/macOS
venv\Scripts\activate     # Windows

# Запустить сервер
python app.py

# Остановить сервер
Ctrl + C
```

### Android
```bash
# Очистить build
./gradlew clean

# Собрать APK
./gradlew assembleDebug

# APK будет в:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## 📝 API Endpoints

| Метод | URL | Описание |
|-------|-----|----------|
| GET | /health | Проверка сервера |
| GET | /products | Список товаров |
| POST | /auth/register | Регистрация |
| POST | /auth/login | Вход |
| POST | /orders | Создание заказа |

### Примеры запросов

**Регистрация**:
```bash
curl -X POST http://localhost:5000/auth/register \
  -H "Content-Type: application/json" \
  -d '{"login":"test","password":"1234"}'
```

**Получить товары**:
```bash
curl http://localhost:5000/products
```

---

## 💡 Полезные ссылки

- **Подробный туториал**: TUTORIAL_PART1.md, TUTORIAL_PART2.md, TUTORIAL_PART3.md
- **Схема БД**: DATABASE_SCHEMA.md
- **API документация**: API_DOCUMENTATION.md
- **Основной README**: ../README.md

---

## 🎓 Что дальше?

После запуска проекта можешь:

1. **Добавить реальные фотографии товаров**
   - Замени файлы в `android/app/src/main/res/drawable/`

2. **Добавить больше товаров**
   ```sql
   INSERT INTO products (name, price, stock_qty, image_key) VALUES
   ('Новый товар', 99.99, 100, 'image_key');
   ```

3. **Изменить цвета приложения**
   - Открой `android/app/src/main/res/values/colors.xml`

4. **Добавить новые функции**
   - История заказов
   - Поиск товаров
   - Фильтры

---

## 🆘 Нужна помощь?

- Смотри подробные туториалы (TUTORIAL_PART*.md)
- Проверь логи сервера (в окне где запущен `python app.py`)
- Проверь Logcat в Android Studio (View → Tool Windows → Logcat)

---

**Удачи! 🚀**
