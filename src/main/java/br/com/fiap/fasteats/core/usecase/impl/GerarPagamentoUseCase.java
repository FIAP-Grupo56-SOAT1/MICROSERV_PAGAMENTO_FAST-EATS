package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.PagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.FormaPagamentoNotFound;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.GerarPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.MetodoPagamentoInputPort;
import br.com.fiap.fasteats.core.validator.GerarPagamentoValidator;

import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_CANCELADO;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_PAGO;

public class GerarPagamentoUseCase implements GerarPagamentoInputPort {
    private final FormaPagamentoInputPort formaPagamentoInputPort;
    private final GerarPagamentoValidator gerarPagamentoValidator;
    private final MetodoPagamentoInputPort metodoPagamentoInputPort;
    private final PagamentoOutputPort pagamentoOutputPort;

    public GerarPagamentoUseCase(FormaPagamentoInputPort formaPagamentoInputPort,
                                 GerarPagamentoValidator gerarPagamentoValidator,
                                 MetodoPagamentoInputPort metodoPagamentoInputPort,
                                 PagamentoOutputPort pagamentoOutputPort) {
        this.formaPagamentoInputPort = formaPagamentoInputPort;
        this.gerarPagamentoValidator = gerarPagamentoValidator;
        this.metodoPagamentoInputPort = metodoPagamentoInputPort;
        this.pagamentoOutputPort = pagamentoOutputPort;
    }

    @Override
    public Pagamento gerar(Long pedidoId, Long formaPagamentoId) {
        gerarPagamentoValidator.validarPedidoStatus(pedidoId);
        gerarPagamentoValidator.validarFormaPagamento(formaPagamentoId);
        removerPagamentoAnterior(pedidoId);

        switch (formaPagamentoInputPort.consultar(formaPagamentoId).getNome()) {
            case PIX -> {
                return metodoPagamentoInputPort.pix(pedidoId);
            }
            case MERCADO_PAGO -> {
                return metodoPagamentoInputPort.mercadoPago(pedidoId);
            }
            default -> throw new FormaPagamentoNotFound("Forma de Pagamento não cadastrada");
        }
    }

    private void removerPagamentoAnterior(Long pedidoId) {
        Optional<Pagamento> pagamento = pagamentoOutputPort.consultarPorPedidoId(pedidoId);
        if (pagamento.isPresent()) {
            String nomeStatusPagamento = pagamento.get().getStatusPagamento().getNome();
            if (nomeStatusPagamento.equals(STATUS_PAGO) || nomeStatusPagamento.equals(STATUS_CANCELADO))
                throw new RegraNegocioException("Erro ao gerar novo Pagamento, pois o pedido já possui um Pagamento com status " + nomeStatusPagamento);
            pagamentoOutputPort.remover(pagamento.get().getId());
        }
    }
}
