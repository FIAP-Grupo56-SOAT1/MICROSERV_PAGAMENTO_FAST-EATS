package br.com.fiap.fasteats.core.validator.impl;

import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.core.validator.AlterarPagamentoStatusValidator;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;

public class AlterarPagamentoStatusValidatorImpl implements AlterarPagamentoStatusValidator {
    private final PagamentoInputPort pagamentoInputPort;
    private final StatusPagamentoInputPort statusPagamentoInputPort;

    public AlterarPagamentoStatusValidatorImpl(PagamentoInputPort pagamentoInputPort, StatusPagamentoInputPort statusPagamentoInputPort) {
        this.pagamentoInputPort = pagamentoInputPort;
        this.statusPagamentoInputPort = statusPagamentoInputPort;
    }

    @Override
    public void validarRecusado(Long pagamentoId) {
        Pagamento pagamento = recuperarPagamento(pagamentoId);
        if (!recuperarNomeStatusPagamento(pagamento.getStatusPagamento().getId()).equals(STATUS_EM_PROCESSAMENTO))
            throw new RegraNegocioException("O Pagamento só pode ser recusado se estiver com o status EM_PROCESSAMENTO");
    }

    @Override
    public void validarCancelado(Long pagamentoId) {
        Pagamento pagamento = recuperarPagamento(pagamentoId);
        if (recuperarNomeStatusPagamento(pagamento.getStatusPagamento().getId()).equals(STATUS_CANCELADO))
            throw new RegraNegocioException("O Pagamento só pode ser cancelado se estiver com o status diferente de CANCELADO");
    }

    @Override
    public void validarPago(Long pagamentoId) {
        Pagamento pagamento = recuperarPagamento(pagamentoId);
        String nomeStatusPagamento = recuperarNomeStatusPagamento(pagamento.getStatusPagamento().getId());
        if (!(nomeStatusPagamento.equals(STATUS_EM_PROCESSAMENTO) || nomeStatusPagamento.equals(STATUS_RECUSADO)))
            throw new RegraNegocioException("O Pagamento só pode ser pago se estiver com o status EM_PROCESSAMENTO ou RECUSADO");
    }

    private Pagamento recuperarPagamento(Long pagamentoId) {
        return pagamentoInputPort.consultar(pagamentoId);
    }

    private String recuperarNomeStatusPagamento(Long statusPagamentoId) {
        return statusPagamentoInputPort.consultar(statusPagamentoId).getNome();
    }
}
