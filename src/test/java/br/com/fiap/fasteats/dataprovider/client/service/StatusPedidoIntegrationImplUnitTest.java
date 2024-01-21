package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.dataprovider.client.exception.MicroservicoPedidoException;
import br.com.fiap.fasteats.dataprovider.client.response.StatusPedidoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_CANCELADO;
import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_PAGO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - Status Pedido Integration")
class StatusPedidoIntegrationImplUnitTest {
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private StatusPedidoIntegrationImpl StatusPedidoIntegrationImpl;
    private AutoCloseable openMocks;
    private static final Long STATUS_PEDIDO_ID = 1L;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("Deve consultar status do pedido com sucesso")
    void consultarStatusDoPedidoComSucesso() {
        // Arrange
        StatusPedidoResponse statusPedidoIntegration = getStatusPedidoResponse(STATUS_PEDIDO_ID, STATUS_PEDIDO_PAGO);
        statusPedidoIntegration.setId(STATUS_PEDIDO_ID);
        when(restTemplate.getForObject(anyString(), eq(StatusPedidoResponse.class), eq(STATUS_PEDIDO_ID)))
                .thenReturn(statusPedidoIntegration);

        // Act
        Optional<StatusPedidoResponse> result = StatusPedidoIntegrationImpl.consultar(STATUS_PEDIDO_ID);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(statusPedidoIntegration, result.get());
    }

    @Test
    @DisplayName("Deve lançar exceção ao consultar status do pedido com erro")
    void consultarStatusDoPedidoComErro() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(StatusPedidoResponse.class), eq(STATUS_PEDIDO_ID)))
                .thenThrow(new RuntimeException("Erro na comunicação"));

        // Act & Assert
        assertThrows(MicroservicoPedidoException.class, () -> StatusPedidoIntegrationImpl.consultar(STATUS_PEDIDO_ID));
    }

    @Test
    @DisplayName("Deve listar status dos pedidos com sucesso")
    void listarStatusDosPedidosComSucesso() {
        // Arrange
        List<StatusPedidoResponse> expectedList = Collections.singletonList(getStatusPedidoResponse(STATUS_PEDIDO_ID, STATUS_PEDIDO_PAGO));
        ResponseEntity<String> responseEntity = ResponseEntity.ok("jsonResponse");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(String.class)))
                .thenReturn(responseEntity);
        when(restTemplate.getForObject(anyString(), eq(List.class)))
                .thenReturn(expectedList);

        // Act
        Optional<List<StatusPedidoResponse>> result = StatusPedidoIntegrationImpl.listar();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedList, result.get());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao listar status dos pedidos com resposta nula")
    void listarStatusDosPedidosComRespostaNula() {
        // Arrange
        ResponseEntity<String> responseEntity = ResponseEntity.ok(null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(String.class)))
                .thenReturn(responseEntity);

        // Act
        Optional<List<StatusPedidoResponse>> result = StatusPedidoIntegrationImpl.listar();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção ao listar status dos pedidos com erro")
    void listarStatusDosPedidosComErro() {
        // Arrange
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(String.class)))
                .thenThrow(new RuntimeException("Erro na comunicação"));

        // Act & Assert
        assertThrows(MicroservicoPedidoException.class, () -> StatusPedidoIntegrationImpl.listar());
    }

    @Test
    @DisplayName("Deve consultar status do pedido por nome com sucesso")
    void consultarStatusDoPedidoPorNomeComSucesso() {
        // Arrange
        StatusPedidoResponse statusPedidoIntegration = getStatusPedidoResponse(STATUS_PEDIDO_ID, STATUS_PEDIDO_CANCELADO);
        statusPedidoIntegration.setId(STATUS_PEDIDO_ID);
        when(restTemplate.getForObject(anyString(), eq(StatusPedidoResponse.class), eq(STATUS_PEDIDO_PAGO)))
                .thenReturn(statusPedidoIntegration);

        // Act
        Optional<StatusPedidoResponse> result = StatusPedidoIntegrationImpl.consultarPorNome(STATUS_PEDIDO_PAGO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(statusPedidoIntegration, result.get());
    }

    @Test
    @DisplayName("Deve lançar exceção ao consultar status do pedido por nome com erro")
    void consultarStatusDoPedidoPorNomeComErro() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(StatusPedidoResponse.class), eq(STATUS_PEDIDO_CANCELADO)))
                .thenThrow(new RuntimeException("Erro na comunicação"));

        // Act & Assert
        assertThrows(MicroservicoPedidoException.class, () -> StatusPedidoIntegrationImpl.consultarPorNome(STATUS_PEDIDO_CANCELADO));
    }

    private StatusPedidoResponse getStatusPedidoResponse(Long statusPedidoId, String statusPedidoNome) {
        StatusPedidoResponse statusPedidoResponse = new StatusPedidoResponse();
        statusPedidoResponse.setId(statusPedidoId);
        statusPedidoResponse.setNome(statusPedidoNome);
        statusPedidoResponse.setAtivo(true);
        return statusPedidoResponse;
    }
}
