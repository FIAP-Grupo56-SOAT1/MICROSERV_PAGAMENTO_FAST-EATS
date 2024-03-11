package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.dataprovider.client.PedidoIntegration;
import br.com.fiap.fasteats.dataprovider.client.exception.AwsSQSException;
import br.com.fiap.fasteats.dataprovider.client.exception.MicroservicoPedidoException;
import br.com.fiap.fasteats.dataprovider.client.request.AtualizarStatusPedidoRequest;
import br.com.fiap.fasteats.dataprovider.client.response.PedidoResponse;
import com.google.gson.Gson;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoIntegrationImpl implements PedidoIntegration {
    @Value("${sqs.queue.pedido.criado}")
    private String filaPedidoCriado;
    @Value("${sqs.queue.pedido.aguardando-pagamento}")
    private String filaPedidoAguardandoPagamento;
    @Value("${sqs.queue.pedido.pago}")
    private String filaPedidoPago;
    @Value("${sqs.queue.pedido.cancelado}")
    private String filaPedidoCancelado;
    @Value("${URL_PEDIDO_SERVICE}")
    private String URL_BASE;
    private final String URI = "/pedidos";
    private final RestTemplate restTemplate;
    private final SqsTemplate sqsTemplate;

    @Override
    public Optional<PedidoResponse> consultar(Long pedidoId) {
        try {
            String url = String.format("%s%s/%d", URL_BASE, URI, pedidoId);
            PedidoResponse pedidoResponse = restTemplate.getForObject(url, PedidoResponse.class, pedidoId);
            return Optional.ofNullable(pedidoResponse);
        } catch (Exception ex) {
            String resposta = String.format("Erro na comunicação com o microserviço Pedido: %s", ex.getMessage());
            log.error(resposta);
            throw new MicroservicoPedidoException(resposta);
        }
    }

    @Override
    public void pedidoCriado(Long pedidoId) {
        enviarParaFilaPedidoStatus(pedidoId, filaPedidoCriado, STATUS_PEDIDO_CRIADO);
    }

    @Override
    public void pedidoAguardandoPagamento(Long pedidoId) {
        enviarParaFilaPedidoStatus(pedidoId, filaPedidoAguardandoPagamento, STATUS_PEDIDO_AGUARDANDO_PAGAMENTO);
    }

    @Override
    public void pedidoPago(Long pedidoId) {
        enviarParaFilaPedidoStatus(pedidoId, filaPedidoPago, STATUS_PEDIDO_PAGO);
    }

    @Override
    public void pedidoCancelado(Long pedidoId) {
        enviarParaFilaPedidoStatus(pedidoId, filaPedidoCancelado, STATUS_PEDIDO_CANCELADO);
    }

    private void enviarParaFilaPedidoStatus(Long pedidoId, String nomeFila, String status) {
        try {
            AtualizarStatusPedidoRequest request = new AtualizarStatusPedidoRequest(pedidoId);
            String mensagem = new Gson().toJson(request);
            sqsTemplate.send(nomeFila, mensagem);
            String mensagemLog = String.format("Atualizar Status do Pedido %d para %s enviado para fila de %s com sucesso!", pedidoId, status, nomeFila);
            log.info(mensagemLog);
        } catch (Exception ex) {
            String resposta = String.format("Erro na comunicação com a fila %s ao tentar altera o status do pedido %d: %s", nomeFila, pedidoId, ex.getMessage());
            log.error(resposta);
            throw new AwsSQSException(resposta);
        }
    }
}
