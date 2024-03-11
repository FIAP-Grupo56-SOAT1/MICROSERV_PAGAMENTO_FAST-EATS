package br.com.fiap.fasteats.core.validator;

public interface AlterarPagamentoStatusValidator {
    void validarEmProcessamento(Long pagamentoId);

    void validarAguardandoPagamentoPedido(Long pagamentoId);

    void validarRecusado(Long pagamentoId);

    void validarCancelado(Long pagamentoId);

    void validarPago(Long pagamentoId);

    void validarConcluido(Long pagamentoId);
}
