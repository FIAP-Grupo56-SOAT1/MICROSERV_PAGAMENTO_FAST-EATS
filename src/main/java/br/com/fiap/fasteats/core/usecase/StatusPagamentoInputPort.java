package br.com.fiap.fasteats.core.usecase;

import br.com.fiap.fasteats.core.domain.model.StatusPagamento;

import java.util.List;

public interface StatusPagamentoInputPort {
    List<StatusPagamento> listar();

    StatusPagamento consultarPorNome(String nome);

    StatusPagamento consultar(Long id);
}
