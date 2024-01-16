package br.com.fiap.fasteats.entrypoint.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagamentoResponse {
    private Long id;
    private FormaPagamentoResponse formaPagamento;
    private StatusPagamentoResponse statusPagamento;
    private Long pedidoId;
    private Long idPagamentoExterno;
    private String qrCode;
    private String urlPagamento;
    private LocalDateTime dataHoraCriado;
    private LocalDateTime dataHoraFinalizado;
}
