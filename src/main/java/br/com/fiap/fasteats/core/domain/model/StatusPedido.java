package br.com.fiap.fasteats.core.domain.model;

import java.util.Objects;

public class StatusPedido {
    private Long id;
    private String nome;

    public StatusPedido() {
    }

    public StatusPedido(Long id, String nome) {
        this.id = id;
        this.nome = nome;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusPedido that = (StatusPedido) o;
        return Objects.equals(id, that.id) && Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    @Override
    public String toString() {
        return "StatusPedido{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
