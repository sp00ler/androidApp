# Установка FastnerShop на Windows

## Проблема
Ошибка `ImportError: cannot import name 'execute_query' from 'database'` возникает, когда:
- Файл `database.py` отсутствует или поврежден
- Файлы скопированы не полностью
- Установлены не все зависимости

## Решение

### 1. Убедитесь, что все файлы на месте

В папке `c:\FastnerShop\server\` должны быть следующие файлы:

```
c:\FastnerShop\server\
├── app.py           # Главный файл Flask приложения
├── db_helper.py     # Модуль для работы с БД (ОБЯЗАТЕЛЬНО!)
├── config.py        # Конфигурация приложения (ОБЯЗАТЕЛЬНО!)
├── requirements.txt # Список зависимостей
└── .env             # Переменные окружения (создать на основе .env.example)
```

**Примечание:** Модуль переименован из `database.py` в `db_helper.py` чтобы избежать конфликта имен с системными модулями Python.

### 2. Проверьте содержимое файлов

#### db_helper.py (обязательно проверьте!)
Файл должен содержать функции:
- `get_db_connection()`
- `execute_query()`

Если файл пустой или содержит ошибки - скопируйте его заново из репозитория.

**Важно:** Используйте `db_helper.py`, а НЕ `database.py` - имя изменено чтобы избежать конфликта с системными модулями Python.

#### config.py
Должен содержать класс `Config` с настройками подключения к БД.

### 3. Установите зависимости

```cmd
cd c:\FastnerShop\server
python -m pip install -r requirements.txt
```

### 4. Проверьте импорты

Запустите тестовый скрипт:

```cmd
cd c:\FastnerShop\server
python test_import.py
```

Все пункты должны быть отмечены галочками ✓.

### 5. Настройте переменные окружения

Создайте файл `.env` на основе `.env.example`:

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=fastener_shop
DB_USER=postgres
DB_PASSWORD=your_password
SERVER_HOST=0.0.0.0
SERVER_PORT=5000
DEBUG=True
```

### 6. Запустите PostgreSQL

Убедитесь, что PostgreSQL запущен и доступен:

```cmd
# Проверка службы PostgreSQL
sc query postgresql-x64-16

# Если не запущен, запустите:
net start postgresql-x64-16
```

### 7. Запустите сервер

```cmd
cd c:\FastnerShop\server
python app.py
```

## Частые ошибки

### ImportError: cannot import name 'execute_query'
**Причина:** Файл `db_helper.py` отсутствует, пустой или поврежден. Или используется старое имя `database.py`.
**Решение:** Скопируйте `db_helper.py` из репозитория. Убедитесь, что в `app.py` используется `from db_helper import execute_query`.

### ModuleNotFoundError: No module named 'psycopg2'
**Причина:** Не установлены зависимости.
**Решение:** `pip install -r requirements.txt`

### ModuleNotFoundError: No module named 'config'
**Причина:** Файл `config.py` отсутствует.
**Решение:** Скопируйте `config.py` из репозитория.

### connection refused (PostgreSQL)
**Причина:** PostgreSQL не запущен или не установлен.
**Решение:** Установите и запустите PostgreSQL.

## Правильная последовательность копирования файлов

1. Скопируйте **ВСЕ** файлы из папки `server/`:
   - app.py ✓
   - db_helper.py ✓ (ОБЯЗАТЕЛЬНО!)
   - config.py ✓ (ОБЯЗАТЕЛЬНО!)
   - requirements.txt ✓
   - .env.example ✓

2. НЕ копируйте файлы по отдельности! Скопируйте всю папку целиком.

3. После копирования проверьте наличие всех файлов.

## Быстрая проверка

Откройте PowerShell или CMD и выполните:

```cmd
cd c:\FastnerShop\server
dir

# Должны увидеть:
# app.py
# db_helper.py  <-- Проверьте этот файл!
# config.py
# requirements.txt
```

Если `db_helper.py` или `config.py` отсутствуют - скопируйте их из репозитория!

## Важное изменение!

**Старое имя:** `database.py` (вызывает конфликт)
**Новое имя:** `db_helper.py` (исправлено)

Если у вас есть файл `database.py`, удалите его и используйте `db_helper.py` вместо него!
