package br.com.fiap.fasteats.core.usecase.impl.unit;

import br.com.fiap.fasteats.core.dataprovider.FormaPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.FormaPagamentoNotFound;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.usecase.impl.FormaPagamentoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Forma de Pagamento")
class FormaPagamentoUseCaseUnitTest {
    @Mock
    private FormaPagamentoOutputPort formaPagamentoOutputPort;
    @InjectMocks
    private FormaPagamentoUseCase formaPagamentoUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar uma forma de pagamento")
    void criar() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(1L, "PIX", false);
        FormaPagamento formaPagamentoResult = getFormaPagamento(1L, "PIX", false);
        formaPagamentoResult.setNome(formaPagamentoResult.getNome().toUpperCase());
        formaPagamentoResult.setAtivo(true);

        when(formaPagamentoOutputPort.criar(formaPagamento)).thenReturn(formaPagamento);

        // Act
        FormaPagamento result = formaPagamentoUseCase.criar(formaPagamento);

        // Assert
        assertEquals(formaPagamentoResult, result);
        verify(formaPagamentoOutputPort).criar(formaPagamento);
    }

    @Test
    @DisplayName("Deve criar uma forma de pagamento externa")
    void criarExterno() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(1L, "PIX", true);
        FormaPagamento formaPagamentoResult = getFormaPagamento(1L, "PIX", true);
        formaPagamentoResult.setNome(formaPagamentoResult.getNome().toUpperCase());
        formaPagamentoResult.setAtivo(true);

        when(formaPagamentoOutputPort.criar(formaPagamento)).thenReturn(formaPagamento);

        // Act
        FormaPagamento result = formaPagamentoUseCase.criar(formaPagamento);

        // Assert
        assertTrue(result.getExterno());
        assertEquals(formaPagamentoResult, result);
        verify(formaPagamentoOutputPort).criar(formaPagamento);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar criar uma forma de pagamento duplicada")
    void criarFormaDePagamentoDuplicada() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(1L, "pix", false);
        FormaPagamento formaPagamentoCadastrada = getFormaPagamento(1L, "PIX", false);
        formaPagamentoCadastrada.setNome(formaPagamentoCadastrada.getNome().toUpperCase());
        formaPagamentoCadastrada.setAtivo(true);

        when(formaPagamentoOutputPort.consultarPorNome(formaPagamento.getNome().toUpperCase())).thenReturn(Optional.of(formaPagamentoCadastrada));

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> formaPagamentoUseCase.criar(formaPagamento));
        verify(formaPagamentoOutputPort).consultarPorNome(formaPagamento.getNome().toUpperCase());
    }

    @Test
    @DisplayName("Deve consultar uma forma de pagamento")
    void consultar() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(1L, "PIX", false);

        when(formaPagamentoOutputPort.consultar(formaPagamento.getId())).thenReturn(Optional.of(formaPagamento));

        // Act
        FormaPagamento result = formaPagamentoUseCase.consultar(formaPagamento.getId());

        // Assert
        assertEquals(formaPagamento, result);
        verify(formaPagamentoOutputPort).consultar(formaPagamento.getId());
    }

    @Test
    @DisplayName("Deve apresentar erro ao consultar uma forma de pagamento não cadastrada")
    void consultaNaoEncontrada() {
        // Act & Assert
        assertThrows(FormaPagamentoNotFound.class, () -> formaPagamentoUseCase.consultar(1L));
        verify(formaPagamentoOutputPort).consultar(1L);
    }

    @Test
    @DisplayName("Deve atualizar uma forma de pagamento")
    void atualizar() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(1L, "pix", false);
        FormaPagamento formaPagamentoResult = getFormaPagamento(1L, "PIX", false);
        formaPagamentoResult.setNome(formaPagamentoResult.getNome().toUpperCase());
        formaPagamentoResult.setAtivo(true);

        when(formaPagamentoOutputPort.consultar(formaPagamento.getId())).thenReturn(Optional.of(formaPagamento));
        when(formaPagamentoOutputPort.atualizar(any())).thenReturn(formaPagamento);

        // Act
        FormaPagamento result = formaPagamentoUseCase.atualizar(formaPagamento);

        // Assert
        assertTrue(result.getAtivo());
        assertEquals(formaPagamentoResult.getNome(), result.getNome());
        verify(formaPagamentoOutputPort).consultar(formaPagamento.getId());
        verify(formaPagamentoOutputPort).atualizar(formaPagamento);
    }

    @Test
    @DisplayName("Deve deletar uma forma de pagamento")
    void deletar() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(1L, "PIX", false);

        when(formaPagamentoOutputPort.consultar(formaPagamento.getId())).thenReturn(Optional.of(formaPagamento));

        // Act
        formaPagamentoUseCase.deletar(formaPagamento.getId());

        // Assert
        verify(formaPagamentoOutputPort).consultar(formaPagamento.getId());
        verify(formaPagamentoOutputPort).deletar(formaPagamento.getId());
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar deletar uma forma de pagamento não cadastrada")
    void deletarFormaDePagamentoNaoEncontrada() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(1L, "PIX", false);

        // Act & Assert
        assertThrows(FormaPagamentoNotFound.class, () -> formaPagamentoUseCase.deletar(formaPagamento.getId()));
        verify(formaPagamentoOutputPort).consultar(formaPagamento.getId());
    }

    @Test
    @DisplayName("Deve listar as formas de pagamento")
    void listar() {
        // Arrange
        List<FormaPagamento> formaPagamentos;
        formaPagamentos = List.of(
                getFormaPagamento(1L, "PIX", false),
                getFormaPagamento(2L, "CARTAO", false),
                getFormaPagamento(3L, "DINHEIRO", false)
        );

        when(formaPagamentoOutputPort.listar()).thenReturn(Optional.of(formaPagamentos));

        // Act
        List<FormaPagamento> result = formaPagamentoUseCase.listar();

        // Assert
        assertEquals(formaPagamentos, result);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar listar as formas de pagamento e não encontrar nenhuma")
    void listarErroNadaEncontrado() {
        // Act & Assert
        assertThrows(FormaPagamentoNotFound.class, () -> formaPagamentoUseCase.listar());
        verify(formaPagamentoOutputPort).listar();
    }

    @Test
    @DisplayName("Deve consultar uma forma de pagamento por nome")
    void consultarPorNome() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(1L, "PIX", false);

        when(formaPagamentoOutputPort.consultarPorNome(formaPagamento.getNome())).thenReturn(Optional.of(formaPagamento));

        // Act
        FormaPagamento result = formaPagamentoUseCase.consultarPorNome(formaPagamento.getNome());

        // Assert
        assertEquals(formaPagamento, result);
        verify(formaPagamentoOutputPort).consultarPorNome(formaPagamento.getNome());
    }

    @Test
    @DisplayName("Deve apresentar erro ao consultar uma forma de pagamento por nome e não encontrar nenhuma")
    void consultarPorNomeErroNadaEncontrado() {
        // Act & Assert
        assertThrows(FormaPagamentoNotFound.class, () -> formaPagamentoUseCase.consultarPorNome("PIX"));
        verify(formaPagamentoOutputPort).consultarPorNome("PIX");
    }

    private FormaPagamento getFormaPagamento(Long id, String nome, Boolean externo) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(id);
        formaPagamento.setNome(nome);
        formaPagamento.setExterno(externo);
        return formaPagamento;
    }
}