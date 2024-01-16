package br.com.fiap.fasteats.core.usecase.paymentmethod;

import br.com.fiap.fasteats.core.domain.model.Pagamento;

public interface PagamentoPixInputPort {
    Pagamento gerarPagamento(Long pedidoId);
}
