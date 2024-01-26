package br.com.fiap.fasteats.core.validator.impl;

import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PedidoNotFound;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.Pedido;
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
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;
import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Cancelar Pagamento Validator")
class CancelarPagamentoValidatorImplUnitTest {
    @Mock
    private PedidoOutputPort pedidoOutputPort;
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private StatusPagamentoInputPort statusPagamentoInputPort;
    @InjectMocks
    private CancelarPagamentoValidatorImpl cancelarPagamentoValidatorImpl;
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
    @DisplayName("Deve validar cancelar pagamento")
    void validarCancelarPagamento() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        Pedido pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_AGUARDANDO_PAGAMENTO);

        when(pedidoOutputPort.consultar(PEDIDO_ID)).thenReturn(java.util.Optional.of(pedido));
        when(pagamentoInputPort.consultarPorIdPedido(PEDIDO_ID)).thenReturn(pagamento);
        when(statusPagamentoInputPort.consultar(STATUS_PAGAMENTO_ID)).thenReturn(getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO));

        // Act
        cancelarPagamentoValidatorImpl.validarCancelarPagamento(PEDIDO_ID);

        // Assert
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
        verify(pagamentoInputPort).consultarPorIdPedido(PEDIDO_ID);
        verify(statusPagamentoInputPort).consultar(pagamento.getStatusPagamento().getId());
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar cancelar pagamento de pedido não encontrado")
    void validarCancelarPagamentoPedidoNaoEncontrado() {
        // Act & Assert
        assertThrows(PedidoNotFound.class, () -> cancelarPagamentoValidatorImpl.validarCancelarPagamento(PEDIDO_ID));
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar cancelar pagamento de pedido com status cancelado")
    void validarCancelarPagamentoPedidoCancelado() {
        // Arrange
        Pedido pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_CANCELADO);

        when(pedidoOutputPort.consultar(PEDIDO_ID)).thenReturn(java.util.Optional.of(pedido));

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> cancelarPagamentoValidatorImpl.validarCancelarPagamento(PEDIDO_ID));
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar cancelar pagamento de pedido com status pago")
    void validarCancelarPagamentoPedidoPago() {
        // Arrange
        Pedido pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_PAGO);

        when(pedidoOutputPort.consultar(PEDIDO_ID)).thenReturn(java.util.Optional.of(pedido));

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> cancelarPagamentoValidatorImpl.validarCancelarPagamento(PEDIDO_ID));
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar cancelar pagamento que já foi cancelado")
    void validarCancelarPagamentoJaCancelado() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        Pedido pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_AGUARDANDO_PAGAMENTO);
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_CANCELADO);
        pagamento.setStatusPagamento(statusPagamento);

        when(pedidoOutputPort.consultar(PEDIDO_ID)).thenReturn(java.util.Optional.of(pedido));
        when(pagamentoInputPort.consultarPorIdPedido(PEDIDO_ID)).thenReturn(pagamento);
        when(statusPagamentoInputPort.consultar(STATUS_PAGAMENTO_ID)).thenReturn(statusPagamento);

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> cancelarPagamentoValidatorImpl.validarCancelarPagamento(PEDIDO_ID));
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar cancelar pagamento que já foi pago")
    void validarCancelarPagamentoJaPago() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        Pedido pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_AGUARDANDO_PAGAMENTO);
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_PAGO);
        pagamento.setStatusPagamento(statusPagamento);

        when(pedidoOutputPort.consultar(PEDIDO_ID)).thenReturn(java.util.Optional.of(pedido));
        when(pagamentoInputPort.consultarPorIdPedido(PEDIDO_ID)).thenReturn(pagamento);
        when(statusPagamentoInputPort.consultar(STATUS_PAGAMENTO_ID)).thenReturn(statusPagamento);

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> cancelarPagamentoValidatorImpl.validarCancelarPagamento(PEDIDO_ID));
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
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

    private Pedido getPedido(Long pedidoId, String statusPedido) {
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatusPedido(statusPedido);
        return pedido;
    }

    private StatusPagamento getStatusPagamento(Long statusPagamentoId, String nomeStatusPagamento) {
        StatusPagamento statusPagamento = new StatusPagamento();
        statusPagamento.setId(statusPagamentoId);
        statusPagamento.setNome(nomeStatusPagamento);
        return statusPagamento;
    }
}