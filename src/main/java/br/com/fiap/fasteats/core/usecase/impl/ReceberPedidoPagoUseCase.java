package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.ConcluirPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.ReceberPedidoPagoInputPort;

public class ReceberPedidoPagoUseCase implements ReceberPedidoPagoInputPort {
    private final PagamentoInputPort pagamentoInputPort;
    private final ConcluirPagamentoOutputPort concluirPagamentoOutputPort;

    public ReceberPedidoPagoUseCase(PagamentoInputPort pagamentoInputPort,
                                    ConcluirPagamentoOutputPort concluirPagamentoOutputPort) {
        this.pagamentoInputPort = pagamentoInputPort;
        this.concluirPagamentoOutputPort = concluirPagamentoOutputPort;
    }

    @Override
    public Pagamento receber(Long pedidoId) {
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(pedidoId);
        return concluirPagamentoOutputPort.concluir(pagamento);
    }
}
