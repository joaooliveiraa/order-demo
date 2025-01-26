package com.example.demo.controller;

import com.example.demo.dto.PedidoDto;
import com.example.demo.entity.Pedido;
import com.example.demo.producer.PedidoProducer;
import com.example.demo.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoProducer pedidoProducer;

    @PostMapping
    public ResponseEntity<Void> criarPedido(@RequestBody PedidoDto pedidoDto) {
        pedidoService.enviarParaKafka(pedidoDto);   //Envia para Kafka para processamento assíncrono
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> consultarPedidos(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "status", required = false) String status) {
        List<Pedido> pedidos = pedidoService.consultarPedidos(id, status);
        return ResponseEntity.ok(pedidos);
    }

}
