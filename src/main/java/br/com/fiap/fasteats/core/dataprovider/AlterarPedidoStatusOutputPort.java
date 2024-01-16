package br.com.fiap.fasteats.core.dataprovider;

import br.com.fiap.fasteats.core.domain.model.Pedido;

import java.util.Optional;

public interface AlterarPedidoStatusOutputPort {
    Optional<Pedido> pago(Long pedidoId);

    Optional<Pedido> cancelado(Long pedidoId);
}
