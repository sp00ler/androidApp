# Схема базы данных

## Диаграмма связей

```
┌─────────────────┐
│     users       │
├─────────────────┤
│ id (PK)         │
│ login (UNIQUE)  │
│ password_hash   │
│ created_at      │
└────────┬────────┘
         │
         │ 1
         │
         │ N
┌────────▼────────┐
│     orders      │
├─────────────────┤
│ id (PK)         │
│ user_id (FK)    │
│ created_at      │
│ total           │
└────────┬────────┘
         │
         │ 1
         │
         │ N
┌────────▼────────┐         ┌─────────────────┐
│  order_items    │    N    │    products     │
├─────────────────┤────────▶├─────────────────┤
│ id (PK)         │    1    │ id (PK)         │
│ order_id (FK)   │         │ name            │
│ product_id (FK) │         │ price           │
│ qty             │         │ stock_qty       │
│ price_at_       │         │ image_key       │
│   purchase      │         │ created_at      │
└─────────────────┘         └─────────────────┘
```

## Таблицы

### 1. users

Таблица пользователей системы.

| Столбец | Тип | Ограничения | Описание |
|---------|-----|-------------|----------|
| id | SERIAL | PRIMARY KEY | Уникальный идентификатор |
| login | VARCHAR(255) | NOT NULL, UNIQUE | Логин пользователя |
| password_hash | VARCHAR(255) | NOT NULL | Хэш пароля (werkzeug.security) |
| created_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | Дата регистрации |

**Индексы:**
- PRIMARY KEY на `id`
- INDEX на `login` (для быстрого поиска)

**Примеры данных:**
```sql
INSERT INTO users (login, password_hash, created_at) VALUES
('demo@example.com', 'scrypt:32768:8:1$...', NOW()),
('test_user', 'scrypt:32768:8:1$...', NOW());
```

---

### 2. products

Каталог товаров (крепежные изделия).

| Столбец | Тип | Ограничения | Описание |
|---------|-----|-------------|----------|
| id | SERIAL | PRIMARY KEY | Уникальный идентификатор |
| name | VARCHAR(255) | NOT NULL | Название товара |
| price | NUMERIC(10,2) | NOT NULL, CHECK >= 0 | Цена за единицу |
| stock_qty | INTEGER | NOT NULL, DEFAULT 0, CHECK >= 0 | Количество на складе |
| image_key | VARCHAR(100) | NULL | Ключ изображения в ресурсах Android |
| created_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | Дата добавления |

**Индексы:**
- PRIMARY KEY на `id`
- INDEX на `stock_qty` WHERE stock_qty > 0 (для отображения товаров в наличии)

**Примеры данных:**
```sql
INSERT INTO products (name, price, stock_qty, image_key) VALUES
('Анкер 10x100 мм', 45.50, 150, 'anker_10x100'),
('Болт М8x40 оцинкованный', 12.30, 500, 'bolt_m8x40'),
('Саморез 4x16 мм', 2.50, 1000, 'samorez_4x16');
```

---

### 3. orders

Заказы пользователей.

| Столбец | Тип | Ограничения | Описание |
|---------|-----|-------------|----------|
| id | SERIAL | PRIMARY KEY | Уникальный идентификатор |
| user_id | INTEGER | NOT NULL, FK → users(id) ON DELETE CASCADE | ID пользователя |
| created_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | Дата создания заказа |
| total | NUMERIC(10,2) | NOT NULL, CHECK >= 0 | Общая сумма заказа |

**Индексы:**
- PRIMARY KEY на `id`
- INDEX на `user_id` (для получения заказов пользователя)
- INDEX на `created_at DESC` (для сортировки по дате)

**Связи:**
- `user_id` → `users.id` (ON DELETE CASCADE)

**Примеры данных:**
```sql
INSERT INTO orders (user_id, created_at, total) VALUES
(1, NOW(), 105.60);
```

---

### 4. order_items

Позиции в заказах (товары).

| Столбец | Тип | Ограничения | Описание |
|---------|-----|-------------|----------|
| id | SERIAL | PRIMARY KEY | Уникальный идентификатор |
| order_id | INTEGER | NOT NULL, FK → orders(id) ON DELETE CASCADE | ID заказа |
| product_id | INTEGER | NOT NULL, FK → products(id) ON DELETE RESTRICT | ID товара |
| qty | INTEGER | NOT NULL, CHECK > 0 | Количество товара |
| price_at_purchase | NUMERIC(10,2) | NOT NULL, CHECK >= 0 | Цена на момент покупки |

**Индексы:**
- PRIMARY KEY на `id`
- INDEX на `order_id` (для получения позиций заказа)
- INDEX на `product_id` (для аналитики по товарам)

**Связи:**
- `order_id` → `orders.id` (ON DELETE CASCADE)
- `product_id` → `products.id` (ON DELETE RESTRICT)

**Примеры данных:**
```sql
INSERT INTO order_items (order_id, product_id, qty, price_at_purchase) VALUES
(1, 1, 2, 45.50),  -- 2 анкера
(1, 3, 6, 2.50);   -- 6 саморезов
```

---

## Типы данных

### SERIAL
- Автоинкрементный целочисленный тип
- Эквивалентно `INTEGER` с `AUTO_INCREMENT`

### NUMERIC(10,2)
- Числовой тип с фиксированной точностью
- 10 цифр всего, 2 после запятой
- Используется для денежных значений

### VARCHAR(n)
- Строковый тип переменной длины
- Максимум n символов

### TIMESTAMP
- Дата и время
- Формат: `2026-01-16 12:00:00`

---

## Ограничения целостности

### Первичные ключи
Все таблицы имеют автоинкрементный первичный ключ `id`.

### Внешние ключи
1. `orders.user_id` → `users.id`
   - ON DELETE CASCADE (удаление пользователя удаляет его заказы)

2. `order_items.order_id` → `orders.id`
   - ON DELETE CASCADE (удаление заказа удаляет его позиции)

3. `order_items.product_id` → `products.id`
   - ON DELETE RESTRICT (нельзя удалить товар, если он есть в заказах)

### CHECK ограничения
- `products.price >= 0`
- `products.stock_qty >= 0`
- `orders.total >= 0`
- `order_items.qty > 0`
- `order_items.price_at_purchase >= 0`

### UNIQUE ограничения
- `users.login` - уникальный логин

---

## SQL запросы

### Получение всех товаров в наличии
```sql
SELECT id, name, price, stock_qty, image_key
FROM products
WHERE stock_qty > 0
ORDER BY id;
```

### Проверка существования пользователя
```sql
SELECT id FROM users WHERE login = 'user@example.com';
```

### Создание заказа
```sql
-- 1. Создание заказа
INSERT INTO orders (user_id, created_at, total)
VALUES (1, NOW(), 103.50)
RETURNING id, created_at;

-- 2. Добавление позиций
INSERT INTO order_items (order_id, product_id, qty, price_at_purchase)
VALUES (1, 1, 2, 45.50);

-- 3. Обновление остатков
UPDATE products
SET stock_qty = stock_qty - 2
WHERE id = 1;
```

### Получение заказов пользователя с позициями
```sql
SELECT
  o.id AS order_id,
  o.created_at,
  o.total,
  oi.id AS item_id,
  p.name AS product_name,
  oi.qty,
  oi.price_at_purchase
FROM orders o
JOIN order_items oi ON o.id = oi.order_id
JOIN products p ON oi.product_id = p.id
WHERE o.user_id = 1
ORDER BY o.created_at DESC, oi.id;
```

### Получение популярных товаров
```sql
SELECT
  p.id,
  p.name,
  SUM(oi.qty) AS total_sold
FROM products p
JOIN order_items oi ON p.id = oi.product_id
GROUP BY p.id, p.name
ORDER BY total_sold DESC
LIMIT 10;
```

---

## Миграции

Для применения схемы:

```bash
psql -U postgres -d fastener_shop -f schema.sql
```

Для загрузки начальных данных:

```bash
psql -U postgres -d fastener_shop -f seed_data.sql
```

Или использовать скрипт:

```bash
./database/init_db.sh
```

---

## Резервное копирование

### Создание backup
```bash
pg_dump -U postgres fastener_shop > backup.sql
```

### Восстановление из backup
```bash
psql -U postgres fastener_shop < backup.sql
```

---

## Производительность

### Рекомендуемые индексы
Все необходимые индексы уже созданы в схеме:
- Индекс на `users.login`
- Индекс на `products.stock_qty`
- Индекс на `orders.user_id`
- Индекс на `orders.created_at`
- Индекс на `order_items.order_id`
- Индекс на `order_items.product_id`

### Анализ производительности
```sql
EXPLAIN ANALYZE
SELECT * FROM products WHERE stock_qty > 0;
```

---

## Безопасность

1. **Пароли**: Хранятся в виде хэша (werkzeug.security)
2. **SQL Injection**: Защита через параметризованные запросы (psycopg2)
3. **Права доступа**: Рекомендуется создать отдельного пользователя БД для приложения

Создание пользователя приложения:
```sql
CREATE USER fastener_app WITH PASSWORD 'secure_password';
GRANT CONNECT ON DATABASE fastener_shop TO fastener_app;
GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA public TO fastener_app;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO fastener_app;
```
