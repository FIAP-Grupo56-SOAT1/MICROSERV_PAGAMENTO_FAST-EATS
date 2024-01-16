package br.com.fiap.fasteats.core.dataprovider;

import br.com.fiap.fasteats.core.domain.model.Pedido;

import java.util.Optional;

public interface PedidoOutputPort {
    Optional<Pedido> consultar(Long id);
}
