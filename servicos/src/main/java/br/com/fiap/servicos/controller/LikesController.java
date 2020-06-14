package br.com.fiap.servicos.controller;

import br.com.fiap.servicos.model.Like;
import br.com.fiap.servicos.service.LikesService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/likes")
@Api(tags="serviço de likes de filmes")
public class LikesController {

    @Autowired
    LikesService likesService;

    @PostMapping
    public ResponseEntity<?> marcar(@RequestBody Like body) {
        Like like = null;
        try {
            like = likesService.marcar(body.getClienteId(), body.getFilmeId());
            return new ResponseEntity<>(like, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
