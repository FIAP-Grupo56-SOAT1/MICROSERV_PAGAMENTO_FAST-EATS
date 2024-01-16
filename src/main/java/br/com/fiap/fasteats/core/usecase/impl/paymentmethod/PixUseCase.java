package br.com.fiap.fasteats.core.usecase.impl.paymentmethod;

import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PedidoNotFound;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.paymentmethod.PagamentoPixInputPort;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;

public class PixUseCase implements PagamentoPixInputPort {
    private final PagamentoInputPort pagamentoInputPort;
    private final PedidoOutputPort  pedidoOutputPort;
    private final FormaPagamentoInputPort formaPagamentoInputPort;

    public PixUseCase(PagamentoInputPort pagamentoInputPort,
                      PedidoOutputPort  pedidoOutputPort,
                      FormaPagamentoInputPort formaPagamentoInputPort) {
        this.pagamentoInputPort = pagamentoInputPort;
        this.pedidoOutputPort = pedidoOutputPort;
        this.formaPagamentoInputPort = formaPagamentoInputPort;
    }

    public Pagamento gerarPagamento(Long pedidoId) {
        Double valorPedido = pedidoOutputPort.consultar(pedidoId).map(Pedido::getValor)
                .orElseThrow(() -> new PedidoNotFound("Pedido não encontrado"));

        Pagamento pagamento = new Pagamento();
        pagamento.setValor(valorPedido);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(formaPagamentoInputPort.consultarPorNome(PIX));
        pagamento.setIdPagamentoExterno(null);
        pagamento.setQrCode(null);
        pagamento.setUrlPagamento(null);
        return pagamentoInputPort.criar(pagamento);
    }
}
