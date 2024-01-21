package br.com.fiap.fasteats.entrypoint.controller;

import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;
import br.com.fiap.fasteats.core.usecase.PagamentoExternoInputPort;
import br.com.fiap.fasteats.entrypoint.controller.mapper.PagamentoMapper;
import br.com.fiap.fasteats.entrypoint.controller.request.PagamentoWebhookDataRequest;
import br.com.fiap.fasteats.entrypoint.controller.request.PagamentoWebhookRequest;
import br.com.fiap.fasteats.entrypoint.controller.request.StatusPagamentoRequest;
import br.com.fiap.fasteats.entrypoint.controller.response.PagamentoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - Pagamento Externo Controller")
class PagamentoExternoControllerUnitTest {
    @Mock
    private PagamentoExternoInputPort pagamentoExternoInputPort;
    @Mock
    private PagamentoMapper pagamentoMapper;
    @InjectMocks
    private PagamentoExternoController pagamentoExternoController;
    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("Deve simular retorno de pagamento externo por Webhook")
    void webhookPagamentoSimulacao() {
        // Arrange
        Long idPagamentoExterno = 123L;
        StatusPagamentoRequest statusMercadoPago = StatusPagamentoRequest.APPROVED;
        Pagamento pagamento = getPagamento(1L);
        PagamentoResponse pagamentoResponse = getPagamentoResponse(1L);

        when(pagamentoExternoInputPort.atualizarPagamento(any(PagamentoExterno.class))).thenReturn(pagamento);
        when(pagamentoMapper.toPagamentoResponse(pagamento)).thenReturn(pagamentoResponse);

        // Act
        ResponseEntity<PagamentoResponse> result = pagamentoExternoController.webhookPagamentoSimulacao(idPagamentoExterno, statusMercadoPago);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(pagamentoResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(pagamentoExternoInputPort).atualizarPagamento(any(PagamentoExterno.class));
        verify(pagamentoMapper).toPagamentoResponse(pagamento);
    }

    @Test
    @DisplayName("Deve processar retorno de pagamento externo por Webhook")
    void webhookPagamento() {
        // Arrange
        PagamentoWebhookDataRequest data = new PagamentoWebhookDataRequest();
        data.setId("123");
        PagamentoWebhookRequest pagamentoWebhookRequest = new PagamentoWebhookRequest();
        pagamentoWebhookRequest.setId("123");
        pagamentoWebhookRequest.setType("payment");
        pagamentoWebhookRequest.setData(data);
        Pagamento pagamento = getPagamento(1L);
        PagamentoResponse pagamentoResponse = getPagamentoResponse(1L);

        when(pagamentoExternoInputPort.atualizarPagamento(any(PagamentoExterno.class))).thenReturn(pagamento);
        when(pagamentoMapper.toPagamentoResponse(pagamento)).thenReturn(pagamentoResponse);

        // Act
        ResponseEntity<PagamentoResponse> result = pagamentoExternoController.webhookPagamento(pagamentoWebhookRequest);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(pagamentoResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(pagamentoExternoInputPort).atualizarPagamento(any(PagamentoExterno.class));
        verify(pagamentoMapper).toPagamentoResponse(pagamento);
    }

    @Test
    @DisplayName("Deve cancelar pagamento do Mercado Pago")
    void cancelarPagamento() {
        // Arrange
        Long idPagamentoExterno = 123L;

        // Act
        ResponseEntity<Void> result = pagamentoExternoController.cancelarPagamento(idPagamentoExterno);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(pagamentoExternoInputPort).cancelarPagamentoExterno(idPagamentoExterno);
    }

    private Pagamento getPagamento(Long pagamentoId) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(1L);
        pagamento.setQrCode(null);
        pagamento.setUrlPagamento(null);
        return pagamento;
    }

    private PagamentoResponse getPagamentoResponse(Long id) {
        return PagamentoResponse.builder()
                .id(id)
                .formaPagamento(null)
                .statusPagamento(null)
                .pedidoId(1L)
                .idPagamentoExterno(null)
                .qrCode(null)
                .urlPagamento(null)
                .dataHoraCriado(null)
                .dataHoraFinalizado(null)
                .build();
    }
}
