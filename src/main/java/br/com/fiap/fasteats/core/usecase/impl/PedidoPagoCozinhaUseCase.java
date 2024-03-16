package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.ConcluirPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PedidoPagoCozinhaInputPort;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_PAGO;

public class PedidoPagoCozinhaUseCase implements PedidoPagoCozinhaInputPort {
    private final PagamentoInputPort pagamentoInputPort;
    private final ConcluirPagamentoOutputPort concluirPagamentoOutputPort;

    public PedidoPagoCozinhaUseCase(PagamentoInputPort pagamentoInputPort, ConcluirPagamentoOutputPort concluirPagamentoOutputPort) {
        this.pagamentoInputPort = pagamentoInputPort;
        this.concluirPagamentoOutputPort = concluirPagamentoOutputPort;
    }

    @Override
    public Pagamento enviarPedidoPagoCozinha(Long pedidoId) {
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(pedidoId);
        if (!pagamento.getStatusPagamento().getNome().equals(STATUS_PAGO))
            throw new RegraNegocioException("O status do pagamento deve ser " + STATUS_PAGO + " para o pedido ser enviado para a cozinha");
        return concluirPagamentoOutputPort.concluir(pagamento);
    }
}
