#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Тестовый скрипт для проверки UTF-8 кодировки в JSON ответах"""

from flask import Flask, jsonify

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False
app.config['JSONIFY_MIMETYPE'] = 'application/json; charset=utf-8'
app.json.ensure_ascii = False

@app.after_request
def after_request(response):
    if response.content_type and 'application/json' in response.content_type:
        response.headers['Content-Type'] = 'application/json; charset=utf-8'
    return response

@app.route('/test')
def test():
    return jsonify({
        "ok": False,
        "error": "Ошибка подключения к базе данных"
    })

if __name__ == '__main__':
    print("Запуск тестового сервера на http://localhost:5001/test")
    app.run(host='127.0.0.1', port=5001, debug=True)
