package br.com.fiap.fasteats.core.validator.impl;

import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PedidoNotFound;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.core.validator.GerarPagamentoValidator;

import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_AGUARDANDO_PAGAMENTO;
import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_CRIADO;

public class GerarPagamentoValidatorImpl implements GerarPagamentoValidator {
    private final FormaPagamentoInputPort formaPagamentoInputPort;
    private final PedidoOutputPort pedidoOutputPort;

    public GerarPagamentoValidatorImpl(FormaPagamentoInputPort formaPagamentoInputPort,
                                       PedidoOutputPort pedidoOutputPort) {
        this.formaPagamentoInputPort = formaPagamentoInputPort;
        this.pedidoOutputPort = pedidoOutputPort;
    }

    @Override
    public void validarPedidoStatus(Long pedidoId) {
        Pedido pedido = pedidoOutputPort.consultar(pedidoId).orElseThrow(() -> new PedidoNotFound("Pedido não encontrado"));
        String statusPedido = pedido.getStatusPedido();
        if (!(statusPedido.equals(STATUS_PEDIDO_CRIADO) || statusPedido.equals(STATUS_PEDIDO_AGUARDANDO_PAGAMENTO)))
            throw new RegraNegocioException("Só é possível gerar um pagamento quando o pedido está no status CRIADO ou AGUARDANDO_PAGAMENTO");
    }

    @Override
    public void validarFormaPagamento(Long formaPagamentoId) {
        formaPagamentoInputPort.consultar(formaPagamentoId);
    }
}
