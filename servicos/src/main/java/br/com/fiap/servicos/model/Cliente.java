package br.com.fiap.servicos.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;

@ApiModel
public class Cliente {

    private Long id;
    private String nome;
    private String cpf;
    private Integer idade;

    public Cliente() {}

    public Cliente(Long id, String nome, String cpf, Integer idade) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cliente usuario = (Cliente) o;

        if (!id.equals(usuario.id)) return false;
        if (!nome.equals(usuario.nome)) return false;
        if (!cpf.equals(usuario.cpf)) return false;
        return idade != null ? idade.equals(usuario.idade) : usuario.idade == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + nome.hashCode();
        result = 31 * result + cpf.hashCode();
        result = 31 * result + (idade != null ? idade.hashCode() : 0);
        return result;
    }
}
