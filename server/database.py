import psycopg2
from psycopg2.extras import RealDictCursor
from config import Config
import logging

logger = logging.getLogger(__name__)

def get_db_connection():
    """Создание подключения к базе данных PostgreSQL"""
    try:
        conn = psycopg2.connect(
            Config.get_db_connection_string(),
            cursor_factory=RealDictCursor
        )
        return conn
    except psycopg2.Error as e:
        logger.error(f"Ошибка подключения к БД: {e}")
        raise

def execute_query(query, params=None, fetchone=False, fetchall=False, commit=False):
    """
    Выполнение SQL запроса

    Args:
        query: SQL запрос
        params: Параметры запроса
        fetchone: Вернуть одну запись
        fetchall: Вернуть все записи
        commit: Выполнить commit

    Returns:
        Результат запроса или None
    """
    conn = None
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        cursor.execute(query, params)

        result = None
        if fetchone:
            result = cursor.fetchone()
        elif fetchall:
            result = cursor.fetchall()

        if commit:
            conn.commit()
            if cursor.description and not fetchone and not fetchall:
                result = cursor.fetchone()

        cursor.close()
        return result
    except psycopg2.Error as e:
        if conn:
            conn.rollback()
        logger.error(f"Ошибка выполнения запроса: {e}")
        raise
    finally:
        if conn:
            conn.close()
