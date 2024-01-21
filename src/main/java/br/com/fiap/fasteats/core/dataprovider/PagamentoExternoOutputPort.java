package br.com.fiap.fasteats.core.dataprovider;


import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;

public interface PagamentoExternoOutputPort {
    PagamentoExterno consultar(PagamentoExterno pagamentoExternoRequisicao);

    PagamentoExterno cancelarPagamento(Long pagamentoExternoId);

    Pagamento recuperarPagamentoDePagamentoExterno(PagamentoExterno pagamentoExternoRequisicao);

    StatusPagamento conveterStatusPagamento(String statusPagamentoExterno);
}
