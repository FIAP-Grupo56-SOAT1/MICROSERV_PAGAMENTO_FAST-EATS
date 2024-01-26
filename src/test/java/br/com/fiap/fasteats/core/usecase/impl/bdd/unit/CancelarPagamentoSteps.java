package br.com.fiap.fasteats.core.usecase.impl.bdd.unit;

import br.com.fiap.fasteats.core.dataprovider.CancelarPedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.CancelarPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.CancelarPagamentoValidator;
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
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_CANCELADO;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_EM_PROCESSAMENTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CancelarPagamentoSteps {
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    @Mock
    private CancelarPedidoOutputPort cancelarPedidoOutputPort;
    @Mock
    private CancelarPagamentoValidator cancelarPagamentoValidator;
    @InjectMocks
    private CancelarPagamentoUseCase cancelarPagamentoUseCase;
    AutoCloseable openMocks;
    private Pagamento pagamento;
    private Pagamento resultado;
    private Pagamento pagamentoExterno;
    private Exception regraNegocioException;

    @Before
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Dado("que o pagamento interno exista no sistema")
    public void queOPagamentoInternoExistaNoSistema() {
        pagamento = getPagamentoInterno(1L, 1L);
        when(pagamentoInputPort.consultarPorIdPedido(pagamento.getPedidoId())).thenReturn(pagamento);
    }

    @Quando("o pagamento interno for cancelado")
    public void oPagamentoInternoForCancelado() {
        Pagamento pagamentoCancelado = getPagamentoInterno(1L, 1L);
        pagamentoCancelado.setStatusPagamento(getStatusPagamento(2L, STATUS_CANCELADO));

        when(alterarPagamentoStatusInputPort.cancelado(pagamento.getId())).thenReturn(pagamentoCancelado);

        resultado = cancelarPagamentoUseCase.cancelar(pagamento.getPedidoId());
    }

    @Entao("o status do pagamento interno deve ser alterado para cancelado")
    public void oStatusDoPagamentoInternoDeveSerAlteradoParaCancelado() {
        assertNotNull(resultado);
        assertEquals(STATUS_CANCELADO, resultado.getStatusPagamento().getNome());
    }

    @Entao("o pedido deve ser cancelado")
    public void oPedidoDeveSerCancelado() {
        verify(cancelarPedidoOutputPort).cancelar(pagamento.getPedidoId());
    }

    @Entao("o pagamento interno deve ser marcado como cancelado")
    public void oPagamentoInternoDeveSerMarcadoComoCancelado() {
        verify(alterarPagamentoStatusInputPort).cancelado(pagamento.getId());
    }

    @Dado("que o pagamento externo exista no sistema")
    public void queOPagamentoExternoExistaNoSistema() {
        pagamentoExterno = getPagamentoExterno(1L, 1L);
    }

    @Quando("eu tento cancelar o pagamento externo")
    public void euTentoCancelarOPagamentoExterno() {
        when(pagamentoInputPort.consultarPorIdPedido(pagamentoExterno.getPedidoId())).thenReturn(pagamentoExterno);
        try {
            cancelarPagamentoUseCase.cancelar(pagamentoExterno.getPedidoId());
        }catch (Exception e){
            regraNegocioException = e;
        }
    }

    @Entao("deve gerar um erro de regra de negócio")
    public void deveGerarUmErroDeRegraDeNegocio() {
        assertNotNull(regraNegocioException);
        assertTrue(regraNegocioException instanceof RegraNegocioException);
    }

    private Pagamento getPagamentoInterno(Long pagamentoId, Long pedidoId) {
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

    private Pagamento getPagamentoExterno(Long pagamentoId, Long pedidoId) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamentoExterno(2L));
        pagamento.setValor(100.0);
        pagamento.setIdPagamentoExterno(null);
        pagamento.setQrCode(null);
        pagamento.setUrlPagamento(null);
        return pagamento;
    }

    private FormaPagamento getFormaPagamentoExterno(Long formaPagamentoId) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(formaPagamentoId);
        formaPagamento.setNome(MERCADO_PAGO);
        formaPagamento.setExterno(true);
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
