#!/bin/bash

# Скрипт запуска сервера FastenerShop

echo "Запуск PostgreSQL..."
sudo service postgresql start

echo "Проверка подключения к БД..."
sleep 2

echo "Запуск Flask сервера..."
cd /home/user/androidApp/server
python app.py
