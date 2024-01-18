package br.com.fiap.fasteats.core.usecase.impl.unit;

import br.com.fiap.fasteats.core.dataprovider.PagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.AlterarPagamentoStatusUseCase;
import br.com.fiap.fasteats.core.validator.AlterarPagamentoStatusValidator;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("Teste Unitário - Alterar Status do Pagamento")
class AlterarPagamentoStatusUseCaseUnitTest {
    @Mock
    private StatusPagamentoInputPort statusPagamentoInputPort;
    @Mock
    private PagamentoOutputPort pagamentoOutputPort;
    @Mock
    private AlterarPagamentoStatusValidator alterarPagamentoStatusValidator;
    @InjectMocks
    private AlterarPagamentoStatusUseCase alterarPagamentoStatusUseCase;
    private final Long PAGAMENTO_ID = 1L;
    private final Long PEDIDO_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve alterar o status do pagamento para recusado")
    void recusado() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID);
        StatusPagamento statusPagamentoRecusado = getStatusPagamento(2L, STATUS_RECUSADO);
        Pagamento pagamentoRecusado = getPagamento(PAGAMENTO_ID, PEDIDO_ID);
        pagamentoRecusado.setStatusPagamento(statusPagamentoRecusado);

        when(pagamentoOutputPort.consultar(pagamento.getId())).thenReturn(Optional.of(pagamento));
        when(statusPagamentoInputPort.consultarPorNome(STATUS_RECUSADO)).thenReturn(statusPagamentoRecusado);
        when(pagamentoOutputPort.salvarPagamento(pagamento)).thenReturn(pagamentoRecusado);

        // Act
        Pagamento resultado = alterarPagamentoStatusUseCase.recusado(PAGAMENTO_ID);

        // Assert
        assertNotNull(resultado);
        assertEquals(STATUS_RECUSADO, pagamento.getStatusPagamento().getNome());
        assertEquals(STATUS_RECUSADO, resultado.getStatusPagamento().getNome());
        verify(alterarPagamentoStatusValidator).validarRecusado(pagamento.getId());
        verify(pagamentoOutputPort).salvarPagamento(pagamento);
    }

    @Test
    @DisplayName("Deve alterar o status do pagamento para cancelado")
    void cancelado() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID);
        StatusPagamento statusPagamentoCancelado = getStatusPagamento(2L, STATUS_CANCELADO);
        Pagamento pagamentoCancelado = getPagamento(PAGAMENTO_ID, PEDIDO_ID);
        pagamentoCancelado.setStatusPagamento(statusPagamentoCancelado);
        pagamentoCancelado.setDataHoraFinalizado(LocalDateTime.now());

        when(pagamentoOutputPort.consultar(pagamento.getId())).thenReturn(Optional.of(pagamento));
        when(statusPagamentoInputPort.consultarPorNome(STATUS_CANCELADO)).thenReturn(statusPagamentoCancelado);
        when(pagamentoOutputPort.salvarPagamento(pagamento)).thenReturn(pagamentoCancelado);

        // Act
        Pagamento resultado = alterarPagamentoStatusUseCase.cancelado(PAGAMENTO_ID);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getDataHoraFinalizado());
        assertNotNull(pagamento.getDataHoraFinalizado());
        assertEquals(STATUS_CANCELADO, pagamento.getStatusPagamento().getNome());
        assertEquals(STATUS_CANCELADO, resultado.getStatusPagamento().getNome());
        verify(alterarPagamentoStatusValidator).validarCancelado(pagamento.getId());
        verify(pagamentoOutputPort).salvarPagamento(pagamento);
    }

    @Test
    @DisplayName("Deve alterar o status do pagamento para pago")
    void pago() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID);
        StatusPagamento statusPagamentoPago = getStatusPagamento(2L, STATUS_PAGO);
        Pagamento pagamentoPago = getPagamento(PAGAMENTO_ID, PEDIDO_ID);
        pagamentoPago.setStatusPagamento(statusPagamentoPago);
        pagamentoPago.setDataHoraFinalizado(LocalDateTime.now());

        when(pagamentoOutputPort.consultar(pagamento.getId())).thenReturn(Optional.of(pagamento));
        when(statusPagamentoInputPort.consultarPorNome(STATUS_PAGO)).thenReturn(statusPagamentoPago);
        when(pagamentoOutputPort.salvarPagamento(pagamento)).thenReturn(pagamentoPago);

        // Act
        Pagamento resultado = alterarPagamentoStatusUseCase.pago(PAGAMENTO_ID);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getDataHoraFinalizado());
        assertNotNull(pagamento.getDataHoraFinalizado());
        assertEquals(STATUS_PAGO, pagamento.getStatusPagamento().getNome());
        assertEquals(STATUS_PAGO, resultado.getStatusPagamento().getNome());
        verify(alterarPagamentoStatusValidator).validarPago(pagamento.getId());
        verify(pagamentoOutputPort).salvarPagamento(pagamento);
    }

    private Pagamento getPagamento(Long pagamentoId, Long pedidoId) {
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

    private StatusPagamento getStatusPagamento(Long statusPagamentoId, String nomeStatusPagamento) {
        StatusPagamento statusPagamento = new StatusPagamento();
        statusPagamento.setId(statusPagamentoId);
        statusPagamento.setNome(nomeStatusPagamento);
        return statusPagamento;
    }
}