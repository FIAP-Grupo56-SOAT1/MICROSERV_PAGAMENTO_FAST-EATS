package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.CancelarPagamentoOutputPort;
import br.com.fiap.fasteats.core.dataprovider.CancelarPedidoOutputPort;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CancelarPagamentoAdapter implements CancelarPagamentoOutputPort {
    private final CancelarPedidoOutputPort cancelarPedidoOutputPort;
    private final AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;

    @Override
    @Transactional
    public Pagamento cancelar(Long pagamentoId, Long pedidoId) {
        cancelarPedidoOutputPort.cancelar(pedidoId);
        return alterarPagamentoStatusInputPort.cancelado(pagamentoId);
    }
}
