package br.com.fiap.fasteats.core.usecase.impl.unit;

import br.com.fiap.fasteats.core.dataprovider.PagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.FormaPagamentoNotFound;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.MetodoPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.GerarPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.GerarPagamentoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - Gerar Pagamento")
class GerarPagamentoUseCaseUnitTest {
    @Mock
    private FormaPagamentoInputPort formaPagamentoInputPort;
    @Mock
    private GerarPagamentoValidator gerarPagamentoValidator;
    @Mock
    private MetodoPagamentoInputPort metodoPagamentoInputPort;
    @Mock
    private PagamentoOutputPort pagamentoOutputPort;
    @InjectMocks
    private GerarPagamentoUseCase gerarPagamentoUseCase;

    private final Long PAGAMENTO_ID = 1L;
    private  final Long PEDIDO_ID = 1L;
    private final Long FORMA_PAGAMENTO_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve gerar um pagamento interno PIX sem pagamento anterior")
    void gerarInterno() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, PIX, false);
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);

        doNothing().when(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        doNothing().when(gerarPagamentoValidator).validarFormaPagamento(FORMA_PAGAMENTO_ID);
        when(pagamentoOutputPort.consultarPorPedidoId(PEDIDO_ID)).thenReturn(Optional.empty());
        when(formaPagamentoInputPort.consultar(FORMA_PAGAMENTO_ID)).thenReturn(formaPagamento);
        when(metodoPagamentoInputPort.pix(PEDIDO_ID)).thenReturn(pagamento);

        // Act
        Pagamento result = gerarPagamentoUseCase.gerar(PEDIDO_ID, FORMA_PAGAMENTO_ID);

        // Assert
        assertNotNull(result);
        assertEquals(PIX, result.getFormaPagamento().getNome());
        verify(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        verify(gerarPagamentoValidator).validarFormaPagamento(formaPagamento.getId());
        verify(pagamentoOutputPort).consultarPorPedidoId(PEDIDO_ID);
        verify(formaPagamentoInputPort).consultar(FORMA_PAGAMENTO_ID);
        verify(metodoPagamentoInputPort).pix(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve gerar um pagamento externo Mercado Pago sem pagamento anterior")
    void gerarExterno() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, MERCADO_PAGO, true);
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, true);

        doNothing().when(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        doNothing().when(gerarPagamentoValidator).validarFormaPagamento(FORMA_PAGAMENTO_ID);
        when(pagamentoOutputPort.consultarPorPedidoId(PEDIDO_ID)).thenReturn(Optional.empty());
        when(formaPagamentoInputPort.consultar(FORMA_PAGAMENTO_ID)).thenReturn(formaPagamento);
        when(metodoPagamentoInputPort.mercadoPago(PEDIDO_ID)).thenReturn(pagamento);

        // Act
        Pagamento result = gerarPagamentoUseCase.gerar(PEDIDO_ID, FORMA_PAGAMENTO_ID);

        // Assert
        assertNotNull(result);
        assertEquals(MERCADO_PAGO, result.getFormaPagamento().getNome());
        verify(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        verify(gerarPagamentoValidator).validarFormaPagamento(formaPagamento.getId());
        verify(pagamentoOutputPort).consultarPorPedidoId(PEDIDO_ID);
        verify(formaPagamentoInputPort).consultar(FORMA_PAGAMENTO_ID);
        verify(metodoPagamentoInputPort).mercadoPago(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar gerar um pagamento com Forma de Pagamento não cadastrada")
    void gerarFormaPagamentoNaoCadastrada() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, "TESTE", false);

        when(formaPagamentoInputPort.consultar(FORMA_PAGAMENTO_ID)).thenReturn(formaPagamento);

        // Act & Assert
        assertThrows(FormaPagamentoNotFound.class, () -> gerarPagamentoUseCase.gerar(PEDIDO_ID, FORMA_PAGAMENTO_ID));
        verify(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        verify(gerarPagamentoValidator).validarFormaPagamento(formaPagamento.getId());
        verify(formaPagamentoInputPort).consultar(FORMA_PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar gerar um pagamento com pedido já pago")
    void gerarParaPedidoJaPago() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        pagamento.setStatusPagamento(getStatusPagamento(STATUS_PAGO));

        doNothing().when(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        doNothing().when(gerarPagamentoValidator).validarFormaPagamento(FORMA_PAGAMENTO_ID);
        when(pagamentoOutputPort.consultarPorPedidoId(PEDIDO_ID)).thenReturn(Optional.of(pagamento));

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> gerarPagamentoUseCase.gerar(PEDIDO_ID, FORMA_PAGAMENTO_ID));
        verify(pagamentoOutputPort).consultarPorPedidoId(PEDIDO_ID);
        verify(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        verify(pagamentoOutputPort).consultarPorPedidoId(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar gerar um pagamento para pedido já cancelado")
    void gerarParaPedidoJaCancelado() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        pagamento.setStatusPagamento(getStatusPagamento(STATUS_CANCELADO));

        doNothing().when(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        doNothing().when(gerarPagamentoValidator).validarFormaPagamento(FORMA_PAGAMENTO_ID);
        when(pagamentoOutputPort.consultarPorPedidoId(PEDIDO_ID)).thenReturn(Optional.of(pagamento));

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> gerarPagamentoUseCase.gerar(PEDIDO_ID, FORMA_PAGAMENTO_ID));
        verify(pagamentoOutputPort).consultarPorPedidoId(PEDIDO_ID);
        verify(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        verify(pagamentoOutputPort).consultarPorPedidoId(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve gerar um pagamento interno PIX para um pedido com tentativa de pagamento anterior")
    void gerarInternoComPagamentoAnterior() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, PIX, false);
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        Pagamento pagamentoAnterior = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        pagamentoAnterior.setStatusPagamento(getStatusPagamento(STATUS_RECUSADO));

        doNothing().when(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        doNothing().when(gerarPagamentoValidator).validarFormaPagamento(FORMA_PAGAMENTO_ID);
        when(pagamentoOutputPort.consultarPorPedidoId(PEDIDO_ID)).thenReturn(Optional.of(pagamentoAnterior));
        when(formaPagamentoInputPort.consultar(FORMA_PAGAMENTO_ID)).thenReturn(formaPagamento);
        when(metodoPagamentoInputPort.pix(PEDIDO_ID)).thenReturn(pagamento);

        // Act
        Pagamento result = gerarPagamentoUseCase.gerar(PEDIDO_ID, FORMA_PAGAMENTO_ID);

        // Assert
        assertNotNull(result);
        assertEquals(PIX, result.getFormaPagamento().getNome());
        verify(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        verify(gerarPagamentoValidator).validarFormaPagamento(formaPagamento.getId());
        verify(pagamentoOutputPort).consultarPorPedidoId(PEDIDO_ID);
        verify(pagamentoOutputPort).remover(pagamentoAnterior.getId());
        verify(formaPagamentoInputPort).consultar(FORMA_PAGAMENTO_ID);
        verify(metodoPagamentoInputPort).pix(PEDIDO_ID);
    }

    private Pagamento getPagamento(Long pagamentoId, Long pedidoId, Long formaPagamentoId, boolean externo) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamento(formaPagamentoId, externo ? MERCADO_PAGO : PIX, externo));
        pagamento.setValor(100.0);
        pagamento.setIdPagamentoExterno(null);
        pagamento.setQrCode(null);
        pagamento.setUrlPagamento(null);
        return pagamento;
    }

    private StatusPagamento getStatusPagamento(String nome) {
        StatusPagamento statusPagamento = new StatusPagamento();
        statusPagamento.setNome(nome);
        return statusPagamento;
    }

    private FormaPagamento getFormaPagamento(Long formaPagamentoId, String nome, Boolean externo) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(formaPagamentoId);
        formaPagamento.setNome(nome);
        formaPagamento.setExterno(externo);
        formaPagamento.setAtivo(true);
        return formaPagamento;
    }
}