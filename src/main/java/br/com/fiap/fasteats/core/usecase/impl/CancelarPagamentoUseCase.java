package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.CancelarPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.CancelarPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.validator.CancelarPagamentoValidator;

public class CancelarPagamentoUseCase implements CancelarPagamentoInputPort {
    private final PagamentoInputPort pagamentoInputPort;
    private final CancelarPagamentoOutputPort cancelarPagamentoOutputPort;
    private final CancelarPagamentoValidator cancelarPagamentoValidator;

    public CancelarPagamentoUseCase(PagamentoInputPort pagamentoInputPort,
                                    CancelarPagamentoOutputPort cancelarPagamentoOutputPort,
                                    CancelarPagamentoValidator cancelarPagamentoValidator) {
        this.pagamentoInputPort = pagamentoInputPort;
        this.cancelarPagamentoOutputPort = cancelarPagamentoOutputPort;
        this.cancelarPagamentoValidator = cancelarPagamentoValidator;
    }

    @Override
    public Pagamento cancelar(Long pedidoId) {
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(pedidoId);

        if (Boolean.TRUE.equals(pagamento.getFormaPagamento().getExterno()))
            throw new RegraNegocioException("Para cancelar um pagamento externo, utilize o endpoint específico de cada método de pagamento");

        cancelarPagamentoValidator.validarCancelarPagamento(pedidoId);
        return cancelarPagamentoOutputPort.cancelar(pagamento.getId(), pedidoId);
    }
}
