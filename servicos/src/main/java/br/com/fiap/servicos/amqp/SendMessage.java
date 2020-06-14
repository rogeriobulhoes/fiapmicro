package br.com.fiap.servicos.amqp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SendMessage {
    //private final RabbitTemplate rabbitTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${fanout.exchange.name}")
    String exchangeName;

    public void sendLikeMessage(Long filmeId, int val) throws JsonProcessingException {
        handleSendMessage(filmeId, val, "likes");
    }

    public void sendAssistidosMessage(Long filmeId) throws JsonProcessingException {
        handleSendMessage(filmeId, 0, "assistidos");
    }

    public void sendFavoritosMessage(Long filmeId, int val) throws JsonProcessingException {
        handleSendMessage(filmeId, val, "favoritos");
    }

    private void handleSendMessage(long filmeId, int val, String tipo) throws JsonProcessingException {
        Map<String, String> payload = new HashMap<>();
        payload.put("tipo", "tipo");
        payload.put("filmeId", String.valueOf(filmeId));
        payload.put("val", String.valueOf(val));
        String msg = objectMapper.writeValueAsString(payload);
        Message message = MessageBuilder
                .withBody(msg.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
        this.rabbitTemplate.convertAndSend("filmes_events_queue", message);
    }
}