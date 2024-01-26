package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.dataprovider.repository.StatusPagamentoRepository;
import br.com.fiap.fasteats.dataprovider.repository.entity.StatusPagamentoEntity;
import br.com.fiap.fasteats.dataprovider.repository.mapper.StatusPagamentoEntityMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Status Pagamento Adapter")
class StatusPagamentoAdapterUnitTest {
    @Mock
    private StatusPagamentoRepository statusPagamentoRepository;
    @Mock
    private StatusPagamentoEntityMapper statusPagamentoEntityMapper;
    @InjectMocks
    private StatusPagamentoAdapter statusPagamentoAdapter;
    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("Deve listar status pagamento")
    void listar() {
        // Arrange
        List<StatusPagamento> statusPagamento = List.of(
                getStatusPagamento(1L, STATUS_RECUSADO),
                getStatusPagamento(2L, STATUS_CANCELADO),
                getStatusPagamento(3L, STATUS_PAGO),
                getStatusPagamento(4L, STATUS_EM_PROCESSAMENTO)
        );

        List<StatusPagamentoEntity> statusPagamentoEntity = statusPagamento.stream()
                .map(this::toStatusPagamentoEntity)
                .toList();

        when(statusPagamentoRepository.findAll()).thenReturn(statusPagamentoEntity);
        when(statusPagamentoEntityMapper.toStatusPagamento(statusPagamentoEntity.get(0))).thenReturn(toStatusPagamento(statusPagamentoEntity.get(0)));
        when(statusPagamentoEntityMapper.toStatusPagamento(statusPagamentoEntity.get(1))).thenReturn(toStatusPagamento(statusPagamentoEntity.get(1)));
        when(statusPagamentoEntityMapper.toStatusPagamento(statusPagamentoEntity.get(2))).thenReturn(toStatusPagamento(statusPagamentoEntity.get(2)));
        when(statusPagamentoEntityMapper.toStatusPagamento(statusPagamentoEntity.get(3))).thenReturn(toStatusPagamento(statusPagamentoEntity.get(3)));

        // Act
        List<StatusPagamento> statusPagamentoList = statusPagamentoAdapter.listar();

        // Assert
        assertEquals(statusPagamentoList.size(), statusPagamento.size());
        assertEquals(statusPagamentoList.get(0).getId(), statusPagamento.get(0).getId());
        assertEquals(statusPagamentoList.get(0).getNome(), statusPagamento.get(0).getNome());
        assertEquals(statusPagamentoList.get(0).getAtivo(), statusPagamento.get(0).getAtivo());
        verify(statusPagamentoRepository).findAll();
    }

    @Test
    @DisplayName("Deve consultar status pagamento por nome")
    void consultarPorNome() {
        // Arrange
        StatusPagamento statusPagamento = getStatusPagamento(1L, STATUS_EM_PROCESSAMENTO);
        StatusPagamentoEntity statusPagamentoEntity = toStatusPagamentoEntity(statusPagamento);

        when(statusPagamentoRepository.findByNome(STATUS_EM_PROCESSAMENTO)).thenReturn(List.of(statusPagamentoEntity));
        when(statusPagamentoEntityMapper.toStatusPagamento(statusPagamentoEntity)).thenReturn(toStatusPagamento(statusPagamentoEntity));

        // Act
        StatusPagamento statusPagamentoConsultado = statusPagamentoAdapter.consultarPorNome(STATUS_EM_PROCESSAMENTO).get();

        // Assert
        assertEquals(statusPagamentoConsultado.getId(), statusPagamento.getId());
        assertEquals(statusPagamentoConsultado.getNome(), statusPagamento.getNome());
        assertEquals(statusPagamentoConsultado.getAtivo(), statusPagamento.getAtivo());
        verify(statusPagamentoRepository).findByNome(STATUS_EM_PROCESSAMENTO);
    }

    @Test
    @DisplayName("Deve consultar status pagamento por id")
    void consultar() {
        // Arrange
        StatusPagamento statusPagamento = getStatusPagamento(1L, STATUS_EM_PROCESSAMENTO);
        StatusPagamentoEntity statusPagamentoEntity = toStatusPagamentoEntity(statusPagamento);

        when(statusPagamentoRepository.findById(1L)).thenReturn(java.util.Optional.of(statusPagamentoEntity));
        when(statusPagamentoEntityMapper.toStatusPagamento(statusPagamentoEntity)).thenReturn(toStatusPagamento(statusPagamentoEntity));

        // Act
        Optional<StatusPagamento> statusPagamentoConsultado = statusPagamentoAdapter.consultar(1L);

        // Assert
        assertTrue(statusPagamentoConsultado.isPresent());
        assertEquals(statusPagamentoConsultado.get().getId(), statusPagamento.getId());
        assertEquals(statusPagamentoConsultado.get().getNome(), statusPagamento.getNome());
        assertEquals(statusPagamentoConsultado.get().getAtivo(), statusPagamento.getAtivo());
        verify(statusPagamentoRepository).findById(1L);
    }

    private StatusPagamento getStatusPagamento(Long statusPagamentoId, String nomeStatusPagamento) {
        StatusPagamento statusPagamento = new StatusPagamento();
        statusPagamento.setId(statusPagamentoId);
        statusPagamento.setNome(nomeStatusPagamento);
        statusPagamento.setAtivo(true);
        return statusPagamento;
    }

    public StatusPagamento toStatusPagamento(StatusPagamentoEntity statusPagamentoEntity) {
        StatusPagamento statusPagamento = new StatusPagamento();
        statusPagamento.setId(statusPagamentoEntity.getId());
        statusPagamento.setNome(statusPagamentoEntity.getNome());
        statusPagamento.setAtivo(statusPagamentoEntity.getAtivo());
        return statusPagamento;
    }

    public StatusPagamentoEntity toStatusPagamentoEntity(StatusPagamento statusPagamentoEntity) {
        StatusPagamentoEntity statusPagamentoEntity1 = new StatusPagamentoEntity();
        statusPagamentoEntity1.setId(statusPagamentoEntity.getId());
        statusPagamentoEntity1.setNome(statusPagamentoEntity.getNome());
        statusPagamentoEntity1.setAtivo(statusPagamentoEntity.getAtivo());
        return statusPagamentoEntity1;
    }
}