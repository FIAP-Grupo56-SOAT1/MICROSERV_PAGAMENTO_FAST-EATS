package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.core.dataprovider.CancelarPagamentoOutputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.dataprovider.client.CancelarPagamentoIntegration;
import br.com.fiap.fasteats.dataprovider.client.response.CancelarPagamentoResponse;
import com.google.gson.Gson;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.cloud.aws.sqs.enabled", havingValue = "true", matchIfMissing = true)
public class CancelarPagamentoIntegrationImpl implements CancelarPagamentoIntegration {
    @Value("${sqs.queue.pagamento.cancelar-pagamento}")
    private String filaPagamentoCancelarPagamento;
    private final PagamentoInputPort pagamentoInputPort;
    private final CancelarPagamentoOutputPort cancelarPagamentoOutputPort;

    @Override
    @SqsListener("${sqs.queue.pagamento.cancelar-pagamento}")
    public void cancelarPagamento(String mensagem) {
        CancelarPagamentoResponse cancelarPagamentoResponse = new Gson().fromJson(mensagem, CancelarPagamentoResponse.class);
        Long pedidoId = cancelarPagamentoResponse.getPedidoId();
        try {
            Long pagamentoId = pagamentoInputPort.consultarPorIdPedido(pedidoId).getId();
            cancelarPagamentoOutputPort.cancelar(pagamentoId, pedidoId);
            log.info("Pedido {} cancelado recebido da fila {} com sucesso!", pedidoId, filaPagamentoCancelarPagamento);
        } catch (Exception ex) {
            log.error("Erro ao processar cancelar pagamento do pedido {} da fila {}: {}", pedidoId, filaPagamentoCancelarPagamento, ex.getMessage());
            throw ex;
        }
    }
}
