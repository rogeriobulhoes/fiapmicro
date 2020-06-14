package br.com.fiap.servicos.repository;

import br.com.fiap.servicos.model.Favorito;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FavoritosRepository extends CrudRepository<Favorito, Long> {

    boolean existsByClienteIdAndFilmeId(@Param("clienteId") long clienteId, @Param("filmeId") long filmeId);

    Favorito findByClienteIdAndFilmeId(@Param("clienteId") Long clienteId, @Param("filmeId") Long filmeId);
}
