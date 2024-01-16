package br.com.fiap.fasteats.core.usecase;

import br.com.fiap.fasteats.core.domain.model.Pagamento;

public interface MetodoPagamentoInputPort {
    Pagamento pix(Long pedidoId);
    Pagamento mercadoPago(Long pedidoId);
}
