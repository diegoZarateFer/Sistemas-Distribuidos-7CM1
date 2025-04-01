package com.diego.demo.controller;

import com.diego.demo.entity.Pedido;
import com.diego.demo.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @PostMapping("/{productoId}/{cantidad}")
    public Pedido realizarPedido(@PathVariable Long productoId, @PathVariable Integer cantidad) {
        return pedidoService.realizarPedido(productoId, cantidad);
    }
}