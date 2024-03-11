package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.AlterarPedidoStatusOutputPort;
import br.com.fiap.fasteats.core.dataprovider.RealizarPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RealizarPagamentoAdapter implements RealizarPagamentoOutputPort {
    private final AlterarPedidoStatusOutputPort alterarPedidoStatusOutputPort;
    private final AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    private final PagamentoInputPort pagamentoInputPort;

    @Override
    @Transactional
    public Pagamento realizarPagamento(Long pagamentoId, Long pedidoId) {
        alterarPagamentoStatusInputPort.aguardandoPagamentoPedido(pagamentoId);
        alterarPedidoStatusOutputPort.pago(pedidoId);
        return pagamentoInputPort.adicionarTentativaPagamento(pagamentoId);
    }
}
