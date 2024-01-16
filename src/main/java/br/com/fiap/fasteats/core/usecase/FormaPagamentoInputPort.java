package br.com.fiap.fasteats.core.usecase;

import br.com.fiap.fasteats.core.domain.model.FormaPagamento;

import java.util.List;

public interface FormaPagamentoInputPort {
    FormaPagamento criar(FormaPagamento formaPagamento);

    FormaPagamento consultar(Long id);

    FormaPagamento atualizar(FormaPagamento formaPagamento);

    void deletar(Long id);

    List<FormaPagamento> listar();

    FormaPagamento consultarPorNome(String nome);
}