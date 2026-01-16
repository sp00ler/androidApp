-- Начальные данные для приложения "Магазин крепежа"

-- Очистка таблиц (если есть данные)
TRUNCATE TABLE order_items, orders, products, users RESTART IDENTITY CASCADE;

-- Добавление тестовых пользователей
-- Пароль для всех: "test123" (хэш будет создан через Flask)
INSERT INTO users (login, password_hash, created_at) VALUES
('demo@example.com', 'scrypt:32768:8:1$placeholder', NOW()),
('test_user', 'scrypt:32768:8:1$placeholder', NOW());

-- Добавление товаров (3 позиции согласно ТЗ)
INSERT INTO products (name, price, stock_qty, image_key, created_at) VALUES
('Анкер 10x100 мм', 45.50, 150, 'anker_10x100', NOW()),
('Болт М8x40 оцинкованный', 12.30, 500, 'bolt_m8x40', NOW()),
('Саморез 4x16 мм', 2.50, 1000, 'samorez_4x16', NOW());

-- Добавление тестового заказа (опционально)
INSERT INTO orders (user_id, created_at, total) VALUES
(1, NOW() - INTERVAL '1 day', 105.60);

INSERT INTO order_items (order_id, product_id, qty, price_at_purchase) VALUES
(1, 1, 2, 45.50),  -- 2 анкера
(1, 3, 6, 2.50);   -- 6 саморезов

-- Проверка данных
SELECT 'Пользователи:' AS info;
SELECT id, login, created_at FROM users;

SELECT 'Товары:' AS info;
SELECT id, name, price, stock_qty, image_key FROM products;

SELECT 'Заказы:' AS info;
SELECT o.id, o.user_id, u.login, o.total, o.created_at
FROM orders o
JOIN users u ON o.user_id = u.id;

SELECT 'Позиции заказов:' AS info;
SELECT oi.id, oi.order_id, p.name, oi.qty, oi.price_at_purchase
FROM order_items oi
JOIN products p ON oi.product_id = p.id;
