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
├── database.py      # Модуль для работы с БД (ОБЯЗАТЕЛЬНО!)
├── config.py        # Конфигурация приложения (ОБЯЗАТЕЛЬНО!)
├── requirements.txt # Список зависимостей
└── .env             # Переменные окружения (создать на основе .env.example)
```

### 2. Проверьте содержимое файлов

#### database.py (обязательно проверьте!)
Файл должен содержать функции:
- `get_db_connection()`
- `execute_query()`

Если файл пустой или содержит ошибки - скопируйте его заново из репозитория.

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
**Причина:** Файл `database.py` отсутствует, пустой или поврежден.
**Решение:** Скопируйте `database.py` из репозитория заново.

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
   - database.py ✓ (ОБЯЗАТЕЛЬНО!)
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
# database.py  <-- Проверьте этот файл!
# config.py
# requirements.txt
```

Если `database.py` или `config.py` отсутствуют - скопируйте их из репозитория!
