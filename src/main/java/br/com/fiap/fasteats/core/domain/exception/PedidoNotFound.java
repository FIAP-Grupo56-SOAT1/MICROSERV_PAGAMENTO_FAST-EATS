package br.com.fiap.fasteats.core.domain.exception;

public class PedidoNotFound extends RuntimeException {

    public PedidoNotFound(String mensagem) {
        super(mensagem);
    }

}
