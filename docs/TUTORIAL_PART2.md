# 📚 Подробная инструкция - ЧАСТЬ 2: Сервер Flask

Продолжаем! В первой части мы установили все программы и создали базу данных. Теперь создадим сервер.

---

## 🐍 ЧАСТЬ 4: Создание сервера (Flask)

### 4.1 Что такое сервер простыми словами?

Сервер - это программа, которая:
1. Слушает запросы от приложения (например: "Дай мне список товаров")
2. Обращается к базе данных
3. Отправляет ответ обратно

**Аналогия**: Представь библиотеку:
- Ты (приложение) подходишь к библиотекарю (серверу)
- Просишь книгу (отправляешь запрос)
- Библиотекарь идёт к полкам (база данных), находит книгу
- Отдаёт тебе книгу (отправляет ответ)

---

### 4.2 Создаём файл зависимостей

**Что делаем?** Создаём список программ, которые нужны для сервера.

**Шаг 1**: Открой текстовый редактор

**Шаг 2**: Создай файл `requirements.txt` в папке `FastenerShop/server`:

```
Flask==3.0.0
Flask-CORS==4.0.0
psycopg2-binary==2.9.9
python-dotenv==1.0.0
werkzeug==3.0.1
```

**Что это значит?**
- `Flask` - сам сервер (как движок автомобиля)
- `Flask-CORS` - позволяет приложению общаться с сервером (убирает блокировку)
- `psycopg2-binary` - для работы с PostgreSQL (мост к базе данных)
- `python-dotenv` - для чтения настроек из файла
- `werkzeug` - для шифрования паролей (чтобы хакеры не украли)

**Сохрани** файл в папке `server` с именем `requirements.txt`

---

### 4.3 Устанавливаем зависимости

**Что делаем?** Установим все эти программы на компьютер.

#### Для Windows:

Открой командную строку и введи:

```cmd
cd FastenerShop\server
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt
```

#### Для macOS/Linux:

Открой терминал и введи:

```bash
cd FastenerShop/server
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

**Что происходит?**

1. `cd FastenerShop/server` - переходим в папку server
2. `python -m venv venv` - создаём "виртуальную среду"
   - **Простыми словами**: Это как создать отдельную коробку для игрушек этого проекта, чтобы они не смешивались с игрушками других проектов
3. `activate` - "входим" в эту коробку
   - Ты увидишь `(venv)` в начале строки - это значит, что ты внутри виртуальной среды
4. `pip install -r requirements.txt` - устанавливаем все программы из списка

Установка займёт 1-2 минуты. Дождись сообщения "Successfully installed..."

---

### 4.4 Создаём файл настроек

**Что делаем?** Создаём файл с паролями и настройками.

Создай файл `.env` в папке `server`:

```
# Настройки базы данных
DB_HOST=localhost
DB_PORT=5432
DB_NAME=fastener_shop
DB_USER=postgres
DB_PASSWORD=postgres

# Настройки Flask
SECRET_KEY=my-super-secret-key-12345
DEBUG=True
HOST=0.0.0.0
PORT=5000
```

**Объяснение каждой строки:**

- `DB_HOST=localhost` - база данных находится на этом компьютере
  - **Простыми словами**: "localhost" = мой компьютер
- `DB_PORT=5432` - номер "двери" для базы данных
  - **Аналогия**: Как номер квартиры в доме. PostgreSQL живёт в квартире 5432
- `DB_NAME=fastener_shop` - название нашей базы данных
- `DB_USER=postgres` - логин для входа в базу
- `DB_PASSWORD=postgres` - пароль (тот, который ты придумал при установке PostgreSQL!)
  - **ВАЖНО!** Если придумал другой пароль - напиши его здесь

- `SECRET_KEY` - секретный ключ для шифрования (можно оставить как есть)
- `DEBUG=True` - режим отладки (показывает ошибки подробно)
- `HOST=0.0.0.0` - слушать на всех сетевых интерфейсах
- `PORT=5000` - сервер будет работать на порту 5000

**Сохрани** файл с именем `.env` (точка в начале обязательна!)

---

### 4.5 Создаём файл config.py

**Что делаем?** Создаём файл, который читает настройки из `.env`

Создай файл `config.py` в папке `server`:

```python
# Этот файл читает настройки из .env файла
import os
from dotenv import load_dotenv

# Загружаем настройки из .env
load_dotenv()

class Config:
    """Класс с настройками приложения"""

    # Настройки базы данных
    DB_HOST = os.getenv('DB_HOST', 'localhost')
    DB_PORT = os.getenv('DB_PORT', '5432')
    DB_NAME = os.getenv('DB_NAME', 'fastener_shop')
    DB_USER = os.getenv('DB_USER', 'postgres')
    DB_PASSWORD = os.getenv('DB_PASSWORD', 'postgres')

    # Настройки Flask
    SECRET_KEY = os.getenv('SECRET_KEY', 'default-secret-key')
    DEBUG = os.getenv('DEBUG', 'True') == 'True'
    HOST = os.getenv('HOST', '0.0.0.0')
    PORT = int(os.getenv('PORT', '5000'))
```

**Что делает этот код?**

- `load_dotenv()` - читает файл `.env`
- `os.getenv('DB_HOST', 'localhost')` - берёт значение `DB_HOST` из `.env`, если не найдёт - использует `localhost`
- `class Config:` - создаёт класс (коробку) с настройками

**Простыми словами**: Этот файл - как телефонная книга. Вместо того, чтобы каждый раз писать пароль от базы данных, мы просто смотрим в Config и берём оттуда.

**Сохрани** как `config.py`

---

### 4.6 Создаём файл database.py

**Что делаем?** Создаём файл для работы с базой данных.

Создай файл `database.py` в папке `server`:

```python
# Этот файл отвечает за общение с базой данных
import psycopg2
import psycopg2.extras
from config import Config

def get_connection():
    """
    Создаёт подключение к базе данных

    Простыми словами: Открывает "дверь" в базу данных
    """
    return psycopg2.connect(
        host=Config.DB_HOST,
        port=Config.DB_PORT,
        database=Config.DB_NAME,
        user=Config.DB_USER,
        password=Config.DB_PASSWORD
    )

def execute_query(query, params=None, fetchone=False, fetchall=False, commit=False):
    """
    Выполняет SQL запрос к базе данных

    Параметры:
    - query: SQL запрос (например, "SELECT * FROM products")
    - params: параметры для запроса (для безопасности)
    - fetchone: вернуть одну строку
    - fetchall: вернуть все строки
    - commit: сохранить изменения (для INSERT, UPDATE, DELETE)

    Простыми словами:
    Эта функция - как посыльный. Ты даёшь ему письмо (запрос),
    он относит его в базу данных и приносит ответ.
    """
    # Открываем подключение
    conn = get_connection()
    cursor = conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor)

    try:
        # Выполняем запрос
        cursor.execute(query, params)

        # Если нужно сохранить изменения
        if commit:
            conn.commit()
            if fetchone:
                return cursor.fetchone()
            return True

        # Если нужно вернуть одну строку
        if fetchone:
            return cursor.fetchone()

        # Если нужно вернуть все строки
        if fetchall:
            return cursor.fetchall()

        return cursor.fetchall()

    except Exception as e:
        # Если ошибка - откатываем изменения
        conn.rollback()
        raise e
    finally:
        # Закрываем подключение
        cursor.close()
        conn.close()
```

**Что делает каждая функция?**

1. `get_connection()` - подключается к базе данных
   - **Аналогия**: Открывает дверь в библиотеку

2. `execute_query()` - выполняет запрос
   - **Аналогия**: Спрашивает библиотекаря "Дай книгу X" и получает её

**Сохрани** как `database.py`

---

### 4.7 Создаём главный файл сервера app.py

**Что делаем?** Создаём главный файл сервера - сердце всего проекта!

Создай файл `app.py` в папке `server`. Это большой файл, но я объясню каждую часть:

```python
# Главный файл сервера Flask
# Здесь мы создаём все "точки доступа" (endpoints)

from flask import Flask, request, jsonify
from flask_cors import CORS
from werkzeug.security import generate_password_hash, check_password_hash
from datetime import datetime

from config import Config
from database import execute_query

# Создаём приложение Flask
app = Flask(__name__)
app.config.from_object(Config)
CORS(app)  # Разрешаем запросы от Android приложения

print("=" * 50)
print("🚀 Сервер магазина крепежа")
print("=" * 50)

# ========================================
# ENDPOINTS (Точки доступа)
# ========================================

# Endpoint 1: Проверка здоровья сервера
@app.route('/health', methods=['GET'])
def health_check():
    """
    Проверка: работает ли сервер?

    Попробуй в браузере: http://localhost:5000/health
    """
    return jsonify({
        "status": "ok",
        "timestamp": datetime.now().isoformat()
    }), 200

# Endpoint 2: Получить список товаров
@app.route('/products', methods=['GET'])
def get_products():
    """
    Получить список всех товаров

    Попробуй в браузере: http://localhost:5000/products
    """
    try:
        # SQL запрос: выбрать все товары, где количество > 0
        query = """
            SELECT id, name, price, stock_qty, image_key
            FROM products
            WHERE stock_qty > 0
            ORDER BY id
        """
        products = execute_query(query, fetchall=True)

        print(f"✅ Отправлено {len(products)} товаров")

        return jsonify({
            "ok": True,
            "products": products
        }), 200
    except Exception as e:
        print(f"❌ Ошибка получения товаров: {e}")
        return jsonify({
            "ok": False,
            "error": "Ошибка получения товаров"
        }), 500

# Endpoint 3: Регистрация нового пользователя
@app.route('/auth/register', methods=['POST'])
def register():
    """
    Регистрация нового пользователя

    Ожидает JSON:
    {
        "login": "user@example.com",
        "password": "password123"
    }
    """
    try:
        # Получаем данные от приложения
        data = request.get_json()
        login = data.get('login')
        password = data.get('password')

        # Проверяем, что данные заполнены
        if not login or not password:
            return jsonify({
                "ok": False,
                "error": "Нужно указать логин и пароль"
            }), 400

        # Проверяем длину
        if len(login) < 3:
            return jsonify({
                "ok": False,
                "error": "Логин должен быть минимум 3 символа"
            }), 400

        if len(password) < 4:
            return jsonify({
                "ok": False,
                "error": "Пароль должен быть минимум 4 символа"
            }), 400

        # Проверяем, не занят ли логин
        check_query = "SELECT id FROM users WHERE login = %s"
        existing = execute_query(check_query, (login,), fetchone=True)

        if existing:
            return jsonify({
                "ok": False,
                "error": "Этот логин уже занят"
            }), 409

        # Шифруем пароль (чтобы хакеры не украли)
        password_hash = generate_password_hash(password)

        # Создаём пользователя
        insert_query = """
            INSERT INTO users (login, password_hash, created_at)
            VALUES (%s, %s, NOW())
            RETURNING id, login
        """
        user = execute_query(insert_query, (login, password_hash), commit=True)

        print(f"✅ Зарегистрирован пользователь: {login}")

        return jsonify({
            "ok": True,
            "message": "Регистрация успешна",
            "user": {
                "id": user['id'],
                "login": user['login']
            }
        }), 201

    except Exception as e:
        print(f"❌ Ошибка регистрации: {e}")
        return jsonify({
            "ok": False,
            "error": "Ошибка регистрации"
        }), 500

# Endpoint 4: Вход пользователя
@app.route('/auth/login', methods=['POST'])
def login():
    """
    Вход пользователя

    Ожидает JSON:
    {
        "login": "user@example.com",
        "password": "password123"
    }
    """
    try:
        data = request.get_json()
        login = data.get('login')
        password = data.get('password')

        if not login or not password:
            return jsonify({
                "ok": False,
                "error": "Нужно указать логин и пароль"
            }), 400

        # Ищем пользователя
        query = "SELECT id, login, password_hash FROM users WHERE login = %s"
        user = execute_query(query, (login,), fetchone=True)

        # Проверяем пароль
        if not user or not check_password_hash(user['password_hash'], password):
            return jsonify({
                "ok": False,
                "error": "Неверный логин или пароль"
            }), 401

        # Генерируем токен (в реальном проекте использовать JWT)
        token = f"demo-token-{user['id']}"

        print(f"✅ Вход выполнен: {login}")

        return jsonify({
            "ok": True,
            "message": "Вход выполнен",
            "user": {
                "id": user['id'],
                "login": user['login']
            },
            "token": token
        }), 200

    except Exception as e:
        print(f"❌ Ошибка входа: {e}")
        return jsonify({
            "ok": False,
            "error": "Ошибка входа"
        }), 500

# Endpoint 5: Создать заказ
@app.route('/orders', methods=['POST'])
def create_order():
    """
    Создать заказ

    Ожидает JSON:
    {
        "user_id": 1,
        "items": [
            {"product_id": 1, "qty": 2},
            {"product_id": 3, "qty": 5}
        ]
    }
    """
    try:
        data = request.get_json()
        user_id = data.get('user_id')
        items = data.get('items')

        if not user_id or not items:
            return jsonify({
                "ok": False,
                "error": "Нужно указать user_id и items"
            }), 400

        # Считаем общую сумму
        total = 0
        order_items_data = []

        for item in items:
            product_id = item.get('product_id')
            qty = item.get('qty', 1)

            # Получаем информацию о товаре
            product_query = "SELECT id, name, price, stock_qty FROM products WHERE id = %s"
            product = execute_query(product_query, (product_id,), fetchone=True)

            if not product:
                return jsonify({
                    "ok": False,
                    "error": f"Товар #{product_id} не найден"
                }), 404

            # Проверяем остаток
            if product['stock_qty'] < qty:
                return jsonify({
                    "ok": False,
                    "error": f"Недостаточно товара '{product['name']}'"
                }), 400

            item_total = float(product['price']) * qty
            total += item_total

            order_items_data.append({
                'product_id': product_id,
                'qty': qty,
                'price': product['price']
            })

        # Создаём заказ
        order_query = """
            INSERT INTO orders (user_id, created_at, total)
            VALUES (%s, NOW(), %s)
            RETURNING id, created_at
        """
        order = execute_query(order_query, (user_id, total), commit=True)
        order_id = order['id']

        # Добавляем позиции заказа
        for item_data in order_items_data:
            # Добавляем позицию
            item_query = """
                INSERT INTO order_items (order_id, product_id, qty, price_at_purchase)
                VALUES (%s, %s, %s, %s)
            """
            execute_query(item_query, (
                order_id,
                item_data['product_id'],
                item_data['qty'],
                item_data['price']
            ), commit=True)

            # Уменьшаем остаток товара
            update_query = """
                UPDATE products
                SET stock_qty = stock_qty - %s
                WHERE id = %s
            """
            execute_query(update_query, (item_data['qty'], item_data['product_id']), commit=True)

        print(f"✅ Создан заказ #{order_id} на сумму {total:.2f} ₽")

        return jsonify({
            "ok": True,
            "message": "Заказ создан",
            "order": {
                "id": order_id,
                "total": f"{total:.2f}",
                "created_at": order['created_at'].isoformat()
            }
        }), 201

    except Exception as e:
        print(f"❌ Ошибка создания заказа: {e}")
        return jsonify({
            "ok": False,
            "error": "Ошибка создания заказа"
        }), 500

# Запуск сервера
if __name__ == '__main__':
    print(f"\n🌐 Сервер запускается на http://{Config.HOST}:{Config.PORT}")
    print("📝 Доступные endpoints:")
    print("   GET  /health           - Проверка работы сервера")
    print("   GET  /products         - Список товаров")
    print("   POST /auth/register    - Регистрация")
    print("   POST /auth/login       - Вход")
    print("   POST /orders           - Создание заказа")
    print("\n💡 Для остановки нажми Ctrl+C\n")
    print("=" * 50)

    app.run(host=Config.HOST, port=Config.PORT, debug=Config.DEBUG)
```

**Сохрани** как `app.py`

---

### 4.8 Запускаем сервер!

Наконец-то! Давай запустим сервер и проверим, что всё работает.

#### Шаг 1: Активируй виртуальную среду

**Windows**:
```cmd
cd FastenerShop\server
venv\Scripts\activate
```

**macOS/Linux**:
```bash
cd FastenerShop/server
source venv/bin/activate
```

Ты должен увидеть `(venv)` в начале строки.

#### Шаг 2: Запусти сервер

```bash
python app.py
```

Должно появиться:

```
==================================================
🚀 Сервер магазина крепежа
==================================================

🌐 Сервер запускается на http://0.0.0.0:5000
📝 Доступные endpoints:
   GET  /health           - Проверка работы сервера
   GET  /products         - Список товаров
   POST /auth/register    - Регистрация
   POST /auth/login       - Вход
   POST /orders           - Создание заказа

💡 Для остановки нажми Ctrl+C

==================================================
 * Running on http://127.0.0.1:5000
```

#### Шаг 3: Проверь, что сервер работает

**НЕ ЗАКРЫВАЙ** окно с сервером! Открой новое окно.

Открой браузер и перейди на:

```
http://localhost:5000/health
```

Должен увидеть:
```json
{
  "status": "ok",
  "timestamp": "2026-01-16T12:00:00"
}
```

Теперь попробуй:
```
http://localhost:5000/products
```

Должен увидеть список товаров:
```json
{
  "ok": true,
  "products": [
    {
      "id": 1,
      "name": "Анкер 10x100 мм",
      "price": 45.50,
      "stock_qty": 150,
      "image_key": "anker_10x100"
    },
    ...
  ]
}
```

**Если видишь это - ПОЗДРАВЛЯЮ! Сервер работает!** 🎉🎉🎉

---

## 📝 Что мы сделали?

В этой части мы:

1. ✅ Создали файл зависимостей (`requirements.txt`)
2. ✅ Установили все нужные библиотеки
3. ✅ Создали файл настроек (`.env`)
4. ✅ Создали конфигурацию (`config.py`)
5. ✅ Создали модуль для работы с БД (`database.py`)
6. ✅ Создали главный файл сервера (`app.py`) с 5 endpoints
7. ✅ Запустили сервер и проверили его работу

---

## 🎮 Что дальше?

В следующей части (TUTORIAL_PART3.md) мы:

- Создадим Android приложение в Android Studio
- Напишем код для экранов (Авторизация, Каталог, Корзина)
- Подключим приложение к серверу
- Запустим всё вместе!

**Продолжение следует...**
