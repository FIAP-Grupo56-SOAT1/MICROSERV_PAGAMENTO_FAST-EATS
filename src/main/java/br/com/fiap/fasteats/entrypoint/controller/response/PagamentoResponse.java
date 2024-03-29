package br.com.fiap.fasteats.entrypoint.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Double valor;
    private FormaPagamentoResponse formaPagamento;
    private StatusPagamentoResponse statusPagamento;
    private Long pedidoId;
    private Long idPagamentoExterno;
    private String qrCode;
    private String urlPagamento;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraCriado;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraFinalizado;
}
