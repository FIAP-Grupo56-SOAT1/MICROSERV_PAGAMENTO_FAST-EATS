package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.MetodoPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.metodopagamento.MercadoPagoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.metodopagamento.PixUseCase;
import br.com.fiap.fasteats.core.usecase.metodopagamento.PagamentoMercadoPagoInputPort;
import br.com.fiap.fasteats.core.usecase.metodopagamento.PagamentoPixInputPort;
import br.com.fiap.fasteats.dataprovider.client.MercadoPagoIntegration;

public class MetodoPagamentoUseCase implements MetodoPagamentoInputPort {
    private final PagamentoInputPort pagamentoInputPort;
    private final FormaPagamentoInputPort formaPagamentoInputPort;
    private final PedidoOutputPort pedidoOutputPort;
    private final MercadoPagoIntegration mercadoPagoIntegration;


    public MetodoPagamentoUseCase(PagamentoInputPort pagamentoInputPort,
                                  FormaPagamentoInputPort formaPagamentoInputPort,
                                  PedidoOutputPort pedidoOutputPort,
                                  MercadoPagoIntegration mercadoPagoIntegration) {
        this.pagamentoInputPort = pagamentoInputPort;
        this.formaPagamentoInputPort = formaPagamentoInputPort;
        this.pedidoOutputPort = pedidoOutputPort;
        this.mercadoPagoIntegration = mercadoPagoIntegration;
    }

    @Override
    public Pagamento pix(Long pedidoId) {
        final PagamentoPixInputPort pagamentoPix = new PixUseCase(pagamentoInputPort, pedidoOutputPort, formaPagamentoInputPort);
        return pagamentoPix.gerarPagamento(pedidoId);
    }

    @Override
    public Pagamento mercadoPago(Long pedidoId) {
        final PagamentoMercadoPagoInputPort mercadoPagoUseCase = new MercadoPagoUseCase(pagamentoInputPort, formaPagamentoInputPort,
                                                                                        mercadoPagoIntegration, pedidoOutputPort);
        return mercadoPagoUseCase.gerarPagamento(pedidoId);
    }
}
