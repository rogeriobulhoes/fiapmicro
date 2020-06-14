package br.com.fiap.servicos.service;

import br.com.fiap.servicos.amqp.SendMessage;
import br.com.fiap.servicos.client.ClienteDiscoveryClient;
import br.com.fiap.servicos.client.FilmeDiscoveryClient;
import br.com.fiap.servicos.model.Cliente;
import br.com.fiap.servicos.model.Filme;
import br.com.fiap.servicos.model.Like;
import br.com.fiap.servicos.repository.LikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikesService {

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private FilmeDiscoveryClient filmeDiscoveryClient;

    @Autowired
    private ClienteDiscoveryClient clienteDiscoveryClient;

    @Autowired
    private SendMessage sendMessage;

    public Like marcar(long clienteId, long filmeId) throws Exception {
        Cliente cliente = clienteDiscoveryClient.findClienteById(clienteId);
        Filme filme = filmeDiscoveryClient.findFilmeById(filmeId);

        Like filmesalvo = null;
        Like like = new Like(clienteId, filmeId);
        if (likesRepository.existsByClienteIdAndFilmeId(clienteId, filmeId)) {
            filmesalvo = likesRepository.findByClienteIdAndFilmeId(clienteId, filmeId);
            likesRepository.deleteById(filmesalvo.getId());
            sendMessage.sendLikeMessage(filmeId, -1);
        } else {
            filmesalvo = likesRepository.save(like);
            sendMessage.sendLikeMessage(filmeId, 1);
        }
        return filmesalvo;
    }

}
