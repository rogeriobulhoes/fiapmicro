package br.com.fiap.filmes.controller;

import br.com.fiap.filmes.model.Filme;
import br.com.fiap.filmes.service.FilmeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@Api(tags="filmes service")
public class FilmeController {

    @Autowired
    private FilmeService filmeService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping(value = "/filmes", produces = {MediaType.APPLICATION_JSON_VALUE} )
    @ApiOperation(value="busca todos os filmes. Também utiliza os parâmetros genero e título.")
    public ResponseEntity<Page<Filme>> getFilmes(
            Pageable pageable,
            @RequestParam(value="genero", required=false) String genero,
            @RequestParam(value="titulo", required=false) String titulo) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }
        Page<Filme> filmes = null;
        if (genero != null && ! genero.isEmpty())
            filmes = filmeService.findAllByGenero(pageable, genero);
        else if (titulo != null && ! titulo.isEmpty())
            filmes = filmeService.findAllLikeTitulo(pageable, titulo);
        else
            filmes = filmeService.findAll(pageable);

        return new ResponseEntity<Page<Filme>>(filmes, HttpStatus.OK);
    }

    @GetMapping(value = "/filmes/{id}", produces = {MediaType.APPLICATION_JSON_VALUE} )
    @ApiOperation(value="busca um filme")
    public ResponseEntity<Filme> getOneFilme(@PathVariable final Long id) {
        Filme filme = filmeService.findById(id);
        return new ResponseEntity<Filme>(filme, HttpStatus.OK);
    }

    @GetMapping(value = "/filmes-mais-vistos", produces = {MediaType.APPLICATION_JSON_VALUE} )
    @ApiOperation(value="filmes que já foram assistidos")
    public ResponseEntity<Page<Filme>> getMaisAssistidos(Pageable pageable) {

        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }
        Page<Filme> filmes = null;
        filmes = filmeService.findByAssistidoGreaterThan(pageable);

        return new ResponseEntity<Page<Filme>>(filmes, HttpStatus.OK);
    }

    @GetMapping(value = "/filmes-sucessos", produces = {MediaType.APPLICATION_JSON_VALUE} )
    @ApiOperation(value="sucessos por categoria")
    public ResponseEntity<Page<Filme>> getPreferidosPorCategoria(
            Pageable pageable,
            @RequestParam(value="genero", required=true) String genero) {

        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }
        Page<Filme> filmes = filmeService.findAllByGeneroAndAssistidoGreaterThan(pageable, genero);

        return new ResponseEntity<Page<Filme>>(filmes, HttpStatus.OK);
    }

    @PostMapping(value = "/filmes")
    @ApiOperation(value="adiciona um filme")
    public ResponseEntity<Filme> addFilme(@RequestBody String cliente) {
        Filme aFilme = null;
        try {
            aFilme = objectMapper.readValue(cliente, Filme.class);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Filme novoFilme = filmeService.addFilme(aFilme);
        if (novoFilme != null) {
            return new ResponseEntity<Filme>(novoFilme, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
