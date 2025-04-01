package com.diego.demo.service;

import com.diego.demo.entity.Producto;
import com.diego.demo.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto obtenerProducto(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public void actualizarStock(Long id, int nuevoStock) {
        Producto producto = obtenerProducto(id);
        if (producto != null) {
            producto.setStock(nuevoStock);
            productoRepository.save(producto);
        }
    }
}