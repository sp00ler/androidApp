#!/bin/bash
# Скрипт для запуска Flask сервера

set -e

echo "=================================="
echo "Запуск Flask сервера"
echo "=================================="

# Проверка наличия виртуального окружения
if [ ! -d "venv" ]; then
    echo "Виртуальное окружение не найдено. Создание..."
    python3 -m venv venv
    echo "Виртуальное окружение создано."
fi

# Активация виртуального окружения
echo "Активация виртуального окружения..."
source venv/bin/activate

# Установка зависимостей
if [ ! -f "venv/.dependencies_installed" ]; then
    echo "Установка зависимостей..."
    pip install -r requirements.txt
    touch venv/.dependencies_installed
    echo "Зависимости установлены."
fi

# Проверка наличия .env
if [ ! -f ".env" ]; then
    echo "Файл .env не найден. Копирование из .env.example..."
    cp .env.example .env
    echo "Отредактируйте .env при необходимости"
fi

# Запуск сервера
echo "=================================="
echo "Сервер запускается на http://0.0.0.0:5000"
echo "Для остановки нажмите Ctrl+C"
echo "=================================="
python app.py
