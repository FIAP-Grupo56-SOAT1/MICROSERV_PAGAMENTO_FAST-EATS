package br.com.fiap.fasteats.dataprovider.client;

public interface CozinhaPedidoIntegration {
    void receberPedido(Long pedidoID);

    void erroReceberPedido(String mensagem);
}
