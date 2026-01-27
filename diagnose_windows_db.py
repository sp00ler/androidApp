#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
Диагностика PostgreSQL для Windows
Этот скрипт проверяет подключение и состояние базы данных
"""

import sys
import os

print("=" * 70)
print("ДИАГНОСТИКА POSTGRESQL - Магазин крепежа")
print("=" * 70)

# Проверка 1: Импорт библиотеки psycopg2
print("\n[1/5] Проверка библиотеки psycopg2...")
try:
    import psycopg2
    from psycopg2 import sql
    print("✓ psycopg2 установлен")
except ImportError as e:
    print(f"✗ ОШИБКА: psycopg2 не установлен!")
    print(f"  Установите: pip install psycopg2 или pip install psycopg2-binary")
    sys.exit(1)

# Проверка 2: Подключение к PostgreSQL (база postgres)
print("\n[2/5] Проверка подключения к PostgreSQL...")
try:
    conn = psycopg2.connect(
        host='localhost',
        port=5432,
        dbname='postgres',  # Подключаемся к стандартной базе
        user='postgres',
        password='postgres'
    )
    print("✓ Подключение к PostgreSQL успешно (база: postgres)")

    cursor = conn.cursor()
    cursor.execute("SELECT version();")
    version = cursor.fetchone()[0]
    print(f"  PostgreSQL версия: {version[:50]}...")

except psycopg2.OperationalError as e:
    print(f"✗ ОШИБКА подключения к PostgreSQL!")
    print(f"  Причина: {e}")
    print("\n  ВОЗМОЖНЫЕ РЕШЕНИЯ:")
    print("  1. Проверьте, что PostgreSQL запущен")
    print("  2. Проверьте пароль пользователя postgres")
    print("  3. Проверьте настройки pg_hba.conf")
    sys.exit(1)

# Проверка 3: Существование базы fastener_shop
print("\n[3/5] Проверка существования базы fastener_shop...")
cursor.execute("""
    SELECT datname FROM pg_database WHERE datname = 'fastener_shop';
""")
result = cursor.fetchone()

if result:
    print("✓ База данных 'fastener_shop' существует")
    db_exists = True
else:
    print("✗ База данных 'fastener_shop' НЕ существует!")
    print("\n  РЕШЕНИЕ:")
    print("  Запустите в pgAdmin (подключившись к базе 'postgres'):")
    print("  1. setup_windows_db_step1_create_db.sql")
    db_exists = False

cursor.close()
conn.close()

if not db_exists:
    print("\n" + "=" * 70)
    print("ВЫВОД: Создайте базу данных сначала!")
    print("=" * 70)
    sys.exit(0)

# Проверка 4: Подключение к fastener_shop
print("\n[4/5] Проверка подключения к базе fastener_shop...")
try:
    conn = psycopg2.connect(
        host='localhost',
        port=5432,
        dbname='fastener_shop',
        user='postgres',
        password='postgres'
    )
    print("✓ Подключение к fastener_shop успешно")
except psycopg2.OperationalError as e:
    print(f"✗ ОШИБКА подключения к fastener_shop: {e}")
    sys.exit(1)

# Проверка 5: Существование таблиц
print("\n[5/5] Проверка таблиц в базе...")
cursor = conn.cursor()

tables = ['users', 'products', 'orders', 'order_items']
tables_status = {}

for table in tables:
    cursor.execute("""
        SELECT EXISTS (
            SELECT FROM information_schema.tables
            WHERE table_name = %s
        );
    """, (table,))
    exists = cursor.fetchone()[0]
    tables_status[table] = exists

    if exists:
        cursor.execute(f"SELECT COUNT(*) FROM {table};")
        count = cursor.fetchone()[0]
        print(f"  ✓ Таблица '{table}' существует (записей: {count})")
    else:
        print(f"  ✗ Таблица '{table}' НЕ существует!")

cursor.close()
conn.close()

# Итоговый вывод
print("\n" + "=" * 70)
if all(tables_status.values()):
    print("ВЫВОД: Всё в порядке! База данных настроена правильно.")
    print("=" * 70)
    print("\nТеперь можете запустить:")
    print("  python test_connection.py")
    print("  python server/app.py")
else:
    print("ВЫВОД: Таблицы не созданы!")
    print("=" * 70)
    print("\nРЕШЕНИЕ:")
    print("1. Откройте pgAdmin 4")
    print("2. Подключитесь к базе данных 'fastener_shop'")
    print("3. Откройте Query Tool (Инструменты → Query Tool)")
    print("4. Откройте файл: setup_windows_db_step2_schema.sql")
    print("5. Нажмите Execute (F5)")
    print("\nИли через psql:")
    print("  psql -U postgres -d fastener_shop -f setup_windows_db_step2_schema.sql")

print("\n" + "=" * 70)
