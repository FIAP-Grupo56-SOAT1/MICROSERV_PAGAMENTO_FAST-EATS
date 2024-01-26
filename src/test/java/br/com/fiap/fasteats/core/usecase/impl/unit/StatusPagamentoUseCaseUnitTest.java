package br.com.fiap.fasteats.core.usecase.impl.unit;

import br.com.fiap.fasteats.core.dataprovider.StatusPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.StatusPagametoNotFound;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.impl.StatusPagamentoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Status Pagamento")
class StatusPagamentoUseCaseUnitTest {
    @Mock
    private StatusPagamentoOutputPort statusPagamentoOutputPort;
    @InjectMocks
    private StatusPagamentoUseCase statusPagamentoUseCase;
    private final Long STATUS_PAGAMENTO_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve consultar um status de pagamento")
    void consultar() {
        // Arrange
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO);

        when(statusPagamentoOutputPort.consultar(statusPagamento.getId())).thenReturn(java.util.Optional.of(statusPagamento));

        // Act
        StatusPagamento result = statusPagamentoUseCase.consultar(statusPagamento.getId());

        // Assert
        assertEquals(statusPagamento, result);
        verify(statusPagamentoOutputPort).consultar(statusPagamento.getId());
    }

    @Test
    @DisplayName("Deve apresentar um erro ao consultar um status de pagamento não encontrado")
    void consultarNaoEncontrado() {
        // Act & Assert
        assertThrows(StatusPagametoNotFound.class, () -> statusPagamentoUseCase.consultar(STATUS_PAGAMENTO_ID));
        verify(statusPagamentoOutputPort).consultar(STATUS_PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve retornar uma lista de status de pagamento")
    void listar() {
        // Arrange
        List<StatusPagamento> statusPagamentos = List.of(
                getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO),
                getStatusPagamento(STATUS_PAGAMENTO_ID + 1, STATUS_PAGO),
                getStatusPagamento(STATUS_PAGAMENTO_ID + 2, STATUS_CANCELADO)
        );

        when(statusPagamentoOutputPort.listar()).thenReturn(statusPagamentos);

        // Act
        List<StatusPagamento> result = statusPagamentoUseCase.listar();

        // Assert
        assertEquals(statusPagamentos, result);
        verify(statusPagamentoOutputPort).listar();
    }

    @Test
    @DisplayName("Deve consultar um status de pagamento por nome")
    void consultarPorNome() {
        // Arrange
        StatusPagamento statusPagamento = getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO);

        when(statusPagamentoOutputPort.consultarPorNome(statusPagamento.getNome()))
                .thenReturn(java.util.Optional.of(statusPagamento));

        // Act
        StatusPagamento result = statusPagamentoUseCase.consultarPorNome(statusPagamento.getNome());

        // Assert
        assertEquals(statusPagamento, result);
        verify(statusPagamentoOutputPort).consultarPorNome(statusPagamento.getNome());
    }

    @Test
    @DisplayName("Deve apresentar um erro ao consultar um status de pagamento por nome não encontrado")
    void consultarPorNomeNaoEncontrado() {
        // Arrange
        String nomeStatusPagamento = "TESTE";

        // Act & Assert
        assertThrows(StatusPagametoNotFound.class, () -> statusPagamentoUseCase.consultarPorNome(nomeStatusPagamento));
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