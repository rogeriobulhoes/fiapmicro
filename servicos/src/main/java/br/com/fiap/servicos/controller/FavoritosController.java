package br.com.fiap.servicos.controller;

import br.com.fiap.servicos.model.Favorito;
import br.com.fiap.servicos.model.Like;
import br.com.fiap.servicos.service.FavoritosService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/favoritos")
@Api(tags="marca um filme (favorita) para ser visto no futuro")
public class FavoritosController {

    @Autowired
    FavoritosService favoritosService;

    @PostMapping
    public ResponseEntity<?> marcar(@RequestBody Favorito body) {
        Favorito favorito = null;
        try {
            favorito = favoritosService.marcar(body.getClienteId(), body.getFilmeId());
            return new ResponseEntity<>(favorito, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
