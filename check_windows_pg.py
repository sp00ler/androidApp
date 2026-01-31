#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""Проверка подключения к PostgreSQL Windows из WSL"""

import psycopg2
import sys

# Попробуем подключиться к Windows PostgreSQL
# В WSL Windows хост обычно доступен через специальный IP

hosts_to_try = [
    'localhost',
    '127.0.0.1',
    '21.0.0.1',  # Обычно gateway в WSL
    'host.docker.internal',
]

for host in hosts_to_try:
    try:
        print(f"\nПопытка подключения к {host}:5432...")
        conn = psycopg2.connect(
            host=host,
            port=5432,
            dbname='fastener_shop',
            user='postgres',
            password='postgres',
            connect_timeout=3
        )
        print(f"✓ УСПЕХ! Подключение к {host} работает!")

        cursor = conn.cursor()
        cursor.execute("SELECT COUNT(*) FROM products;")
        count = cursor.fetchone()[0]
        print(f"  Товаров в базе: {count}")

        cursor.close()
        conn.close()

    except Exception as e:
        print(f"✗ Ошибка на {host}: {type(e).__name__}: {e}")

print("\n" + "="*50)
print("Для подключения из Windows к Windows PostgreSQL:")
print("1. Проверьте pg_hba.conf")
print("2. Проверьте postgresql.conf (listen_addresses)")
print("3. Проверьте пароль пользователя postgres")
