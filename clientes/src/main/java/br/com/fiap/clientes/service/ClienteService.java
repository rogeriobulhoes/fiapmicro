package br.com.fiap.clientes.service;

import br.com.fiap.clientes.model.Cliente;
import br.com.fiap.clientes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public Page<Cliente> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Cliente findById(Long id) {
        if (id == null)
            throw new ClienteNotFoundException("id invalido");
        Optional<Cliente> cliente = repository.findById(id);
        cliente.orElseThrow(() -> new ClienteNotFoundException("id: " + id));
        return cliente.get();
    }

    public Cliente addCliente(Cliente cliente) {
        return repository.save(cliente);
    }
}
