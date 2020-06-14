package br.com.fiap.servicos.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = "servicos_events_queue")
public class Receiver {

    @RabbitHandler
    public void process(byte[] event) {
        System.out.println(new String(event));
    }
}
