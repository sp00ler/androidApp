-- Скрипт настройки базы данных для Windows PostgreSQL - ШАГ 1
--
-- ВАЖНО: Запустите этот скрипт в pgAdmin4 Query Tool,
-- подключившись к базе "postgres" или "PostgreSQL 16"
--
-- Альтернатива: psql -U postgres -f setup_windows_db_step1_create_db.sql

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

-- Готово! Теперь запустите setup_windows_db_step2_schema.sql
SELECT '✓ База данных fastener_shop создана!' AS status,
       'Теперь подключитесь к БД "fastener_shop" и запустите setup_windows_db_step2_schema.sql' AS next_step;
