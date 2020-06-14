package br.com.fiap.clientes.model;

import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@ApiModel
public class Chamado {

    private UUID id;

    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    private Long clienteId;

    private String motivo;

    private String descricao;

    public Chamado() {
    }

    public Chamado(Long clienteId, String motivo) {
        this.clienteId = clienteId;
        this.motivo = motivo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Long getCliente() {
        return clienteId;
    }

    public void setCliente(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chamado)) return false;

        Chamado chamado = (Chamado) o;

        if (!id.equals(chamado.id)) return false;
        if (!dataAbertura.equals(chamado.dataAbertura)) return false;
        if (!clienteId.equals(chamado.clienteId)) return false;
        return Objects.equals(motivo, chamado.motivo);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
