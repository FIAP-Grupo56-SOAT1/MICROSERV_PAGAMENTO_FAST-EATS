package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.ConcluirPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.EmitirComprovantePagamentoInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ConcluirPagamentoAdapter implements ConcluirPagamentoOutputPort {
    private final AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    private final EmitirComprovantePagamentoInputPort emitirComprovantePagamentoInputPort;

    @Override
    @Transactional
    public Pagamento concluir(Pagamento pagamento) {
        alterarPagamentoStatusInputPort.pago(pagamento.getId());
        emitirComprovantePagamentoInputPort.emitir(pagamento.getPedidoId());
        return alterarPagamentoStatusInputPort.concluido(pagamento.getId());
    }
}
