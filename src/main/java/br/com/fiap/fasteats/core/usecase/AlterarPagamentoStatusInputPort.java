package br.com.fiap.fasteats.core.usecase;

import br.com.fiap.fasteats.core.domain.model.Pagamento;

public interface AlterarPagamentoStatusInputPort {
    Pagamento emProcessamento(Long pagamentoId);

    Pagamento aguardandoPagamentoPedido(Long pagamentoId);

    Pagamento recusado(Long pagamentoId);

    Pagamento cancelado(Long pagamentoId);

    Pagamento pago(Long pagamentoId);

    Pagamento concluido(Long pagamentoId);
}
