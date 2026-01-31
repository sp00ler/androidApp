#!/bin/bash
# Скрипт инициализации базы данных PostgreSQL

set -e

# Параметры подключения (можно переопределить через переменные окружения)
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-fastener_shop}"
DB_USER="${DB_USER:-postgres}"

echo "==================================="
echo "Инициализация базы данных"
echo "==================================="
echo "Host: $DB_HOST:$DB_PORT"
echo "Database: $DB_NAME"
echo "User: $DB_USER"
echo "==================================="

# Создание базы данных (если не существует)
echo "Создание базы данных $DB_NAME..."
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -tc "SELECT 1 FROM pg_database WHERE datname = '$DB_NAME'" | grep -q 1 || \
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -c "CREATE DATABASE $DB_NAME"

# Применение схемы
echo "Применение схемы..."
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$(dirname "$0")/schema.sql"

# Загрузка начальных данных
echo "Загрузка начальных данных..."
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$(dirname "$0")/seed_data.sql"

echo "==================================="
echo "Инициализация завершена успешно!"
echo "==================================="
