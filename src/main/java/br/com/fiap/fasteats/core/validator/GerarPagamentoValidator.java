package br.com.fiap.fasteats.core.validator;

public interface GerarPagamentoValidator {
    void validarPedidoStatus(Long pedidoId);
    void validarFormaPagamento(Long formaPagamentoId);
}
