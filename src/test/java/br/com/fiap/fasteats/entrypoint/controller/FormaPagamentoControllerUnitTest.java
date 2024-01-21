package br.com.fiap.fasteats.entrypoint.controller;

import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.entrypoint.controller.mapper.FormaPagamentoMapper;
import br.com.fiap.fasteats.entrypoint.controller.request.FormaPagamentoRequest;
import br.com.fiap.fasteats.entrypoint.controller.response.FormaPagamentoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - Forma Pagamento Controller")
class FormaPagamentoControllerUnitTest {
    @Mock
    private FormaPagamentoInputPort formaPagamentoInputPort;
    @Mock
    private FormaPagamentoMapper formaPagamentoMapper;
    @InjectMocks
    private FormaPagamentoController formaPagamentoController;
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
    @DisplayName("Deve criar uma nova forma de pagamento")
    void criarFormaPagamento() {
        // Arrange
        FormaPagamentoRequest request = getFormaPagamentoRequest("DINHEIRO", false);
        FormaPagamento formaPagamento = getFormaPagamento(1L, "DINHEIRO", false);
        FormaPagamentoResponse response = getFormaPagamentoResponse(1L, "DINHEIRO", false);

        when(formaPagamentoMapper.toFormaPagamento(request)).thenReturn(formaPagamento);
        when(formaPagamentoInputPort.criar(formaPagamento)).thenReturn(formaPagamento);
        when(formaPagamentoMapper.toFormaPagamentoResponse(formaPagamento)).thenReturn(response);

        // Act
        ResponseEntity<FormaPagamentoResponse> result = formaPagamentoController.criarFormaPagamento(request);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(response, result.getBody());
        verify(formaPagamentoMapper).toFormaPagamento(request);
        verify(formaPagamentoInputPort).criar(formaPagamento);
        verify(formaPagamentoMapper).toFormaPagamentoResponse(formaPagamento);
    }

    @Test
    @DisplayName("Deve atualizar uma forma de pagamento existente")
    void atualizarFormaPagamento() {
        // Arrange
        Long id = 1L;
        FormaPagamentoRequest request = getFormaPagamentoRequest("VR", true);
        FormaPagamento formaPagamento = getFormaPagamento(id, "VR", true);
        formaPagamento.setId(id);
        FormaPagamentoResponse response = new FormaPagamentoResponse();

        when(formaPagamentoMapper.toFormaPagamento(request)).thenReturn(formaPagamento);
        when(formaPagamentoInputPort.atualizar(formaPagamento)).thenReturn(formaPagamento);
        when(formaPagamentoMapper.toFormaPagamentoResponse(formaPagamento)).thenReturn(response);

        // Act
        ResponseEntity<FormaPagamentoResponse> result = formaPagamentoController.atualizarFormaPagamento(id, request);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(response, result.getBody());
        verify(formaPagamentoMapper).toFormaPagamento(request);
        verify(formaPagamentoInputPort).atualizar(formaPagamento);
        verify(formaPagamentoMapper).toFormaPagamentoResponse(formaPagamento);
    }

    @Test
    @DisplayName("Deve deletar uma forma de pagamento existente")
    void deletarFormaPagamento() {
        // Arrange
        Long id = 1L;
        doNothing().when(formaPagamentoInputPort).deletar(id);

        // Act
        ResponseEntity<Void> result = formaPagamentoController.deletarFormaPagamento(id);

        // Assert
        assertEquals(204, result.getStatusCodeValue());
        verify(formaPagamentoInputPort).deletar(id);
    }

    @Test
    @DisplayName("Deve consultar uma forma de pagamento por ID")
    void consultarFormaPagamento() {
        // Arrange
        Long id = 1L;
        FormaPagamento formaPagamento = getFormaPagamento(id, "DINHEIRO", false);
        FormaPagamentoResponse response = getFormaPagamentoResponse(id, "DINHEIRO", false);
        when(formaPagamentoInputPort.consultar(id)).thenReturn(formaPagamento);
        when(formaPagamentoMapper.toFormaPagamentoResponse(formaPagamento)).thenReturn(response);

        // Act
        ResponseEntity<FormaPagamentoResponse> result = formaPagamentoController.consultarFormaPagamento(id);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(response, result.getBody());
        verify(formaPagamentoInputPort).consultar(id);
        verify(formaPagamentoMapper).toFormaPagamentoResponse(formaPagamento);
    }

    @Test
    @DisplayName("Deve consultar uma forma de pagamento por nome")
    void consultarFormaPagamentoPorNome() {
        // Arrange
        String nome = "CARTAO_CREDITO";
        FormaPagamento formaPagamento = getFormaPagamento(1L, nome, true);
        FormaPagamentoResponse response = getFormaPagamentoResponse(1L, nome, true);

        when(formaPagamentoInputPort.consultarPorNome(nome)).thenReturn(formaPagamento);
        when(formaPagamentoMapper.toFormaPagamentoResponse(formaPagamento)).thenReturn(response);

        // Act
        ResponseEntity<FormaPagamentoResponse> result = formaPagamentoController.consultarFormaPagamentoPorNome(nome);

        // Assert
        assertNotNull(result.getBody());
        assertEquals(response, result.getBody());
        verify(formaPagamentoInputPort).consultarPorNome(nome);
        verify(formaPagamentoMapper).toFormaPagamentoResponse(formaPagamento);
    }

    @Test
    @DisplayName("Deve listar todas as formas de pagamento")
    void listarFormaPagamentos() {
        // Arrange
        List<FormaPagamento> formaPagamentoList = Collections.singletonList(getFormaPagamento(1L, "DINHEIRO", false));
        List<FormaPagamentoResponse> responseList = Collections.singletonList(getFormaPagamentoResponse(1L, "DINHEIRO", false));

        when(formaPagamentoInputPort.listar()).thenReturn(formaPagamentoList);
        when(formaPagamentoMapper.toFormaPagamentoResponseList(formaPagamentoList)).thenReturn(responseList);

        // Act
        ResponseEntity<List<FormaPagamentoResponse>> result = formaPagamentoController.listarFormaPagamentos();

        // Assert
        assertNotNull(result.getBody());
        assertEquals(responseList, result.getBody());
        verify(formaPagamentoInputPort).listar();
        verify(formaPagamentoMapper).toFormaPagamentoResponseList(formaPagamentoList);
    }

    private FormaPagamento getFormaPagamento(Long id, String nome, Boolean externo) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(id);
        formaPagamento.setNome(nome);
        formaPagamento.setExterno(externo);
        formaPagamento.setAtivo(true);
        return formaPagamento;
    }

    private FormaPagamentoResponse getFormaPagamentoResponse(Long id, String nome, Boolean externo) {
        FormaPagamentoResponse formaPagamentoResponse = new FormaPagamentoResponse();
        formaPagamentoResponse.setId(id);
        formaPagamentoResponse.setNome(nome);
        formaPagamentoResponse.setExterno(externo);
        formaPagamentoResponse.setAtivo(true);
        return formaPagamentoResponse;
    }

    private FormaPagamentoRequest getFormaPagamentoRequest(String nome, Boolean externo) {
        FormaPagamentoRequest formaPagamentoRequest = new FormaPagamentoRequest();
        formaPagamentoRequest.setNome(nome);
        formaPagamentoRequest.setExterno(externo);
        formaPagamentoRequest.setAtivo(true);
        return formaPagamentoRequest;
    }
}
