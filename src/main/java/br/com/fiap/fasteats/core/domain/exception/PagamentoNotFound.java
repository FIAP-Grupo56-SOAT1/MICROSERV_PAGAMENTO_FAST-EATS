package br.com.fiap.fasteats.core.domain.exception;

public class PagamentoNotFound extends RuntimeException {

    public PagamentoNotFound(String mensagem) {
        super(mensagem);
    }

}
