package br.com.fiap.fasteats.core.validator.impl;

import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_CANCELADO;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_EM_PROCESSAMENTO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Alterar Pagamento Status Validator")
class AlterarPagamentoStatusValidatorImplUnitTest {
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private StatusPagamentoInputPort statusPagamentoInputPort;
    @InjectMocks
    private AlterarPagamentoStatusValidatorImpl alterarPagamentoStatusValidatorImpl;

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
    @DisplayName("Deve validar o pagamento para alterar o status para recusado")
    void validarRecusado() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO);

        when(pagamentoInputPort.consultar(PAGAMENTO_ID)).thenReturn(pagamento);
        when(statusPagamentoInputPort.consultar(STATUS_PAGAMENTO_ID)).thenReturn(statusPagamento);

        // Act
        alterarPagamentoStatusValidatorImpl.validarRecusado(PAGAMENTO_ID);

        // Assert
        verify(pagamentoInputPort).consultar(PAGAMENTO_ID);
        verify(statusPagamentoInputPort).consultar(STATUS_PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao validar o pagamento com status cancelado para alterar o status para recusado")
    void validarRecusadoPagamentoStatusCancelado() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_CANCELADO);

        when(pagamentoInputPort.consultar(PAGAMENTO_ID)).thenReturn(pagamento);
        when(statusPagamentoInputPort.consultar(STATUS_PAGAMENTO_ID)).thenReturn(statusPagamento);

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> alterarPagamentoStatusValidatorImpl.validarRecusado(PAGAMENTO_ID));
        verify(pagamentoInputPort).consultar(PAGAMENTO_ID);
        verify(statusPagamentoInputPort).consultar(STATUS_PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve validar o pagamento para alterar o status para cancelado")
    void validarCancelado() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO);

        when(pagamentoInputPort.consultar(PAGAMENTO_ID)).thenReturn(pagamento);
        when(statusPagamentoInputPort.consultar(STATUS_PAGAMENTO_ID)).thenReturn(statusPagamento);

        // Act
        alterarPagamentoStatusValidatorImpl.validarCancelado(PAGAMENTO_ID);

        // Assert
        verify(pagamentoInputPort).consultar(PAGAMENTO_ID);
        verify(statusPagamentoInputPort).consultar(STATUS_PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao validar o pagamento com status cancelado para alterar o status para cancelado")
    void validarCanceladoPagamentoCancelado() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_CANCELADO);

        when(pagamentoInputPort.consultar(PAGAMENTO_ID)).thenReturn(pagamento);
        when(statusPagamentoInputPort.consultar(STATUS_PAGAMENTO_ID)).thenReturn(statusPagamento);

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> alterarPagamentoStatusValidatorImpl.validarCancelado(PAGAMENTO_ID));
        verify(pagamentoInputPort).consultar(PAGAMENTO_ID);
        verify(statusPagamentoInputPort).consultar(STATUS_PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve validar o pagamento para alterar o status para pago")
    void validarPago() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO);

        when(pagamentoInputPort.consultar(PAGAMENTO_ID)).thenReturn(pagamento);
        when(statusPagamentoInputPort.consultar(STATUS_PAGAMENTO_ID)).thenReturn(statusPagamento);

        // Act
        alterarPagamentoStatusValidatorImpl.validarPago(PAGAMENTO_ID);

        // Assert
        verify(pagamentoInputPort).consultar(PAGAMENTO_ID);
        verify(statusPagamentoInputPort).consultar(STATUS_PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao validar o pagamento com status cancelado para alterar o status para pago")
    void validarPagoPagamentoCancelado() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_CANCELADO);

        when(pagamentoInputPort.consultar(PAGAMENTO_ID)).thenReturn(pagamento);
        when(statusPagamentoInputPort.consultar(STATUS_PAGAMENTO_ID)).thenReturn(statusPagamento);

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> alterarPagamentoStatusValidatorImpl.validarPago(PAGAMENTO_ID));
        verify(pagamentoInputPort).consultar(PAGAMENTO_ID);
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