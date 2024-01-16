package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.core.domain.exception.PedidoNotFound;
import br.com.fiap.fasteats.dataprovider.client.CozinhaPedidoIntegration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CozinhaPedidoIntegrationImpl implements CozinhaPedidoIntegration {
    private final Logger logger = LoggerFactory.getLogger(CozinhaPedidoIntegrationImpl.class);

    private final RestTemplate restTemplate;

    @Value("${URL_COZINHA_PEDIDO_SERVICE}")
    private String URL_BASE;

    private final String URI = "/cozinha-pedido";

    @Override
    public void receberPedido(Long idPedido) {
        try {
            var resposta = restTemplate.postForObject(URL_BASE + URI + "/{idPedido}/receber-pedido",
                    null, String.class, idPedido);
            System.out.println("Resposta da requisição POST com path param: " + resposta);
        } catch (Exception ex) {
            logger.error("Erro retorno microservice cozinha ", ex.getCause());
            throw new PedidoNotFound("Erro retorno microservice cozinha " + ex.getMessage());
        }
    }
}
