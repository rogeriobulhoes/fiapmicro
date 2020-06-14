package br.com.fiap.filmes.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;

@Entity
@ApiModel
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titulo;
    private Integer anoLancamento;
    private String genero; //acao | aventura | classico | comedia | documentario | drama | terror | ficção | show
    private String lingua; //ingles | portugues
    private String tipo; //filme | serie
    @Column(columnDefinition = "integer default 0")
    private Integer likes;
    @Column(columnDefinition = "integer default 0")
    private Integer assistido;

    @Column(columnDefinition = "TEXT", length=1024)
    private String detalhe;

    public Filme() {}

    public Filme(Long id, String titulo, Integer anoLancamento,
                 String genero, String lingua, String tipo, Integer assistido, Integer likes) {
        this.id = id;
        this.titulo = titulo;
        this.anoLancamento = anoLancamento;
        this.genero = genero;
        this.lingua = lingua;
        this.tipo = tipo;
        this.assistido = assistido;
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Integer anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }
    public String getDetalhe() {
        return this.detalhe;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getAssistido() {
        return assistido;
    }

    public void setAssistido(Integer assistido) {
        this.assistido = assistido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Filme filme = (Filme) o;

        if (!id.equals(filme.id)) return false;
        if (!titulo.equals(filme.titulo)) return false;
        if (anoLancamento != null ? !anoLancamento.equals(filme.anoLancamento) : filme.anoLancamento != null)
            return false;
        if (genero != null ? !genero.equals(filme.genero) : filme.genero != null) return false;
        if (lingua != null ? !lingua.equals(filme.lingua) : filme.lingua != null) return false;
        return tipo != null ? tipo.equals(filme.tipo) : filme.tipo == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + titulo.hashCode();
        result = 31 * result + (anoLancamento != null ? anoLancamento.hashCode() : 0);
        result = 31 * result + (genero != null ? genero.hashCode() : 0);
        result = 31 * result + (lingua != null ? lingua.hashCode() : 0);
        result = 31 * result + (tipo != null ? tipo.hashCode() : 0);
        return result;
    }
}
