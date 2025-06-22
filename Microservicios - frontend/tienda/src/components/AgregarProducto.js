import React, { useState } from "react";

function AgregarProducto({ onProductoAgregado }) {
  const [nuevoProducto, setNuevoProducto] = useState({ nombre: "", cantidad: "" });

  const agregarProducto = () => {
    fetch("http://3.22.60.3/agregar", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(nuevoProducto),
    })
      .then(() => {
        setNuevoProducto({ nombre: "", cantidad: "" });
        alert("Producto agregado");
        if (onProductoAgregado) onProductoAgregado();
      })
      .catch(console.error);
  };

  return (
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
  );
}

export default AgregarProducto;
