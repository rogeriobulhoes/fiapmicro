package br.com.fiap.servicos.controller;

import br.com.fiap.servicos.model.Assistido;
import br.com.fiap.servicos.model.Favorito;
import br.com.fiap.servicos.service.AssistidosService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/assistidos")
@Api(tags="serviço de marcacao de filmes assistidos")
public class AssistidosController {

    @Autowired
    AssistidosService assistidosService;

    @PostMapping
    public ResponseEntity<?> marcar(@RequestBody Assistido body) throws Exception {
        Assistido assistido = null;
        try {
            assistido = assistidosService.marcar(body.getClienteId(), body.getFilmeId());
            return new ResponseEntity<>(assistido, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
