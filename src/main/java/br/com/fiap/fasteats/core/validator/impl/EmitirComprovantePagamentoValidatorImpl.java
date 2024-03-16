package br.com.fiap.fasteats.core.validator.impl;

import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.core.validator.EmitirComprovantePagamentoValidator;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_CONCLUIDO;

public class EmitirComprovantePagamentoValidatorImpl implements EmitirComprovantePagamentoValidator {
    private final PagamentoInputPort pagamentoInputPort;
    private final StatusPagamentoInputPort statusPagamentoInputPort;

    public EmitirComprovantePagamentoValidatorImpl(PagamentoInputPort pagamentoInputPort,
                                                   StatusPagamentoInputPort statusPagamentoInputPort) {
        this.pagamentoInputPort = pagamentoInputPort;
        this.statusPagamentoInputPort = statusPagamentoInputPort;
    }

    @Override
    public void validarEmitirComprovantePagamento(Long pedidoId) {
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(pedidoId);
        if (!statusPagamentoInputPort.consultar(pagamento.getStatusPagamento().getId()).getNome().equals(STATUS_CONCLUIDO)) {
            throw new RegraNegocioException("O status do pagamento deve ser" + STATUS_CONCLUIDO + "para emissão do comprovante de pagamento");
        }
    }
}
