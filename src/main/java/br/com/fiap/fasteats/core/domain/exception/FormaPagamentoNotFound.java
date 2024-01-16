package br.com.fiap.fasteats.core.domain.exception;

public class FormaPagamentoNotFound extends RuntimeException{
    public FormaPagamentoNotFound(String mensagem) {
        super(mensagem);
    }
}
