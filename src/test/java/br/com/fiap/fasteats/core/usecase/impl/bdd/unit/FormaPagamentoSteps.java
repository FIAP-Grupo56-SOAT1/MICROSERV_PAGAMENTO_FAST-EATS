package br.com.fiap.fasteats.core.usecase.impl.bdd.unit;

import br.com.fiap.fasteats.core.dataprovider.FormaPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.FormaPagamentoNotFound;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.usecase.impl.FormaPagamentoUseCase;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FormaPagamentoSteps {
    @Mock
    private FormaPagamentoOutputPort formaPagamentoOutputPort;
    @InjectMocks
    private FormaPagamentoUseCase formaPagamentoUseCase;
    AutoCloseable openMocks;
    private FormaPagamento formaPagamento;
    private FormaPagamento formaPagamentoResult;
    List<FormaPagamento> formasPagamento;
    private Exception exception;

    @Before
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Dado("que uma nova forma de pagamento {string} deve ser criada")
    public void queUmaNovaFormaDePagamentoDeveSerCriada(String nome) {
        formaPagamento = getFormaPagamento(1L, nome, false);

        when(formaPagamentoOutputPort.criar(formaPagamento)).thenReturn(formaPagamento);
    }

    @Quando("eu chamar o método de criação da forma de pagamento")
    public void euChamarOMétodoDeCriaçãoDaFormaDePagamento() {
        formaPagamentoUseCase.criar(formaPagamento);
    }

    @Entao("a forma de pagamento criada deve estar ativa e com o nome em maiúsculas")
    public void aFormaDePagamentoCriadaDeveEstarAtivaEComONomeEmMaiúsculas() {
        assertEquals(formaPagamento.getNome().toUpperCase(), formaPagamento.getNome());
        assertTrue(formaPagamento.getAtivo());
        verify(formaPagamentoOutputPort).criar(formaPagamento);
    }

    @Dado("que uma nova forma de pagamento externa {string} deve ser criada")
    public void queUmaNovaFormaDePagamentoExternaDeveSerCriada(String nome) {
        formaPagamento = getFormaPagamento(1L, nome, true);

        when(formaPagamentoOutputPort.criar(formaPagamento)).thenReturn(formaPagamento);
    }

    @Entao("a forma de pagamento criada deve ser marcada como externa")
    public void aFormaDePagamentoCriadaDeveSerMarcadaComoExterna() {
        assertTrue(formaPagamento.getExterno());
        assertEquals(formaPagamento.getNome().toUpperCase(), formaPagamento.getNome());
        assertTrue(formaPagamento.getAtivo());
        verify(formaPagamentoOutputPort).criar(formaPagamento);
    }

    @Dado("que uma forma de pagamento {string} já existe")
    public void queUmaFormaDePagamentoJaExiste(String nome) {
        formaPagamento = getFormaPagamento(1L, nome, false);
        FormaPagamento formaPagamentoCadastrada = getFormaPagamento(1L, nome, false);
        formaPagamentoCadastrada.setNome(formaPagamentoCadastrada.getNome().toUpperCase());
        formaPagamentoCadastrada.setAtivo(true);

        when(formaPagamentoOutputPort.consultarPorNome(nome.toUpperCase())).thenReturn(Optional.of(formaPagamentoCadastrada));
    }

    @Quando("eu tentar criar uma nova forma de pagamento com o mesmo nome")
    public void euTentarCriarUmaNovaFormaDePagamentoComOMesmoNome() {
        try {
            formaPagamentoUseCase.criar(formaPagamento);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Entao("deve lançar uma exceção de regra de negócio")
    public void deveLancarUmaExcecaoDeRegraDeNegocio() {
        assertNotNull(exception);
        assertTrue(exception instanceof RegraNegocioException);
        verify(formaPagamentoOutputPort).consultarPorNome(formaPagamento.getNome().toUpperCase());
    }

    @Dado("que uma forma de pagamento com ID {long} existe")
    public void queUmaFormaDePagamentoComIDExiste(Long id) {
        formaPagamento = getFormaPagamento(id, "PIX", false);

        when(formaPagamentoOutputPort.consultar(id)).thenReturn(Optional.of(formaPagamento));
    }

    @Quando("eu consultar a forma de pagamento pelo ID")
    public void euConsultarAFormaDePagamentoPeloID() {
        formaPagamentoUseCase.consultar(1L);
    }

    @Entao("devo receber a forma de pagamento esperada")
    public void devoReceberAFormaDePagamentoEsperada() {
        verify(formaPagamentoOutputPort).consultar(anyLong());
    }

    @Dado("que uma forma de pagamento com ID {long} não existe")
    public void queUmaFormaDePagamentoComIDNaoExiste(Long id) {
        formaPagamento = getFormaPagamento(id, "PIX", false);
        when(formaPagamentoOutputPort.consultar(id)).thenReturn(Optional.empty());
    }

    @Quando("eu tentar consultar a forma de pagamento pelo ID {long}")
    public void euTentarConsultarAFormaDePagamentoPeloID(Long id) {
        try {
            formaPagamentoUseCase.consultar(id);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Quando("eu tentar atualizar a forma de pagamento")
    public void euTentarAtualizarAFormaDePagamento() {
        try {
            formaPagamentoUseCase.atualizar(formaPagamento);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Entao("deve lançar uma exceção de forma de pagamento não encontrada")
    public void deveLancarUmaExcecaoDeFormaDePagamentoNaoEncontrada() {
        assertTrue(exception instanceof FormaPagamentoNotFound);
    }

    @Dado("que uma forma de pagamento com ID {long} existe para atualização")
    public void queUmaFormaDePagamentoComIDExisteParaAtualizacao(Long id) {
        formaPagamento = getFormaPagamento(id, "PIX", false);
        formaPagamentoResult = getFormaPagamento(id, "PIX", false);

        when(formaPagamentoOutputPort.consultar(id)).thenReturn(Optional.of(formaPagamento));
        when(formaPagamentoOutputPort.atualizar(any(FormaPagamento.class))).thenReturn(formaPagamentoResult);
    }

    @Quando("eu atualizar a forma de pagamento")
    public void euAtualizarAFormaDePagamento() {
        formaPagamentoResult = getFormaPagamento(formaPagamento.getId(), "PIX", false);
        formaPagamentoResult.setAtivo(true);

        formaPagamentoUseCase.atualizar(formaPagamento);
    }

    @Entao("a forma de pagamento deve estar ativa e com o nome em maiúsculas")
    public void aFormaDePagamentoDeveEstarAtivaEComONomeEmMaiúsculas() {
        assertEquals(formaPagamentoResult.getNome(), formaPagamento.getNome());
        assertTrue(formaPagamento.getAtivo());
        verify(formaPagamentoOutputPort).atualizar(formaPagamento);
    }

    @Dado("que uma forma de pagamento com ID {long} não existe para atualização")
    public void queUmaFormaDePagamentoComIDNaoExisteParaAtualizacao(Long id) {
        when(formaPagamentoOutputPort.consultar(id)).thenReturn(Optional.empty());
    }

    @Dado("que uma forma de pagamento com ID {long} existe para deleção")
    public void queUmaFormaDePagamentoComIDExisteParaDelecao(Long id) {
        formaPagamento = getFormaPagamento(id, "PIX", false);

        when(formaPagamentoOutputPort.consultar(id)).thenReturn(Optional.of(formaPagamento));
    }

    @Quando("eu deletar a forma de pagamento")
    public void euDeletarAFormaDePagamento() {
        formaPagamentoUseCase.deletar(1L);
    }

    @Entao("a forma de pagamento deve ser removida")
    public void aFormaDePagamentoDeveSerRemovida() {
        verify(formaPagamentoOutputPort).deletar(1L);
    }

    @Dado("que uma forma de pagamento com ID {long} não existe para deleção")
    public void queUmaFormaDePagamentoComIDNaoExisteParaDelecao(Long id) {
        when(formaPagamentoOutputPort.consultar(id)).thenReturn(Optional.empty());
    }

    @Quando("eu tentar deletar a forma de pagamento")
    public void euTentarDeletarAFormaDePagamento() {
        try {
            formaPagamentoUseCase.deletar(999L);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Dado("que existem formas de pagamento cadastradas")
    public void queExistemFormasDePagamentoCadastradas() {
        formasPagamento = List.of(
                getFormaPagamento(1L, "PIX", false),
                getFormaPagamento(2L, "DINHEIRO", false),
                getFormaPagamento(3L, "CARTÃO", false)
        );

        when(formaPagamentoOutputPort.listar()).thenReturn(Optional.of(formasPagamento));
    }

    @Quando("eu listar todas as formas de pagamento")
    public void euListarTodasAsFormasDePagamento() {
        formaPagamentoUseCase.listar();
    }

    @Entao("devo receber a lista de formas de pagamento")
    public void devoReceberAListaDeFormasDePagamento() {
        assertEquals(3, formasPagamento.size());
        verify(formaPagamentoOutputPort).listar();
    }

    @Dado("que não existem formas de pagamento cadastradas")
    public void queNaoExistemFormasDePagamentoCadastradas() {
        when(formaPagamentoOutputPort.listar()).thenReturn(Optional.empty());
    }

    @Entao("deve lançar uma exceção de forma de pagamento não encontrada para listagem")
    public void deveLancarUmaExcecaoDeFormaDePagamentoNaoEncontradaParaListagem() {
        assertThrows(FormaPagamentoNotFound.class, () -> formaPagamentoUseCase.listar());
        verify(formaPagamentoOutputPort).listar();
    }

    @Quando("eu tentar listar todas as formas de pagamento")
    public void euTentarListarTodasAsFormasDePagamento() {
        try {
            formaPagamentoUseCase.listar();
        } catch (Exception e) {
            exception = e;
        }
    }

    @Entao("deve lançar uma exceção de formas de pagamento não encontradas")
    public void deveLancarUmaExcecaoDeFormasDePagamentoNaoEncontradas() {
        assertTrue(exception instanceof FormaPagamentoNotFound);
        verify(formaPagamentoOutputPort).listar();
    }

    @Dado("que uma forma de pagamento com nome {string} existe")
    public void queUmaFormaDePagamentoComNomeExiste(String nome) {
        formaPagamento = getFormaPagamento(1L, nome, false);

        when(formaPagamentoOutputPort.consultarPorNome(nome.toUpperCase())).thenReturn(Optional.of(formaPagamento));
    }

    @Quando("eu consultar a forma de pagamento pelo nome {string}")
    public void euConsultarAFormaDePagamentoPeloNome(String nome) {
        try {
            formaPagamentoUseCase.consultarPorNome(nome);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Entao("devo receber a forma de pagamento {string} como resultado")
    public void devoReceberAFormaDePagamentoComoResultado(String nome) {
        assertEquals(nome.toUpperCase(), formaPagamento.getNome());
        verify(formaPagamentoOutputPort).consultarPorNome(nome.toUpperCase());
    }

    @Dado("que uma forma de pagamento com nome {string} não existe")
    public void queUmaFormaDePagamentoComIDExiste(String nome) {
        when(formaPagamentoOutputPort.consultarPorNome(nome.toUpperCase())).thenReturn(Optional.empty());
    }

    private FormaPagamento getFormaPagamento(Long id, String nome, Boolean externo) {
        return new FormaPagamento(id, nome, externo, true);
    }
}
