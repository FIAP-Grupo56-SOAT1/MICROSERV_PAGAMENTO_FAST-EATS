package br.com.fiap.fasteats.core.usecase.impl.paymentmethod;

import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PagamentoExternoException;
import br.com.fiap.fasteats.core.domain.exception.PedidoNotFound;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.paymentmethod.PagamentoMercadoPagoInputPort;
import br.com.fiap.fasteats.dataprovider.client.MercadoPagoIntegration;

import java.util.Objects;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;

public class MercadoPagoUseCase implements PagamentoMercadoPagoInputPort {
    private final PagamentoInputPort pagamentoInputPort;
    private final FormaPagamentoInputPort formaPagamentoInputPort;
    private final MercadoPagoIntegration mercadoPagoIntegration;
    private final PedidoOutputPort pedidoOutputPort;

    public MercadoPagoUseCase(PagamentoInputPort pagamentoInputPort,
                              FormaPagamentoInputPort formaPagamentoInputPort,
                              MercadoPagoIntegration mercadoPagoIntegration,
                              PedidoOutputPort pedidoOutputPort) {
        this.pagamentoInputPort = pagamentoInputPort;
        this.formaPagamentoInputPort = formaPagamentoInputPort;
        this.mercadoPagoIntegration = mercadoPagoIntegration;
        this.pedidoOutputPort = pedidoOutputPort;
    }

    public Pagamento gerarPagamento(Long pedidoId) {
        Double valorPedido = pedidoOutputPort.consultar(pedidoId).map(Pedido::getValor)
                .orElseThrow(() -> new PedidoNotFound("Pedido não encontrado"));
        PagamentoExterno pagamentoExterno = mercadoPagoIntegration.enviarSolicitacaoPagamento(valorPedido);

        if (Objects.isNull(pagamentoExterno) || pagamentoExterno.getId().describeConstable().isEmpty())
            throw new PagamentoExternoException("Erro ao gerar pagamento");

        Pagamento pagamento = new Pagamento();
        pagamento.setPedidoId(pedidoId);
        pagamento.setValor(valorPedido);
        pagamento.setFormaPagamento(formaPagamentoInputPort.consultarPorNome(MERCADO_PAGO));
        pagamento.setIdPagamentoExterno(pagamentoExterno.getId());
        pagamento.setQrCode(pagamentoExterno.getQrCode());
        pagamento.setUrlPagamento(pagamentoExterno.getUrlPagamento());
        return pagamentoInputPort.criar(pagamento);
    }
}
