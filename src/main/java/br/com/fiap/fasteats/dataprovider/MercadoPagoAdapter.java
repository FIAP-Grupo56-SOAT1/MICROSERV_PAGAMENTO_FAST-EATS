package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.PagamentoExternoOutputPort;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.dataprovider.client.MercadoPagoIntegration;
import br.com.fiap.fasteats.dataprovider.constants.StatusMercadoPagoConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;

@Component
@RequiredArgsConstructor
public class MercadoPagoAdapter implements PagamentoExternoOutputPort {
    private final PagamentoInputPort pagamentoInputPort;
    private final StatusPagamentoInputPort statusPagamentoInputPort;
    private final MercadoPagoIntegration mercadoPagoIntegration;

    @Override
    public PagamentoExterno consultar(PagamentoExterno pagamentoExternoRequisicao) {
        return mercadoPagoIntegration.consultarPagamento(pagamentoExternoRequisicao);
    }

    @Override
    public PagamentoExterno cancelarPagamento(Long pagamentoExternoId) {
        return mercadoPagoIntegration.cancelarPagamento(pagamentoExternoId);
    }

    @Override
    public Pagamento recuperarPagamentoDePagamentoExterno(PagamentoExterno pagamentoExternoRequisicao) {
        PagamentoExterno pagamentoExterno = consultar(pagamentoExternoRequisicao);
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPagamentoExterno(pagamentoExterno.getId());
        pagamento.setStatusPagamento(conveterStatusPagamento(pagamentoExterno.getStatus()));
        return pagamento;
    }

    @Override
    public StatusPagamento conveterStatusPagamento(String statusPagamentoExterno) {
        switch (statusPagamentoExterno) {
            case StatusMercadoPagoConstants.STATUS_APROVADO,
                 StatusMercadoPagoConstants.STATUS_AUTORIZADO -> {
                return statusPagamentoInputPort.consultarPorNome(STATUS_PAGO);
            }
            case StatusMercadoPagoConstants.STATUS_EM_PROCESSO,
                 StatusMercadoPagoConstants.STATUS_EM_MEDIACAO,
                 StatusMercadoPagoConstants.STATUS_PENDENTE -> {
                return statusPagamentoInputPort.consultarPorNome(STATUS_EM_PROCESSAMENTO);
            }
            case StatusMercadoPagoConstants.STATUS_CANCELADO,
                 StatusMercadoPagoConstants.STATUS_ESTORNADO,
                 StatusMercadoPagoConstants.STATUS_DEVOLVIDO -> {
                return statusPagamentoInputPort.consultarPorNome(STATUS_CANCELADO);
            }
            case StatusMercadoPagoConstants.STATUS_REJEITADO -> {
                return statusPagamentoInputPort.consultarPorNome(STATUS_RECUSADO);
            }
            default -> {
                return new StatusPagamento();
            }
        }
    }
}
