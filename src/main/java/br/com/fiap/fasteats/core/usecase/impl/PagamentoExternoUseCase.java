package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.CancelarPagamentoOutputPort;
import br.com.fiap.fasteats.core.dataprovider.PagamentoExternoOutputPort;
import br.com.fiap.fasteats.core.dataprovider.RealizarPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.StatusPagametoNotFound;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;
import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoExternoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.validator.CancelarPagamentoValidator;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;

public class PagamentoExternoUseCase implements PagamentoExternoInputPort {

    private final PagamentoInputPort pagamentoInputPort;
    private final PagamentoExternoOutputPort pagamentoExternoOutputPort;
    private final RealizarPagamentoOutputPort realizarPagamentoOutputPort;
    private final AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    private final CancelarPagamentoOutputPort cancelarPagamentoOutputPort;
    private final CancelarPagamentoValidator cancelarPagamentoValidator;

    public PagamentoExternoUseCase(PagamentoInputPort pagamentoInputPort,
                                   PagamentoExternoOutputPort pagamentoExternoOutputPort,
                                   RealizarPagamentoOutputPort realizarPagamentoOutputPort,
                                   AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort,
                                   CancelarPagamentoOutputPort cancelarPagamentoOutputPort,
                                   CancelarPagamentoValidator cancelarPagamentoValidator) {
        this.pagamentoInputPort = pagamentoInputPort;
        this.pagamentoExternoOutputPort = pagamentoExternoOutputPort;
        this.realizarPagamentoOutputPort = realizarPagamentoOutputPort;
        this.alterarPagamentoStatusInputPort = alterarPagamentoStatusInputPort;
        this.cancelarPagamentoOutputPort = cancelarPagamentoOutputPort;
        this.cancelarPagamentoValidator = cancelarPagamentoValidator;
    }

    @Override
    public Pagamento atualizarPagamento(PagamentoExterno pagamentoExternoRequisicao) {
        try {
            Long pedidoId = pagamentoInputPort.consultarPorIdPagamentoExterno(pagamentoExternoRequisicao.getId()).getPedidoId();
            Pagamento pagamentoAtualizadoExterno = pagamentoExternoOutputPort.recuperarPagamentoDePagamentoExterno(pagamentoExternoRequisicao);
            Long pagamentoId = pagamentoAtualizadoExterno.getId();
            String nomeStatusPagamento = pagamentoAtualizadoExterno.getStatusPagamento().getNome();

            if (nomeStatusPagamento.equals(STATUS_CANCELADO))
                cancelarPagamentoValidator.validarCancelarPagamento(pedidoId);

            return atualizarStatusPagamento(pedidoId, pagamentoId, nomeStatusPagamento);
        } catch (Exception e) {
            return new Pagamento();
        }
    }

    @Override
    public void cancelarPagamentoExterno(Long pagamentoExternoId) {
        pagamentoExternoOutputPort.cancelarPagamento(pagamentoExternoId);
    }

    private Pagamento atualizarStatusPagamento(Long pedidoId, Long pagamentoId, String nomeStatusPagamento) {
        return switch (nomeStatusPagamento) {
            case STATUS_PAGO -> realizarPagamentoOutputPort.realizarPagamento(pagamentoId, pedidoId);
            case STATUS_RECUSADO -> alterarPagamentoStatusInputPort.recusado(pedidoId);
            case STATUS_CANCELADO -> cancelarPagamentoOutputPort.cancelar(pagamentoId, pedidoId);
            default -> throw new StatusPagametoNotFound("Status Pagamento não encontrado");
        };
    }
}
