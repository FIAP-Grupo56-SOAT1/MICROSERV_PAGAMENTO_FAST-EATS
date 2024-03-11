package br.com.fiap.fasteats.core.usecase;

import br.com.fiap.fasteats.core.domain.model.Pagamento;

import java.util.List;

public interface PagamentoInputPort {
    List<Pagamento> listar();

    Pagamento consultarPorIdPedido(Long idPedido);

    Pagamento criar(Pagamento pagamento);

    Pagamento atualizar(Pagamento pagamento);

    Pagamento consultar(Long pagamentoId);

    Pagamento consultarPorIdPagamentoExterno(Long pagamentoExternoId);

    void remover(Long pagamentoId);

    Pagamento adicionarTentativaPagamento(Long pagamentoId);
}
