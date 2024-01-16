package br.com.fiap.fasteats.core.validator.impl;

import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PedidoNotFound;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.core.validator.EmitirComprovantePagamentoValidator;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_PAGO;
import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_PAGO;

public class EmitirComprovantePagamentoValidatorImpl implements EmitirComprovantePagamentoValidator {
    private final PedidoOutputPort pedidoOutputPort;
    private final PagamentoInputPort pagamentoInputPort;
    private final StatusPagamentoInputPort statusPagamentoInputPort;

    public EmitirComprovantePagamentoValidatorImpl(PedidoOutputPort pedidoOutputPort,
                                                   PagamentoInputPort pagamentoInputPort,
                                                   StatusPagamentoInputPort statusPagamentoInputPort) {
        this.pedidoOutputPort = pedidoOutputPort;
        this.pagamentoInputPort = pagamentoInputPort;
        this.statusPagamentoInputPort = statusPagamentoInputPort;
    }

    @Override
    public void validarEmitirComprovantePagamento(Long pedidoId) {
        Pedido pedido = pedidoOutputPort.consultar(pedidoId).orElseThrow(() -> new PedidoNotFound("Pedido não encontrado"));
        String statusPedido = pedido.getStatusPedido();

        if (!statusPedido.equals(STATUS_PEDIDO_PAGO))
            throw new RegraNegocioException("O status do pedido deve ser pago para emissão do comprovante de pagamento");

        Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(pedidoId);
        if (!statusPagamentoInputPort.consultar(pagamento.getStatusPagamento().getId()).getNome().equals(STATUS_PAGO)) {
            throw new RegraNegocioException("O status do pagamento deve ser pago para emissão do comprovante de pagamento");
        }
    }
}
