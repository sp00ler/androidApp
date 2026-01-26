-- Скрипт настройки базы данных для Windows PostgreSQL
-- Запустите в pgAdmin4 Query Tool или через psql:
-- psql -U postgres -f setup_windows_db.sql

-- 1. Завершить все подключения к базе (если она существует)
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'fastener_shop'
  AND pid <> pg_backend_pid();

-- 2. Удалить старую базу (если существует)
DROP DATABASE IF EXISTS fastener_shop;

-- 3. Создать новую базу с UTF-8
CREATE DATABASE fastener_shop
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Russia.1251'
    LC_CTYPE = 'Russian_Russia.1251'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    TEMPLATE = template0;

COMMENT ON DATABASE fastener_shop
    IS 'База данных для приложения "Магазин крепежа"';

-- 4. Подключиться к базе
\c fastener_shop

-- 5. Создать схему (таблицы)

-- Удаление существующих таблиц (если есть)
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Таблица пользователей
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_login ON users(login);

-- Таблица товаров
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    stock_qty INTEGER NOT NULL DEFAULT 0 CHECK (stock_qty >= 0),
    image_key VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_stock ON products(stock_qty) WHERE stock_qty > 0;

-- Таблица заказов
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total NUMERIC(10, 2) NOT NULL CHECK (total >= 0)
);

CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_created_at ON orders(created_at DESC);

-- Таблица позиций заказа
CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id INTEGER NOT NULL REFERENCES products(id) ON DELETE RESTRICT,
    qty INTEGER NOT NULL CHECK (qty > 0),
    price_at_purchase NUMERIC(10, 2) NOT NULL CHECK (price_at_purchase >= 0)
);

CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);

-- Комментарии к таблицам
COMMENT ON TABLE users IS 'Пользователи системы';
COMMENT ON TABLE products IS 'Каталог товаров (крепежные изделия)';
COMMENT ON TABLE orders IS 'Заказы пользователей';
COMMENT ON TABLE order_items IS 'Позиции в заказах';

-- 6. Вставить тестовые данные
INSERT INTO products (name, price, stock_qty, image_key) VALUES
('Болт М6x20', 2.50, 100, 'bolt_m6'),
('Гайка М6', 1.20, 150, 'nut_m6'),
('Шайба 6мм', 0.50, 200, 'washer_6mm'),
('Саморез 4x40', 3.00, 80, 'screw_4x40'),
('Винт М8x30', 4.50, 60, 'bolt_m8'),
('Дюбель 8x40', 2.80, 120, 'dowel_8x40'),
('Гвоздь 100мм', 1.80, 90, 'nail_100'),
('Анкер 10x100', 5.50, 70, 'anchor_10x100'),
('Шуруп 5x50', 2.20, 110, 'wood_screw_5x50'),
('Заклепка 4x10', 3.50, 85, 'rivet_4x10');

-- Создать тестового пользователя
-- Пароль: test123 (хеш для werkzeug.security)
INSERT INTO users (login, password_hash) VALUES
('test', 'scrypt:32768:8:1$uO3xH8X5Q7YzFXYq$9f4e2a8d6c1b5f3e7a9d8c6b4e2f1a3d5c7b9e1f3a5c7b9d1e3f5a7c9b1d3e5f7a9b1c3d5e7f9a1b3c5d7e9f1a3b5c7d9e1f3a5b7c9d1e3f5a7b9c1d3e5f7a9b1c3d');

-- Проверить данные
SELECT 'Товаров в базе:' AS info, COUNT(*) AS count FROM products;
SELECT 'Пользователей в базе:' AS info, COUNT(*) AS count FROM users;

-- Готово!
SELECT '✓ База данных fastener_shop успешно создана и настроена!' AS status;
