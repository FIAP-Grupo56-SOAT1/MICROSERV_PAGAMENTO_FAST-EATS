package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.AlterarPedidoStatusOutputPort;
import br.com.fiap.fasteats.core.dataprovider.PagamentoExternoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.StatusPagametoNotFound;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;
import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.EmitirComprovantePagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoExternoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.validator.CancelarPagamentoValidator;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;

public class PagamentoExternoUseCase implements PagamentoExternoInputPort {

    private final PagamentoInputPort pagamentoInputPort;
    private final PagamentoExternoOutputPort pagamentoExternoOutputPort;
    private final EmitirComprovantePagamentoInputPort emitirComprovantePagamentoInputPort;
    private final AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    private final AlterarPedidoStatusOutputPort alterarPedidoStatusOutputPort;
    private final CancelarPagamentoValidator cancelarPagamentoValidator;

    public PagamentoExternoUseCase(PagamentoInputPort pagamentoInputPort,
                                   PagamentoExternoOutputPort pagamentoExternoOutputPort,
                                   EmitirComprovantePagamentoInputPort emitirComprovantePagamentoInputPort,
                                   AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort,
                                   AlterarPedidoStatusOutputPort alterarPedidoStatusOutputPort,
                                   CancelarPagamentoValidator cancelarPagamentoValidator) {
        this.pagamentoInputPort = pagamentoInputPort;
        this.pagamentoExternoOutputPort = pagamentoExternoOutputPort;
        this.emitirComprovantePagamentoInputPort = emitirComprovantePagamentoInputPort;
        this.alterarPagamentoStatusInputPort = alterarPagamentoStatusInputPort;
        this.alterarPedidoStatusOutputPort = alterarPedidoStatusOutputPort;
        this.cancelarPagamentoValidator = cancelarPagamentoValidator;
    }

    @Override
    public Pagamento atualizarPagamento(PagamentoExterno pagamentoExternoRequisicao) {
        try {
            Pagamento pagamento = pagamentoInputPort.consultarPorIdPagamentoExterno(pagamentoExternoRequisicao.getId());
            Pagamento pagamentoAtualizadoExterno = pagamentoExternoOutputPort.recuperarPagamentoDePagamentoExterno(pagamentoExternoRequisicao);
            String nomeStatusPagamento = pagamentoAtualizadoExterno.getStatusPagamento().getNome();

            if (nomeStatusPagamento.equals(STATUS_CANCELADO))
                cancelarPagamentoValidator.validarCancelarPagamento(pagamento.getPedidoId());

            if (nomeStatusPagamento.equals(STATUS_PAGO) || nomeStatusPagamento.equals(STATUS_CANCELADO))
                atualizarStatusPedido(pagamento.getPedidoId(), nomeStatusPagamento);

            atualizarStatusPagamento(pagamento.getPedidoId(), nomeStatusPagamento);
            return emitirComprovantePagamento(pagamento.getPedidoId());
        } catch (Exception e) {
            return new Pagamento();
        }
    }

    @Override
    public void cancelarPagamentoExterno(Long pagamentoExternoId) {
        pagamentoExternoOutputPort.cancelarPagamento(pagamentoExternoId);
    }

    private Pagamento emitirComprovantePagamento(Long pedidoId) {
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(pedidoId);
        if (pagamento.getStatusPagamento().getNome().equals(STATUS_PAGO))
            return emitirComprovantePagamentoInputPort.emitir(pedidoId);
        return pagamento;
    }

    private void atualizarStatusPagamento(Long pedidoId, String nomeStatusPagamento) {
        switch (nomeStatusPagamento) {
            case STATUS_PAGO -> alterarPagamentoStatusInputPort.pago(pedidoId);
            case STATUS_RECUSADO -> alterarPagamentoStatusInputPort.recusado(pedidoId);
            case STATUS_CANCELADO -> alterarPagamentoStatusInputPort.cancelado(pedidoId);
            default -> throw new StatusPagametoNotFound("Status Pagamento não encontrado");
        }
    }

    private void atualizarStatusPedido(Long pedidoId, String nomeStatusPagamento) {
        switch (nomeStatusPagamento) {
            case STATUS_PAGO -> alterarPedidoStatusOutputPort.pago(pedidoId);
            case STATUS_CANCELADO -> alterarPedidoStatusOutputPort.cancelado(pedidoId);
            default -> {
                break;
            }
        }
    }
}
