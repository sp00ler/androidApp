# Миграция: database.py → db_helper.py

## Проблема
Модуль `database.py` вызывает конфликт имен с системными модулями Python на некоторых платформах (особенно Windows), что приводит к ошибке:
```
ImportError: cannot import name 'execute_query' from 'database'
```

## Решение
Модуль переименован в `db_helper.py` чтобы избежать конфликта.

## Что нужно сделать

### На Linux (если использовали database.py)
```bash
cd /home/user/androidApp/server
# Старый файл больше не нужен
rm -f database.py
```

### На Windows
```cmd
cd c:\FastnerShop\server

# 1. Удалите старый файл (если есть)
del database.py

# 2. Скопируйте новый файл db_helper.py из репозитория

# 3. Убедитесь, что app.py использует новый импорт:
# from db_helper import execute_query
```

## Проверка
Запустите тестовый скрипт:
```
python test_import.py
```

Вы должны увидеть:
```
✓ db_helper импортирован успешно
```

## Технические детали

### Старая версия
```python
from database import execute_query  # ❌ Конфликт имен
```

### Новая версия
```python
from db_helper import execute_query  # ✅ Работает
```

Функциональность модуля не изменилась, изменилось только имя файла.
