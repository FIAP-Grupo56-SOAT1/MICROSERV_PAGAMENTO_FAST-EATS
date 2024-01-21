package br.com.fiap.fasteats.dataprovider.client.exception;

public class MicroservicoPedidoException extends IllegalArgumentException {
    public MicroservicoPedidoException(String mensagem) {
        super(mensagem);
    }
}
