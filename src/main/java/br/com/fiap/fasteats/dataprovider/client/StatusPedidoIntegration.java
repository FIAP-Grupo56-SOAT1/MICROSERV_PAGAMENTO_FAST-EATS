package br.com.fiap.fasteats.dataprovider.client;

import br.com.fiap.fasteats.dataprovider.client.response.StatusPedidoResponse;

import java.util.List;
import java.util.Optional;

public interface StatusPedidoIntegration {
    Optional<StatusPedidoResponse> consultar(Long id);

    Optional<List<StatusPedidoResponse>> listar();

    Optional<StatusPedidoResponse> consultarPorNome(String nome);
}
