package br.com.fiap.filmes.amqp;

import br.com.fiap.filmes.model.Filme;
import br.com.fiap.filmes.repository.FilmeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RabbitListener(queues = "filmes_events_queue")
public class Receiver {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FilmeRepository filmeRepository;

    @RabbitHandler
    public void process(byte[] event) throws IOException {
        Map<String, String> payload = objectMapper.readValue(new String(event), new TypeReference<Map<String, String>>(){});

        Long filmeId = null;
        String tipo = payload.getOrDefault("tipo", "");
        if ("likes".equals(tipo)) {
            int val = Integer.valueOf(payload.getOrDefault("val", "0"));
            filmeId = Long.valueOf(payload.get("filmeId"));
            Optional<Filme> filme = filmeRepository.findById(filmeId);
            if (filme.isPresent()) {
                Filme fil = filme.get();
                if (fil.getLikes() > 0)
                    fil.setLikes(fil.getLikes() + val);
                filmeRepository.save(fil);
            }
        } else if ("assistidos".equals(tipo)) {
            filmeId = Long.valueOf(payload.get("filmeId"));
            Optional<Filme> filme = filmeRepository.findById(filmeId);
            if (filme.isPresent()) {
                Filme fil = filme.get();
                fil.setAssistido(fil.getAssistido() + 1);
                filmeRepository.save(fil);
            }
        } else {
            //TODO implementar
        }

    }

}
