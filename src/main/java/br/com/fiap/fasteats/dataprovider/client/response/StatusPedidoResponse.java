package br.com.fiap.fasteats.dataprovider.client.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusPedidoResponse {
    private Long id;
    private String nome;
    private Boolean ativo;
}
