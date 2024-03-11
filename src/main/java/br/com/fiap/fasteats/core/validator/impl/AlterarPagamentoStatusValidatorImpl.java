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
    public void validarEmProcessamento(Long pagamentoId) {
        Pagamento pagamento = recuperarPagamento(pagamentoId);
        if (!recuperarNomeStatusPagamento(pagamento.getStatusPagamento().getId()).equals(STATUS_AGUARDANDO_PAGAMENTO_PEDIDO))
            throw new RegraNegocioException("O Pagamento só pode voltar para o status " + STATUS_EM_PROCESSAMENTO + " se estiver com o status " + STATUS_AGUARDANDO_PAGAMENTO_PEDIDO);
    }

    @Override
    public void validarAguardandoPagamentoPedido(Long pagamentoId) {
        Pagamento pagamento = recuperarPagamento(pagamentoId);
        if (!recuperarNomeStatusPagamento(pagamento.getStatusPagamento().getId()).equals(STATUS_EM_PROCESSAMENTO))
            throw new RegraNegocioException("O Pagamento só pode ir para o status " + STATUS_AGUARDANDO_PAGAMENTO_PEDIDO + " se estiver com o status " + STATUS_EM_PROCESSAMENTO);
    }

    @Override
    public void validarRecusado(Long pagamentoId) {
        Pagamento pagamento = recuperarPagamento(pagamentoId);
        if (!recuperarNomeStatusPagamento(pagamento.getStatusPagamento().getId()).equals(STATUS_EM_PROCESSAMENTO))
            throw new RegraNegocioException("O Pagamento só pode ser recusado se estiver com o status " + STATUS_EM_PROCESSAMENTO);
    }

    @Override
    public void validarCancelado(Long pagamentoId) {
        Pagamento pagamento = recuperarPagamento(pagamentoId);
        if (recuperarNomeStatusPagamento(pagamento.getStatusPagamento().getId()).equals(STATUS_CANCELADO))
            throw new RegraNegocioException("O Pagamento só pode ser cancelado se estiver com o status diferente de " + STATUS_CANCELADO);
    }

    @Override
    public void validarPago(Long pagamentoId) {
        Pagamento pagamento = recuperarPagamento(pagamentoId);
        String nomeStatusPagamento = recuperarNomeStatusPagamento(pagamento.getStatusPagamento().getId());
        if (!(nomeStatusPagamento.equals(STATUS_EM_PROCESSAMENTO) ||
              nomeStatusPagamento.equals(STATUS_AGUARDANDO_PAGAMENTO_PEDIDO) ||
              nomeStatusPagamento.equals(STATUS_RECUSADO) ||
              nomeStatusPagamento.equals(STATUS_CONCLUIDO)))
            throw new RegraNegocioException(String.format("O Pagamento só pode ser pago se estiver com o status %s, %s ou %s",
                    STATUS_EM_PROCESSAMENTO, STATUS_RECUSADO, STATUS_CONCLUIDO));
    }

    @Override
    public void validarConcluido(Long pagamentoId) {
        Pagamento pagamento = recuperarPagamento(pagamentoId);
        if (!recuperarNomeStatusPagamento(pagamento.getStatusPagamento().getId()).equals(STATUS_PAGO))
            throw new RegraNegocioException("O Pagamento só pode ser concluído se estiver com o status " + STATUS_PAGO);
    }

    private Pagamento recuperarPagamento(Long pagamentoId) {
        return pagamentoInputPort.consultar(pagamentoId);
    }

    private String recuperarNomeStatusPagamento(Long statusPagamentoId) {
        return statusPagamentoInputPort.consultar(statusPagamentoId).getNome();
    }
}
