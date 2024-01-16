package br.com.fiap.fasteats.core.usecase;

import br.com.fiap.fasteats.core.domain.model.Pagamento;

public interface CancelarPagamentoInputPort {
    Pagamento cancelar(Long pagamentoId);
}
