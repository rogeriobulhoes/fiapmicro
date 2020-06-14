package br.com.fiap.servicos.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@ApiModel
public class Assistido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long clienteId;
    private Long filmeId;

    public Assistido() {}

    public Assistido(Long clienteId, Long filmeId) {
        this.clienteId = clienteId;
        this.filmeId = filmeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getFilmeId() {
        return filmeId;
    }

    public void setFilmeId(Long filmeId) {
        this.filmeId = filmeId;
    }


}
