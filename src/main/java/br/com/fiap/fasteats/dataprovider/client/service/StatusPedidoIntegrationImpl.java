package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.dataprovider.client.StatusPedidoIntegration;
import br.com.fiap.fasteats.dataprovider.client.exception.MicroservicoPedidoException;
import br.com.fiap.fasteats.dataprovider.client.response.StatusPedidoResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StatusPedidoIntegrationImpl implements StatusPedidoIntegration {
    private final Logger logger = LoggerFactory.getLogger(StatusPedidoIntegrationImpl.class);
    private final RestTemplate restTemplate;
    @Value("${URL_PEDIDO_SERVICE}")
    private String URL_BASE;
    private final String URI = "/status-pedidos";

    @Override
    public Optional<StatusPedidoResponse> consultar(Long id) {
        try {
            String url = String.format("%s%s/%d", URL_BASE, URI, id);
            StatusPedidoResponse statusPedidoResponse =
                    restTemplate.getForObject(url, StatusPedidoResponse.class, id);
            return Optional.ofNullable(statusPedidoResponse);
        } catch (Exception ex) {
            String resposta = String.format("Erro na comunicação com o microserviço Pedido: %s", ex.getMessage());
            logger.error(resposta);
            throw new MicroservicoPedidoException(resposta);
        }
    }

    @Override
    public Optional<List<StatusPedidoResponse>> listar() {
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    URL_BASE + URI + "/",
                    HttpMethod.GET,
                    null,
                    String.class);
            String jsonResponse = responseEntity.getBody();

            if (jsonResponse != null && !jsonResponse.isEmpty()) {
                String url = String.format("%s%s/", URL_BASE, URI);
                List<StatusPedidoResponse> statusPedidoResponseList = restTemplate.getForObject(url, List.class);
                return Optional.ofNullable(statusPedidoResponseList);
            } else {
                logger.warn("A resposta do microservice de pedido está vazia ou nula.");
                return Optional.empty();
            }
        } catch (Exception ex) {
            String resposta = String.format("Erro na comunicação com o microserviço Pedido: %s", ex.getMessage());
            logger.error(resposta);
            throw new MicroservicoPedidoException(resposta);
        }
    }

    @Override
    public Optional<StatusPedidoResponse> consultarPorNome(String nome) {
        try {
            String url = String.format("%s%s/consultar-por-nome/%s", URL_BASE, URI, nome);
            StatusPedidoResponse statusPedidoResponse =
                    restTemplate.getForObject(url, StatusPedidoResponse.class, nome);
            return Optional.ofNullable(statusPedidoResponse);
        } catch (Exception ex) {
            String resposta = String.format("Erro na comunicação com o microserviço Pedido: %s", ex.getMessage());
            logger.error(resposta);
            throw new MicroservicoPedidoException(resposta);
        }
    }
}
