package com.example.demo.consumer;

import com.example.demo.entity.Pedido;
import com.example.demo.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoConsumer {

    @Autowired
    private PedidoService pedidoService;

    @KafkaListener(topics = "topico-pedido", groupId = "pedido-group")
    public void consumirPedido(String pedidoJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Pedido pedido = objectMapper.readValue(pedidoJson, Pedido.class);
            Pedido pedidoProcessado = pedidoService.processarPedido(pedido);
            // Aqui você pode adicionar lógica para comunicar com o Produto Externo B se necessário
            System.out.println("Pedido processado: " + pedidoProcessado.getStatus());
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
        }
    }
}
