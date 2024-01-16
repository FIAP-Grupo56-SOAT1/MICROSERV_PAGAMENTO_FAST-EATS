package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.core.domain.exception.PedidoNotFound;
import br.com.fiap.fasteats.dataprovider.client.PedidoIntegration;
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
            PedidoResponse pedidoResponse =
                    restTemplate.getForObject(URL_BASE + URI + "/{id}", PedidoResponse.class, id);

            return Optional.ofNullable(pedidoResponse);
        } catch (Exception ex) {
            logger.error("Erro retorno microservice pedido ", ex.getCause());
            throw new PedidoNotFound("Erro retorno microservice pedido " + ex.getMessage());
        }
    }

    @Override
    public void atualizarStatus(Long id, Long idStatus) {
        try {
            restTemplate.put(URL_BASE + URI + "/{id}/status/{idStatus}", null, id, idStatus);
        } catch (Exception ex) {
            logger.error("Erro atualizar status microservice pedido ", ex.getCause());
            throw new PedidoNotFound("Erro atualizar status microservice pedido " + ex.getMessage());
        }
    }
}
