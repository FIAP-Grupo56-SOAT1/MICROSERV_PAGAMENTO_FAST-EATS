package br.com.fiap.fasteats.core.dataprovider;

public interface AlterarPedidoStatusOutputPort {
    void criado(Long pedidoId);

    void aguardandoPagamento(Long pedidoId);

    void pago(Long pedidoId);

    void cancelado(Long pedidoId);
}
