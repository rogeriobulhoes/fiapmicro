package br.com.fiap.servicos.service;

import br.com.fiap.servicos.amqp.SendMessage;
import br.com.fiap.servicos.client.ClienteDiscoveryClient;
import br.com.fiap.servicos.client.FilmeDiscoveryClient;
import br.com.fiap.servicos.model.Assistido;
import br.com.fiap.servicos.model.Cliente;
import br.com.fiap.servicos.model.Filme;
import br.com.fiap.servicos.repository.AssistidosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssistidosService {

    @Autowired
    private AssistidosRepository assistidosRepository;

    @Autowired
    private FilmeDiscoveryClient filmeDiscoveryClient;

    @Autowired
    private ClienteDiscoveryClient clienteDiscoveryClient;

    @Autowired
    private SendMessage sendMessage;

    public Assistido marcar(long clienteId, long filmeId) throws Exception {
        Cliente cliente = clienteDiscoveryClient.findClienteById(clienteId);
        Filme filme = filmeDiscoveryClient.findFilmeById(filmeId);

        Assistido assistido = new Assistido(clienteId, filmeId);
        assistidosRepository.save(assistido);
        sendMessage.sendAssistidosMessage(assistido.getFilmeId());

        return assistido;
    }
}
