package br.com.fiap.fasteats.core.domain.exception;

public class RegraNegocioException extends IllegalArgumentException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
