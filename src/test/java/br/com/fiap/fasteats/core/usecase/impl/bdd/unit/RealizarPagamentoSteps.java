package br.com.fiap.fasteats.core.usecase.impl.bdd.unit;

import br.com.fiap.fasteats.core.dataprovider.AlterarPedidoStatusOutputPort;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.EmitirComprovantePagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.RealizarPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.RealizarPagamentoValidator;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
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
import static org.mockito.Mockito.when;

public class RealizarPagamentoSteps {

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
    private Pagamento pagamento;
    private Pedido pedido;
    private Exception exception;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Dado("^que o pedido com ID (\\d+) está no status \"([^\"]*)\"$")
    public void queOPedidoComIDEstáNoStatus(Long pedidoId, String statusPedido) {
        pedido = getPedido(pedidoId, statusPedido);
        pagamento = getPagamento(PAGAMENTO_ID, pedidoId, FORMA_PAGAMENTO_ID, false);

        when(pagamentoInputPort.consultarPorIdPedido(pedidoId)).thenReturn(pagamento);
    }

    @Entao("a forma de pagamento é PIX")
    public void aFormaDePagamentoEPIX() {
        assertEquals(PIX, pagamento.getFormaPagamento().getNome());
    }

    @Dado("^que o pedido com ID (\\d+) é tem um pagamento externo e está no status \"([^\"]*)\"$")
    public void queOPedidoComPagExternoComIDEstáNoStatus(Long pedidoId, String statusPedido) {
        pedido = getPedido(pedidoId, statusPedido);
        pagamento = getPagamento(PAGAMENTO_ID, pedidoId, FORMA_PAGAMENTO_ID, true);

        when(pagamentoInputPort.consultarPorIdPedido(pedidoId)).thenReturn(pagamento);
    }

    @Entao("a forma de pagamento é MERCADO_PAGO")
    public void aFormaDePagamentoEMercadoPago() {
        assertEquals(MERCADO_PAGO, pagamento.getFormaPagamento().getNome());
    }

    @Quando("^o pagamento for realizado$")
    public void oPagamentoForRealizado() {
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, PIX, false);
        Pagamento pagamentoRealizado = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_PAGO);
        pagamentoRealizado.setStatusPagamento(getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_PAGO));

        when(formaPagamentoInputPort.consultar(pagamento.getFormaPagamento().getId())).thenReturn(formaPagamento);
        when(alterarPedidoStatusOutputPort.pago(pagamento.getPedidoId())).thenReturn(Optional.of(pedido));
        when(alterarPagamentoStatusInputPort.pago(PAGAMENTO_ID)).thenReturn(pagamentoRealizado);
        when(emitirComprovantePagamentoInputPort.emitir(pagamento.getPedidoId())).thenReturn(pagamentoRealizado);

        try {
            pagamento = realizarPagamentoUseCase.realizarPagamento(pagamento.getPedidoId());
        } catch (Exception e) {
            exception = e;
        }
    }

    @Quando("^ocorrer a tentativa de pagamento$")
    public void ocorrerTentativaPagamento() {
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, MERCADO_PAGO, true);
        pagamento.setFormaPagamento(formaPagamento);

        when(formaPagamentoInputPort.consultar(pagamento.getFormaPagamento().getId())).thenReturn(formaPagamento);

        try {
            pagamento = realizarPagamentoUseCase.realizarPagamento(pagamento.getPedidoId());
        } catch (Exception e) {
            exception = e;
        }
    }

    @Quando("^a alteração de status do pedido não for realizado$")
    public void naoAlterarStatusPedido() {
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, PIX, false);
        pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_CANCELADO);

        when(formaPagamentoInputPort.consultar(pagamento.getFormaPagamento().getId())).thenReturn(formaPagamento);
        when(alterarPedidoStatusOutputPort.pago(pagamento.getPedidoId())).thenReturn(Optional.of(pedido));

        try {
            pagamento = realizarPagamentoUseCase.realizarPagamento(pagamento.getPedidoId());
        } catch (Exception e) {
            exception = e;
        }
    }

    @Entao("^o status do pagamento deve ser \"([^\"]*)\"$")
    public void oStatusDoPagamentoDeveSer(String statusPagamento) {
        assertEquals(statusPagamento, pagamento.getStatusPagamento().getNome());
    }

    @Entao("^deve ser emitido um comprovante de pagamento$")
    public void deveSerEmitidoUmComprovanteDePagamento() {
        assertNotNull(emitirComprovantePagamentoInputPort.emitir(pedido.getId()));
    }

    @Entao("^uma exceção de regra de negócio deve ser lançada$")
    public void umaExcecaoDeRegraDeNegocioDeveSerLancadaComAMensagem() {
        assertTrue(exception instanceof RegraNegocioException);
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
