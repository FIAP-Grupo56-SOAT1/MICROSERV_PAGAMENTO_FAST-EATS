package br.com.fiap.fasteats.core.domain.exception;

public class StatusPedidoNotFound extends RuntimeException {
    public StatusPedidoNotFound(String mensagem) {
        super(mensagem);
    }
}