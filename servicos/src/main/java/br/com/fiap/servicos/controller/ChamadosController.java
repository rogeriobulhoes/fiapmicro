package br.com.fiap.servicos.controller;

import br.com.fiap.servicos.model.Chamado;
import br.com.fiap.servicos.service.ChamadosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/chamados")
@Api(tags="serviço de abertura de chamados")
public class ChamadosController {

    @Autowired
    private ChamadosService chamadosService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE} )
    @ApiOperation(value="busca um chamado")
    public ResponseEntity<Chamado> getOneTicket(@PathVariable final String id) {
        Chamado chamado = chamadosService.findById(id);
        return new ResponseEntity<Chamado>(chamado, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value="abrir um chamado técnico")
    public ResponseEntity<Chamado> abrirChamado(@RequestBody String chamado) throws InterruptedException {
        Chamado aChamado = null;

        try {
            aChamado = objectMapper.readValue(chamado, Chamado.class);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Chamado novoChamado = chamadosService.abrirChamado(aChamado);
        if (novoChamado != null) {
            return new ResponseEntity<Chamado>(novoChamado, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value="fechar um chamado técnico")
    public ResponseEntity<Chamado> fecharChamado(@PathVariable final String id) {
        Chamado aChamado = chamadosService.fecharChamado(id);

        return new ResponseEntity<Chamado>(aChamado, HttpStatus.OK);
    }
}
