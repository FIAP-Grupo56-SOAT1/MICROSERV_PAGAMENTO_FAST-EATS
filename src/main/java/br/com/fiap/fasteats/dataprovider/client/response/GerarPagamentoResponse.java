package br.com.fiap.fasteats.dataprovider.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GerarPagamentoResponse {
    private Long pedidoId;
    private Long formaPagamentoId;
}
