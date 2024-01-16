package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.dataprovider.client.PedidoIntegration;
import br.com.fiap.fasteats.dataprovider.client.mapper.PedidoMapper;
import br.com.fiap.fasteats.dataprovider.client.response.PedidoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PedidoAdapter implements PedidoOutputPort {
    private final PedidoIntegration pedidoIntegration;
    private final PedidoMapper pedidoMapper;

    @Override
    public Optional<Pedido> consultar(Long id) {
        Optional<PedidoResponse> pedidoResponse = pedidoIntegration.consultar(id);
        return pedidoResponse.map(pedidoMapper::toPedido);
    }
}
