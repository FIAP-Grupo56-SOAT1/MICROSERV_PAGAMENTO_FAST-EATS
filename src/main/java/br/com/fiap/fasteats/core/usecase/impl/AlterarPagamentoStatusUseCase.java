package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.PagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PagamentoNotFound;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.core.validator.AlterarPagamentoStatusValidator;

import java.time.LocalDateTime;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;

public class AlterarPagamentoStatusUseCase implements AlterarPagamentoStatusInputPort {
    private final StatusPagamentoInputPort statusPagamentoInputPort;
    private final PagamentoOutputPort pagamentoOutputPort;
    private final AlterarPagamentoStatusValidator alterarPagamentoStatusValidator;

    public AlterarPagamentoStatusUseCase(StatusPagamentoInputPort statusPagamentoInputPort,
                                         PagamentoOutputPort pagamentoOutputPort, 
                                         AlterarPagamentoStatusValidator alterarPagamentoStatusValidator) {
        this.statusPagamentoInputPort = statusPagamentoInputPort;
        this.pagamentoOutputPort = pagamentoOutputPort;
        this.alterarPagamentoStatusValidator = alterarPagamentoStatusValidator;
    }

    @Override
    public Pagamento recusado(Long pagamentoId) {
        alterarPagamentoStatusValidator.validarRecusado(pagamentoId);
        Pagamento pagamento = recuperarPagamento(pagamentoId);
        Pagamento pagamentoAtualizado = atualizarStatusPagamento(pagamento, STATUS_RECUSADO);
        return pagamentoOutputPort.salvarPagamento(pagamentoAtualizado);
    }

    @Override
    public Pagamento cancelado(Long pagamentoId) {
        alterarPagamentoStatusValidator.validarCancelado(pagamentoId);
        Pagamento pagamento = recuperarPagamento(pagamentoId);
        Pagamento pagamentoAtualizado = atualizarStatusPagamento(pagamento, STATUS_CANCELADO);
        pagamentoAtualizado.setDataHoraFinalizado(LocalDateTime.now());
        return pagamentoOutputPort.salvarPagamento(pagamentoAtualizado);
    }

    @Override
    public Pagamento pago(Long pagamentoId) {
        alterarPagamentoStatusValidator.validarPago(pagamentoId);
        Pagamento pagamento = recuperarPagamento(pagamentoId);
        Pagamento pagamentoAtualizado = atualizarStatusPagamento(pagamento, STATUS_PAGO);
        pagamentoAtualizado.setDataHoraFinalizado(LocalDateTime.now());
        return pagamentoOutputPort.salvarPagamento(pagamentoAtualizado);
    }

    private Pagamento recuperarPagamento(Long pagamentoId) {
        return pagamentoOutputPort.consultar(pagamentoId).orElseThrow(() -> new PagamentoNotFound("Pagamento não encontrado id " + pagamentoId));
    }

    private Pagamento atualizarStatusPagamento(Pagamento pagamento, String novoStatusPagamento) {
        pagamento.setStatusPagamento(statusPagamentoInputPort.consultarPorNome(novoStatusPagamento));
        return pagamento;
    }
}
