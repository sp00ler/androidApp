#!/usr/bin/env python3
"""Тестовый скрипт для проверки импортов"""

print("=" * 60)
print("Проверка импортов модулей")
print("=" * 60)

try:
    print("\n1. Импорт config...")
    from config import Config
    print("   ✓ Config импортирован успешно")
    print(f"   DB: {Config.DB_NAME}")
except Exception as e:
    print(f"   ✗ Ошибка импорта config: {e}")

try:
    print("\n2. Импорт db_helper (database module)...")
    from db_helper import execute_query, get_db_connection
    print("   ✓ db_helper импортирован успешно")
    print(f"   Функции: execute_query, get_db_connection")
except Exception as e:
    print(f"   ✗ Ошибка импорта db_helper: {e}")
    import traceback
    traceback.print_exc()

try:
    print("\n3. Проверка psycopg2...")
    import psycopg2
    print(f"   ✓ psycopg2 установлен: версия {psycopg2.__version__}")
except Exception as e:
    print(f"   ✗ psycopg2 не установлен: {e}")

try:
    print("\n4. Проверка Flask...")
    import flask
    print(f"   ✓ Flask установлен")
except Exception as e:
    print(f"   ✗ Flask не установлен: {e}")

print("\n" + "=" * 60)
print("Проверка завершена")
print("=" * 60)
