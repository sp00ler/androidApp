from flask import Flask, request, jsonify
from flask_cors import CORS
from werkzeug.security import generate_password_hash, check_password_hash
import logging
from datetime import datetime

from config import Config
from database import execute_query

# Настройка логирования
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

app = Flask(__name__)
app.config.from_object(Config)
CORS(app)


@app.route('/health', methods=['GET'])
def health_check():
    """Проверка работоспособности сервера"""
    return jsonify({"status": "ok", "timestamp": datetime.now().isoformat()}), 200


@app.route('/products', methods=['GET'])
def get_products():
    """Получение списка товаров"""
    try:
        query = """
            SELECT id, name, price, stock_qty, image_key
            FROM products
            WHERE stock_qty > 0
            ORDER BY id
        """
        products = execute_query(query, fetchall=True)

        return jsonify({
            "ok": True,
            "products": products
        }), 200
    except Exception as e:
        logger.error(f"Ошибка получения товаров: {e}")
        return jsonify({
            "ok": False,
            "error": "Ошибка получения товаров"
        }), 500


@app.route('/auth/register', methods=['POST'])
def register():
    """Регистрация нового пользователя"""
    try:
        data = request.get_json()

        if not data or not data.get('login') or not data.get('password'):
            return jsonify({
                "ok": False,
                "error": "Необходимо указать логин и пароль"
            }), 400

        login = data['login'].strip()
        password = data['password']

        # Валидация
        if len(login) < 3:
            return jsonify({
                "ok": False,
                "error": "Логин должен быть не короче 3 символов"
            }), 400

        if len(password) < 4:
            return jsonify({
                "ok": False,
                "error": "Пароль должен быть не короче 4 символов"
            }), 400

        # Проверка существования пользователя
        check_query = "SELECT id FROM users WHERE login = %s"
        existing_user = execute_query(check_query, (login,), fetchone=True)

        if existing_user:
            return jsonify({
                "ok": False,
                "error": "Пользователь с таким логином уже существует"
            }), 409

        # Создание пользователя
        password_hash = generate_password_hash(password)
        insert_query = """
            INSERT INTO users (login, password_hash, created_at)
            VALUES (%s, %s, NOW())
            RETURNING id, login, created_at
        """
        user = execute_query(insert_query, (login, password_hash), commit=True)

        return jsonify({
            "ok": True,
            "message": "Регистрация успешна",
            "user": {
                "id": user['id'],
                "login": user['login']
            }
        }), 201
    except Exception as e:
        logger.error(f"Ошибка регистрации: {e}")
        return jsonify({
            "ok": False,
            "error": "Ошибка регистрации"
        }), 500


@app.route('/auth/login', methods=['POST'])
def login():
    """Авторизация пользователя"""
    try:
        data = request.get_json()

        if not data or not data.get('login') or not data.get('password'):
            return jsonify({
                "ok": False,
                "error": "Необходимо указать логин и пароль"
            }), 400

        login = data['login'].strip()
        password = data['password']

        # Поиск пользователя
        query = "SELECT id, login, password_hash FROM users WHERE login = %s"
        user = execute_query(query, (login,), fetchone=True)

        if not user or not check_password_hash(user['password_hash'], password):
            return jsonify({
                "ok": False,
                "error": "Неверный логин или пароль"
            }), 401

        # В продакшене здесь должен быть JWT токен
        token = f"demo-token-{user['id']}"

        return jsonify({
            "ok": True,
            "message": "Авторизация успешна",
            "user": {
                "id": user['id'],
                "login": user['login']
            },
            "token": token
        }), 200
    except Exception as e:
        logger.error(f"Ошибка авторизации: {e}")
        return jsonify({
            "ok": False,
            "error": "Ошибка авторизации"
        }), 500


@app.route('/orders', methods=['POST'])
def create_order():
    """Оформление заказа"""
    try:
        data = request.get_json()

        if not data or not data.get('user_id') or not data.get('items'):
            return jsonify({
                "ok": False,
                "error": "Необходимо указать user_id и items"
            }), 400

        user_id = data['user_id']
        items = data['items']

        if not isinstance(items, list) or len(items) == 0:
            return jsonify({
                "ok": False,
                "error": "Список товаров пуст"
            }), 400

        # Расчет общей суммы и проверка остатков
        total = 0
        order_items_data = []

        for item in items:
            product_id = item.get('product_id')
            qty = item.get('qty', 1)

            if not product_id or qty <= 0:
                return jsonify({
                    "ok": False,
                    "error": "Некорректные данные товара"
                }), 400

            # Получение информации о товаре
            product_query = "SELECT id, name, price, stock_qty FROM products WHERE id = %s"
            product = execute_query(product_query, (product_id,), fetchone=True)

            if not product:
                return jsonify({
                    "ok": False,
                    "error": f"Товар с id={product_id} не найден"
                }), 404

            if product['stock_qty'] < qty:
                return jsonify({
                    "ok": False,
                    "error": f"Недостаточно товара '{product['name']}' на складе"
                }), 400

            item_total = float(product['price']) * qty
            total += item_total

            order_items_data.append({
                'product_id': product_id,
                'qty': qty,
                'price': product['price']
            })

        # Создание заказа
        order_query = """
            INSERT INTO orders (user_id, created_at, total)
            VALUES (%s, NOW(), %s)
            RETURNING id, created_at
        """
        order = execute_query(order_query, (user_id, total), commit=True)
        order_id = order['id']

        # Добавление позиций заказа и обновление остатков
        for item_data in order_items_data:
            # Добавление позиции
            item_query = """
                INSERT INTO order_items (order_id, product_id, qty, price_at_purchase)
                VALUES (%s, %s, %s, %s)
            """
            execute_query(item_query, (
                order_id,
                item_data['product_id'],
                item_data['qty'],
                item_data['price']
            ), commit=True)

            # Обновление остатка
            update_query = """
                UPDATE products
                SET stock_qty = stock_qty - %s
                WHERE id = %s
            """
            execute_query(update_query, (item_data['qty'], item_data['product_id']), commit=True)

        return jsonify({
            "ok": True,
            "message": "Заказ успешно оформлен",
            "order": {
                "id": order_id,
                "total": f"{total:.2f}",
                "created_at": order['created_at'].isoformat()
            }
        }), 201
    except Exception as e:
        logger.error(f"Ошибка оформления заказа: {e}")
        return jsonify({
            "ok": False,
            "error": "Ошибка оформления заказа"
        }), 500


@app.errorhandler(404)
def not_found(error):
    return jsonify({"ok": False, "error": "Endpoint не найден"}), 404


@app.errorhandler(500)
def internal_error(error):
    return jsonify({"ok": False, "error": "Внутренняя ошибка сервера"}), 500


if __name__ == '__main__':
    logger.info(f"Запуск сервера на {Config.HOST}:{Config.PORT}")
    app.run(host=Config.HOST, port=Config.PORT, debug=Config.DEBUG)
