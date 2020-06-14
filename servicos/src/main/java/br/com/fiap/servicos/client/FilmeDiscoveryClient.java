package br.com.fiap.servicos.client;

import br.com.fiap.servicos.model.Filme;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class FilmeDiscoveryClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand
    public Filme fallback(long filmeId) {
        return new Filme(filmeId, "filme Fallback - Fiap2DVP", 2019, "drama", "portugues", "filme-fallback", 0, 0);
    }

    @HystrixCommand(fallbackMethod = "fallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500")
            },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "101"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1440")
            })
    public Filme findFilmeById(long filmeId) {
        List<ServiceInstance> instances = discoveryClient.getInstances("filmes");

        if (instances.size() == 0) {
            return null;
        }

        String serviceUri = String.format("%s/v1/filmes/%d", instances.get(0).getUri().toString(), filmeId);

        ResponseEntity<Filme> restExchange =
                restTemplate.getForEntity(serviceUri, Filme.class);

        return restExchange.getBody();
    }

}
