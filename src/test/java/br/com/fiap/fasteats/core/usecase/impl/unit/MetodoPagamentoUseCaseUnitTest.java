package br.com.fiap.fasteats.core.usecase.impl.unit;

import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PagamentoExternoException;
import br.com.fiap.fasteats.core.domain.exception.PedidoNotFound;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.MetodoPagamentoUseCase;
import br.com.fiap.fasteats.dataprovider.client.MercadoPagoIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_AGUARDANDO_PAGAMENTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Método de Pagamento")
class MetodoPagamentoUseCaseUnitTest {
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private FormaPagamentoInputPort formaPagamentoInputPort;
    @Mock
    private PedidoOutputPort pedidoOutputPort;
    @Mock
    private MercadoPagoIntegration mercadoPagoIntegration;
    @InjectMocks
    private MetodoPagamentoUseCase metodoPagamentoUseCase;
    private final Long PAGAMENTO_ID = 1L;
    private final Long PAGAMENTO_EXTERNO_ID = 1234567890L;
    private final Long PEDIDO_ID = 1L;
    private final Long FORMA_PAGAMENTO_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve gerar um pagamento interno PIX")
    void pix() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        Pedido pedido = getPedido(PEDIDO_ID, 100.0);

        when(pedidoOutputPort.consultar(PEDIDO_ID)).thenReturn(Optional.of(pedido));
        when(pagamentoInputPort.criar(any(Pagamento.class))).thenReturn(pagamento);

        // Act
        Pagamento result = metodoPagamentoUseCase.pix(PEDIDO_ID);

        // Assert
        assertEquals(pagamento, result);
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
        verify(pagamentoInputPort).criar(any(Pagamento.class));
    }

    @Test
    @DisplayName("Erro ao tentar gerar um pagamento interno PIX com pedido não encontrado")
    void pixPedidoNaoEncontrado() {
        // Act & Assert
        assertThrows(PedidoNotFound.class, () -> metodoPagamentoUseCase.pix(PEDIDO_ID));
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve gerar um pagamento externo Mercado Pago")
    void mercadoPago() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, true);
        Pedido pedido = getPedido(PEDIDO_ID, 100.0);
        PagamentoExterno pagamentoExterno = getPagamentoExterno(PAGAMENTO_EXTERNO_ID, "QR_CODE", "URL_PAGAMENTO");
        pagamento.setIdPagamentoExterno(pagamentoExterno.getId());
        pagamento.setQrCode(pagamentoExterno.getQrCode());
        pagamento.setUrlPagamento(pagamentoExterno.getUrlPagamento());

        when(pedidoOutputPort.consultar(PEDIDO_ID)).thenReturn(Optional.of(pedido));
        when(mercadoPagoIntegration.enviarSolicitacaoPagamento(pedido.getValor())).thenReturn(pagamentoExterno);
        when(pagamentoInputPort.criar(any(Pagamento.class))).thenReturn(pagamento);

        // Act
        Pagamento result = metodoPagamentoUseCase.mercadoPago(PEDIDO_ID);

        // Assert
        assertEquals(pagamento, result);
        assertEquals(pagamentoExterno.getId(), result.getIdPagamentoExterno());
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
        verify(mercadoPagoIntegration).enviarSolicitacaoPagamento(pedido.getValor());
        verify(pagamentoInputPort).criar(any(Pagamento.class));
    }

    @Test
    @DisplayName("Erro ao tentar gerar um pagamento externo Mercado Pago com pedido não encontrado")
    void mercadoPagoPedidoNaoEncontrado() {
        // Act & Assert
        assertThrows(PedidoNotFound.class, () -> metodoPagamentoUseCase.mercadoPago(PEDIDO_ID));
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
    }

    @Test
    @DisplayName("Erro ao tentar gerar um pagamento externo Mercado Pago com pagamento externo não gerado")
    void mercadoPagoPagamentoExternoNaoGerado() {
        // Arrange
        Pedido pedido = getPedido(PEDIDO_ID, 100.0);

        when(pedidoOutputPort.consultar(PEDIDO_ID)).thenReturn(Optional.of(pedido));

        // Act & Assert
        assertThrows(PagamentoExternoException.class, () -> metodoPagamentoUseCase.mercadoPago(PEDIDO_ID));
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
        verify(mercadoPagoIntegration).enviarSolicitacaoPagamento(pedido.getValor());
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

    private PagamentoExterno getPagamentoExterno(Long pagamentoExternoId, String qrCode, String urlPagamento) {
        PagamentoExterno pagamentoExterno = new PagamentoExterno();
        pagamentoExterno.setId(pagamentoExternoId);
        pagamentoExterno.setQrCode(qrCode);
        pagamentoExterno.setUrlPagamento(urlPagamento);
        return pagamentoExterno;
    }

    private FormaPagamento getFormaPagamento(Long formaPagamentoId, String nome, Boolean externo) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(formaPagamentoId);
        formaPagamento.setNome(nome);
        formaPagamento.setExterno(externo);
        formaPagamento.setAtivo(true);
        return formaPagamento;
    }

    private Pedido getPedido(Long pedidoId, Double valor) {
        Pedido pedido = new Pedido(pedidoId, STATUS_PEDIDO_AGUARDANDO_PAGAMENTO, valor, null, null, null);
        return pedido;
    }
}
