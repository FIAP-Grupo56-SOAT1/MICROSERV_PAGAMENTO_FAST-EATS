package br.com.fiap.fasteats.core.usecase;

import br.com.fiap.fasteats.core.domain.model.Pagamento;

public interface AlterarPagamentoStatusInputPort {
    Pagamento recusado(Long pagamentoId);

    Pagamento cancelado(Long pagamentoId);

    Pagamento pago(Long pagamentoId);
}
