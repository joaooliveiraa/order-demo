package com.example.demo.controller;

import com.example.demo.entity.Pedido;
import com.example.demo.producer.PedidoProducer;
import com.example.demo.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoProducer pedidoProducer;

    @PostMapping
    public ResponseEntity<Void> criarPedido(@RequestBody Pedido pedido) {
        pedidoService.enviarParaKafka(pedido);   //Envia para Kafka para processamento assíncrono
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> consultarPedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.consultarPedido(id);  // Consulta do pedido após cálculo
        return ResponseEntity.ok(pedido);
    }
}
