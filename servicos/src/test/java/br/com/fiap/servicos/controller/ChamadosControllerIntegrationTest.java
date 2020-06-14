package br.com.fiap.servicos.controller;

import br.com.fiap.servicos.TestUtil;
import br.com.fiap.servicos.model.Chamado;
import br.com.fiap.servicos.repository.ChamadosRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ChamadosControllerIntegrationTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ChamadosRepository repo;

    @Autowired
    private MockMvc mvc;

    Chamado tkt1;

    @Before
    public void setUp() throws Exception {
        repo.deleteAll();
        tkt1 = new Chamado(1L, "filme nao funciona");
        tkt1.setDescricao("Contentam solicitar meu envenenou dementava nas lar rapaselho belchiore. Ordenou quantas teu tua " +
                "fizeram ficamos das boa simples. Ca de submetter repetidas eu comparsas. Firmeza dez mas mal" +
                " diz degraus queriam. Ha operem justas na chamma. Dou lhe litigantes sympathias gravemente ira. " +
                "Busquemos essencial desalinho so desataram as na respondeu encontrou. Minima abysmo animar ar " +
                "sentar forcar tornas os da. Encostado emmudecer rua clamoroso dei foi viu contribue.");
        tkt1.setDataAbertura(LocalDateTime.now().minusDays(1));
        tkt1.setDataFechamento(LocalDateTime.now());

        tkt1 = repo.save(tkt1);

    }

    @After
    public void clear() throws SQLException {
        repo.deleteAll();
        TestUtil.cleanupDatabase(dataSource);
    }

    @Test
    public void shoudRetrieveChamadoById() throws Exception {
        mvc.perform(get("/v1/chamados/{id}", tkt1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                ;
    }

    @Test
    public void shoudThrowChamadoNotFoundExceptionWhenFindChamadoByIdInvalid() throws Exception {
        mvc.perform(get("/v1/chamados/{id}", "numerochamadoinexistente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.details").value(org.hamcrest.Matchers.containsString("/v1/chamados/numerochamadoinexistente")))
                .andDo(print())
        ;
    }

    @Test
    public void shouldAddChamadoAndVerify() throws Exception {
        Chamado tkt1 = new Chamado(1L, "filme nao funciona");
        tkt1.setDescricao("Contentam solicitar meu envenenou dementava nas lar rapaselho belchiore. Ordenou quantas teu tua " +
                "fizeram ficamos das boa simples. Ca de submetter repetidas eu comparsas. Firmeza dez mas mal" +
                " diz degraus queriam. Ha operem justas na chamma. Dou lhe litigantes sympathias gravemente ira. " +
                "Busquemos essencial desalinho so desataram as na respondeu encontrou. Minima abysmo animar ar " +
                "sentar forcar tornas os da. Encostado emmudecer rua clamoroso dei foi viu contribue.");

        MvcResult result = mvc.perform(post("/v1/chamados")
                .content(TestUtil.asJsonString(tkt1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.dataAbertura", is(notNullValue())))
                .andDo(print())
                .andReturn()
                ;

        String response = result.getResponse().getContentAsString();
        UUID id = UUID.fromString(JsonPath.parse(response).read("$.id"));
        LocalDateTime data = LocalDateTime.parse(JsonPath.parse(response).read("$.dataAbertura"));

        mvc.perform(get("/v1/chamados/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.dataAbertura", is(notNullValue())))
                .andExpect(jsonPath("$.dataFechamento", is(nullValue())))
                .andDo(print())
                .andReturn()
        ;
    }

    @Test
    public void shouldCloseChamadoAndVerify() throws Exception {
        Chamado tkt1 = new Chamado(1L, "filme nao funciona");
        tkt1.setDescricao("Contentam solicitar meu envenenou dementava nas lar rapaselho belchiore. Ordenou quantas teu tua " +
                "fizeram ficamos das boa simples. Ca de submetter repetidas eu comparsas. Firmeza dez mas mal" +
                " diz degraus queriam. Ha operem justas na chamma. Dou lhe litigantes sympathias gravemente ira. " +
                "Busquemos essencial desalinho so desataram as na respondeu encontrou. Minima abysmo animar ar " +
                "sentar forcar tornas os da. Encostado emmudecer rua clamoroso dei foi viu contribue.");

        MvcResult result = mvc.perform(post("/v1/chamados")
                .content(TestUtil.asJsonString(tkt1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.dataAbertura", is(notNullValue())))
                .andExpect(jsonPath("$.dataFechamento", is(nullValue())))
                .andDo(print())
                .andReturn()
                ;

        String response = result.getResponse().getContentAsString();
        UUID id = UUID.fromString(JsonPath.parse(response).read("$.id"));
        LocalDateTime data = LocalDateTime.parse(JsonPath.parse(response).read("$.dataAbertura"));

        mvc.perform(put("/v1/chamados/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.dataFechamento", is(notNullValue())))
                .andDo(print())
                .andReturn()
        ;

    }


}
