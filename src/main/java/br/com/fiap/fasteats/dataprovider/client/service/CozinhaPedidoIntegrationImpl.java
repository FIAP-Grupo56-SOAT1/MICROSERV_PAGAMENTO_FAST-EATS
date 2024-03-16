package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.dataprovider.client.CozinhaPedidoIntegration;
import br.com.fiap.fasteats.dataprovider.client.exception.AwsSQSException;
import br.com.fiap.fasteats.dataprovider.client.request.CozinhaReceberPedidoRequest;
import com.google.gson.Gson;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CozinhaPedidoIntegrationImpl implements CozinhaPedidoIntegration {
    @Value("${sqs.queue.cozinha.receber.pedido}")
    private String filaCozinhaReceberPedido;
    private final SqsTemplate sqsTemplate;

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
}
