package br.com.fiap.fasteats.core.domain.model;

public class StatusPagamento {
    private Long id;
    private String nome;
    private Boolean ativo;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public StatusPagamento() {
    }

    public StatusPagamento(Long id, String nome, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
    }
}
