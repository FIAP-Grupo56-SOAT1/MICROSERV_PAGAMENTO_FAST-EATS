package br.com.fiap.fasteats.dataprovider.client.exception;

public class AwsSQSException extends RuntimeException {
    public AwsSQSException(String mensagem) {
        super(mensagem);
    }
}
