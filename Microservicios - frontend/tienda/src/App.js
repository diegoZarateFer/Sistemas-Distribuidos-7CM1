import React, { useState } from "react";
import AgregarProducto from "./components/AgregarProducto";
import ComprarProducto from "./components/ComprarProducto";
import Inventario from "./components/Inventario";
import "./App.css";

function App() {
  const [activeTab, setActiveTab] = useState("agregar");

  const [refreshInventario, setRefreshInventario] = useState(false);

  const cambiarPestana = (tab) => {
    setActiveTab(tab);
    if (tab === "inventario") {
      setRefreshInventario((prev) => !prev);
    }
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
        <AgregarProducto onProductoAgregado={() => setRefreshInventario((r) => !r)} />
      )}

      {activeTab === "comprar" && (
        <ComprarProducto onCompraExitosa={() => setRefreshInventario((r) => !r)} />
      )}

      {activeTab === "inventario" && <Inventario key={refreshInventario} />}
    </div>
  );
}

export default App;
