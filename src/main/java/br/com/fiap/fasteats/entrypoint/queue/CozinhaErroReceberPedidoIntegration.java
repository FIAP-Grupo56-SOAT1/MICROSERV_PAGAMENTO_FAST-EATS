package br.com.fiap.fasteats.entrypoint.queue;

import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.dataprovider.client.response.CozinhaErroReceberPedidoResponse;
import com.google.gson.Gson;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.cloud.aws.sqs.enabled", havingValue = "true", matchIfMissing = true)
public class CozinhaErroReceberPedidoIntegration {
    @Value("${sqs.queue.cozinha.erro.receber.pedido}")
    private String filaCozinhaErroReceberPedido;
    private final SqsTemplate sqsTemplate;
    private final AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    private final PagamentoInputPort pagamentoInputPort;

    @SqsListener("${sqs.queue.cozinha.erro.receber.pedido}")
    public void erroReceberPedido(String mensagem) {
        try {
            CozinhaErroReceberPedidoResponse resposta = new Gson().fromJson(mensagem, CozinhaErroReceberPedidoResponse.class);
            Long pagamentoId = pagamentoInputPort.consultarPorIdPedido(resposta.getPedidoId()).getId();
            alterarPagamentoStatusInputPort.pago(pagamentoId);
            log.info(String.format("O Pagamento %d retornou para o status pago, mensagem da fila %s!", pagamentoId, filaCozinhaErroReceberPedido));
        } catch (Exception ex) {
            String mensagemErro = String.format("Erro ao processar mensagem da fila %s : {}", filaCozinhaErroReceberPedido);
            log.error(mensagemErro, ex.getMessage());
            throw ex;
        }
    }
}
