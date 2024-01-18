package br.com.fiap.fasteats.core.usecase.impl.unit;

import br.com.fiap.fasteats.core.dataprovider.AlterarPedidoStatusOutputPort;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.*;
import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.EmitirComprovantePagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.RealizarPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.RealizarPagamentoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_EM_PROCESSAMENTO;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_PAGO;
import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_CANCELADO;
import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_PAGO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Realizar Pagamento")
class RealizarPagamentoUseCaseUnitTest {
    @Mock
    private FormaPagamentoInputPort formaPagamentoInputPort;
    @Mock
    private AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    @Mock
    private AlterarPedidoStatusOutputPort alterarPedidoStatusOutputPort;
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private EmitirComprovantePagamentoInputPort emitirComprovantePagamentoInputPort;
    @Mock
    private RealizarPagamentoValidator realizarPagamentoValidator;
    @InjectMocks
    private RealizarPagamentoUseCase realizarPagamentoUseCase;
    private final Long PAGAMENTO_ID = 1L;
    private final Long PEDIDO_ID = 1L;
    private final Long FORMA_PAGAMENTO_ID = 1L;
    private final Long STATUS_PAGAMENTO_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve realizar pagamento")
    void realizarPagamento() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, PIX, false);
        Pagamento pagamentoRealizado = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        Pedido pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_PAGO);
        pagamentoRealizado.setStatusPagamento(getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_PAGO));

        when(pagamentoInputPort.consultarPorIdPedido(pagamento.getPedidoId())).thenReturn(pagamento);
        when(formaPagamentoInputPort.consultar(pagamento.getFormaPagamento().getId())).thenReturn(formaPagamento);
        when(alterarPedidoStatusOutputPort.pago(pagamento.getPedidoId())).thenReturn(Optional.of(pedido));
        when(alterarPagamentoStatusInputPort.pago(PAGAMENTO_ID)).thenReturn(pagamentoRealizado);

        // Act
        Pagamento result = realizarPagamentoUseCase.realizarPagamento(pagamento.getPedidoId());

        // Assert
        assertEquals(STATUS_PAGO, result.getStatusPagamento().getNome());
        verify(realizarPagamentoValidator).validarStatusPedido(pagamento.getPedidoId());
        verify(pagamentoInputPort).consultarPorIdPedido(pagamento.getPedidoId());
        verify(formaPagamentoInputPort).consultar(pagamento.getFormaPagamento().getId());
        verify(alterarPagamentoStatusInputPort).pago(pagamento.getId());
        verify(alterarPedidoStatusOutputPort).pago(pagamento.getPedidoId());
        verify(emitirComprovantePagamentoInputPort).emitir(pagamento.getPedidoId());
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar realizar um pagamento externo")
    void realizarPagamentoExterno() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, MERCADO_PAGO, true);

        when(pagamentoInputPort.consultarPorIdPedido(pagamento.getPedidoId())).thenReturn(pagamento);
        when(formaPagamentoInputPort.consultar(pagamento.getFormaPagamento().getId())).thenReturn(formaPagamento);

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> realizarPagamentoUseCase.realizarPagamento(PEDIDO_ID));
        verify(realizarPagamentoValidator).validarStatusPedido(pagamento.getPedidoId());
        verify(pagamentoInputPort).consultarPorIdPedido(pagamento.getPedidoId());
        verify(formaPagamentoInputPort).consultar(pagamento.getFormaPagamento().getId());
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar realizar um pagamento com pedido com status diferente de PAGO")
    void realizarPagamentoPedidoNaoPago() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, PIX, false);
        Pedido pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_CANCELADO);

        when(pagamentoInputPort.consultarPorIdPedido(pagamento.getPedidoId())).thenReturn(pagamento);
        when(formaPagamentoInputPort.consultar(pagamento.getFormaPagamento().getId())).thenReturn(formaPagamento);
        when(alterarPedidoStatusOutputPort.pago(pagamento.getPedidoId())).thenReturn(Optional.of(pedido));

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> realizarPagamentoUseCase.realizarPagamento(PEDIDO_ID));
        verify(realizarPagamentoValidator).validarStatusPedido(pagamento.getPedidoId());
        verify(pagamentoInputPort).consultarPorIdPedido(pagamento.getPedidoId());
        verify(formaPagamentoInputPort).consultar(pagamento.getFormaPagamento().getId());
        verify(alterarPedidoStatusOutputPort).pago(pagamento.getPedidoId());
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

    private Pedido getPedido(Long pedidoId, String statusPedido) {
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatusPedido(statusPedido);
        return pedido;
    }
}