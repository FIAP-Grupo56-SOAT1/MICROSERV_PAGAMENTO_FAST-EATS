package br.com.fiap.fasteats.core.domain.exception;

public class StatusPagametoNotFound extends RuntimeException {
    public StatusPagametoNotFound(String mensagem) {
        super(mensagem);
    }
}