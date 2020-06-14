package br.com.fiap.servicos.repository;

import br.com.fiap.servicos.model.Like;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LikesRepository extends CrudRepository<Like, Long> {

    boolean existsByClienteIdAndFilmeId(@Param("clienteId") Long clienteId, @Param("filmeId") Long filmeId);

    Like findByClienteIdAndFilmeId(@Param("clienteId") Long clienteId, @Param("filmeId") Long filmeId);
}
