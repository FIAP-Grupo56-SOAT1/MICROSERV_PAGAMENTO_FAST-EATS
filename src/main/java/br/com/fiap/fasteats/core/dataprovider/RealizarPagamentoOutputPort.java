package br.com.fiap.fasteats.core.dataprovider;

import br.com.fiap.fasteats.core.domain.model.Pagamento;

public interface RealizarPagamentoOutputPort {
    Pagamento realizarPagamento(Long pagamentoId, Long pedidoId);
}
