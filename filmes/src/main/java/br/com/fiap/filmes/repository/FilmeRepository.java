package br.com.fiap.filmes.repository;

import br.com.fiap.filmes.model.Filme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilmeRepository extends PagingAndSortingRepository<Filme, Long> {

    Page<Filme> findAllByGenero(Pageable pageable, @Param("genero") String genero);

    Page<Filme> findByTituloContains(Pageable pageable, @Param("titulo") String titulo);

    @Query("select f from Filme f where f.assistido > 0 order by f.assistido desc, f.titulo")
    Page<Filme> findByAssistidoGreaterThan(Pageable pageable);

    //@Query("select f from Filme f where f.genero = ?1 and f.assistido > 0 order by f.assistido desc, f.titulo")
    Page<Filme> findAllByGeneroAndAssistidoGreaterThan(Pageable pageable, @Param("genero") String genero, @Param("assistido") Integer assistido);
}
