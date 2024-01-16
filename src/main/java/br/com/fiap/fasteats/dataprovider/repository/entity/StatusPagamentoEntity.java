package br.com.fiap.fasteats.dataprovider.repository.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "StatusPagamento")
@Table(name = "STATUS_PAGAMENTO")
@EqualsAndHashCode(of = "id")
public class StatusPagamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_pagamento_id")
    private Long id;

    @Column(nullable = true, length = 100)
    private String nome;

    @Column(nullable = true)
    private Boolean ativo = true;

}
