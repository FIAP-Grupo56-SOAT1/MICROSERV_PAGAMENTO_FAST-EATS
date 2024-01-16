package br.com.fiap.fasteats.core.dataprovider;

import br.com.fiap.fasteats.core.domain.model.StatusPagamento;

import java.util.List;
import java.util.Optional;

public interface StatusPagamentoOutputPort {
    List<StatusPagamento> listar();

    Optional<StatusPagamento> consultarPorNome(String nome);

    Optional<StatusPagamento> consultar(Long id);
}
