package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.RealizarPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.RealizarPagamentoInputPort;
import br.com.fiap.fasteats.core.validator.RealizarPagamentoValidator;


public class RealizarPagamentoUseCase implements RealizarPagamentoInputPort {
    private final FormaPagamentoInputPort formaPagamentoInputPort;
    private final RealizarPagamentoOutputPort realizarPagamentoOutputPort;
    private final PagamentoInputPort pagamentoInputPort;
    private final RealizarPagamentoValidator realizarPagamentoValidator;

    public RealizarPagamentoUseCase(FormaPagamentoInputPort formaPagamentoInputPort,
                                    RealizarPagamentoOutputPort realizarPagamentoOutputPort,
                                    PagamentoInputPort pagamentoInputPort,
                                    RealizarPagamentoValidator realizarPagamentoValidator) {
        this.formaPagamentoInputPort = formaPagamentoInputPort;
        this.realizarPagamentoOutputPort = realizarPagamentoOutputPort;
        this.pagamentoInputPort = pagamentoInputPort;
        this.realizarPagamentoValidator = realizarPagamentoValidator;
    }

    @Override
    public Pagamento pagar(Long pedidoId) {
        realizarPagamentoValidator.validarStatusPedido(pedidoId);
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(pedidoId);

        if (pagamentoExterno(pagamento))
            throw new RegraNegocioException("O Pagamento deve ser realizado externamente");

        if (pagamento.getTentativasPagamento() > 0)
            throw new RegraNegocioException("O Pagamento não pode ser realizado, pois já houve tentativas de pagamento, utilize reprocessar pagamento");

        return realizarPagamentoOutputPort.realizarPagamento(pagamento.getId(), pedidoId);
    }

    private boolean pagamentoExterno(Pagamento pagamento) {
        return Boolean.TRUE.equals(formaPagamentoInputPort.consultar(pagamento.getFormaPagamento().getId()).getExterno());
    }
}
