from flask import Flask, request, jsonify
import sqlite3

app = Flask(__name__)

@app.route('/agregar', methods=['POST'])
def agregar():
    data = request.get_json()
    nombre = data['nombre']
    cantidad = int(data['cantidad'])

    conn = sqlite3.connect('../productos.db')
    cursor = conn.cursor()
    cursor.execute("INSERT INTO productos (nombre, cantidad) VALUES (?, ?)", (nombre, cantidad))
    conn.commit()
    conn.close()

    return jsonify({"mensaje": "Producto agregado"}), 201

if __name__ == '__main__':
    app.run(port=5001)
