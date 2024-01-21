package br.com.fiap.fasteats.entrypoint.controller;

import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.GerarPagamentoInputPort;
import br.com.fiap.fasteats.entrypoint.controller.mapper.PagamentoMapper;
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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Gerar Pagamento Controller")
class GerarPagamentoControllerUnitTest {
    @Mock
    private GerarPagamentoInputPort gerarPagamentoInputPort;
    @Mock
    private PagamentoMapper pagamentoMapper;
    @InjectMocks
    private GerarPagamentoController gerarPagamentoController;
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
    @DisplayName("Deve gerar pagamento de pedido")
    void gerarPagamento() {
        // Arrange
        Long idPedido = 1L;
        Long formaPagamentoId = 2L;

        Pagamento pagamento = getPagamento(1L);
        PagamentoResponse pagamentoResponse = getPagamentoResponse(1L);

        when(gerarPagamentoInputPort.gerar(idPedido, formaPagamentoId)).thenReturn(pagamento);
        when(pagamentoMapper.toPagamentoResponse(pagamento)).thenReturn(pagamentoResponse);

        // Act
        ResponseEntity<PagamentoResponse> result = gerarPagamentoController.gerarPagamento(idPedido, formaPagamentoId);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(pagamentoResponse, result.getBody());
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(gerarPagamentoInputPort).gerar(idPedido, formaPagamentoId);
        verify(pagamentoMapper).toPagamentoResponse(pagamento);
    }

    private Pagamento getPagamento(Long pagamentoId) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(1L);
        pagamento.setValor(100.0);
        pagamento.setIdPagamentoExterno(null);
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
                .dataHoraCriado(LocalDateTime.now())
                .dataHoraFinalizado(null)
                .build();
    }
}
