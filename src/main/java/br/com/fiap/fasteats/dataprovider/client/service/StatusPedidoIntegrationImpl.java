package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.core.domain.exception.StatusPedidoNotFound;
import br.com.fiap.fasteats.dataprovider.client.StatusPedidoIntegration;
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

    private final String PATH = "/status-pedidos";

    @Override
    public Optional<StatusPedidoResponse> consultar(Long id) {
        try {
            StatusPedidoResponse statusPedidoResponse =
                    restTemplate.getForObject(URL_BASE + PATH + "/{id}", StatusPedidoResponse.class, id);

            return Optional.ofNullable(statusPedidoResponse);
        } catch (Exception ex) {
            logger.error("Erro retorno microservice pedido ", ex.getCause());
            throw new StatusPedidoNotFound("Erro retorno microservice pedido " + ex.getMessage());
        }
    }

    @Override
    public Optional<List<StatusPedidoResponse>> listar() {
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    URL_BASE + PATH + "/",
                    HttpMethod.GET,
                    null,
                    String.class);

            String jsonResponse = responseEntity.getBody();

            if (jsonResponse != null && !jsonResponse.isEmpty()) {
                List<StatusPedidoResponse> statusPedidoResponseList = restTemplate.getForObject(
                        URL_BASE + PATH + "/",
                        List.class);  // Aqui você obtém diretamente uma lista de objetos

                return Optional.of(statusPedidoResponseList);
            } else {
                // Log para identificar se a resposta está vazia
                logger.warn("A resposta do microservice de pedido está vazia ou nula.");
                return Optional.empty();
            }
        } catch (Exception ex) {
            // Log detalhado para investigar o problema
            logger.error("Erro ao obter dados do microservice de pedido ", ex);
            throw new StatusPedidoNotFound("Erro ao obter dados do microservice de pedido: " + ex.getMessage());
        }
    }

    @Override
    public Optional<StatusPedidoResponse> consultarPorNome(String nome) {
        try {
            StatusPedidoResponse statusPedidoResponse =
                    restTemplate.getForObject(URL_BASE + PATH + "/consultar-por-nome/{nome}", StatusPedidoResponse.class, nome);

            return Optional.ofNullable(statusPedidoResponse);
        } catch (Exception ex) {
            logger.error("Erro retorno microservice pedido ", ex.getCause());
            throw new StatusPedidoNotFound("Erro retorno microservice pedido " + ex.getMessage());
        }
    }
}
