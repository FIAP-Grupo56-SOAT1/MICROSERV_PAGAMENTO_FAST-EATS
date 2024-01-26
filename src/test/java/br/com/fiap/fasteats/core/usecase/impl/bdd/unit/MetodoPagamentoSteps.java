package br.com.fiap.fasteats.core.usecase.impl.bdd.unit;

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
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MetodoPagamentoSteps {
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
    AutoCloseable openMocks;
    private final Long PAGAMENTO_ID = 1L;
    private final Long PAGAMENTO_EXTERNO_ID = 1234567890L;
    private final Long PEDIDO_ID = 1L;
    private final Long FORMA_PAGAMENTO_ID = 1L;
    private Pagamento pagamento;
    private Pedido pedido;
    private PagamentoExterno pagamentoExterno;
    private Exception exception;

    @Before
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Dado("que existe um pedido com ID {long} no sistema")
    public void existeUmPedidoComIdNoSistema(Long pedidoId) {
        pedido = getPedido(pedidoId, 100.0);
        when(pedidoOutputPort.consultar(pedidoId)).thenReturn(Optional.of(pedido));
    }

    @Dado("que não existe um pedido com ID {long} no sistema")
    public void naoExisteUmPedidoComIdNoSistema(Long pedidoId) {
        pedido = getPedido(pedidoId, 100.0);
        when(pedidoOutputPort.consultar(pedidoId)).thenReturn(Optional.empty());
    }

    @Quando("o método de pagamento PIX é acionado para o pedido {long}")
    public void metodoPagamentoPixAcionado(Long pedidoId) {
        try {
            pagamento = getPagamento(PAGAMENTO_ID, pedidoId, FORMA_PAGAMENTO_ID, false);
            when(pagamentoInputPort.criar(any(Pagamento.class))).thenReturn(pagamento);
            pagamento = metodoPagamentoUseCase.pix(pedidoId);
        }catch (Exception e){
            exception = e;
        }

    }

    @Entao("deve ser gerado um pagamento interno PIX com sucesso")
    public void deveSerGeradoUmPagamentoInternoPIXComSucesso() {
        assertEquals(pedido.getId(), pagamento.getPedidoId());
        verify(pedidoOutputPort).consultar(pedido.getId());
        verify(pagamentoInputPort).criar(any(Pagamento.class));
    }

    @Entao("deve ser lançada a exceção PedidoNotFound")
    public void deveSerLancadaExcecaoPedidoNotFound() {
        assertTrue(exception instanceof PedidoNotFound);
        verify(pedidoOutputPort).consultar(pedido.getId());
    }

    @Dado("que o Mercado Pago aceita a solicitação de pagamento")
    public void mercadoPagoAceitaSolicitacaoPagamento() {
        pagamentoExterno = getPagamentoExterno(PAGAMENTO_EXTERNO_ID, "QR_CODE", "URL_PAGAMENTO");
        when(mercadoPagoIntegration.enviarSolicitacaoPagamento(pedido.getValor())).thenReturn(pagamentoExterno);
    }

    @Quando("o método de pagamento Mercado Pago é acionado para o pedido {long}")
    public void metodoPagamentoMercadoPagoAcionado(Long pedidoId) {
        try {
            pagamento = getPagamento(PAGAMENTO_ID, pedidoId, FORMA_PAGAMENTO_ID, true);
            pagamentoExterno = getPagamentoExterno(PAGAMENTO_EXTERNO_ID, "QR_CODE", "URL_PAGAMENTO");
            pagamento.setIdPagamentoExterno(pagamentoExterno.getId());

            when(pagamentoInputPort.criar(any(Pagamento.class))).thenReturn(pagamento);
            when(mercadoPagoIntegration.enviarSolicitacaoPagamento(pedido.getValor())).thenReturn(pagamentoExterno);

            pagamento = metodoPagamentoUseCase.mercadoPago(pedidoId);
        }catch (Exception e){
            exception = e;
        }
    }

    @Quando("o método de pagamento Mercado Pago deve ser acionado para o pedido {long}")
    public void metodoPagamentoMercadoPagoDeveSerAcionado(Long pedidoId) {
        try {
            pagamento = getPagamento(PAGAMENTO_ID, pedidoId, FORMA_PAGAMENTO_ID, true);
            pagamentoExterno = getPagamentoExterno(PAGAMENTO_EXTERNO_ID, "QR_CODE", "URL_PAGAMENTO");
            pagamento.setIdPagamentoExterno(pagamentoExterno.getId());

            when(pagamentoInputPort.criar(any(Pagamento.class))).thenReturn(pagamento);

            pagamento = metodoPagamentoUseCase.mercadoPago(pedidoId);
        }catch (Exception e){
            exception = e;
        }
    }

    @Entao("deve ser gerado um pagamento externo Mercado Pago com sucesso")
    public void deveSerGeradoUmPagamentoExternoMercadoPagoComSucesso() {
        assertEquals(pedido.getId(), pagamento.getPedidoId());
        assertEquals(pagamentoExterno.getId(), pagamento.getIdPagamentoExterno());
        verify(pedidoOutputPort).consultar(pedido.getId());
        verify(mercadoPagoIntegration).enviarSolicitacaoPagamento(pedido.getValor());
        verify(pagamentoInputPort).criar(any(Pagamento.class));
    }

    @Entao("deve ocorrer um erro ao tentar gerar um pagamento externo Mercado Pago")
    public void deveOcorrerUmErroAoTentarGerarUmPagamentoExternoMercadoPago() {
        assertTrue(exception instanceof PagamentoExternoException);
        verify(pedidoOutputPort).consultar(pedido.getId());
        verify(mercadoPagoIntegration).enviarSolicitacaoPagamento(pedido.getValor());
        verify(pagamentoInputPort, times(0)).criar(any(Pagamento.class));
    }

    @Entao("deve ser lançada a exceção PagamentoExternoException")
    public void deveSerLancadaExcecaoPagamentoExternoException() {
       assertTrue(exception instanceof PagamentoExternoException);
        verify(pedidoOutputPort).consultar(pedido.getId());
        verify(mercadoPagoIntegration).enviarSolicitacaoPagamento(pedido.getValor());
    }

    private Pagamento getPagamento(Long pagamentoId, Long pedidoId, Long formaPagamentoId, boolean externo) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamento(formaPagamentoId, externo ? "MERCADO_PAGO" : "PIX", externo));
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
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setValor(valor);
        return pedido;
    }
}
