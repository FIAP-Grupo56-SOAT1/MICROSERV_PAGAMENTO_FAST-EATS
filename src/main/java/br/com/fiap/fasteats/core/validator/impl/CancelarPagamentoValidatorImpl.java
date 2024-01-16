package br.com.fiap.fasteats.core.validator.impl;

import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PedidoNotFound;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.core.validator.CancelarPagamentoValidator;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_CANCELADO;
import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_CANCELADO;
import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_PAGO;

public class CancelarPagamentoValidatorImpl implements CancelarPagamentoValidator {
    private final PedidoOutputPort pedidoOutputPort;
    private final PagamentoInputPort pagamentoInputPort;
    private final StatusPagamentoInputPort statusPagamentoInputPort;

    public CancelarPagamentoValidatorImpl(PedidoOutputPort pedidoOutputPort,
                                          PagamentoInputPort pagamentoInputPort,
                                          StatusPagamentoInputPort statusPagamentoInputPort) {
        this.pedidoOutputPort = pedidoOutputPort;
        this.pagamentoInputPort = pagamentoInputPort;
        this.statusPagamentoInputPort = statusPagamentoInputPort;
    }

    @Override
    public void validarCancelarPagamento(Long pedidoId) {
        Pedido pedido = pedidoOutputPort.consultar(pedidoId).orElseThrow(() -> new PedidoNotFound("Pedido não encontrado"));
        String pedidoStatus = pedido.getStatusPedido();

        if (pedidoStatus.equals(STATUS_PEDIDO_CANCELADO))
            throw new RegraNegocioException("O pedido já foi cancelado");

        if (pedidoStatus.equals(STATUS_PEDIDO_PAGO))
            throw new RegraNegocioException("O pedido já foi pago");

        Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(pedidoId);
        String pagamentoStatus = statusPagamentoInputPort.consultar(pagamento.getStatusPagamento().getId()).getNome();

        if (pagamentoStatus.equals(STATUS_CANCELADO))
            throw new RegraNegocioException("O pagamento já foi cancelado");

        if (pagamentoStatus.equals(STATUS_PEDIDO_PAGO))
            throw new RegraNegocioException("O pagamento já foi pago");
    }
}
