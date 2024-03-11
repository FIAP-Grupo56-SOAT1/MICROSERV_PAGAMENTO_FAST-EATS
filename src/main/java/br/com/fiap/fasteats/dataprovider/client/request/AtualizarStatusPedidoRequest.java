package br.com.fiap.fasteats.dataprovider.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AtualizarStatusPedidoRequest {
    private Long pedidoId;
}
