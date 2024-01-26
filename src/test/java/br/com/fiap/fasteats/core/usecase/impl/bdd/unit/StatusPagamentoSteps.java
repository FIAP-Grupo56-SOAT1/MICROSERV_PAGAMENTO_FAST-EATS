package br.com.fiap.fasteats.core.usecase.impl.bdd.unit;

import br.com.fiap.fasteats.core.dataprovider.StatusPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.StatusPagametoNotFound;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.impl.StatusPagamentoUseCase;
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

public class StatusPagamentoSteps {
    @Mock
    private StatusPagamentoOutputPort statusPagamentoOutputPort;
    @InjectMocks
    private StatusPagamentoUseCase statusPagamentoUseCase;
    private Long statusPagamentoId = 1L;
    AutoCloseable openMocks;
    private StatusPagamento statusPagamento;
    private List<StatusPagamento> statusPagamentos;
    private String nomeStatusPagamento;
    private Exception exception;

    @Before
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Dado("que existe um status de pagamento ID {long} cadastrado")
    public void queExisteUmStatusDePagamentoCadastrado(Long id) {
        statusPagamentoId = id;
        statusPagamento = getStatusPagamento(statusPagamentoId, STATUS_EM_PROCESSAMENTO);

        when(statusPagamentoOutputPort.consultar(statusPagamento.getId())).thenReturn(Optional.of(statusPagamento));
    }

    @Quando("eu consultar o status de pagamento ID {long}")
    public void euConsultarOStatusDePagamento(Long id) {
        statusPagamentoId = id;
        statusPagamento = statusPagamentoUseCase.consultar(statusPagamentoId);
    }

    @Entao("o status de pagamento deve ser retornado")
    public void oStatusDePagamentoDeveSerRetornado() {
        assertNotNull(statusPagamento);
        assertEquals(statusPagamentoId, statusPagamento.getId());
        verify(statusPagamentoOutputPort).consultar(statusPagamentoId);
    }

    @Dado("que não existe um status de pagamento com ID {long} cadastrado")
    public void queNaoExisteUmStatusDePagamentoCadastrado(Long id) {
        statusPagamentoId = id;
        when(statusPagamentoOutputPort.consultar(statusPagamentoId)).thenReturn(Optional.empty());
    }

    @Quando("eu tentar consultar o status de pagamento com ID {long}")
    public void euConsultarOStatusDePagamentoComID(Long id) {
        statusPagamentoId = id;
        try {
            statusPagamento = statusPagamentoUseCase.consultar(statusPagamentoId);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Entao("deve ser lançada a exceção StatusPagametoNotFound")
    public void deveSerLancadaAExcecaoStatusPagametoNotFound() {
        assertEquals(StatusPagametoNotFound.class, exception.getClass());
        verify(statusPagamentoOutputPort).consultar(statusPagamentoId);
    }

    @Dado("que existem status de pagamento cadastrados")
    public void queExistemStatusDePagamentoCadastrados() {
        statusPagamentos = List.of(
                getStatusPagamento(statusPagamentoId, STATUS_EM_PROCESSAMENTO),
                getStatusPagamento(statusPagamentoId + 1, PIX),
                getStatusPagamento(statusPagamentoId + 2, MERCADO_PAGO)
        );

        when(statusPagamentoOutputPort.listar()).thenReturn(statusPagamentos);
    }

    @Quando("eu listar os status de pagamento")
    public void euListarOsStatusDePagamento() {
        statusPagamentos = statusPagamentoUseCase.listar();
    }

    @Entao("a lista de status de pagamento deve ser retornada")
    public void aListaDeStatusDePagamentoDeveSerRetornada() {
        assertNotNull(statusPagamentos);
        assertEquals(3, statusPagamentos.size());
        verify(statusPagamentoOutputPort).listar();
    }

    @Dado("que existe um status de pagamento com nome {string} cadastrado")
    public void queExisteUmStatusDePagamentoComNomeCadastrado(String nome) {
        statusPagamento = getStatusPagamento(statusPagamentoId, nome);

        when(statusPagamentoOutputPort.consultarPorNome(statusPagamento.getNome()))
                .thenReturn(java.util.Optional.of(statusPagamento));
    }

    @Quando("eu consultar o status de pagamento com nome {string}")
    public void euConsultarOStatusDePagamentoComNome(String nome) {
        nomeStatusPagamento = nome;
        statusPagamento = statusPagamentoUseCase.consultarPorNome(nome);
    }

    @Entao("o status de pagamento com o nome pesquisado deve ser retornado")
    public void oStatusDePagamentoDeveSerRetornadoComNome() {
        assertNotNull(statusPagamento);
        assertEquals(nomeStatusPagamento, statusPagamento.getNome());
        verify(statusPagamentoOutputPort).consultarPorNome(nomeStatusPagamento);
    }

    @Dado("que não existe um status de pagamento com nome {string} cadastrado")
    public void queNaoExisteUmStatusDePagamentoComNomeCadastrado(String nome) {
        nomeStatusPagamento = nome;
        when(statusPagamentoOutputPort.consultarPorNome(nomeStatusPagamento)).thenReturn(Optional.empty());
    }

    @Quando("eu tentar consultar o status de pagamento com nome {string}")
    public void euTentarConsultarOStatusDePagamentoComNome(String nome) {
        nomeStatusPagamento = nome;
        try {
            statusPagamento = statusPagamentoUseCase.consultarPorNome(nomeStatusPagamento);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Entao("deve ser lançada a exceção StatusPagametoNotFound para consulta por nome")
    public void deveSerLancadaAExcecaoStatusPagametoNotFoundParaConsultaPorNome() {
        assertEquals(StatusPagametoNotFound.class, exception.getClass());
        verify(statusPagamentoOutputPort).consultarPorNome(nomeStatusPagamento);
    }

    private StatusPagamento getStatusPagamento(Long statusPagamentoId, String nomeStatusPagamento) {
        StatusPagamento statusPagamento = new StatusPagamento();
        statusPagamento.setId(statusPagamentoId);
        statusPagamento.setNome(nomeStatusPagamento);
        statusPagamento.setAtivo(true);
        return statusPagamento;
    }
}
