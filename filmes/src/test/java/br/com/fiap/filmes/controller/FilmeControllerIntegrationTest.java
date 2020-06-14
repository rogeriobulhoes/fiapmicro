package br.com.fiap.filmes.controller;

import br.com.fiap.filmes.TestUtil;
import br.com.fiap.filmes.model.Filme;
import br.com.fiap.filmes.repository.FilmeRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FilmeControllerIntegrationTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private FilmeRepository repo;

    @Autowired
    private MockMvc mvc;

    private Page<Filme> page = null;

    List<Filme> filmes;
    Filme fil1, fil2, fil3;

    @Before
    public void setUp() throws Exception {

        fil1 = new Filme(1L, "Back To The Future", 1984, "ficção", "ingles", "filme", 0, 0);
        fil1.setDetalhe(
                "Old education him departure any arranging one prevailed. " +
                        "Their end whole might began her. Behaved the comfort another fifteen eat. " +
                        "Partiality had his themselves ask pianoforte increasing discovered. So mr delay " +
                        "at since place whole above miles. He to observe conduct at detract because. " +
                        "Way ham unwilling not breakfast furniture explained perpetual. " +
                        "Or mr surrounded conviction so astonished literature. Songs to an blush woman " +
                        "be sorry young. We certain as removal attempt.");

        fil2 = new Filme(2L, "Stranger Things", 2017, "ficção", "ingles", "serie", 0, 0);
        fil2.setDetalhe(
                "Now led tedious shy lasting females off. Dashwood marianne in of entrance " +
                        "be on wondered possible building. Wondered sociable he carriage in speedily margaret. " +
                        "Up devonshire of he thoroughly insensible alteration. An mr settling occasion insisted " +
                        "distance ladyship so. Not attention say frankness intention out dashwoods now curiosity. " +
                        "Stronger ecstatic as no judgment daughter speedily thoughts. Worse downs nor might she " +
                        "court did nay forth these.");

        fil3 = new Filme(3L, "The Wall", 1975, "documentario", "ingles", "filme", 0, 0);
        fil3.setDetalhe(
                "Projecting surrounded literature yet delightful alteration but bed men. " +
                        "Open are from long why cold. If must snug by upon sang loud left. " +
                        "As me do preference entreaties compliment motionless ye literature. " +
                        "Day behaviour explained law remainder. Produce can cousins account you pasture. " +
                        "Peculiar delicate an pleasant provided do perceive.");


        filmes = Arrays.asList(fil1, fil2, fil3);
        repo.saveAll(filmes);
    }

    @After
    public void clear() throws SQLException {
        repo.deleteAll();
        TestUtil.cleanupDatabase(dataSource);
    }

    @Test
    public void shoudRetrieveAllFilmes() throws Exception {
        mvc.perform(get("/v1/filmes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[*].id", hasSize(filmes.size())))
                .andExpect(jsonPath("$.content[*].titulo", hasSize(filmes.size())))
                .andExpect(jsonPath("$.content[0].titulo", is(fil1.getTitulo())))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andDo(print())
                .andReturn()
                ;
    }

    @Test
    public void shoudRetrieveAllFilmesByGenero() throws Exception {
        final String BUSCA = "ficção";
        filmes = filmes
           .stream()
           .filter(f -> f.getGenero().equals(BUSCA)).collect(Collectors.toList());

        mvc.perform(get("/v1/filmes?genero=ficção")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[*].id", hasSize(filmes.size())))
                .andExpect(jsonPath("$.content[0].genero", is("ficção")))
                .andDo(print())
                .andReturn()
        ;
    }

    @Test
    public void shoudRetrieveFilmesByTituloContains() throws Exception {
        final String BUSCA = "Back To";
        filmes = filmes
                .stream()
                .filter(f -> f.getTitulo().startsWith(BUSCA)).collect(Collectors.toList());

        mvc.perform(get("/v1/filmes?titulo="+BUSCA)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[*].id", hasSize(filmes.size())))
                .andExpect(jsonPath("$.content[0].titulo", is(startsWith(BUSCA))))
                .andDo(print())
                .andReturn()
        ;
    }

    @Test
    public void shoudRetrieveFilmeById() throws Exception {
        mvc.perform(get("/v1/filmes/{id}", fil2.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                ;
    }

    @Test
    public void shoudThrowFilmeNotFoundExceptionWhenFindFilmeById() throws Exception {
        mvc.perform(get("/v1/filmes/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.details").value(org.hamcrest.Matchers.containsString("/v1/filmes/0")))
                .andDo(print())
        ;
    }

    @Test
    public void shouldAddFilmeAndVerify() throws Exception {
        Filme fil3 = new Filme(null, "Game of Thrones", 2005, "aventura", "ingles", "serie", 0, 0);

        MvcResult result = mvc.perform(post("/v1/filmes")
                .content(TestUtil.asJsonString(fil3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.titulo", is(fil3.getTitulo())))
                .andDo(print())
                .andReturn()
                ;

        String response = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        mvc.perform(get("/v1/filmes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.titulo", is(fil3.getTitulo())))
                .andDo(print())
                .andReturn()
        ;
    }


}
