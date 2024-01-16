package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.AlterarPedidoStatusOutputPort;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.usecase.*;
import br.com.fiap.fasteats.core.validator.RealizarPagamentoValidator;

import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_PAGO;


public class RealizarPagamentoUseCase implements RealizarPagamentoInputPort {
    private final FormaPagamentoInputPort formaPagamentoInputPort;
    private final AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    private final AlterarPedidoStatusOutputPort alterarPedidoStatusOutputPort;
    private final PagamentoInputPort pagamentoInputPort;
    private final EmitirComprovantePagamentoInputPort emitirComprovantePagamentoInputPort;
    private final RealizarPagamentoValidator realizarPagamentoValidator;

    public RealizarPagamentoUseCase(FormaPagamentoInputPort formaPagamentoInputPort,
                                    AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort,
                                    AlterarPedidoStatusOutputPort alterarPedidoStatusOutputPort,
                                    PagamentoInputPort pagamentoInputPort,
                                    EmitirComprovantePagamentoInputPort emitirComprovantePagamentoInputPort,
                                    RealizarPagamentoValidator realizarPagamentoValidator) {
        this.formaPagamentoInputPort = formaPagamentoInputPort;
        this.alterarPagamentoStatusInputPort = alterarPagamentoStatusInputPort;
        this.alterarPedidoStatusOutputPort = alterarPedidoStatusOutputPort;
        this.pagamentoInputPort = pagamentoInputPort;
        this.emitirComprovantePagamentoInputPort = emitirComprovantePagamentoInputPort;
        this.realizarPagamentoValidator = realizarPagamentoValidator;
    }

    @Override
    public Pagamento realizarPagamento(Long pedidoId) {
        realizarPagamentoValidator.validarStatusPedido(pedidoId);
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(pedidoId);

        if (pagamentoExterno(pagamento))
            throw new RegraNegocioException("O Pagamento deve ser realizado externamente");

        Pedido pedido = alterarPedidoStatusOutputPort.pago(pedidoId).orElseThrow(() -> new RegraNegocioException("Erro ao alterar status do pedido"));

        if (!pedido.getStatusPedido().equals(STATUS_PEDIDO_PAGO))
            throw new RegraNegocioException("Pedido com o status " + pedido.getStatusPedido().toUpperCase()+ ", não é possível realizar o pagamento");

        Pagamento pagamentoRealizado = alterarPagamentoStatusInputPort.pago(pagamento.getId());
        emitirComprovantePagamentoInputPort.emitir(pedidoId);
        return pagamentoRealizado;
    }

    private boolean pagamentoExterno(Pagamento pagamento) {
        return Boolean.TRUE.equals(formaPagamentoInputPort.consultar(pagamento.getFormaPagamento().getId()).getExterno());
    }
}
