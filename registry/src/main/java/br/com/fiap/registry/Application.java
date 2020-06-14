package br.com.fiap.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/*
 * Fiap 2DVP - 2019 / MICROSERVICES ARCHITECTURE / API / CONTAINERS / TADEU D’ALESSANDRO BARBOSA
 * RM 333328 - EDMILSON FIGUEIREDO
 * RM 334231 - BRUNO MARQUES DOS SANTOS PANASIO
 * RM 334014 - RODRIGO BARBOZA GONÇALVES
 * RM 334401 - RICARDO AMARAL CAEIRO
 * */
@EnableEurekaServer
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

