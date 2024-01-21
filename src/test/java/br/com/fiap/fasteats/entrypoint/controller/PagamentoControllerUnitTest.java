package br.com.fiap.fasteats.entrypoint.controller;

import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.AlterarFormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.CancelarPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.RealizarPagamentoInputPort;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Pagamento Controller")
class PagamentoControllerUnitTest {
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private RealizarPagamentoInputPort realizarPagamentoInputPort;
    @Mock
    private CancelarPagamentoInputPort cancelarPagamentoInputPort;
    @Mock
    private AlterarFormaPagamentoInputPort alterarFormaPagamentoInputPort;
    @Mock
    private PagamentoMapper pagamentoMapper;
    @InjectMocks
    private PagamentoController pagamentoController;
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
    @DisplayName("Deve realizar pagamento de pedido")
    void realizarPagamento() {
        // Arrange
        Long idPedido = 1L;
        Pagamento pagamento = getPagamento(1L);
        PagamentoResponse pagamentoResponse = getPagamentoResponse(1L);

        when(realizarPagamentoInputPort.realizarPagamento(idPedido)).thenReturn(pagamento);
        when(pagamentoMapper.toPagamentoResponse(pagamento)).thenReturn(pagamentoResponse);

        // Act
        ResponseEntity<PagamentoResponse> result = pagamentoController.realizarPagamento(idPedido);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(pagamentoResponse, result.getBody());
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(realizarPagamentoInputPort).realizarPagamento(idPedido);
        verify(pagamentoMapper).toPagamentoResponse(pagamento);
    }

    @Test
    @DisplayName("Deve listar todos os pagamentos")
    void listarPagamentos() {
        // Arrange
        List<Pagamento> pagamentos = Collections.singletonList(getPagamento(1L));
        List<PagamentoResponse> pagamentosResponse = Collections.singletonList(getPagamentoResponse(1L));

        when(pagamentoInputPort.listar()).thenReturn(pagamentos);
        when(pagamentoMapper.toPagamentosResponse(pagamentos)).thenReturn(pagamentosResponse);

        // Act
        ResponseEntity<List<PagamentoResponse>> result = pagamentoController.listarPagamentos();

        // Assert
        assertNotNull(result.getBody());
        assertEquals(pagamentosResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(pagamentoInputPort).listar();
        verify(pagamentoMapper).toPagamentosResponse(pagamentos);
    }

    @Test
    @DisplayName("Deve consultar um pagamento por ID")
    void consultarPagamento() {
        // Arrange
        Long idPagamento = 1L;
        Pagamento pagamento = getPagamento(idPagamento);
        PagamentoResponse pagamentoResponse = getPagamentoResponse(idPagamento);

        when(pagamentoInputPort.consultar(idPagamento)).thenReturn(pagamento);
        when(pagamentoMapper.toPagamentoResponse(pagamento)).thenReturn(pagamentoResponse);

        // Act
        ResponseEntity<PagamentoResponse> result = pagamentoController.consultarPagamento(idPagamento);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(pagamentoResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(pagamentoInputPort).consultar(idPagamento);
        verify(pagamentoMapper).toPagamentoResponse(pagamento);
    }

    @Test
    @DisplayName("Deve consultar um pagamento por ID pagamento externo")
    void consultarPorIdPagamentoExterno() {
        // Arrange
        Long idPagamentoExterno = 123L;
        Pagamento pagamento = getPagamento(1L);
        PagamentoResponse pagamentoResponse = getPagamentoResponse(1L);

        when(pagamentoInputPort.consultarPorIdPagamentoExterno(idPagamentoExterno)).thenReturn(pagamento);
        when(pagamentoMapper.toPagamentoResponse(pagamento)).thenReturn(pagamentoResponse);

        // Act
        ResponseEntity<PagamentoResponse> result = pagamentoController.consultarPorIdPagamentoExterno(idPagamentoExterno);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(pagamentoResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(pagamentoInputPort).consultarPorIdPagamentoExterno(idPagamentoExterno);
        verify(pagamentoMapper).toPagamentoResponse(pagamento);
    }

    @Test
    @DisplayName("Deve consultar um pagamento por ID pedido")
    void consultarPagamentoPorIdPedido() {
        // Arrange
        Long idPedido = 1L;
        Pagamento pagamento = getPagamento(1L);
        PagamentoResponse pagamentoResponse = getPagamentoResponse(1L);

        when(pagamentoInputPort.consultarPorIdPedido(idPedido)).thenReturn(pagamento);
        when(pagamentoMapper.toPagamentoResponse(pagamento)).thenReturn(pagamentoResponse);

        // Act
        ResponseEntity<PagamentoResponse> result = pagamentoController.consultarPagamentoPorIdPedido(idPedido);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(pagamentoResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(pagamentoInputPort).consultarPorIdPedido(idPedido);
        verify(pagamentoMapper).toPagamentoResponse(pagamento);
    }

    @Test
    @DisplayName("Deve alterar a forma de pagamento")
    void alterarFormaPagamento() {
        // Arrange
        Long idPagamento = 1L;
        Long idFormaPagamento = 2L;
        Pagamento pagamento = getPagamento(idPagamento);
        PagamentoResponse pagamentoResponse = getPagamentoResponse(idPagamento);

        when(alterarFormaPagamentoInputPort.alterarFormaPagamento(idPagamento, idFormaPagamento)).thenReturn(pagamento);
        when(pagamentoMapper.toPagamentoResponse(pagamento)).thenReturn(pagamentoResponse);

        // Act
        ResponseEntity<PagamentoResponse> result = pagamentoController.cancelarPagamento(idPagamento, idFormaPagamento);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(pagamentoResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(alterarFormaPagamentoInputPort).alterarFormaPagamento(idPagamento, idFormaPagamento);
        verify(pagamentoMapper).toPagamentoResponse(pagamento);
    }

    @Test
    @DisplayName("Deve cancelar o pagamento de um pedido")
    void cancelarPagamento() {
        // Arrange
        Long idPedido = 1L;
        Pagamento pagamento = getPagamento(1L);
        PagamentoResponse pagamentoResponse = getPagamentoResponse(1L);

        when(cancelarPagamentoInputPort.cancelar(idPedido)).thenReturn(pagamento);
        when(pagamentoMapper.toPagamentoResponse(pagamento)).thenReturn(pagamentoResponse);

        // Act
        ResponseEntity<PagamentoResponse> result = pagamentoController.cancelarPagamento(idPedido);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(pagamentoResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(cancelarPagamentoInputPort).cancelar(idPedido);
        verify(pagamentoMapper).toPagamentoResponse(pagamento);
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
                .dataHoraCriado(LocalDateTime.now())
                .dataHoraFinalizado(null)
                .build();
    }
}
