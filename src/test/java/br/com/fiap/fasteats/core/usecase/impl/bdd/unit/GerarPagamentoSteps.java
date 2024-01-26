package br.com.fiap.fasteats.core.usecase.impl.bdd.unit;

import br.com.fiap.fasteats.core.domain.exception.FormaPagamentoNotFound;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.MetodoPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.GerarPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.GerarPagamentoValidator;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class GerarPagamentoSteps {
    @Mock
    private FormaPagamentoInputPort formaPagamentoInputPort;
    @Mock
    private MetodoPagamentoInputPort metodoPagamentoInputPort;
    @Mock
    private GerarPagamentoValidator gerarPagamentoValidator;
    @InjectMocks
    private GerarPagamentoUseCase gerarPagamentoUseCase;
    AutoCloseable openMocks;
    private Long pedidoId;
    private Long formaPagamentoId;
    private Pagamento resultadoPagamento;
    private Exception exception;

    @Before
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Dado("uma forma de pagamento PIX com ID {long}")
    public void umaFormaDePagamentoPIXComID(Long id) {
        formaPagamentoId = id;
        FormaPagamento formaPagamento = getFormaPagamento(id, PIX, false);
        Pagamento pagamento = getPagamento(1L, pedidoId);
        pagamento.setFormaPagamento(formaPagamento);

        when(formaPagamentoInputPort.consultar(id)).thenReturn(formaPagamento);
        when(metodoPagamentoInputPort.pix(pedidoId)).thenReturn(pagamento);
    }

    @Dado("uma forma de pagamento Mercado Pago com ID {long}")
    public void umaFormaDePagamentoMercadoPagoComID(Long id) {
        formaPagamentoId = id;
        FormaPagamento formaPagamento = getFormaPagamento(formaPagamentoId, MERCADO_PAGO, true);
        Pagamento pagamento = getPagamento(1L, pedidoId);
        pagamento.setFormaPagamento(formaPagamento);

        when(formaPagamentoInputPort.consultar(formaPagamentoId)).thenReturn(formaPagamento);
        when(metodoPagamentoInputPort.mercadoPago(pedidoId)).thenReturn(pagamento);
    }

    @Dado("uma forma de pagamento nao cadastrada com ID {long}")
    public void umaFormaDePagamentoNaoCadastradaComID(Long id) {
        formaPagamentoId = id;
        FormaPagamento formaPagamento = getFormaPagamento(formaPagamentoId, "Nao Cadastrada", false);
        when(formaPagamentoInputPort.consultar(formaPagamentoId)).thenReturn(formaPagamento);
    }

    @Dado("um pedido com ID {long}")
    public void umPedidoComID(Long id) {
        pedidoId = id;
    }

    @Quando("tento gerar o pagamento")
    public void tentoGerarOPagamento() {
        try {
            resultadoPagamento = gerarPagamentoUseCase.gerar(pedidoId, formaPagamentoId);
        } catch (Exception e) {
            exception = e;
        }

    }

    @Entao("devo obter um pagamento interno PIX")
    public void devoObterUmPagamentoInternoPIX() {
        assertNotNull(resultadoPagamento);
        assertEquals(PIX, resultadoPagamento.getFormaPagamento().getNome());
        assertFalse(resultadoPagamento.getFormaPagamento().getExterno());
    }

    @Entao("devo obter um pagamento externo Mercado Pago")
    public void devoObterUmPagamentoExternoMercadoPago() {
        assertNotNull(resultadoPagamento);
        assertEquals(MERCADO_PAGO, resultadoPagamento.getFormaPagamento().getNome());
        assertTrue(resultadoPagamento.getFormaPagamento().getExterno());
    }

    @Entao("devo receber uma mensagem de erro FormaPagamentoNotFound")
    public void devoReceberUmaMensagemDeErroFormaPagamentoNotFound() {
        assertNotNull(exception);
        assertTrue(exception instanceof FormaPagamentoNotFound);
    }

    private Pagamento getPagamento(Long pagamentoId, Long pedidoId) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        return pagamento;
    }

    private FormaPagamento getFormaPagamento(Long formaPagamentoId, String nome, boolean externo) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(formaPagamentoId);
        formaPagamento.setNome(nome);
        formaPagamento.setExterno(externo);
        return formaPagamento;
    }
}
