
from flask import Flask, jsonify
import sqlite3

app = Flask(__name__)

@app.route('/inventario', methods=['GET'])
def inventario():
    conn = sqlite3.connect('../productos.db')
    cursor = conn.cursor()
    cursor.execute("SELECT id, nombre, cantidad FROM productos")
    productos = cursor.fetchall()
    conn.close()

    return jsonify([
        {"id": p[0], "nombre": p[1], "cantidad": p[2]} for p in productos
    ])

if __name__ == '__main__':
    app.run(port=5000)
