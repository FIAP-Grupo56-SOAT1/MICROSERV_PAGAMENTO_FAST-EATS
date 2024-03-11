package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.RealizarPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.ReprocessarPagamentoInputPort;
import br.com.fiap.fasteats.core.validator.RealizarPagamentoValidator;

public class ReprocessarPagamentoUseCase implements ReprocessarPagamentoInputPort {
    private final RealizarPagamentoOutputPort realizarPagamentoOutputPort;
    private final PagamentoInputPort pagamentoInputPort;
    private final RealizarPagamentoValidator realizarPagamentoValidator;

    public ReprocessarPagamentoUseCase(RealizarPagamentoOutputPort realizarPagamentoOutputPort,
                                       PagamentoInputPort pagamentoInputPort,
                                       RealizarPagamentoValidator realizarPagamentoValidator) {
        this.realizarPagamentoOutputPort = realizarPagamentoOutputPort;
        this.pagamentoInputPort = pagamentoInputPort;
        this.realizarPagamentoValidator = realizarPagamentoValidator;
    }

    @Override
    public void reprocessar(Long pedidoId) {
        realizarPagamentoValidator.validarStatusPedido(pedidoId);
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(pedidoId);
        if (pagamento.getTentativasPagamento() < 1)
            throw new RegraNegocioException("O Pagamento não pode ser reprocessado, pois não houve tentativas de pagamento");
        
        realizarPagamentoOutputPort.realizarPagamento(pagamento.getId(), pedidoId);
    }
}
