package br.com.fiap.servicos.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;

@Entity(name = "gostei")
@ApiModel
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long clienteId;
    private Long filmeId;

    public Like() {}

    public Like(Long clienteId, Long filmeId) {
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
