package br.com.fiap.fasteats.dataprovider.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Pagamento")
@Table(name = "PAGAMENTO")
public class PagamentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagamento_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "forma_pagamento_id")
    private FormaPagamentoEntity formaPagamento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_pagamento_id")
    private StatusPagamentoEntity statusPagamento;

    @Column(nullable = false, name = "valor")
    private Double valor;

    @Column(nullable = false, name = "pedido_id")
    private Long pedidoId;

    @Column(nullable = false, name = "data_criado")
    private LocalDateTime dataHoraCriado;

    @Column(nullable = false, name = "data_processamento")
    private LocalDateTime dataHoraProcessamento;

    @Column(nullable = false, name = "data_finalizado")
    private LocalDateTime dataHoraFinalizado;

    @Column(nullable = false, name = "pagamento_externo_id")
    private Long idPagamentoExterno;

    @Column(nullable = false, name = "url_pagamento")
    private String urlPagamento;

    @Column(nullable = false, name = "qr_code")
    private String qrCode;
}
