package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.domain.exception.FormaPagamentoNotFound;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.GerarPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.MetodoPagamentoInputPort;
import br.com.fiap.fasteats.core.validator.GerarPagamentoValidator;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;

public class GerarPagamentoUseCase implements GerarPagamentoInputPort {
    private final FormaPagamentoInputPort formaPagamentoInputPort;
    private final GerarPagamentoValidator gerarPagamentoValidator;
    private final MetodoPagamentoInputPort metodoPagamentoInputPort;

    public GerarPagamentoUseCase(FormaPagamentoInputPort formaPagamentoInputPort,
                                 GerarPagamentoValidator gerarPagamentoValidator,
                                 MetodoPagamentoInputPort metodoPagamentoInputPort) {
        this.formaPagamentoInputPort = formaPagamentoInputPort;
        this.gerarPagamentoValidator = gerarPagamentoValidator;
        this.metodoPagamentoInputPort = metodoPagamentoInputPort;
    }

    @Override
    public Pagamento gerar(Long pedidoId, long formaPagamentoId) {
        gerarPagamentoValidator.validarPedidoStatus(pedidoId);
        gerarPagamentoValidator.validarFormaPagamento(formaPagamentoId);

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
}
