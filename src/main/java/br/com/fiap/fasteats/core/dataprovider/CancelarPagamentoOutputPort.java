package br.com.fiap.fasteats.core.dataprovider;

import br.com.fiap.fasteats.core.domain.model.Pagamento;

public interface CancelarPagamentoOutputPort {
    Pagamento cancelar(Long pagamentoId, Long pedidoId);
}
