package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.dataprovider.client.CozinhaPedidoIntegration;
import br.com.fiap.fasteats.dataprovider.client.exception.MicroservicoCozinhaException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
        String resposta;
        try {
            String url = String.format("%s%s/%d/receber-pedido", URL_BASE, URI, idPedido);
            ResponseEntity<String> respostaEntity = restTemplate.postForEntity(url, null, String.class, idPedido);

            if (!respostaEntity.getStatusCode().is2xxSuccessful()) {
                resposta = String.format("Erro ao chamar o microserviço Cozinha, status code: %s - body: %s",
                        respostaEntity.getStatusCode(),
                        respostaEntity.getBody());
                throw new MicroservicoCozinhaException(resposta);
            }

            resposta = String.format("Resposta da requisição POST microserviço Cozinha, status code: %s - body: %s",
                    respostaEntity.getStatusCode(),
                    respostaEntity.getBody());
            logger.info(resposta);
        } catch (Exception e) {
            resposta = String.format("Erro na comunicação com o microserviço Cozinha: %s", e.getMessage());
            logger.error(resposta);
            throw new MicroservicoCozinhaException(resposta);
        }
    }
}
