package br.com.fiap.servicos.service;

import br.com.fiap.servicos.model.Chamado;
import br.com.fiap.servicos.repository.ChamadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChamadosService {

    @Autowired
    private ChamadosRepository repo;

    public Chamado abrirChamado(Chamado aChamado) {
        aChamado.setDataAbertura(LocalDateTime.now());
        Chamado res = repo.save(aChamado);
        return res;
    }

    public Chamado fecharChamado(String id) {
        Chamado aChamado = findById(id);
        aChamado.setDataFechamento(LocalDateTime.now());
        Chamado res = repo.save(aChamado);
        return res;
    }

    public Chamado findById(String id) {
        if (id == null)
            throw new ChamadoNotFoundException("chamado invalido");
        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            throw new ChamadoNotFoundException("chamado invalido");
        }
        Optional<Chamado> chamado = repo.findById(uuid);
        chamado.orElseThrow(() -> new ChamadoNotFoundException("chamado invalido: " + id));
        return chamado.get();

    }
}
