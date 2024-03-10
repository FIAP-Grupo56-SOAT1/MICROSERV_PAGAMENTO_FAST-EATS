package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.core.dataprovider.AlterarPedidoStatusOutputPort;
import br.com.fiap.fasteats.core.usecase.GerarPagamentoInputPort;
import br.com.fiap.fasteats.dataprovider.client.GerarPagamentoIntegration;
import br.com.fiap.fasteats.dataprovider.client.response.GerarPagamentoResponse;
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
public class GerarPagamentoIntegrationImpl implements GerarPagamentoIntegration {
    @Value("${sqs.queue.pagamento.gerar.pagamento}")
    private String filaPagamentoGerarPagamento;
    private final GerarPagamentoInputPort gerarPagamentoInputPort;
    private final AlterarPedidoStatusOutputPort alterarPedidoStatusOutputPort;

    @Override
    @SqsListener("${sqs.queue.pagamento.gerar.pagamento}")
    public void gerar(String mensagem) {
        GerarPagamentoResponse gerarPagamentoResponse = new Gson().fromJson(mensagem, GerarPagamentoResponse.class);
        try {
            gerarPagamentoInputPort.gerar(gerarPagamentoResponse.getPedidoId(), gerarPagamentoResponse.getFormaPagamentoId());
            String logSuceso = String.format("Pedido %d com forma de pagamento %d recebido para gerar pagamento, mensagem da fila %s",
                    gerarPagamentoResponse.getPedidoId(), gerarPagamentoResponse.getFormaPagamentoId(), filaPagamentoGerarPagamento);
            log.info(logSuceso);
        } catch (Exception ex) {
            String mensagemErro = String.format("Erro ao processar gerar pagamento Pedido %d Forma de Pagamento %d da fila %s: {}"
                    , gerarPagamentoResponse.getPedidoId(), gerarPagamentoResponse.getFormaPagamentoId(), filaPagamentoGerarPagamento);
            log.error(mensagemErro, ex.getMessage());
            alterarPedidoStatusOutputPort.criado(gerarPagamentoResponse.getPedidoId());
        }
    }
}
