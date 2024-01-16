package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.AlterarFormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.GerarPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;

public class AlterarFormaPagamentoUseCase implements AlterarFormaPagamentoInputPort {
    private final PagamentoInputPort pagamentoInputPort;
    private final GerarPagamentoInputPort gerarPagamentoInputPort;

    public AlterarFormaPagamentoUseCase(PagamentoInputPort pagamentoInputPort, GerarPagamentoInputPort gerarPagamentoInputPort) {
        this.pagamentoInputPort = pagamentoInputPort;
        this.gerarPagamentoInputPort = gerarPagamentoInputPort;
    }

    @Override
    public Pagamento alterarFormaPagamento(Long pagamentoId, Long formaPagamentoId) {
        Long pedidoId = pagamentoInputPort.consultar(pagamentoId).getPedidoId();
        return gerarPagamentoInputPort.gerar(pedidoId, formaPagamentoId);
    }
}
