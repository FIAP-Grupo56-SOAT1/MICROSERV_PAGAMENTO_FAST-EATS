package br.com.fiap.fasteats.core.usecase.paymentmethod;

import br.com.fiap.fasteats.core.domain.model.Pagamento;

public interface PagamentoMercadoPagoInputPort {
    Pagamento gerarPagamento(Long pedidoId);
}
