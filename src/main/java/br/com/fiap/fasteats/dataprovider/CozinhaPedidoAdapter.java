package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.CozinhaPedidoOutputPort;
import br.com.fiap.fasteats.dataprovider.client.CozinhaPedidoIntegration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CozinhaPedidoAdapter implements CozinhaPedidoOutputPort {
    private final CozinhaPedidoIntegration cozinhaPedidoIntegration;

    @Override
    public void receberPedido(Long pedidoId) {
        cozinhaPedidoIntegration.receberPedido(pedidoId);
    }
}
