package br.com.fiap.servicos.service;

import br.com.fiap.servicos.TestConfiguration;
import br.com.fiap.servicos.TestUtil;
import br.com.fiap.servicos.model.Cliente;
import br.com.fiap.servicos.model.Filme;
import br.com.fiap.servicos.model.Like;
import br.com.fiap.servicos.repository.LikesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "server.port:0",
        "eureka.client.enabled:false",
        "spring.application.name=servicos",
        "spring.cloud.discovery.client.simple.instances.clientes[0].uri=http://localhost:8888",
        "spring.cloud.discovery.client.simple.instances.filmes[0].uri=http://localhost:8888"})
@ActiveProfiles("test")
public class LikesServiceIntegrationTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private LikesRepository repo;

    @Autowired
    private LikesService likesService;

    Like like;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Value("${fanout.exchange.name}")
    String fanoutExchangeName;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8888);

    private static final String QUEUE_NAME = "filmes_events_queue";

    @Before
    public void setUp() throws Exception {
        repo.deleteAll();
        like = new Like(1L, 1L);

        //like = repo.save(like);
        Cliente cli1 = new Cliente(1L, "cliente1", "1234567890", 25);
        Filme fil1 = new Filme(2L, "Back To The Future", 1984, "ficção", "ingles", "filme", 0, 0);
        ObjectMapper om = new ObjectMapper();

        wireMockRule
                .stubFor(get(urlPathEqualTo("/v1/clientes/1"))
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(om.writeValueAsString(cli1)))
                );
        wireMockRule
                .stubFor(get(urlPathEqualTo("/v1/filmes/1"))
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(om.writeValueAsString(fil1)))
                );

    }

    @After
    public void clear() throws SQLException {
        repo.deleteAll();
        TestUtil.cleanupDatabase(dataSource);
        wireMockRule.stop();
    }

    @Test
    public void shouldMarkALikeOnAFilme() throws Exception {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class)) {

            queueAndExchangeSetup(context);

            likesService.marcar(1L, 1L);
            Message message = rabbitTemplate.receive(QUEUE_NAME);

            boolean res = repo.existsByClienteIdAndFilmeId(1L, 1L);

            assertTrue(res);
            assertThat(message).isNotNull();

//            wireMockRule.verify(1, getRequestedFor(urlPathEqualTo("/v1/clientes/1")));
//            wireMockRule.verify(1, getRequestedFor(urlPathEqualTo("/v1/filmes/1")));
        }
    }

    @Test
    public void shouldUnMarkALikeOnAFilme() throws Exception {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class)) {

            repo.save(like);
            likesService.marcar(1L, 1L);
            Message message = rabbitTemplate.receive(QUEUE_NAME);

            boolean res = repo.existsByClienteIdAndFilmeId(1L, 1L);
            queueAndExchangeSetup(context);

            assertFalse(res);
            assertThat(message).isNotNull();

//            wireMockRule.verify(1, getRequestedFor(urlPathEqualTo("/v1/clientes/1")));
//            wireMockRule.verify(1, getRequestedFor(urlPathEqualTo("/v1/filmes/1")));
        }
    }

    private void queueAndExchangeSetup(BeanFactory context) {

        Queue queue = new Queue(QUEUE_NAME, false);
        rabbitAdmin.declareQueue(queue);
        FanoutExchange exchange = new FanoutExchange(fanoutExchangeName);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange));

    }

}
