package br.com.fiap.fasteats.core.usecase.impl.bdd.unit;

import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.GerarPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.AlterarFormaPagamentoUseCase;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

public class AlterarFormaPagamentoSteps {
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private GerarPagamentoInputPort gerarPagamentoInputPort;
    @InjectMocks
    private AlterarFormaPagamentoUseCase alterarFormaPagamentoUseCase;
    private final Long PAGAMENTO_ID = 1L;
    private final Long PEDIDO_ID = 1L;
    private Pagamento pagamentoAnterior;
    private Pagamento pagamentoAlterado;
    private Long formaPagamentoId;
    private Long pedidoId;
    private Pagamento pagamentoAtualizado;
    AutoCloseable openMocks;
    @Before
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Dado("que o pagamento exista no sistema e não esteja pago")
    public void queOPagamentoExistaNoSistemaENaoEstejaPago() {
        boolean pagamentoExterno = true;
        pagamentoAnterior = getPagamento(PAGAMENTO_ID, PEDIDO_ID, pagamentoExterno);
    }

    @Quando("a forma de pagamento for alterada")
    public void aFormaDePagamentoForAlterada() {
        formaPagamentoId = 2L;
        pedidoId = pagamentoAnterior.getPedidoId();
        pagamentoAlterado = getPagamento(PAGAMENTO_ID, PEDIDO_ID, false);

        when(pagamentoInputPort.consultar(PAGAMENTO_ID)).thenReturn(pagamentoAnterior);
        when(gerarPagamentoInputPort.gerar(pedidoId, formaPagamentoId)).thenReturn(pagamentoAlterado);

        pagamentoAtualizado = alterarFormaPagamentoUseCase.alterarFormaPagamento(PAGAMENTO_ID, formaPagamentoId);
    }

    @Entao("um pagamento com a forma de pagamento alterada deve ser criado")
    public void umPagamentoComAFormaDePagamentoAlteradaDeveSerCriado() {
        assertNotEquals(pagamentoAtualizado, pagamentoAnterior);
        assertNotEquals(pagamentoAtualizado.getFormaPagamento().getId(), pagamentoAnterior.getFormaPagamento().getId());
        assertEquals(formaPagamentoId, pagamentoAtualizado.getFormaPagamento().getId());
        assertEquals(pagamentoAtualizado.getValor(), pagamentoAnterior.getValor());
    }

    private Pagamento getPagamento(Long pagamentoId, Long pedidoId, boolean externo) {
        Pagamento pagamento = new Pagamento();
        Long formaPagamentoId = externo ? 1L : 2L;
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamento(formaPagamentoId, externo ? MERCADO_PAGO : PIX, externo));
        pagamento.setValor(100.0);
        pagamento.setIdPagamentoExterno(null);
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
}
