package br.com.fiap.fasteats.core.usecase.impl.bdd.unit;

import br.com.fiap.fasteats.core.dataprovider.AlterarPedidoStatusOutputPort;
import br.com.fiap.fasteats.core.dataprovider.PagamentoExternoOutputPort;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.EmitirComprovantePagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.PagamentoExternoUseCase;
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
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PagamentoExternoSteps {
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private PagamentoExternoOutputPort pagamentoExternoOutputPort;
    @Mock
    private EmitirComprovantePagamentoInputPort emitirComprovantePagamentoInputPort;
    @Mock
    private AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    @Mock
    private AlterarPedidoStatusOutputPort alterarPedidoStatusOutputPort;
    @Mock
    private CancelarPagamentoValidator cancelarPagamentoValidator;
    @InjectMocks
    private PagamentoExternoUseCase pagamentoExternoUseCase;
    private final Long PAGAMENTO_ID = 1L;
    private final Long PAGAMENTO_EXTERNO_ID = 1234567890L;
    private final Long PEDIDO_ID = 1L;
    private final Long FORMA_PAGAMENTO_ID = 1L;
    private final Long STATUS_PAGAMENTO_ID = 1L;
    AutoCloseable openMocks;
    private PagamentoExterno pagamentoExterno;
    private Pagamento pagamento;
    private Pagamento pagamentoAtualizadoExterno;
    private Exception exception;

    @Before
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        openMocks.close();
    }


    @Dado("que o pagamento externo com ID {long} está marcado como {string}")
    public void queOPagamentoExternoComIDEstaMarcadoComo(Long pagamentoExternoId, String status) {
        pagamentoExterno = getPagamentoExterno(pagamentoExternoId, "QrCode", "UrlPagamento");
        pagamentoExterno.setStatus(status);
        pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, pagamentoExternoId, true);
        pagamentoAtualizadoExterno = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, pagamentoExternoId, true);
        pagamento.setStatusPagamento(getStatusPagamento(STATUS_PAGAMENTO_ID, status));
        pagamentoAtualizadoExterno.setStatusPagamento(getStatusPagamento(STATUS_PAGAMENTO_ID, status));

        when(pagamentoInputPort.consultarPorIdPagamentoExterno(pagamentoExternoId)).thenReturn(pagamento);
        when(pagamentoExternoOutputPort.recuperarPagamentoDePagamentoExterno(pagamentoExterno)).thenReturn(pagamentoAtualizadoExterno);
        when(pagamentoInputPort.consultarPorIdPedido(PEDIDO_ID)).thenReturn(pagamento);
        when(emitirComprovantePagamentoInputPort.emitir(PEDIDO_ID)).thenReturn(pagamento);
    }

    @Quando("eu atualizo o status do pagamento por meio de um pagamento externo")
    public void euAtualizoOPagamentoExterno() {
        pagamento = pagamentoExternoUseCase.atualizarPagamento(pagamentoExterno);
    }

    @Entao("o status do pagamento e do pedido devem ser alterados para pago")
    public void oStatusDoPagamentoDeveSerPago() {
        assertEquals(STATUS_PAGO, pagamento.getStatusPagamento().getNome());
        verify(alterarPagamentoStatusInputPort).pago(PEDIDO_ID);
        verify(alterarPedidoStatusOutputPort).pago(PEDIDO_ID);
        verify(emitirComprovantePagamentoInputPort).emitir(PEDIDO_ID);
    }

    @Entao("o status do pagamento deve ser recusado")
    public void oStatusDoPagamentoDeveSerRecusado() {
        assertEquals(STATUS_RECUSADO, pagamento.getStatusPagamento().getNome());
        verify(alterarPagamentoStatusInputPort).recusado(PEDIDO_ID);
    }

    @Entao("o status do pagamento e do pedido devem ser alterados para cancelado")
    public void oStatusDoPagamentoDeveSerCancelado() {
        assertEquals(STATUS_CANCELADO, pagamento.getStatusPagamento().getNome());
        verify(alterarPagamentoStatusInputPort).cancelado(PEDIDO_ID);
        verify(alterarPedidoStatusOutputPort).cancelado(PEDIDO_ID);
    }

    @Entao("deve ser retornado um pagamento vazio")
    public void deveSerApresentadoUmErroInformandoQueOStatusDoPagamentoNaoFoiEncontrado() {
        assertNull(pagamento.getId());
    }

    @Dado("que o pagamento externo com ID {long} existe")
    public void queOPagamentoExternoComIDExiste(Long pagamentoExternoId) {
        pagamentoExterno = getPagamentoExterno(pagamentoExternoId, "QrCode", "UrlPagamento");
    }

    @Quando("eu cancelo o pagamento externo")
    public void euCanceloOPagamentoExterno() {
        pagamentoExternoUseCase.cancelarPagamentoExterno(pagamentoExterno.getId());
    }

    @Entao("o pagamento externo deve ser cancelado com sucesso")
    public void oPagamentoExternoDeveSerCanceladoComSucesso() {
        verify(pagamentoExternoOutputPort).cancelarPagamento(pagamentoExterno.getId());
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
