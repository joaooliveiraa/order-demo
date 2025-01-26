package com.example.demo.producer;

import com.example.demo.entity.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PedidoProducer {

    @Autowired
    private KafkaTemplate<String, Pedido> kafkaTemplate;

    private static final String TOPIC = "topico-pedido";

    public void enviarParaKafka(Pedido pedido) {
        kafkaTemplate.send(TOPIC, pedido);
    }
}
