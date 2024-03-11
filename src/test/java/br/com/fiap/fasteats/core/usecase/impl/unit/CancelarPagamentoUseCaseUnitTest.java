package br.com.fiap.fasteats.core.usecase.impl.unit;

import br.com.fiap.fasteats.core.dataprovider.CancelarPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.CancelarPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.CancelarPagamentoValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_CANCELADO;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_EM_PROCESSAMENTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - Cancelar Pagamento")
class CancelarPagamentoUseCaseUnitTest {
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private CancelarPagamentoOutputPort cancelarPagamentoOutputPort;
    @Mock
    private CancelarPagamentoValidator cancelarPagamentoValidator;
    @InjectMocks
    private CancelarPagamentoUseCase cancelarPagamentoUseCase;
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
    @DisplayName("Deve cancelar um pagamento interno")
    void cancelar() {
        // Arrange
        Pagamento pagamento = getPagamentoInterno(1L, 1L);
        Pagamento pagamentoCancelado = getPagamentoInterno(1L, 1L);
        pagamentoCancelado.setStatusPagamento(getStatusPagamento(2L, STATUS_CANCELADO));

        when(pagamentoInputPort.consultarPorIdPedido(pagamento.getPedidoId())).thenReturn(pagamento);
        doNothing().when(cancelarPagamentoValidator).validarCancelarPagamento(pagamento.getPedidoId());
        when(cancelarPagamentoOutputPort.cancelar(pagamento.getId(), pagamento.getPedidoId())).thenReturn(pagamentoCancelado);

        // Act
        Pagamento resultado = cancelarPagamentoUseCase.cancelar(pagamento.getPedidoId());

        // Assert
        assertEquals(STATUS_CANCELADO, resultado.getStatusPagamento().getNome());
        verify(cancelarPagamentoValidator).validarCancelarPagamento(pagamento.getPedidoId());
        verify(cancelarPagamentoOutputPort).cancelar(pagamento.getId(), pagamento.getPedidoId());
    }

    @Test
    @DisplayName("Deve gerar erro ao tentar cancelar um pagamento externo")
    void testErroCancelarPagamentoExterno() {
        // Arrange
        Pagamento pagamentoExterno = getPagamentoExterno(1L, 1L);

        when(pagamentoInputPort.consultarPorIdPedido(pagamentoExterno.getPedidoId())).thenReturn(pagamentoExterno);

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> cancelarPagamentoUseCase.cancelar(pagamentoExterno.getPedidoId()));
    }


    private Pagamento getPagamentoInterno(Long pagamentoId, Long pedidoId) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamentoInterno(1L));
        pagamento.setStatusPagamento(getStatusPagamento(1L, STATUS_EM_PROCESSAMENTO));
        pagamento.setValor(100.0);
        pagamento.setIdPagamentoExterno(null);
        pagamento.setQrCode(null);
        pagamento.setUrlPagamento(null);
        return pagamento;
    }

    private FormaPagamento getFormaPagamentoInterno(Long formaPagamentoId) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(formaPagamentoId);
        formaPagamento.setNome(PIX);
        formaPagamento.setExterno(false);
        formaPagamento.setAtivo(true);
        return formaPagamento;
    }

    private Pagamento getPagamentoExterno(Long pagamentoId, Long pedidoId) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamentoExterno(2L));
        pagamento.setValor(100.0);
        pagamento.setIdPagamentoExterno(null);
        pagamento.setQrCode(null);
        pagamento.setUrlPagamento(null);
        return pagamento;
    }

    private FormaPagamento getFormaPagamentoExterno(Long formaPagamentoId) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(formaPagamentoId);
        formaPagamento.setNome(MERCADO_PAGO);
        formaPagamento.setExterno(true);
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