package br.com.fiap.clientes.repository;

import br.com.fiap.clientes.model.Cliente;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ClienteRepository extends PagingAndSortingRepository<Cliente,Long> {

}
