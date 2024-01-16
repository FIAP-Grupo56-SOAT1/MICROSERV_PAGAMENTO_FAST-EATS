package br.com.fiap.fasteats.core.dataprovider;

import br.com.fiap.fasteats.core.domain.model.Pagamento;

import java.util.List;
import java.util.Optional;

public interface PagamentoOutputPort {
    List<Pagamento> listar();

    Optional<Pagamento> consultar(Long id);

    Optional<Pagamento> consultarPorPedidoId(long pedidoId);

    Pagamento salvarPagamento(Pagamento pagamento);

    Optional<Pagamento> consultarPorIdPagamentoExterno(Long idPagamentoExterno);
}
