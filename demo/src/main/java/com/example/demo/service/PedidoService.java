package com.example.demo.service;

import com.example.demo.dto.PedidoDto;
import com.example.demo.entity.Pedido;
import com.example.demo.entity.Produto;
import com.example.demo.repository.PedidoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "topico-pedido";

    public Pedido processarPedido(PedidoDto pedidoDto) {

        Pedido pedido = new Pedido();

        BigDecimal valorTotal = pedidoDto.getProdutos().stream()
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setCliente(pedidoDto.getCliente());
        pedido.setValorTotal(valorTotal);
        pedido.setStatus("Finalizado");

        pedidoRepository.save(pedido);
        return pedido;
    }

    public void enviarParaKafka(PedidoDto pedidoDto) {
        String pedidoJson = serializarPedido(pedidoDto);
        if (pedidoJson != null) {
            kafkaTemplate.send(TOPIC, pedidoJson);
            System.out.println("Pedido enviado com sucesso - Status: " + pedidoDto.getStatus());
        } else {
            System.out.println("Falha ao serializar o pedido. Pedido não enviado.");
        }
    }

    private String serializarPedido(PedidoDto pedidoDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(pedidoDto);
        } catch (JsonProcessingException e) {
            System.out.println("Erro ao serializar PedidoDto para JSON: " + e.getMessage());
            return null;
        }
    }

    public List<Pedido> consultarPedidos(Long id, String status) {
        if (id != null) {
            return pedidoRepository.findById(id)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        }

        if (status != null && status.equalsIgnoreCase("finalizado")) {
            return pedidoRepository.findByStatus("Finalizado");
        }

        return pedidoRepository.findAll();
    }
}
