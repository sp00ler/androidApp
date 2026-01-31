# Решение проблем с PostgreSQL на Windows

## Ваша ситуация

✓ **Порт 5432 открыт** - PostgreSQL работает
? **Но приложение не подключается** - скорее всего база не создана

---

## Шаг 1: Диагностика

Запустите скрипт диагностики:

```bash
python diagnose_windows_db.py
```

Этот скрипт проверит:
- ✓ Подключение к PostgreSQL
- ✓ Существование базы `fastener_shop`
- ✓ Наличие таблиц (users, products, orders, order_items)
- ✓ Количество записей в таблицах

---

## Шаг 2: Создание базы данных (если её нет)

### Вариант A: Через pgAdmin 4 (РЕКОМЕНДУЕТСЯ для Windows)

#### Шаг 2.1: Создание базы данных

1. Откройте **pgAdmin 4**
2. В дереве слева найдите **Servers → PostgreSQL 16**
3. Кликните правой кнопкой на **Databases**
4. Выберите **Query Tool** (Инструменты запросов)
5. **ВАЖНО**: Убедитесь, что подключены к базе **postgres** (не fastener_shop!)
6. Скопируйте содержимое файла `setup_windows_db_step1_create_db.sql`
7. Вставьте в Query Tool
8. Нажмите **Execute** (▶️) или F5

**Результат**: Должно появиться сообщение "✓ База данных fastener_shop создана!"

#### Шаг 2.2: Создание таблиц и данных

1. В pgAdmin в дереве слева найдите **Databases → fastener_shop**
2. Кликните правой кнопкой на **fastener_shop**
3. Выберите **Query Tool**
4. **ВАЖНО**: Убедитесь, что подключены к базе **fastener_shop**!
5. Скопируйте содержимое файла `setup_windows_db_step2_schema.sql`
6. Вставьте в Query Tool
7. Нажмите **Execute** (▶️) или F5

**Результат**: Должны появиться сообщения:
- Товаров в базе: 10
- Пользователей в базе: 1
- ✓ Схема и данные успешно созданы!

---

### Вариант B: Через командную строку psql

Если у вас установлен `psql` и он доступен в PATH:

```cmd
REM Шаг 1: Создание базы данных
psql -U postgres -f setup_windows_db_step1_create_db.sql

REM Шаг 2: Создание таблиц
psql -U postgres -d fastener_shop -f setup_windows_db_step2_schema.sql
```

---

## Шаг 3: Проверка подключения

После создания базы и таблиц запустите тест:

```bash
python test_connection.py
```

**Ожидаемый результат**:
```
Config loaded successfully
Connection string: host=localhost port=5432 dbname=fastener_shop user=postgres password=postgres client_encoding=UTF8

Попытка подключения...
✓ Подключение успешно!
PostgreSQL version: PostgreSQL 16.x ...
Количество товаров: 10

✓ Всё работает!
```

---

## Шаг 4: Запуск сервера

Если тест прошёл успешно, запустите Flask сервер:

```bash
cd server
python app.py
```

**Ожидаемый вывод**:
```
Подключение к БД успешно!
✓ Товаров в базе: 10
 * Running on http://0.0.0.0:5000
```

---

## Частые ошибки и решения

### Ошибка: "FATAL: password authentication failed for user postgres"

**Решение**:
1. Откройте pgAdmin 4
2. При первом подключении введите правильный пароль
3. Обновите файл `server/.env`:
   ```
   DB_PASSWORD=ваш_реальный_пароль
   ```

---

### Ошибка: "database fastener_shop does not exist"

**Решение**: Выполните **Шаг 2.1** выше (создание базы данных)

---

### Ошибка: "relation products does not exist"

**Решение**: Выполните **Шаг 2.2** выше (создание таблиц)

---

### Ошибка: "psycopg2 module not found"

**Решение**:
```bash
pip install psycopg2-binary
```

---

## Проверка: Что должно быть в базе после настройки

Подключитесь к базе `fastener_shop` в pgAdmin и выполните:

```sql
-- Проверка таблиц
SELECT table_name FROM information_schema.tables
WHERE table_schema = 'public';

-- Должны быть: users, products, orders, order_items

-- Проверка товаров
SELECT id, name, price, stock_qty FROM products;

-- Должно быть 10 товаров:
-- Болт М6x20, Гайка М6, Шайба 6мм, Саморез 4x40, ...
```

---

## Полезные SQL команды для проверки

```sql
-- Проверить версию PostgreSQL
SELECT version();

-- Список всех баз данных
SELECT datname FROM pg_database;

-- Список таблиц в текущей базе
\dt

-- Количество записей в таблице products
SELECT COUNT(*) FROM products;

-- Посмотреть все товары
SELECT * FROM products;
```

---

## Нужна помощь?

Если проблема не решена, запустите:

```bash
python diagnose_windows_db.py > diagnostic_log.txt
```

И пришлите содержимое файла `diagnostic_log.txt`.
