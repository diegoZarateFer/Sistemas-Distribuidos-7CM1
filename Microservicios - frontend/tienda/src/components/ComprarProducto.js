import React, { useState } from "react";

function ComprarProducto({ onCompraExitosa }) {
  const [compraId, setCompraId] = useState("");
  const [compraCantidad, setCompraCantidad] = useState(1);

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
        if (onCompraExitosa) onCompraExitosa();
      })
      .catch(() => alert("Error: ID no válido o stock insuficiente"));
  };

  return (
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
  );
}

export default ComprarProducto;
