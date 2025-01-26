package com.example.demo.service;

import com.example.demo.entity.Pedido;
import com.example.demo.entity.Produto;
import com.example.demo.repository.PedidoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "topico-pedido";

    public Pedido processarPedido(Pedido pedido) {
        BigDecimal valorTotal = pedido.getProdutos().stream()
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(valorTotal);
        pedido.setStatus("Finalizado");

        pedidoRepository.save(pedido);
        return pedido;
    }

    public void enviarParaKafka(Pedido pedido) {
        String pedidoJson = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            pedidoJson = objectMapper.writeValueAsString(pedido);
        } catch (Exception e) {

        }
        System.out.println("Pedido enviado " + pedido.getStatus());
        kafkaTemplate.send(TOPIC, pedidoJson);
    }

    public Pedido consultarPedido(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
}
