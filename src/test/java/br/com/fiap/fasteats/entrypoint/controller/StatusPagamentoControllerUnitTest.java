package br.com.fiap.fasteats.entrypoint.controller;

import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.entrypoint.controller.mapper.StatusPagamentoMapper;
import br.com.fiap.fasteats.entrypoint.controller.response.StatusPagamentoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_EM_PROCESSAMENTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - StatusPagamentoController")
class StatusPagamentoControllerUnitTest {
    @Mock
    private StatusPagamentoInputPort statusPagamentoInputPort;
    @Mock
    private StatusPagamentoMapper statusPagamentoMapper;
    @InjectMocks
    private StatusPagamentoController statusPagamentoController;
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
    @DisplayName("Deve consultar status de pagamento por ID")
    void consultarStatusPagamento() {
        // Arrange
        Long id = 1L;
        StatusPagamento statusPagamento = getStatusPagamento(id);
        StatusPagamentoResponse statusPagamentoResponse = getStatusPagamentoResponse(id);

        when(statusPagamentoInputPort.consultar(id)).thenReturn(statusPagamento);
        when(statusPagamentoMapper.toStatusPagamentoResponse(statusPagamento)).thenReturn(statusPagamentoResponse);

        // Act
        ResponseEntity<StatusPagamentoResponse> result = statusPagamentoController.consultarStatusPagamento(id);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(statusPagamentoResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(statusPagamentoInputPort).consultar(id);
        verify(statusPagamentoMapper).toStatusPagamentoResponse(statusPagamento);
    }

    @Test
    @DisplayName("Deve consultar status de pagamento por nome")
    void consultarStatusPagamentoPorNome() {
        // Arrange
        String nome = "Aprovado";
        StatusPagamento statusPagamento = getStatusPagamento(1L);
        StatusPagamentoResponse statusPagamentoResponse = getStatusPagamentoResponse(1L);

        when(statusPagamentoInputPort.consultarPorNome(nome)).thenReturn(statusPagamento);
        when(statusPagamentoMapper.toStatusPagamentoResponse(statusPagamento)).thenReturn(statusPagamentoResponse);

        // Act
        ResponseEntity<StatusPagamentoResponse> result = statusPagamentoController.consultarStatusPagamentoPorNome(nome);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(statusPagamentoResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(statusPagamentoInputPort).consultarPorNome(nome);
        verify(statusPagamentoMapper).toStatusPagamentoResponse(statusPagamento);
    }

    @Test
    @DisplayName("Deve listar status de pagamentos")
    void listarStatusPedidos() {
        // Arrange
        List<StatusPagamento> listStatusPagamento = new ArrayList<>();
        listStatusPagamento.add(getStatusPagamento(1L));
        listStatusPagamento.add(getStatusPagamento(2L));

        List<StatusPagamentoResponse> statusPagamentoResponses = new ArrayList<>();
        statusPagamentoResponses.add(getStatusPagamentoResponse(1L));
        statusPagamentoResponses.add(getStatusPagamentoResponse(2L));

        when(statusPagamentoInputPort.listar()).thenReturn(listStatusPagamento);
        when(statusPagamentoMapper.toStatusPagamentoResponse(any(StatusPagamento.class))).thenReturn(statusPagamentoResponses.get(0), statusPagamentoResponses.get(1));

        // Act
        ResponseEntity<List<StatusPagamentoResponse>> result = statusPagamentoController.listarStatusPedidos();

        // Assert
        assertNotNull(result.getBody());
        assertEquals(statusPagamentoResponses, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(statusPagamentoInputPort).listar();
        verify(statusPagamentoMapper, times(2)).toStatusPagamentoResponse(any(StatusPagamento.class));
    }

    private StatusPagamento getStatusPagamento(Long id) {
        return new StatusPagamento(id, STATUS_EM_PROCESSAMENTO, true);
    }

    private StatusPagamentoResponse getStatusPagamentoResponse(Long id) {
        return StatusPagamentoResponse.builder()
                .id(id)
                .nome("Aprovado")
                .ativo(true)
                .build();
    }
}
