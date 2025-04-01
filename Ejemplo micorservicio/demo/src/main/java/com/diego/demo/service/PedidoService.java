package com.diego.demo.service;

import com.diego.demo.entity.Pedido;
import com.diego.demo.entity.Producto;
import com.diego.demo.repository.PedidoRepository;
import com.diego.demo.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido realizarPedido(Long productoId, Integer cantidad) {
        Producto producto = productoRepository.findById(productoId).orElse(null);

        if (producto != null && producto.getStock() >= cantidad) {
            double total = producto.getPrecio() * cantidad;
            Pedido pedido = new Pedido(productoId, cantidad, total);
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);
            return pedidoRepository.save(pedido);
        }
        return null;
    }
}