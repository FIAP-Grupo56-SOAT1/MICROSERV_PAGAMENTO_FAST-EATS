package br.com.fiap.fasteats.core.dataprovider;


import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;

public interface PagamentoExternoOutputPort {
    PagamentoExterno consultar(PagamentoExterno pagamentoExternoRequisicao);

    PagamentoExterno cancelarPagamento(Long pagamentoExternoId);

    Pagamento recuperarPagamentoDePagamentoExterno(PagamentoExterno pagamentoExternoRequisicao);
}
