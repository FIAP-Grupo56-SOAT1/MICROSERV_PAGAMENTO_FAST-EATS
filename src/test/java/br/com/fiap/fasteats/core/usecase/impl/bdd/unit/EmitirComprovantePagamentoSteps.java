package br.com.fiap.fasteats.core.usecase.impl.bdd.unit;

import br.com.fiap.fasteats.core.dataprovider.CozinhaPedidoOutputPort;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.EmitirComprovantePagamentoUseCase;
import br.com.fiap.fasteats.core.validator.EmitirComprovantePagamentoValidator;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_PAGO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmitirComprovantePagamentoSteps {
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private CozinhaPedidoOutputPort cozinhaPedidoOutputPort;
    @Mock
    private EmitirComprovantePagamentoValidator emitirComprovantePagamentoValidator;
    @InjectMocks
    private EmitirComprovantePagamentoUseCase emitirComprovantePagamentoUseCase;
    AutoCloseable openMocks;
    private Pagamento pagamento;
    private Pagamento resultado;

    @Before
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Dado("que o sistema possui um pagamento interno que pode ser emitido comprovante")
    public void queOSistemaPossuiUmPagamentoInternoQuePodeSerEmitidoComprovante() {
        pagamento = getPagamentoInterno(1L, 1L);
    }

    @Quando("eu emitir o comprovante de pagamento para o pedido")
    public void euEmitirOComprovanteDePagamentoParaOPedido() {
        when(pagamentoInputPort.consultarPorIdPedido(pagamento.getPedidoId())).thenReturn(pagamento);
        resultado = emitirComprovantePagamentoUseCase.emitir(pagamento.getPedidoId());
    }

    @Entao("o status do pagamento interno deve ser alterado para pago")
    public void oStatusDoPagamentoInternoDeveSerAlteradoParaPago() {
        assertNotNull(resultado);
        assertEquals(STATUS_PAGO, resultado.getStatusPagamento().getNome());
    }

    @Entao("o pedido deve ser recebido pela cozinha")
    public void oPedidoDeveSerRecebidoPelaCozinha() {
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
