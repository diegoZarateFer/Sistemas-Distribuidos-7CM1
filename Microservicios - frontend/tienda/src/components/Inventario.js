import React, { useEffect, useState } from "react";

function Inventario() {
  const [productos, setProductos] = useState([]);

  const cargarInventario = () => {
    fetch("http://3.22.60.3/inventario")
      .then((res) => res.json())
      .then((data) => setProductos(data))
      .catch(console.error);
  };

  useEffect(() => {
    cargarInventario();
  }, []);

  return (
    <ul className="product-list">
      {productos.map((p) => (
        <li key={p.id} className="product-item">
          <span>
            {p.id} - {p.nombre} ({p.cantidad})
          </span>
        </li>
      ))}
    </ul>
  );
}

export default Inventario;
