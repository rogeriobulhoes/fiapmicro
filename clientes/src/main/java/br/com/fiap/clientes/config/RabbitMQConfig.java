package br.com.fiap.clientes.config;

import br.com.fiap.clientes.amqp.Receiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue getEventTopic() {
        return new Queue("clientes_events_queue");
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setConsumerTagStrategy(q -> "myConsumerFor." + q);
        factory.setConcurrentConsumers(2);
        return factory;
    }

    //@Profile("receiver")
    @Bean
    public Receiver receiver() {
        return new Receiver();
    }

    @Bean
    public Binding binding1(final Queue queue1, final Exchange fanoutExchange) {
        return BindingBuilder.bind(queue1).to(fanoutExchange).with("clientes.events.*").noargs();
    }

}