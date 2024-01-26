package br.com.fiap.fasteats.core.domain.model;

public class FormaPagamento {
    private Long id;
    private String nome;
    private Boolean externo;
    private Boolean ativo;

    public FormaPagamento() {
    }

    public FormaPagamento(Long id, String nome, Boolean externo, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.externo = externo;
        this.ativo = ativo;
    }

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

    public Boolean getExterno() {
        return externo;
    }

    public void setExterno(Boolean externo) {
        this.externo = externo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
