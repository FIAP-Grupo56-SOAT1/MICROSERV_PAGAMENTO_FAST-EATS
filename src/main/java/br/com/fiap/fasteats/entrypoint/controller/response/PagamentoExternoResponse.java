package br.com.fiap.fasteats.entrypoint.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagamentoExternoResponse {
    private Long id;
    private OffsetDateTime dateCreated;
    private OffsetDateTime dateApproved;
    private OffsetDateTime dateOfExpiration;
    private String status;
    private String qrCode;
    private String qrCodeBase64;
    private String urlPagamento;
    private String mensagem;
}
