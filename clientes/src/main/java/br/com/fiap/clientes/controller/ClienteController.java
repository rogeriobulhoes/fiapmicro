package br.com.fiap.clientes.controller;

import br.com.fiap.clientes.client.ChamadoDiscoveryClient;
import br.com.fiap.clientes.model.Chamado;
import br.com.fiap.clientes.model.Cliente;
import br.com.fiap.clientes.service.ClienteService;
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

import java.io.IOException;

@RestController
@RequestMapping("/v1/clientes")
@Api(tags="clientes service")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChamadoDiscoveryClient chamadoDiscoveryClient;



    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE} )
    @ApiOperation(value="busca todos os clientes")
    public ResponseEntity<Page<Cliente>> getClientes(Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }
        Page<Cliente> clientes = clienteService.findAll(pageable);
        return new ResponseEntity<Page<Cliente>>(clientes, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE} )
    @ApiOperation(value="busca um cliente")
    public ResponseEntity<Cliente> getOneCliente(@PathVariable final Long id) {
        Cliente cliente = clienteService.findById(id);
        return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value="adiciona um cliente")
    public ResponseEntity<Cliente> addCliente(@RequestBody String cliente) {
        Cliente aCliente = null;
        try {
            aCliente = objectMapper.readValue(cliente, Cliente.class);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Cliente novoCliente = clienteService.addCliente(aCliente);
        if (novoCliente != null) {
            return new ResponseEntity<Cliente>(novoCliente, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/chamados")
    @ApiOperation(value="abre um chamado para um cliente")
    public ResponseEntity<Chamado> openTicketForCliente(@PathVariable final Long id, @RequestBody String chamado)  {
        Chamado aChamado = null;
        try {
            Cliente cliente = clienteService.findById(id);

            aChamado = objectMapper.readValue(chamado, Chamado.class);
            aChamado.setDataAbertura(null);
            aChamado.setDataFechamento(null);
            aChamado.setCliente(cliente.getId());

            aChamado = chamadoDiscoveryClient.openTicket(aChamado);
            return new ResponseEntity<Chamado>(aChamado, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
