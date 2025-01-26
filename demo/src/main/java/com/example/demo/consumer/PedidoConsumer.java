package com.example.demo.consumer;

import com.example.demo.dto.PedidoDto;
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
            PedidoDto pedidoDto = objectMapper.readValue(pedidoJson, PedidoDto.class);
            Pedido pedidoProcessado = pedidoService.processarPedido(pedidoDto);
            // Nesse ponto poderia ser adicionado logica para se comunicar com o Produto Externo B.
            System.out.println("Pedido processado com sucesso - Status: " + pedidoProcessado.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
