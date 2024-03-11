package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.AlterarPedidoStatusOutputPort;
import br.com.fiap.fasteats.dataprovider.client.PedidoIntegration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlterarPedidoStatusAdapter implements AlterarPedidoStatusOutputPort {
    private final PedidoIntegration pedidoIntegration;

    @Override
    public void criado(Long pedidoId) {
        pedidoIntegration.pedidoCriado(pedidoId);
    }

    @Override
    public void aguardandoPagamento(Long pedidoId) {
        pedidoIntegration.pedidoAguardandoPagamento(pedidoId);
    }

    @Override
    public void pago(Long pedidoId) {
        pedidoIntegration.pedidoPago(pedidoId);
    }

    @Override
    public void cancelado(Long pedidoId) {
        pedidoIntegration.pedidoCancelado(pedidoId);
    }
}
