package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.dataprovider.client.CozinhaPedidoIntegration;
import br.com.fiap.fasteats.dataprovider.client.exception.AwsSQSException;
import br.com.fiap.fasteats.dataprovider.client.request.CozinhaReceberPedidoRequest;
import br.com.fiap.fasteats.dataprovider.client.response.CozinhaErroReceberPedidoResponse;
import com.google.gson.Gson;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CozinhaPedidoIntegrationImpl implements CozinhaPedidoIntegration {
    @Value("${sqs.queue.cozinha.receber.pedido}")
    private String filaCozinhaReceberPedido;
    @Value("${sqs.queue.cozinha.erro.receber.pedido}")
    private String filaCozinhaErroReceberPedido;
    private final SqsTemplate sqsTemplate;
    private final AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    private final PagamentoInputPort pagamentoInputPort;

    @Override
    public void receberPedido(Long pedidoID) {
        try {
            CozinhaReceberPedidoRequest req = new CozinhaReceberPedidoRequest(pedidoID);
            String mensagem = new Gson().toJson(req);
            sqsTemplate.send(filaCozinhaReceberPedido, mensagem);
            log.info("Pedido enviado para fila de cozinha-receber-pedido com sucesso!");
        } catch (Exception ex) {
            String resposta = String.format("Erro na comunicação com a fila cozinha-receber-pedido: %s", ex.getMessage());
            log.error(resposta);
            throw new AwsSQSException(resposta);
        }
    }

    @Override
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
