import sqlite3

conn = sqlite3.connect("productos.db")
cursor = conn.cursor()

cursor.execute("""
CREATE TABLE IF NOT EXISTS productos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    cantidad INTEGER NOT NULL
);
""")

conn.commit()
conn.close()
print("Base de datos creada.")
