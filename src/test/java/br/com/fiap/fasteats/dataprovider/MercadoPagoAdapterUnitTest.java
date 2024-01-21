package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.dataprovider.client.MercadoPagoIntegration;
import br.com.fiap.fasteats.dataprovider.constants.StatusMercadoPagoConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - Mercado Pago Adapter")
class MercadoPagoAdapterUnitTest {
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private StatusPagamentoInputPort statusPagamentoInputPort;
    @Mock
    private MercadoPagoIntegration mercadoPagoIntegration;
    @InjectMocks
    private MercadoPagoAdapter mercadoPagoAdapter;
    AutoCloseable openMocks;
    private final Long PAGAMENTO_ID = 1L;
    private final Long PAGAMENTO_EXTERNO_ID = 1234567890L;
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
    @DisplayName("Deve consultar pagamento externo")
    void consultar() {
        // Arrange
        PagamentoExterno pagamentoExterno = getPagamentoExterno(PAGAMENTO_EXTERNO_ID, "qrCode", "urlPagamento");

        when(mercadoPagoIntegration.consultarPagamento(pagamentoExterno)).thenReturn(pagamentoExterno);

        // Act
        PagamentoExterno pagamentoExternoRetorno = mercadoPagoAdapter.consultar(pagamentoExterno);

        // Assert
        assertEquals(pagamentoExterno, pagamentoExternoRetorno);
        verify(mercadoPagoIntegration).consultarPagamento(pagamentoExterno);
    }

    @Test
    @DisplayName("Deve cancelar pagamento externo")
    void cancelarPagamento() {
        // Arrange
        PagamentoExterno pagamentoExterno = getPagamentoExterno(PAGAMENTO_EXTERNO_ID, "qrCode", "urlPagamento");
        pagamentoExterno.setStatus(STATUS_CANCELADO);

        when(mercadoPagoIntegration.cancelarPagamento(PAGAMENTO_EXTERNO_ID)).thenReturn(pagamentoExterno);

        // Act
        PagamentoExterno pagamentoExternoRetorno = mercadoPagoAdapter.cancelarPagamento(PAGAMENTO_EXTERNO_ID);

        // Assert
        assertEquals(pagamentoExterno, pagamentoExternoRetorno);
        verify(mercadoPagoIntegration).cancelarPagamento(PAGAMENTO_EXTERNO_ID);
    }

    @Test
    @DisplayName("Deve recuperar pagamento de pagamento externo")
    void recuperarPagamentoDePagamentoExterno() {
        // Arrange
        PagamentoExterno pagamentoExterno = getPagamentoExterno(PAGAMENTO_EXTERNO_ID, "qrCodePagamento", "urlDoPagamento");
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, PAGAMENTO_EXTERNO_ID, true);
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO);
        pagamentoExterno.setStatus(StatusMercadoPagoConstants.STATUS_EM_PROCESSO);
        pagamento.setQrCode(pagamentoExterno.getQrCode());
        pagamento.setUrlPagamento(pagamentoExterno.getUrlPagamento());

        when(mercadoPagoIntegration.consultarPagamento(pagamentoExterno)).thenReturn(pagamentoExterno);
        when(pagamentoInputPort.consultarPorIdPagamentoExterno(PAGAMENTO_EXTERNO_ID)).thenReturn(pagamento);
        when(statusPagamentoInputPort.consultarPorNome(STATUS_EM_PROCESSAMENTO)).thenReturn(statusPagamento);

        // Act
        Pagamento pagamentoRetorno = mercadoPagoAdapter.recuperarPagamentoDePagamentoExterno(pagamentoExterno);

        // Assert
        assertEquals(pagamento, pagamentoRetorno);
        assertEquals(pagamento.getStatusPagamento().getId(), pagamentoRetorno.getStatusPagamento().getId());
        verify(mercadoPagoIntegration).consultarPagamento(pagamentoExterno);
        verify(pagamentoInputPort).consultarPorIdPagamentoExterno(PAGAMENTO_EXTERNO_ID);
        verify(statusPagamentoInputPort).consultarPorNome(STATUS_EM_PROCESSAMENTO);
    }

    @Test
    @DisplayName("Deve converter para status pagamento pago")
    void conveterStatusPagamentoParaPago() {
        // Arrange
        StatusPagamento statusPagamentoPago = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_PAGO);

        when(statusPagamentoInputPort.consultarPorNome(STATUS_PAGO)).thenReturn(statusPagamentoPago);

        // Act
        StatusPagamento statusPagamentoAprovado = mercadoPagoAdapter.conveterStatusPagamento(StatusMercadoPagoConstants.STATUS_APROVADO);
        StatusPagamento statusPagamentoAutorizado = mercadoPagoAdapter.conveterStatusPagamento(StatusMercadoPagoConstants.STATUS_AUTORIZADO);

        // Assert
        assertEquals(statusPagamentoPago, statusPagamentoAprovado);
        assertEquals(statusPagamentoPago, statusPagamentoAutorizado);
        verify(statusPagamentoInputPort, times(2)).consultarPorNome(STATUS_PAGO);
    }

    @Test
    @DisplayName("Deve converter para status pagamento em processamento")
    void conveterStatusPagamentoParaEmProcessamento() {
        // Arrange
        StatusPagamento statusPagamentoEmProcessamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO);

        when(statusPagamentoInputPort.consultarPorNome(STATUS_EM_PROCESSAMENTO)).thenReturn(statusPagamentoEmProcessamento);

        // Act
        StatusPagamento statusPagamentoEmProcesso = mercadoPagoAdapter.conveterStatusPagamento(StatusMercadoPagoConstants.STATUS_EM_PROCESSO);
        StatusPagamento statusPagamentoEmMediacao = mercadoPagoAdapter.conveterStatusPagamento(StatusMercadoPagoConstants.STATUS_EM_MEDIACAO);
        StatusPagamento statusPagamentoPendente = mercadoPagoAdapter.conveterStatusPagamento(StatusMercadoPagoConstants.STATUS_PENDENTE);

        // Assert
        assertEquals(statusPagamentoEmProcessamento, statusPagamentoEmProcesso);
        assertEquals(statusPagamentoEmProcessamento, statusPagamentoEmMediacao);
        assertEquals(statusPagamentoEmProcessamento, statusPagamentoPendente);
        verify(statusPagamentoInputPort, times(3)).consultarPorNome(STATUS_EM_PROCESSAMENTO);
    }

    @Test
    @DisplayName("Deve converter para status pagamento cancelado")
    void conveterStatusPagamentoParaCancelado() {
        // Arrange
        StatusPagamento statusPagamentoCancelado = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_CANCELADO);

        when(statusPagamentoInputPort.consultarPorNome(STATUS_CANCELADO)).thenReturn(statusPagamentoCancelado);

        // Act
        StatusPagamento statusPagamentoCanceladoResult = mercadoPagoAdapter.conveterStatusPagamento(StatusMercadoPagoConstants.STATUS_CANCELADO);
        StatusPagamento statusPagamentoEstornado = mercadoPagoAdapter.conveterStatusPagamento(StatusMercadoPagoConstants.STATUS_ESTORNADO);
        StatusPagamento statusPagamentoDevolvido = mercadoPagoAdapter.conveterStatusPagamento(StatusMercadoPagoConstants.STATUS_DEVOLVIDO);

        // Assert
        assertEquals(statusPagamentoCancelado, statusPagamentoCanceladoResult);
        assertEquals(statusPagamentoCancelado, statusPagamentoEstornado);
        assertEquals(statusPagamentoCancelado, statusPagamentoDevolvido);
        verify(statusPagamentoInputPort, times(3)).consultarPorNome(STATUS_CANCELADO);
    }

    @Test
    @DisplayName("Deve converter para status pagamento recusado")
    void conveterStatusPagamentoParaRecusado() {
        // Arrange
        StatusPagamento statusPagamentoRecusado = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_RECUSADO);

        when(statusPagamentoInputPort.consultarPorNome(STATUS_RECUSADO)).thenReturn(statusPagamentoRecusado);

        // Act
        StatusPagamento statusPagamentoRejeitado = mercadoPagoAdapter.conveterStatusPagamento(StatusMercadoPagoConstants.STATUS_REJEITADO);

        // Assert
        assertEquals(statusPagamentoRecusado, statusPagamentoRejeitado);
        verify(statusPagamentoInputPort).consultarPorNome(STATUS_RECUSADO);
    }

    @Test
    @DisplayName("Deve retornar um Status Pagamento vazio para um status não mapeado")
    void conveterStatusPagamentoParaStatusNaoMapeado() {
        // Act
        StatusPagamento statusPagamento = mercadoPagoAdapter.conveterStatusPagamento("TESTE");

        // Assert
        assertNull(statusPagamento.getId());
        assertNull(statusPagamento.getNome());
    }


    private Pagamento getPagamento(Long pagamentoId, Long pedidoId, Long formaPagamentoId, Long pagamentoExternoId, boolean externo) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamento(formaPagamentoId, externo ? MERCADO_PAGO : PIX, externo));
        pagamento.setStatusPagamento(getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO));
        pagamento.setIdPagamentoExterno(pagamentoExternoId);
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

    private StatusPagamento getStatusPagamento(Long statusPagamentoId, String nomeStatusPagamento) {
        StatusPagamento statusPagamento = new StatusPagamento();
        statusPagamento.setId(statusPagamentoId);
        statusPagamento.setNome(nomeStatusPagamento);
        return statusPagamento;
    }
}