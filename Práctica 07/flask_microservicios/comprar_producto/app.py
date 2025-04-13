from flask import Flask, request, jsonify
import sqlite3

app = Flask(__name__)

@app.route('/comprar', methods=['POST'])
def comprar():
    data = request.get_json()
    producto_id = int(data['id'])
    cantidad_comprada = int(data['cantidad'])

    conn = sqlite3.connect('../productos.db')
    cursor = conn.cursor()
    cursor.execute("SELECT cantidad FROM productos WHERE id = ?", (producto_id,))
    row = cursor.fetchone()

    if not row:
        return jsonify({"error": "Producto no encontrado"}), 404

    cantidad_actual = row[0]
    if cantidad_comprada > cantidad_actual:
        return jsonify({"error": "Stock insuficiente"}), 400

    nueva_cantidad = cantidad_actual - cantidad_comprada
    cursor.execute("UPDATE productos SET cantidad = ? WHERE id = ?", (nueva_cantidad, producto_id))
    conn.commit()
    conn.close()

    return jsonify({"mensaje": "Compra realizada"}), 200

if __name__ == '__main__':
    app.run(port=5002)
