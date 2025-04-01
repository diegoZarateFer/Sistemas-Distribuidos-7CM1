from flask import Flask, render_template, request, redirect, url_for
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///store.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)

class Product(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(100), nullable=False)
    description = db.Column(db.String(200), nullable=False)
    price = db.Column(db.Float, nullable=False)
    stock = db.Column(db.Integer, nullable=False)

with app.app_context():
    db.create_all()

@app.route('/')
def index():
    products = Product.query.all()
    return render_template('index.html', products=products)

@app.route('/add', methods=['GET', 'POST'])
def add_product():
    if request.method == 'POST':
        name = request.form['name']
        description = request.form['description']
        price = request.form['price']
        stock = request.form['stock']

        new_product = Product(name=name, description=description, price=float(price), stock=int(stock))
        db.session.add(new_product)
        db.session.commit()

        return redirect(url_for('index'))
    return render_template('add_product.html')

@app.route('/buy/<int:product_id>', methods=['POST'])
def buy_product(product_id):
    product = Product.query.get_or_404(product_id)
    if product.stock > 0:
        product.stock -= 1
        db.session.commit()
        message = f"Compra exitosa. Te quedan {product.stock} unidades."
    else:
        message = "Producto agotado."
    return redirect(url_for('index', message=message))

@app.route('/inventory')
def inventory():
    products = Product.query.all()
    return render_template('inventory.html', products=products)

if __name__ == '__main__':
    app.run(debug=True)
