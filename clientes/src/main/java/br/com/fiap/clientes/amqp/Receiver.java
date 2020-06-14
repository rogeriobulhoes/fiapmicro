package br.com.fiap.clientes.amqp;

import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = "clientes_events_queue")
public class Receiver {

    @RabbitHandler
    public void process(byte[] event) {
        System.out.println(new String(event));
    }

}
