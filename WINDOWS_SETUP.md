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

---

# Настройка базы данных PostgreSQL в Windows

## Проблема "Ошибка получения товаров"

Если Flask сервер запускается, но при обращении к `/products` вы видите:
```json
{"ok": false, "error": "Ошибка получения товаров"}
```

**Причина:** База данных `fastener_shop` не создана или не содержит таблицы.

## Решение: Автоматическая настройка

### Способ 1: Через pgAdmin4 (рекомендуется для Windows)

**ШАГ 1: Создание базы данных**

1. **Откройте pgAdmin4**
2. **Подключитесь к PostgreSQL 16** (введите пароль `postgres`)
3. **Откройте Query Tool**:
   - Правый клик на `PostgreSQL 16` → `Query Tool`
4. **Скопируйте весь код из файла `setup_windows_db_step1_create_db.sql`**
5. **Нажмите F5 или кнопку Execute**

Вы увидите:
```
✓ База данных fastener_shop создана!
Теперь подключитесь к БД "fastener_shop" и запустите setup_windows_db_step2_schema.sql
```

**ШАГ 2: Создание схемы и тестовых данных**

1. **Подключитесь к новой базе данных**:
   - В левой панели найдите `Databases` → `fastener_shop`
   - Правый клик на `fastener_shop` → `Query Tool`
2. **Скопируйте весь код из файла `setup_windows_db_step2_schema.sql`**
3. **Нажмите F5 или кнопку Execute**

Вы увидите:
```
✓ Схема и данные успешно созданы!
Товаров в базе: 10
Пользователей в базе: 1
```

**ВАЖНО:** В pgAdmin нельзя использовать команды `\c` (смена базы данных), поэтому настройка разделена на два шага с ручным переключением между базами.

### Способ 2: Через командную строку psql

Если вы предпочитаете командную строку, используйте единый скрипт `setup_windows_db.sql`:

```cmd
cd c:\путь\к\androidApp
psql -U postgres -f setup_windows_db.sql
```

Введите пароль postgres (по умолчанию: `postgres`)

**Примечание:** Этот способ работает только в psql, так как скрипт использует команду `\c` для переключения между базами.

### Способ 3: Пошаговая настройка в pgAdmin4

#### Шаг 1: Создайте базу данных

1. В pgAdmin4: Databases → правый клик → Create → Database
2. Заполните:
   - **Database**: `fastener_shop`
   - **Owner**: `postgres`
   - **Encoding**: `UTF8`
3. Нажмите **Save**

#### Шаг 2: Примените схему

1. Правый клик на `fastener_shop` → Query Tool
2. Откройте содержимое файла `database/schema.sql`
3. Скопируйте и выполните (F5)

#### Шаг 3: Добавьте тестовые данные

В том же Query Tool выполните:

```sql
INSERT INTO products (name, price, stock_qty, image_key) VALUES
('Болт М6x20', 2.50, 100, 'bolt_m6'),
('Гайка М6', 1.20, 150, 'nut_m6'),
('Шайба 6мм', 0.50, 200, 'washer_6mm'),
('Саморез 4x40', 3.00, 80, 'screw_4x40'),
('Винт М8x30', 4.50, 60, 'bolt_m8'),
('Дюбель 8x40', 2.80, 120, 'dowel_8x40');
```

## Проверка настройки

### Тест 1: Проверка через Python

```cmd
cd c:\путь\к\androidApp
python test_connection.py
```

Ожидаемый результат:
```
Config loaded successfully
✓ Подключение успешно!
PostgreSQL version: PostgreSQL 16.x
Количество товаров: 6
✓ Всё работает!
```

### Тест 2: Проверка через Flask

```cmd
cd c:\путь\к\androidApp\server
python app.py
```

В браузере откройте: `http://localhost:5000/products`

Ожидаемый результат:
```json
{
  "ok": true,
  "products": [
    {
      "id": 1,
      "name": "Болт М6x20",
      "price": "2.50",
      "stock_qty": 100,
      "image_key": "bolt_m6"
    },
    ...
  ]
}
```

## Устранение проблем

### "password authentication failed"

Установите пароль для пользователя postgres:

```sql
-- В pgAdmin Query Tool:
ALTER USER postgres WITH PASSWORD 'postgres';
```

### "connection refused"

1. Проверьте, что PostgreSQL запущен:
   - Откройте `services.msc`
   - Найдите `PostgreSQL 16`
   - Status должен быть `Running`

2. Если не запущен, запустите:
   - Правый клик → Start

### "database does not exist"

Выполните файл `setup_windows_db.sql` как описано выше.

### "relation 'products' does not exist"

База создана, но схема не применена. Выполните:
```cmd
psql -U postgres -d fastener_shop -f database/schema.sql
```

## Проверка pg_hba.conf

Файл: `C:\Program Files\PostgreSQL\16\data\pg_hba.conf`

Должна быть строка:
```
host    all    all    127.0.0.1/32    md5
```

После изменений перезапустите PostgreSQL через `services.msc`.
