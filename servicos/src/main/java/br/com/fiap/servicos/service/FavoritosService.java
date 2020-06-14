package br.com.fiap.servicos.service;

import br.com.fiap.servicos.amqp.SendMessage;
import br.com.fiap.servicos.client.ClienteDiscoveryClient;
import br.com.fiap.servicos.client.FilmeDiscoveryClient;
import br.com.fiap.servicos.model.Assistido;
import br.com.fiap.servicos.model.Cliente;
import br.com.fiap.servicos.model.Favorito;
import br.com.fiap.servicos.model.Filme;
import br.com.fiap.servicos.repository.AssistidosRepository;
import br.com.fiap.servicos.repository.FavoritosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoritosService {

    @Autowired
    private FavoritosRepository favoritosRepository;

    @Autowired
    private FilmeDiscoveryClient filmeDiscoveryClient;

    @Autowired
    private ClienteDiscoveryClient clienteDiscoveryClient;

    @Autowired
    private SendMessage sendMessage;

    public Favorito marcar(long clienteId, long filmeId) throws Exception {
        Cliente cliente = clienteDiscoveryClient.findClienteById(clienteId);
        Filme filme = filmeDiscoveryClient.findFilmeById(filmeId);

        Favorito favorito = new Favorito(clienteId, filmeId);
        if (favoritosRepository.existsByClienteIdAndFilmeId(clienteId, filmeId)) {
            favorito = favoritosRepository.findByClienteIdAndFilmeId(clienteId, filmeId);
            favoritosRepository.deleteById(favorito.getId());
            sendMessage.sendFavoritosMessage(filmeId, -1);
        } else {
            favorito = favoritosRepository.save(favorito);
            sendMessage.sendFavoritosMessage(filmeId, 1);
        }
        return favorito;
    }
}
