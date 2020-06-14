package br.com.fiap.filmes.service;

import br.com.fiap.filmes.TestUtil;
import br.com.fiap.filmes.model.Filme;
import br.com.fiap.filmes.repository.FilmeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FilmeServiceIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private FilmeRepository repo;

    @Autowired
    private FilmeService filmeService;

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
    public void shoudRetrieveAllFilmes() {
        page = new PageImpl<Filme>(filmes);
        final Page<Filme> all = filmeService.findAll(PageRequest.of(0, 10));
        assertThat(all.getContent()).isSubsetOf(filmes);
    }

    @Test
    public void shouldRetrieveAllFilmesByGenero() {
        final String BUSCA = "ficção";

        final Page<Filme> byGenero = filmeService.findAllByGenero(PageRequest.of(0, 10), BUSCA);
        assertThat(byGenero.getContent().size()).isEqualTo(2);
        assertThat(byGenero.getContent()).containsExactlyInAnyOrder(fil1, fil2);
    }

    @Test
    public void shouldRetrieveAllFilmesLikeTitulo() {
        final String BUSCA = "Back To";

        final Page<Filme> likeTitulo = filmeService.findAllLikeTitulo(PageRequest.of(0, 10), BUSCA);
        assertThat(likeTitulo.getContent().size()).isEqualTo(1);
        assertThat(likeTitulo.getContent()).containsExactlyInAnyOrder(fil1);
    }

    @Test
    public void shoudRetrieveFilmeById() {
        final Filme find = filmeService.findById(2L);
        assertThat(filmes).contains(find);
        assertThat(find).isEqualTo(fil2);
    }

    @Test
    public void shouldThrowFilmeNotFoundForFilmeWithoutValidId() {
        assertThatThrownBy(() -> filmeService.findById(0L))
                .isInstanceOf(FilmeNotFoundException.class)
                .hasMessage("id: 0");
    }

    @Test
    public void shouldThrowFilmeNotFoundExceptionForFilmeByIdNull() {
        assertThatThrownBy(() -> filmeService.findById(null))
                .isInstanceOf(FilmeNotFoundException.class)
                .hasMessage("id invalido");
    }

    @Test
    public void shouldAddAValidFilme() {

        fil1.setId(null);
        Filme res = filmeService.addFilme(fil1);
        fil1.setId(1L);
        assertThat(res).hasNoNullFieldsOrProperties();
        assertThat(res.getId()).isEqualTo(fil1.getId());
    }


}
