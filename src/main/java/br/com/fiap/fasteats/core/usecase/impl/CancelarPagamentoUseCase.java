package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.CancelarPedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.CancelarPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.validator.CancelarPagamentoValidator;

public class CancelarPagamentoUseCase implements CancelarPagamentoInputPort {
    private final PagamentoInputPort pagamentoInputPort;
    private final AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    private final CancelarPedidoOutputPort cancelarPedidoOutputPort;
    private final CancelarPagamentoValidator cancelarPagamentoValidator;

    public CancelarPagamentoUseCase(PagamentoInputPort pagamentoInputPort,
                                    AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort,
                                    CancelarPedidoOutputPort cancelarPedidoOutputPort,
                                    CancelarPagamentoValidator cancelarPagamentoValidator) {
        this.pagamentoInputPort = pagamentoInputPort;
        this.alterarPagamentoStatusInputPort = alterarPagamentoStatusInputPort;
        this.cancelarPedidoOutputPort = cancelarPedidoOutputPort;
        this.cancelarPagamentoValidator = cancelarPagamentoValidator;
    }

    @Override
    public Pagamento cancelar(Long pedidoId) {
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(pedidoId);

        if (Boolean.TRUE.equals(pagamento.getFormaPagamento().getExterno()))
            throw new RegraNegocioException("Para cancelar um pagamento externo, utilize o endpoint específico de cada método de pagamento");

        cancelarPagamentoValidator.validarCancelarPagamento(pedidoId);
        cancelarPedidoOutputPort.cancelar(pedidoId);
        return alterarPagamentoStatusInputPort.cancelado(pagamento.getId());
    }
}
