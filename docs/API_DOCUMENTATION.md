# API Документация

## Базовый URL

```
http://10.0.2.2:5000/  (для Android эмулятора)
http://localhost:5000/ (для локального тестирования)
```

## Endpoints

### 1. Health Check

Проверка работоспособности сервера.

**Request:**
```http
GET /health
```

**Response:**
```json
{
  "status": "ok",
  "timestamp": "2026-01-16T12:00:00.000000"
}
```

**Status Codes:**
- `200 OK` - Сервер работает

---

### 2. Получение списка товаров

Получение всех товаров с остатком > 0.

**Request:**
```http
GET /products
```

**Response:**
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
    {
      "id": 2,
      "name": "Болт М8x40 оцинкованный",
      "price": 12.30,
      "stock_qty": 500,
      "image_key": "bolt_m8x40"
    },
    {
      "id": 3,
      "name": "Саморез 4x16 мм",
      "price": 2.50,
      "stock_qty": 1000,
      "image_key": "samorez_4x16"
    }
  ]
}
```

**Status Codes:**
- `200 OK` - Успешное получение
- `500 Internal Server Error` - Ошибка сервера

---

### 3. Регистрация пользователя

Создание нового пользователя.

**Request:**
```http
POST /auth/register
Content-Type: application/json

{
  "login": "user@example.com",
  "password": "password123"
}
```

**Response (Success):**
```json
{
  "ok": true,
  "message": "Регистрация успешна",
  "user": {
    "id": 1,
    "login": "user@example.com"
  }
}
```

**Response (Error):**
```json
{
  "ok": false,
  "error": "Пользователь с таким логином уже существует"
}
```

**Status Codes:**
- `201 Created` - Регистрация успешна
- `400 Bad Request` - Некорректные данные
- `409 Conflict` - Пользователь уже существует
- `500 Internal Server Error` - Ошибка сервера

**Валидация:**
- `login` - минимум 3 символа
- `password` - минимум 4 символа

---

### 4. Авторизация пользователя

Вход в систему.

**Request:**
```http
POST /auth/login
Content-Type: application/json

{
  "login": "user@example.com",
  "password": "password123"
}
```

**Response (Success):**
```json
{
  "ok": true,
  "message": "Авторизация успешна",
  "user": {
    "id": 1,
    "login": "user@example.com"
  },
  "token": "demo-token-1"
}
```

**Response (Error):**
```json
{
  "ok": false,
  "error": "Неверный логин или пароль"
}
```

**Status Codes:**
- `200 OK` - Авторизация успешна
- `400 Bad Request` - Некорректные данные
- `401 Unauthorized` - Неверный логин или пароль
- `500 Internal Server Error` - Ошибка сервера

---

### 5. Оформление заказа

Создание заказа с выбранными товарами.

**Request:**
```http
POST /orders
Content-Type: application/json

{
  "user_id": 1,
  "items": [
    {
      "product_id": 1,
      "qty": 2
    },
    {
      "product_id": 3,
      "qty": 5
    }
  ]
}
```

**Response (Success):**
```json
{
  "ok": true,
  "message": "Заказ успешно оформлен",
  "order": {
    "id": 1,
    "total": "103.50",
    "created_at": "2026-01-16T12:00:00.000000"
  }
}
```

**Response (Error):**
```json
{
  "ok": false,
  "error": "Недостаточно товара 'Анкер 10x100 мм' на складе"
}
```

**Status Codes:**
- `201 Created` - Заказ создан
- `400 Bad Request` - Некорректные данные или пустой список
- `404 Not Found` - Товар не найден
- `500 Internal Server Error` - Ошибка сервера

**Валидация:**
- `user_id` - обязательное поле
- `items` - не пустой массив
- `product_id` - существующий товар
- `qty` - положительное число
- Проверка остатков на складе

**Побочные эффекты:**
- Уменьшение `stock_qty` для каждого товара
- Создание записи в таблице `orders`
- Создание записей в таблице `order_items`

---

## Обработка ошибок

Все ошибки возвращаются в формате:

```json
{
  "ok": false,
  "error": "Описание ошибки"
}
```

### Типичные ошибки:

1. **Сервер недоступен**
   - Проверьте запуск Flask сервера
   - Проверьте адрес (10.0.2.2 для эмулятора)

2. **База данных недоступна**
   - Проверьте запуск PostgreSQL
   - Проверьте параметры подключения в `.env`

3. **Валидация не пройдена**
   - Проверьте корректность входных данных
   - Минимальная длина логина: 3 символа
   - Минимальная длина пароля: 4 символа

4. **Недостаточно товара**
   - Товар закончился на складе
   - Запрошенное количество превышает остаток

---

## Примеры использования (curl)

### Проверка сервера
```bash
curl http://localhost:5000/health
```

### Получение товаров
```bash
curl http://localhost:5000/products
```

### Регистрация
```bash
curl -X POST http://localhost:5000/auth/register \
  -H "Content-Type: application/json" \
  -d '{"login":"test@example.com","password":"test123"}'
```

### Авторизация
```bash
curl -X POST http://localhost:5000/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"test@example.com","password":"test123"}'
```

### Создание заказа
```bash
curl -X POST http://localhost:5000/orders \
  -H "Content-Type: application/json" \
  -d '{
    "user_id": 1,
    "items": [
      {"product_id": 1, "qty": 2},
      {"product_id": 3, "qty": 5}
    ]
  }'
```

---

## Rate Limiting

В текущей версии rate limiting не реализован. Для продакшена рекомендуется добавить.

## Аутентификация

В текущей версии используется упрощенная аутентификация с demo-токеном. Для продакшена рекомендуется использовать JWT токены.

## CORS

CORS настроен для разрешения всех источников. Для продакшена рекомендуется ограничить список разрешенных источников.
