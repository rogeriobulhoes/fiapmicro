package br.com.fiap.servicos.amqp;

import br.com.fiap.servicos.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
public class RabbitMQTests {

    private static final String QUEUE_NAME = "filmes_events_queue";
    private static final String EXCHANGE_NAME = UUID.randomUUID().toString();

    @Test
    public void basic_get_case() {
        String messageBody = "Hello world!";
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class)) {
            RabbitTemplate rabbitTemplate = queueAndExchangeSetup(context);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "filmes.events.key1", messageBody);

            Message message = rabbitTemplate.receive(QUEUE_NAME);

            assertThat(message).isNotNull();
            assertThat(message.getBody()).isEqualTo(messageBody.getBytes());
        }
    }

    private RabbitTemplate queueAndExchangeSetup(BeanFactory context) {
        RabbitAdmin rabbitAdmin = context.getBean(RabbitAdmin.class);

        Queue queue = new Queue(QUEUE_NAME, false);
        rabbitAdmin.declareQueue(queue);
        Exchange exchange = new FanoutExchange(EXCHANGE_NAME);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with("filmes.events.*").noargs());


        return context.getBean(RabbitTemplate.class);
    }
}
