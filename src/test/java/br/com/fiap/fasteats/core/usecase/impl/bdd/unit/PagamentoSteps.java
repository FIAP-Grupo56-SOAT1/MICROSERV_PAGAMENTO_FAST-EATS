package br.com.fiap.fasteats.core.usecase.impl.bdd.unit;

import br.com.fiap.fasteats.core.dataprovider.PagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PagamentoNotFound;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.core.validator.PagamentoValidator;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_EM_PROCESSAMENTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PagamentoSteps {
    @Mock
    private PagamentoOutputPort pagamentoOutputPort;
    @Mock
    private StatusPagamentoInputPort statusPagamentoInputPort;
    @Mock
    private PagamentoValidator pagamentoValidator;
    @InjectMocks
    private PagamentoUseCase pagamentoUseCase;
    private Long pagamentoId = 1L;
    private Long pagamentoExternoId = 1234567890L;
    private Long pedidoId = 1L;
    private final Long FORMA_PAGAMENTO_ID = 1L;
    private final Long STATUS_PAGAMENTO_ID = 1L;
    AutoCloseable openMocks;
    private Pagamento pagamento;
    private Pagamento pagamentoAtualizado;
    private List<Pagamento> pagamentos;
    private Exception exception;

    @Before
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Dado("que existem pagamentos cadastrados")
    public void queExistemPagamentosCadastrados() {
        pagamentos = List.of(
                getPagamento(pagamentoId, pedidoId, FORMA_PAGAMENTO_ID, false),
                getPagamento(pagamentoId + 1, pedidoId, FORMA_PAGAMENTO_ID, false),
                getPagamento(pagamentoId + 2, pedidoId, FORMA_PAGAMENTO_ID, false)
        );

        when(pagamentoOutputPort.listar()).thenReturn(pagamentos);
    }

    @Quando("eu listar os pagamentos")
    public void euListarOsPagamentos() {
        List<Pagamento> pagamentos = pagamentoUseCase.listar();
    }

    @Entao("a lista de pagamentos deve ser retornada")
    public void aListaDePagamentosDeveSerRetornada() {
        assertEquals(3, pagamentos.size());
        verify(pagamentoOutputPort).listar();
    }

    @Dado("que existe um pagamento com ID de pedido {long}")
    public void queExisteUmPagamentoComIDDePedido(Long id) {
        pedidoId = id;
        Pagamento pagamento = getPagamento(pagamentoId, pedidoId, FORMA_PAGAMENTO_ID, false);
        when(pagamentoOutputPort.consultarPorPedidoId(pedidoId)).thenReturn(Optional.of(pagamento));
    }

    @Quando("eu consultar o pagamento por ID de pedido {long}")
    public void euConsultarOPagamentoPorIDDePedido(Long pedidoId) {
        pagamento = pagamentoUseCase.consultarPorIdPedido(pedidoId);
    }

    @Entao("o pagamento recuperado pelo ID de pedido deve ser retornado")
    public void oPagamentoPorIdPedidoDeveSerRetornado() {
        assertEquals(pedidoId, pagamento.getPedidoId());
        verify(pagamentoOutputPort).consultarPorPedidoId(pagamento.getPedidoId());
    }

    @Dado("que não existe um pagamento com ID de pedido {long}")
    public void dadoQueNaoExisteUmPagamentoComIDDePedido(Long id) {
        pedidoId = id;
        when(pagamentoOutputPort.consultarPorPedidoId(pedidoId)).thenReturn(Optional.empty());
    }

    @Quando("eu tentar consultar o pagamento por ID de pedido {long}")
    public void euTentarConsultarOPagamentoPorIDDePedido(Long pedidoId) {
        try {
            pagamento = pagamentoUseCase.consultarPorIdPedido(pedidoId);
        } catch (PagamentoNotFound e) {
            exception = e;
        }
    }

    @Entao("deve ser lançada a exceção PagamentoNotFound para pesquisa por ID de pedido")
    public void deveSerLancadaAExcecaoPagamentoNotFoundIdPedido() {
        assertTrue(exception instanceof PagamentoNotFound);
        verify(pagamentoOutputPort).consultarPorPedidoId(pedidoId);
    }

    @Dado("que eu tenho um novo pagamento")
    public void queEuTenhoUmNovoPagamento() {
        pagamento = getPagamento(pagamentoId, pedidoId, FORMA_PAGAMENTO_ID, false);
        when(statusPagamentoInputPort.consultarPorNome(STATUS_EM_PROCESSAMENTO)).thenReturn(getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO));
        when(pagamentoOutputPort.salvarPagamento(pagamento)).thenReturn(pagamento);
    }

    @Quando("eu criar o pagamento")
    public void euCriarOPagamento() {
        pagamento = pagamentoUseCase.criar(pagamento);
    }

    @Entao("o pagamento deve ser salvo com sucesso")
    public void oPagamentoDeveSerSalvoComSucesso() {
        assertNotNull(pagamento.getDataHoraCriado());
        assertNotNull(pagamento.getStatusPagamento());
        assertNotNull(pagamento.getDataHoraProcessamento());
        verify(pagamentoOutputPort).salvarPagamento(pagamento);
    }

    @Dado("que existe um pagamento com ID {long}")
    public void queExisteUmPagamentoComID(Long id) {
        pagamentoId = id;
        pagamento = getPagamento(pagamentoId, pedidoId, FORMA_PAGAMENTO_ID, false);

        when(pagamentoOutputPort.consultar(pagamentoId)).thenReturn(Optional.of(pagamento));
    }

    @Quando("eu atualizar o pagamento")
    public void euAtualizarOPagamento() {
        pagamentoAtualizado = getPagamento(pagamentoId, pedidoId, FORMA_PAGAMENTO_ID, false);
        pagamentoAtualizado.setValor(200.0);

        when(pagamentoOutputPort.salvarPagamento(pagamentoAtualizado)).thenReturn(pagamentoAtualizado);

        pagamento = pagamentoUseCase.atualizar(pagamentoAtualizado);
    }

    @Entao("o pagamento deve ser atualizado com sucesso")
    public void oPagamentoDeveSerAtualizadoComSucesso() {
        assertEquals(pagamentoAtualizado.getValor(), pagamento.getValor());
        verify(pagamentoOutputPort).salvarPagamento(pagamentoAtualizado);
    }

    @Quando("eu consultar o pagamento por ID {long}")
    public void euConsultarOPagamentoPorID(Long pagamentoId) {
        pagamento = pagamentoUseCase.consultar(pagamentoId);
    }

    @Entao("o pagamento deve ser retornado")
    public void oPagamentoDeveSerRetornado() {
        assertEquals(pagamentoId, pagamento.getId());
        verify(pagamentoOutputPort).consultar(pagamentoId);
    }

    @Dado("que não existe um pagamento com ID {long}")
    public void queNaoExisteUmPagamentoComID(Long id) {
        when(pagamentoOutputPort.consultar(id)).thenReturn(Optional.empty());
    }

    @Quando("eu tentar consultar o pagamento por ID {long}")
    public void euTentarConsultarOPagamentoPorID(Long id) {
        pagamentoId = id;
        try {
            pagamento = pagamentoUseCase.consultar(id);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Entao("deve ser lançada a exceção PagamentoNotFound")
    public void deveSerLancadaAExcecaoPagamentoNotFound() {
        assertTrue(exception instanceof PagamentoNotFound);
        verify(pagamentoOutputPort).consultar(pagamentoId);
    }

    @Dado("que existe um pagamento com ID de pagamento externo {long}")
    public void queExisteUmPagamentoComIDDePagamentoExterno(Long id) {
        pagamentoExternoId = id;
        pagamento = getPagamento(pagamentoId, pedidoId, FORMA_PAGAMENTO_ID, true);
        pagamento.setIdPagamentoExterno(pagamentoExternoId);

        when(pagamentoOutputPort.consultarPorIdPagamentoExterno(pagamentoExternoId)).thenReturn(Optional.of(pagamento));
    }

    @Quando("eu consultar o pagamento por ID de pagamento externo {long}")
    public void euConsultarOPagamentoPorIDDePagamentoExterno(Long id) {
        pagamentoExternoId = id;
        pagamento = pagamentoUseCase.consultarPorIdPagamentoExterno(pagamentoExternoId);
    }

    @Entao("o pagamento recuperado pelo ID de pagamento externo deve ser retornado")
    public void oPagamentoRecuperadoPeloIDDePagamentoExternoDeveSerRetornado() {
        assertEquals(pagamentoExternoId, pagamento.getIdPagamentoExterno());
        verify(pagamentoOutputPort).consultarPorIdPagamentoExterno(pagamentoExternoId);
    }

    @Dado("que não existe um pagamento com ID de pagamento externo {long}")
    public void queNaoExisteUmPagamentoComIDDePagamentoExterno(Long id) {
        pagamentoExternoId = id;
        when(pagamentoOutputPort.consultarPorIdPagamentoExterno(pagamentoExternoId)).thenReturn(Optional.empty());
    }

    @Quando("eu tentar consultar o pagamento por ID de pagamento externo {long}")
    public void euTentarConsultarOPagamentoPorIDDePagamentoExterno(Long id) {
        pagamentoExternoId = id;
        try {
            pagamento = pagamentoUseCase.consultarPorIdPagamentoExterno(pagamentoExternoId);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Entao("deve ser lançada a exceção PagamentoNotFound para pesquisa por ID de pagamento externo")
    public void deveSerLancadaAExcecaoPagamentoNotFoundIdPagamentoExterno() {
        assertTrue(exception instanceof PagamentoNotFound);
        verify(pagamentoOutputPort).consultarPorIdPagamentoExterno(pagamentoExternoId);
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
}
