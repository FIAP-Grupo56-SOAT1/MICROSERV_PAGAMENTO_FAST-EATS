package br.com.fiap.fasteats.core.validator.impl;

import br.com.fiap.fasteats.core.dataprovider.PagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.dataprovider.client.response.PedidoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Pagamento Validator")
class PagamentoValidatorImplUnitTest {
    @Mock
    private PagamentoOutputPort pagamentoOutputPort;
    @Mock
    private StatusPagamentoInputPort statusPagamentoInputPort;
    @InjectMocks
    private PagamentoValidatorImpl pagamentoValidatorImpl;
    AutoCloseable openMocks;
    private final Long PAGAMENTO_ID = 1L;
    private final Long PEDIDO_ID = 1L;
    private final Long FORMA_PAGAMENTO_ID = 1L;
    private final Long STATUS_PAGAMENTO_ID = 1L;

    @BeforeEach
    void setUp() {
        openMocks = org.mockito.MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("Deve validar alterar pagamento")
    void validarAlterarPagamento() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO);

        when(pagamentoOutputPort.consultar(PAGAMENTO_ID)).thenReturn(Optional.of(pagamento));
        when(statusPagamentoInputPort.consultar(STATUS_PAGAMENTO_ID)).thenReturn(statusPagamento);

        // Act
        pagamentoValidatorImpl.validarAlterarPagamento(PAGAMENTO_ID);

        // Assert
        verify(pagamentoOutputPort).consultar(PAGAMENTO_ID);
        verify(statusPagamentoInputPort).consultar(STATUS_PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar alterar pagamento com pagamento não encontrado")
    void validarAlterarPagamentoNaoEncontrado() {
        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> pagamentoValidatorImpl.validarAlterarPagamento(PAGAMENTO_ID));
        verify(pagamentoOutputPort).consultar(PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar alterar pagamento com pagamento já pago")
    void validarAlterarPagamentoJaPago() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_PAGO);
        pagamento.setStatusPagamento(statusPagamento);

        when(pagamentoOutputPort.consultar(PAGAMENTO_ID)).thenReturn(Optional.of(pagamento));
        when(statusPagamentoInputPort.consultar(STATUS_PAGAMENTO_ID)).thenReturn(statusPagamento);

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> pagamentoValidatorImpl.validarAlterarPagamento(PAGAMENTO_ID));
        verify(pagamentoOutputPort).consultar(PAGAMENTO_ID);
        verify(statusPagamentoInputPort).consultar(STATUS_PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar alterar pagamento com pagamento já cancelado")
    void validarAlterarPagamentoJaCancelado() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_CANCELADO);
        pagamento.setStatusPagamento(statusPagamento);

        when(pagamentoOutputPort.consultar(PAGAMENTO_ID)).thenReturn(Optional.of(pagamento));
        when(statusPagamentoInputPort.consultar(STATUS_PAGAMENTO_ID)).thenReturn(statusPagamento);

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> pagamentoValidatorImpl.validarAlterarPagamento(PAGAMENTO_ID));
        verify(pagamentoOutputPort).consultar(PAGAMENTO_ID);
        verify(statusPagamentoInputPort).consultar(STATUS_PAGAMENTO_ID);
    }

    private Pagamento getPagamento(Long pagamentoId, Long pedidoId, Long formaPagamentoId, boolean externo) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamento(formaPagamentoId, externo ? MERCADO_PAGO : PIX, externo));
        pagamento.setStatusPagamento(getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO));
        pagamento.setQrCode(null);
        pagamento.setUrlPagamento(null);
        return pagamento;
    }

    private FormaPagamento getFormaPagamento(Long formaPagamentoId, String nome, Boolean externo) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(formaPagamentoId);
        formaPagamento.setNome(nome);
        formaPagamento.setExterno(externo);
        formaPagamento.setAtivo(true);
        return formaPagamento;
    }

    private StatusPagamento getStatusPagamento(Long statusPagamentoId, String nomeStatusPagamento) {
        StatusPagamento statusPagamento = new StatusPagamento();
        statusPagamento.setId(statusPagamentoId);
        statusPagamento.setNome(nomeStatusPagamento);
        return statusPagamento;
    }
}