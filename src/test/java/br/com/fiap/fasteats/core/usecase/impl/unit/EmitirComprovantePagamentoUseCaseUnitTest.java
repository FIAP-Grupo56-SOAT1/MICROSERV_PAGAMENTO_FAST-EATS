package br.com.fiap.fasteats.core.usecase.impl.unit;

import br.com.fiap.fasteats.core.dataprovider.CozinhaPedidoOutputPort;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.EmitirComprovantePagamentoUseCase;
import br.com.fiap.fasteats.core.validator.EmitirComprovantePagamentoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_PAGO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Emitir Comprovante de Pagamento")
class EmitirComprovantePagamentoUseCaseUnitTest {
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private CozinhaPedidoOutputPort cozinhaPedidoOutputPort;
    @Mock
    private EmitirComprovantePagamentoValidator emitirComprovantePagamentoValidator;
    @InjectMocks
    private EmitirComprovantePagamentoUseCase emitirComprovantePagamentoUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve emitir o comprovante de pagamento")
    void emitir() {
        // Arrange
        Pagamento pagamento = getPagamentoInterno(1L, 1L);
        when(pagamentoInputPort.consultarPorIdPedido(pagamento.getPedidoId())).thenReturn(pagamento);

        // Act
        Pagamento result = emitirComprovantePagamentoUseCase.emitir(pagamento.getPedidoId());

        // Assert
        assertEquals(STATUS_PAGO, result.getStatusPagamento().getNome());
        verify(emitirComprovantePagamentoValidator).validarEmitirComprovantePagamento(pagamento.getPedidoId());
        verify(cozinhaPedidoOutputPort).receberPedido(pagamento.getPedidoId());
    }

    private Pagamento getPagamentoInterno(Long pagamentoId, Long pedidoId) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamentoInterno(1L));
        pagamento.setStatusPagamento(getStatusPagamento(1L, STATUS_PAGO));
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