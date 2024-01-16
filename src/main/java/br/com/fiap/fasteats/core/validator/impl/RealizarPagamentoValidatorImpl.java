package br.com.fiap.fasteats.core.validator.impl;

import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PedidoNotFound;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.validator.RealizarPagamentoValidator;

import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_AGUARDANDO_PAGAMENTO;

public class RealizarPagamentoValidatorImpl implements RealizarPagamentoValidator {
    private final PedidoOutputPort pedidoOutputPort;

    public RealizarPagamentoValidatorImpl(PedidoOutputPort pedidoOutputPort) {
        this.pedidoOutputPort = pedidoOutputPort;
    }

    @Override
    public void validarStatusPedido(Long pedidoId) {
        Pedido pedido = pedidoOutputPort.consultar(pedidoId).orElseThrow(() -> new PedidoNotFound("Pedido não encontrado"));
        String status = pedido.getStatusPedido();
        if (!status.equals(STATUS_PEDIDO_AGUARDANDO_PAGAMENTO)) {
            throw new RegraNegocioException("Só é possível realizar o pagamento de um pedido com status " + STATUS_PEDIDO_AGUARDANDO_PAGAMENTO);
        }
    }
}
