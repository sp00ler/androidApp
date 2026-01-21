-- Скрипт для пересоздания базы данных с правильной кодировкой UTF-8
-- Выполнить этот скрипт в Query Tool, подключившись к базе postgres (не к fastener_shop_db!)

-- Завершить все подключения к базе
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'fastener_shop_db'
  AND pid <> pg_backend_pid();

-- Удалить старую базу
DROP DATABASE IF EXISTS fastener_shop_db;

-- Создать новую базу с UTF-8
CREATE DATABASE fastener_shop_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    TEMPLATE = template0;

COMMENT ON DATABASE fastener_shop_db IS 'База данных магазина крепежа';
