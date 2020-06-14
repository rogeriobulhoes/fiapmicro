package br.com.fiap.clientes.client;

import br.com.fiap.clientes.model.Chamado;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Service;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ChamadoDiscoveryClientTest {

    @Mock
    DiscoveryClient discoveryClient;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    ChamadoDiscoveryClient chamadoDiscoveryClient;

    @Test
    public void shouldOpenTicket() {
        Chamado tkt1 = new Chamado(1L, "filme nao funciona");
        tkt1.setDescricao("Contentam solicitar meu envenenou dementava nas lar rapaselho belchiore. Ordenou quantas teu tua " +
                "fizeram ficamos das boa simples. Ca de submetter repetidas eu comparsas. Firmeza dez mas mal" +
                " diz degraus queriam. Ha operem justas na chamma. Dou lhe litigantes sympathias gravemente ira. " +
                "Busquemos essencial desalinho so desataram as na respondeu encontrou. Minima abysmo animar ar " +
                "sentar forcar tornas os da. Encostado emmudecer rua clamoroso dei foi viu contribue.");
        tkt1.setDataAbertura(LocalDateTime.now().minusDays(1));
        tkt1.setId(UUID.randomUUID());


        java.net.URI uri = URI.create("/v1/chamados");
        ServiceInstance instance = mock(ServiceInstance.class);

        when(instance.getUri()).thenReturn(uri);
        List<ServiceInstance> instances = Arrays.asList(instance);
        when(discoveryClient.getInstances("servicos")).thenReturn(instances);
        when(restTemplate.postForEntity(anyString(), any(Chamado.class), eq(Chamado.class)))
                .thenReturn(new ResponseEntity<Chamado>(tkt1, HttpStatus.CREATED));

        Chamado resp = chamadoDiscoveryClient.openTicket(tkt1);
        assertThat(tkt1.getCliente()).isEqualTo(resp.getCliente());
        assertThat(resp.getId()).isNotNull();
        assertThat(resp.getId()).isInstanceOf(UUID.class);
        assertThat(resp.getDataAbertura()).isInstanceOf(LocalDateTime.class);
        assertThat(resp.getDataFechamento()).isNull();
        assertThat(tkt1.getDescricao()).isEqualTo(resp.getDescricao());
        assertThat(tkt1.getMotivo()).isEqualTo(resp.getMotivo());

        verify(discoveryClient, times(1)).getInstances("servicos");
        verify(restTemplate, times(1)).postForEntity(anyString(), any(Chamado.class), eq(Chamado.class));
    }

}
