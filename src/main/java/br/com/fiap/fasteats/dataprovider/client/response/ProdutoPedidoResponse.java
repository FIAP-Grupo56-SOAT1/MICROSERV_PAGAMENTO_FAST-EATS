package br.com.fiap.fasteats.dataprovider.client.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoPedidoResponse {
    private Long idProduto;

    private String nomeProduto;

    private String descricaoProduto;

    private int quantidade;

    private double valor;
}
