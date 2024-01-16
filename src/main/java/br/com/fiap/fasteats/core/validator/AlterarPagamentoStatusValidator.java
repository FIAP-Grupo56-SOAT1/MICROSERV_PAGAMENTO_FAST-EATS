package br.com.fiap.fasteats.core.validator;

public interface AlterarPagamentoStatusValidator {
    void validarRecusado(Long pagamentoId);

    void validarCancelado(Long pagamentoId);

    void validarPago(Long pagamentoId);
}
