package br.com.fiap.fasteats.core.validator;

public interface PagamentoValidator {
    void validarAlterarPagamento(Long pagamentoId);
    void validarRemoverPagamento(Long pagamentoId);
}
