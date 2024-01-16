package br.com.fiap.fasteats.core.usecase;

import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;

public interface PagamentoExternoInputPort {
    Pagamento atualizarPagamento(PagamentoExterno pagamentoExterno);
    void cancelarPagamentoExterno(Long pagamentoExternoId);
}
