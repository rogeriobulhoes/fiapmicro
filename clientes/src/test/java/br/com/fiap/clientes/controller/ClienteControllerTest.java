package br.com.fiap.clientes.controller;

import br.com.fiap.clientes.TestUtil;
import br.com.fiap.clientes.client.ChamadoDiscoveryClient;
import br.com.fiap.clientes.model.Chamado;
import br.com.fiap.clientes.model.Cliente;
import br.com.fiap.clientes.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bouncycastle.cms.RecipientId.password;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    ClienteController clienteController;

    @MockBean
    ClienteService clienteService;

    @MockBean
    ChamadoDiscoveryClient chamadoDiscoveryClient;

    @Autowired
    private ObjectMapper om;

    Cliente cli1;
    Chamado tkt1;

    @Before
    public void setup() {
        cli1 = new Cliente(1L, "cliente1", "1234567890", 25);
        tkt1 = new Chamado(cli1.getId(), "filme nao funciona");
        tkt1.setDescricao("Contentam solicitar meu envenenou dementava nas lar rapaselho belchiore. Ordenou quantas teu tua " +
                "fizeram ficamos das boa simples. Ca de submetter repetidas eu comparsas. Firmeza dez mas mal" +
                " diz degraus queriam. Ha operem justas na chamma. Dou lhe litigantes sympathias gravemente ira. " +
                "Busquemos essencial desalinho so desataram as na respondeu encontrou. Minima abysmo animar ar " +
                "sentar forcar tornas os da. Encostado emmudecer rua clamoroso dei foi viu contribue.");
        tkt1.setDataAbertura(LocalDateTime.now().minusDays(1));
        tkt1.setId(UUID.randomUUID());
    }

    @Test
    public void shouldOpenChamadoForCliente() throws Exception {
        when(clienteService.findById(anyLong())).thenReturn(cli1);
        when(chamadoDiscoveryClient.openTicket(any(Chamado.class))).thenReturn(tkt1);

        MapType mapType = om.getTypeFactory()
                .constructMapType(LinkedHashMap.class, String.class, Object.class);

        Map<String, Object> map = om.convertValue(tkt1, mapType);
        map.put("id", null);
        map.put("cliente", null);
        map.put("dataAbertura", null);
        map.put("dataFechamento", null);

        String chamadostr = om.writerWithDefaultPrettyPrinter().writeValueAsString(map);

        //String jsonContent = TestUtil.asJsonString(tkt1);
        MvcResult result = mvc.perform(post("/v1/clientes/{id}/chamados", cli1.getId())
                .content(chamadostr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.dataAbertura", notNullValue()))
                .andExpect(jsonPath("$.dataFechamento", nullValue()))
                .andExpect(jsonPath("$.cliente", is(cli1.getId().intValue())))
                .andDo(print())
                .andReturn()
                ;

        String response = result.getResponse().getContentAsString();
        UUID id = UUID.fromString(JsonPath.parse(response).read("$.id"));
        LocalDateTime data = LocalDateTime.parse(JsonPath.parse(response).read("$.dataAbertura"));

        assertThat(id).isInstanceOf(UUID.class);
        assertThat(data).isInstanceOf(LocalDateTime.class);

        verify(clienteService, times(1)).findById(anyLong());
        verify(chamadoDiscoveryClient, times(1)).openTicket(any(Chamado.class));

    }
}
