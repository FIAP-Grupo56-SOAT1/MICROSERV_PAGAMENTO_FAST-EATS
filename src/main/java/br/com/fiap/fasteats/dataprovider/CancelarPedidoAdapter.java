package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.AlterarPedidoStatusOutputPort;
import br.com.fiap.fasteats.core.dataprovider.CancelarPedidoOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancelarPedidoAdapter implements CancelarPedidoOutputPort {
    private final AlterarPedidoStatusOutputPort alterarPedidoStatusOutputPort;

    @Override
    public void cancelar(Long pedidoId) {
        alterarPedidoStatusOutputPort.cancelado(pedidoId);
    }
}
