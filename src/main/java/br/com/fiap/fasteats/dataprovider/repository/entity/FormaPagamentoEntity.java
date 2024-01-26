package br.com.fiap.fasteats.dataprovider.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "FormaPagamento")
@Table(name = "FORMA_PAGAMENTO")
public class FormaPagamentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "forma_pagamento_id")
    private Long id;

    @Column(nullable = true, length = 100)
    private String nome;

    @Column(nullable = false)
    private boolean externo = false;

    @Column(nullable = true)
    private boolean ativo = true;
}
