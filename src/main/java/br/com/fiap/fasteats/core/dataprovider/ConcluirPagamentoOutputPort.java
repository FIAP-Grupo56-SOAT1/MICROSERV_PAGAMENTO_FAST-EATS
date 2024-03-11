package br.com.fiap.fasteats.core.dataprovider;

import br.com.fiap.fasteats.core.domain.model.Pagamento;

public interface ConcluirPagamentoOutputPort {
    Pagamento concluir(Pagamento pagamento);
}
