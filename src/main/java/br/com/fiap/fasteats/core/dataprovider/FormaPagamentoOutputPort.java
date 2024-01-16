package br.com.fiap.fasteats.core.dataprovider;

import br.com.fiap.fasteats.core.domain.model.FormaPagamento;

import java.util.List;
import java.util.Optional;

public interface FormaPagamentoOutputPort {
    FormaPagamento criar(FormaPagamento formaPagamento);

    Optional<FormaPagamento> consultar(Long id);

    FormaPagamento atualizar(FormaPagamento formaPagamento);

    void deletar(Long id);

    Optional<List<FormaPagamento>> listar();

    Optional<FormaPagamento> consultarPorNome(String nome);
}
