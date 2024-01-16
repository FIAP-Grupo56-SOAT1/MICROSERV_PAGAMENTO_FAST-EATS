package br.com.fiap.fasteats.core.validator.impl;

import br.com.fiap.fasteats.core.dataprovider.PagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.core.validator.PagamentoValidator;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_CANCELADO;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_PAGO;

public class PagamentoValidatorImpl implements PagamentoValidator {
    private final PagamentoOutputPort pagamentoOutputPort;
    private final StatusPagamentoInputPort statusPagamentoInputPort;

    public PagamentoValidatorImpl(PagamentoOutputPort pagamentoOutputPort, StatusPagamentoInputPort statusPagamentoInputPort) {
        this.pagamentoOutputPort = pagamentoOutputPort;
        this.statusPagamentoInputPort = statusPagamentoInputPort;
    }

    @Override
    public void validarAlterarPagamento(Long pagamentoId) {
        Pagamento pagamento = pagamentoOutputPort.consultar(pagamentoId).orElseThrow(() -> new RegraNegocioException("Pagamento não encontrado"));
        String nomeStatusPagamento = statusPagamentoInputPort.consultar(pagamento.getStatusPagamento().getId()).getNome();

        if (nomeStatusPagamento.equals(STATUS_PAGO))
            throw new RegraNegocioException("O Pagamento não pode ser alterado, pois está com status PAGO");

        if (nomeStatusPagamento.equals(STATUS_CANCELADO))
            throw new RegraNegocioException("O Pagamento não pode ser alterado, pois está com status CANCELADO");
    }
}
