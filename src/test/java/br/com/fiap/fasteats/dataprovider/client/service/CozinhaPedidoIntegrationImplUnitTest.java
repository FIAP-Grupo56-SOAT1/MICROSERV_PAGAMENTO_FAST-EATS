package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.dataprovider.client.exception.MicroservicoCozinhaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - Cozinha Pedido Integration")
class CozinhaPedidoIntegrationImplUnitTest {
    @Mock
    private RestTemplate restTemplate;
    @Value("${URL_COZINHA_PEDIDO_SERVICE}")
    private String URL_BASE;
    private final String URI = "/cozinha-pedido";
    @InjectMocks
    private CozinhaPedidoIntegrationImpl cozinhaPedidoIntegration;
    AutoCloseable openMocks;
    private static final Long PEDIDO_ID = 1L;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("Deve receber um pedido na cozinha")
    void receberPedido() {
        // Arrange
        String url = String.format("%s%s/%d/receber-pedido", URL_BASE, URI, PEDIDO_ID);
        ResponseEntity<String> respostaEntity = ResponseEntity.ok("Pedido recebido com sucesso");

        doReturn(respostaEntity).when(restTemplate).postForEntity(eq(url), any(), eq(String.class), eq(PEDIDO_ID));

        // Act
        cozinhaPedidoIntegration.receberPedido(PEDIDO_ID);

        // Assert
        verify(restTemplate).postForEntity(eq(url), any(), eq(String.class), eq(PEDIDO_ID));
    }

    @Test
    @DisplayName("Deve apresentar erro ao receber um pedido na cozinha, pois o status code é diferente de 200")
    void receberPedidoErroStatusCode400() {
        // Arrange
        String url = String.format("%s%s/%d/receber-pedido", URL_BASE, URI, PEDIDO_ID);
        ResponseEntity<String> respostaEntity = ResponseEntity.badRequest().body("Erro ao receber pedido");

        doReturn(respostaEntity).when(restTemplate).postForEntity(eq(url), any(), eq(String.class), eq(PEDIDO_ID));

        // Act & Assert
        assertThrows(MicroservicoCozinhaException.class, () -> cozinhaPedidoIntegration.receberPedido(PEDIDO_ID));
        verify(restTemplate).postForEntity(eq(url), any(), eq(String.class), eq(PEDIDO_ID));
    }

    @Test
    @DisplayName("Deve apresentar erro ao receber um pedido na cozinha, pois não foi possível se comunicar com o microserviço")
    void receberPedidoException() {
        // Arrange
        String url = String.format("%s%s/%d/receber-pedido", URL_BASE, URI, PEDIDO_ID);

        // Act & Assert
        assertThrows(MicroservicoCozinhaException.class, () -> cozinhaPedidoIntegration.receberPedido(PEDIDO_ID));
        verify(restTemplate).postForEntity(eq(url), any(), eq(String.class), eq(PEDIDO_ID));
    }
}