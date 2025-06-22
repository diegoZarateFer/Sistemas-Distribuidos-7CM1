import React, { useState, useEffect } from "react";
import "./App.css";

function App() {
  const [activeTab, setActiveTab] = useState("agregar");
  const [productos, setProductos] = useState([]);
  const [nuevoProducto, setNuevoProducto] = useState({
    nombre: "",
    cantidad: "",
  });
  const [compraId, setCompraId] = useState("");
  const [compraCantidad, setCompraCantidad] = useState(1);

  useEffect(() => {
    cargarInventario();
  }, []);

  const cargarInventario = () => {
    fetch("http://3.22.60.3/inventario")
      .then((res) => res.json())
      .then((data) => setProductos(data))
      .catch(console.error);
  };

  const cambiarPestana = (tab) => {
    setActiveTab(tab);
    if (tab === "inventario") {
      cargarInventario();
    }
  };

  const agregarProducto = () => {
    fetch("http://3.22.60.3/agregar", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(nuevoProducto),
    })
      .then(() => {
        setNuevoProducto({ nombre: "", cantidad: "" });
        alert("Producto agregado");
      })
      .catch(console.error);
  };

  const comprarProducto = () => {
    fetch("http://3.22.60.3/comprar", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        id: parseInt(compraId),
        cantidad: parseInt(compraCantidad),
      }),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Error en la compra");
        alert("Compra exitosa");
        setCompraId("");
        setCompraCantidad(1);
      })
      .catch(() => alert("Error: ID no válido o stock insuficiente"));
  };

  return (
    <div className="container">
      <h1>Microservicios de Tienda</h1>
      <div className="tabs">
        <button
          className={`tab ${activeTab === "agregar" ? "active" : ""}`}
          onClick={() => cambiarPestana("agregar")}
        >
          Agregar
        </button>
        <button
          className={`tab ${activeTab === "comprar" ? "active" : ""}`}
          onClick={() => cambiarPestana("comprar")}
        >
          Comprar
        </button>
        <button
          className={`tab ${activeTab === "inventario" ? "active" : ""}`}
          onClick={() => cambiarPestana("inventario")}
        >
          Inventario
        </button>
      </div>

      {activeTab === "agregar" && (
        <div className="form">
          <input
            type="text"
            placeholder="Nombre del producto"
            value={nuevoProducto.nombre}
            onChange={(e) =>
              setNuevoProducto({ ...nuevoProducto, nombre: e.target.value })
            }
          />
          <input
            type="number"
            placeholder="Cantidad"
            value={nuevoProducto.cantidad}
            onChange={(e) =>
              setNuevoProducto({ ...nuevoProducto, cantidad: e.target.value })
            }
          />
          <button onClick={agregarProducto}>Agregar producto</button>
        </div>
      )}

      {activeTab === "comprar" && (
        <div className="form">
          <input
            type="number"
            placeholder="ID del producto"
            value={compraId}
            onChange={(e) => setCompraId(e.target.value)}
          />
          <input
            type="number"
            placeholder="Cantidad"
            value={compraCantidad}
            onChange={(e) => setCompraCantidad(e.target.value)}
          />
          <button onClick={comprarProducto}>Comprar</button>
        </div>
      )}

      {activeTab === "inventario" && (
        <ul className="product-list">
          {productos.map((p) => (
            <li key={p.id} className="product-item">
              <span>
                {p.id} - {p.nombre} ({p.cantidad})
              </span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default App;
