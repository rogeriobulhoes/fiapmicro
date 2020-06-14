package br.com.fiap.servicos.repository;

import br.com.fiap.servicos.model.Assistido;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AssistidosRepository extends CrudRepository<Assistido, Long> {

    boolean existsByClienteIdAndFilmeId(@Param("clienteId") long clienteId, @Param("filmeId") long filmeId);

}
