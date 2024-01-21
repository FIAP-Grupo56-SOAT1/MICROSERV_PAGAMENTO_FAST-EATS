package br.com.fiap.fasteats.dataprovider.client.exception;

public class MicroservicoCozinhaException extends IllegalArgumentException {
    public MicroservicoCozinhaException(String mensagem) {
        super(mensagem);
    }
}
