package br.com.fiap.filmes;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Fiap 2DVP - 2019 / MICROSERVICES ARCHITECTURE / API / CONTAINERS / TADEU D’ALESSANDRO BARBOSA
 * RM 333328 - EDMILSON FIGUEIREDO
 * RM 334231 - BRUNO MARQUES DOS SANTOS PANASIO
 * RM 334014 - RODRIGO BARBOZA GONÇALVES
 * RM 334401 - RICARDO AMARAL CAEIRO
 * */
@SpringBootApplication
@RestController
@EnableEurekaClient
@EnableCircuitBreaker
public class Application {

    private static final Logger LOG= Logger.getLogger( Application.class.getName() );

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping("/hello_filmes")
    public String hello() {
        LOG.log(Level.INFO, "requested Service 'filmes'");
        return "Hello from service 'filmes'.";
    }

    @Bean
    public Exchange fanoutExchange(@Value("${fanout.exchange.name}") final String exchangeName) {
        return new FanoutExchange(exchangeName);
    }
}

