package br.com.fiap.fasteats.dataprovider.client;

public interface PagamentoPedidoIntegration {
    void receber(String mensagem);

    void erroPagamentoPedido(String mensagem);

    void erroCancelarPedido(String mensagem);
}
