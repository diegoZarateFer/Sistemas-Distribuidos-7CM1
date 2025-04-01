package com.diego.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productoId;
    private Integer cantidad;
    private Double total;

    public Pedido() {}

    public Pedido(Long productoId, Integer cantidad, Double total) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.total = total;
    }

    public Long getId() { return id; }
    public Long getProductoId() { return productoId; }
    public Integer getCantidad() { return cantidad; }
    public Double getTotal() { return total; }
}
