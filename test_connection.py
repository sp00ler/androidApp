#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""Тест подключения к PostgreSQL"""

import sys
sys.path.insert(0, '/home/user/androidApp/server')

try:
    from config import Config
    print(f"Config loaded successfully")
    print(f"Connection string: {Config.get_db_connection_string()}")

    import psycopg2
    print(f"\nПопытка подключения...")
    conn = psycopg2.connect(Config.get_db_connection_string())
    print(f"✓ Подключение успешно!")

    cursor = conn.cursor()
    cursor.execute("SELECT version();")
    version = cursor.fetchone()
    print(f"PostgreSQL version: {version[0]}")

    cursor.execute("SELECT COUNT(*) FROM products;")
    count = cursor.fetchone()
    print(f"Количество товаров: {count[0]}")

    cursor.close()
    conn.close()
    print("\n✓ Всё работает!")

except Exception as e:
    print(f"\n✗ Ошибка: {type(e).__name__}: {e}")
    import traceback
    traceback.print_exc()
