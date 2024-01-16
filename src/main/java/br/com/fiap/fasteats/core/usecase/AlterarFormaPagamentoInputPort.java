package br.com.fiap.fasteats.core.usecase;

import br.com.fiap.fasteats.core.domain.model.Pagamento;

public interface AlterarFormaPagamentoInputPort {
    Pagamento alterarFormaPagamento(Long pagamentoId, Long formaPagamentoId);
}
