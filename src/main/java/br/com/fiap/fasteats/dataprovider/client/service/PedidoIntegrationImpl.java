package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.dataprovider.client.PedidoIntegration;
import br.com.fiap.fasteats.dataprovider.client.exception.MicroservicoPedidoException;
import br.com.fiap.fasteats.dataprovider.client.response.PedidoResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PedidoIntegrationImpl implements PedidoIntegration {
    private final Logger logger = LoggerFactory.getLogger(PedidoIntegrationImpl.class);
    private final RestTemplate restTemplate;
    @Value("${URL_PEDIDO_SERVICE}")
    private String URL_BASE;
    private final String URI = "/pedidos";

    @Override
    public Optional<PedidoResponse> consultar(Long id) {
        try {
            String url = String.format("%s%s/%d", URL_BASE, URI, id);
            PedidoResponse pedidoResponse = restTemplate.getForObject(url, PedidoResponse.class, id);
            return Optional.ofNullable(pedidoResponse);
        } catch (Exception ex) {
            String resposta = String.format("Erro na comunicação com o microserviço Pedido: %s", ex.getMessage());
            logger.error(resposta);
            throw new MicroservicoPedidoException(resposta);
        }
    }

    @Override
    public void atualizarStatus(Long id, Long idStatus) {
        try {
            String url = String.format("%s%s/%d/status/%d", URL_BASE, URI, id, idStatus);
            restTemplate.put(url, null, id, idStatus);
        } catch (Exception ex) {
            String resposta = String.format("Erro na comunicação com o microserviço Pedido: %s", ex.getMessage());
            logger.error(resposta);
            throw new MicroservicoPedidoException(resposta);
        }
    }
}
