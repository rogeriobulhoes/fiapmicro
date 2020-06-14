package br.com.fiap.filmes.service;

import br.com.fiap.filmes.model.Filme;
import br.com.fiap.filmes.repository.FilmeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class FilmeServiceTest {

    @Mock
    private FilmeRepository filmeRepository;

    @InjectMocks
    private FilmeService filmeService;

    List<Filme> filmes;
    Filme fil1, fil2, fil3;

    @Before()
    public void setup() {
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


        filmes = Arrays.asList(fil1, fil2);
    }

    @Test
    public void shouldRetrieveAllFilmes() {
        Page<Filme> page = new PageImpl<>(filmes);
        when(filmeRepository.findAll(any(Pageable.class))).thenReturn(page);

        assertThat(filmeService.findAll(PageRequest.of(0, 10))).isEqualTo(page);
        verify(filmeRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void shouldRetrieveAllFilmesByGenero() {
        final String BUSCA = "ficção";
        Page<Filme> page = new PageImpl<>(
                filmes
                        .stream()
                        .filter(f -> f.getGenero().equals(BUSCA)).collect(Collectors.toList())
        );

        when(filmeRepository.findAllByGenero(any(Pageable.class), anyString())).thenReturn(page);

        assertThat(filmeService.findAllByGenero(PageRequest.of(0, 10), BUSCA)).isEqualTo(page);
        verify(filmeRepository, times(1)).findAllByGenero(any(Pageable.class), anyString());
    }
    @Test
    public void shouldRetrieveAllFilmesLikeTitulo() {
        final String BUSCA = "Back To";
        filmes = filmes
                .stream()
                .filter(f -> f.getGenero().startsWith(BUSCA)).collect(Collectors.toList());
        Page<Filme> page = new PageImpl<>(filmes);

        when(filmeRepository.findByTituloContains(any(Pageable.class), anyString())).thenReturn(page);

        assertThat(filmeService.findAllLikeTitulo(PageRequest.of(0, 10), BUSCA)).isEqualTo(page);
        verify(filmeRepository, times(1)).findByTituloContains(any(Pageable.class), anyString());
    }

    @Test
    public void shouldFindFilmeById() {
        when(filmeRepository.findById(1L)).thenReturn(Optional.of(fil1));
        assertThat(filmeService.findById(1L)).isEqualTo(fil1);
        verify(filmeRepository, times(1)).findById(anyLong());
    }

    @Test
    public void shouldThrowFilmeNotFoundExceptionForFilmeByIdNull() {
        assertThatExceptionOfType(FilmeNotFoundException.class)
                .isThrownBy(() -> filmeService.findById(null)).withMessage("id invalido");
        verify(filmeRepository, times(0)).findById(null);
    }

    @Test
    public void shouldAddAValidFilme() {
        fil1.setId(null);
        when(filmeRepository.save(any(Filme.class))).thenAnswer(i -> {fil1.setId(1L); return fil1;});

        Filme res = filmeService.addFilme(fil1);

        assertThat(res).hasNoNullFieldsOrProperties();
        assertThat(res.getId()).isEqualTo(fil1.getId());
    }
}
