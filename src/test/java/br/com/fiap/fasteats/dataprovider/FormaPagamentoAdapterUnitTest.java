package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.dataprovider.repository.FormaPagamentoRepository;
import br.com.fiap.fasteats.dataprovider.repository.entity.FormaPagamentoEntity;
import br.com.fiap.fasteats.dataprovider.repository.mapper.FormaPagamentoEntityMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Forma Pagamento Adapter")
class FormaPagamentoAdapterUnitTest {
    @Mock
    private FormaPagamentoRepository formaPagamentoRepository;
    @Mock
    private FormaPagamentoEntityMapper formaPagamentoEntityMapper;
    @InjectMocks
    private FormaPagamentoAdapter formaPagamentoAdapter;
    AutoCloseable openMocks;
    private final Long FORMA_PAGAMENTO_ID = 1L;

    @BeforeEach
    void setUp() {
        openMocks = org.mockito.MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("Deve criar forma de pagamento")
    void criar() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, "PIX", false);
        FormaPagamentoEntity formaPagamentoEntity = toFormaPagamentoEntity(formaPagamento);
        FormaPagamentoEntity formaPagamentoEntitySalva = toFormaPagamentoEntity(formaPagamento);

        when(formaPagamentoEntityMapper.toFormaPagamentoEntity(formaPagamento)).thenReturn(formaPagamentoEntity);
        when(formaPagamentoRepository.save(formaPagamentoEntity)).thenReturn(formaPagamentoEntitySalva);
        when(formaPagamentoEntityMapper.toFormaPagamento(formaPagamentoEntitySalva)).thenReturn(formaPagamento);

        // Act
        FormaPagamento formaPagamentoSalva = formaPagamentoAdapter.criar(formaPagamento);

        // Assert
        assertEquals(formaPagamento, formaPagamentoSalva);
        verify(formaPagamentoRepository).save(formaPagamentoEntity);
        verify(formaPagamentoEntityMapper).toFormaPagamentoEntity(formaPagamento);
        verify(formaPagamentoEntityMapper).toFormaPagamento(formaPagamentoEntitySalva);
    }

    @Test
    @DisplayName("Deve consultar forma de pagamento")
    void consultar() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, PIX, false);
        FormaPagamentoEntity formaPagamentoEntity = toFormaPagamentoEntity(formaPagamento);

        when(formaPagamentoRepository.findById(FORMA_PAGAMENTO_ID)).thenReturn(java.util.Optional.of(formaPagamentoEntity));
        when(formaPagamentoEntityMapper.toFormaPagamento(formaPagamentoEntity)).thenReturn(formaPagamento);

        // Act
        Optional<FormaPagamento> formaPagamentoConsultada = formaPagamentoAdapter.consultar(FORMA_PAGAMENTO_ID);

        // Assert
        assertTrue(formaPagamentoConsultada.isPresent());
        assertEquals(formaPagamento, formaPagamentoConsultada.get());
        verify(formaPagamentoRepository).findById(FORMA_PAGAMENTO_ID);
        verify(formaPagamentoEntityMapper).toFormaPagamento(formaPagamentoEntity);
    }

    @Test
    @DisplayName("Deve atualizar forma de pagamento")
    void atualizar() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, PIX, false);
        FormaPagamentoEntity formaPagamentoEntity = toFormaPagamentoEntity(formaPagamento);
        FormaPagamentoEntity formaPagamentoEntitySalva = toFormaPagamentoEntity(formaPagamento);

        when(formaPagamentoEntityMapper.toFormaPagamentoEntity(formaPagamento)).thenReturn(formaPagamentoEntity);
        when(formaPagamentoRepository.save(formaPagamentoEntity)).thenReturn(formaPagamentoEntitySalva);
        when(formaPagamentoEntityMapper.toFormaPagamento(formaPagamentoEntitySalva)).thenReturn(formaPagamento);

        // Act
        FormaPagamento formaPagamentoAtualizada = formaPagamentoAdapter.atualizar(formaPagamento);

        // Assert
        assertEquals(formaPagamento, formaPagamentoAtualizada);
        verify(formaPagamentoRepository).save(formaPagamentoEntity);
        verify(formaPagamentoEntityMapper).toFormaPagamentoEntity(formaPagamento);
        verify(formaPagamentoEntityMapper).toFormaPagamento(formaPagamentoEntitySalva);
    }

    @Test
    @DisplayName("Deve deletar forma de pagamento")
    void deletar() {
        // Act
        formaPagamentoAdapter.deletar(FORMA_PAGAMENTO_ID);

        // Assert
        verify(formaPagamentoRepository).deleteById(FORMA_PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve listar formas de pagamento")
    void listar() {
        // Arrange
        List<FormaPagamento> formasPagamento = List.of(
                getFormaPagamento(1L, PIX, false),
                getFormaPagamento(2L, MERCADO_PAGO, true)
        );

        List<FormaPagamentoEntity> formaPagamentoEntities = formasPagamento .stream()
                .map(this::toFormaPagamentoEntity)
                .toList();

        when(formaPagamentoRepository.findAll()).thenReturn(formaPagamentoEntities);
        when(formaPagamentoEntityMapper.toFormaPagamento(formaPagamentoEntities.get(0))).thenReturn(formasPagamento.get(0));
        when(formaPagamentoEntityMapper.toFormaPagamento(formaPagamentoEntities.get(1))).thenReturn(formasPagamento.get(1));

        // Act
        Optional<List<FormaPagamento>> formasPagamentoConsultadas = formaPagamentoAdapter.listar();

        // Assert
        assertTrue(formasPagamentoConsultadas.isPresent());
        assertEquals(formasPagamento, formasPagamentoConsultadas.get());
        verify(formaPagamentoRepository).findAll();
        verify(formaPagamentoEntityMapper).toFormaPagamento(formaPagamentoEntities.get(0));
        verify(formaPagamentoEntityMapper).toFormaPagamento(formaPagamentoEntities.get(1));
    }

    @Test
    @DisplayName("Deve consultar forma de pagamento por nome")
    void consultarPorNome() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, PIX, false);
        FormaPagamentoEntity formaPagamentoEntity = toFormaPagamentoEntity(formaPagamento);

        when(formaPagamentoRepository.findByNome(PIX.toUpperCase())).thenReturn(List.of(formaPagamentoEntity));
        when(formaPagamentoEntityMapper.toFormaPagamento(formaPagamentoEntity)).thenReturn(formaPagamento);

        // Act
        Optional<FormaPagamento> formaPagamentoConsultada = formaPagamentoAdapter.consultarPorNome(PIX);

        // Assert
        assertTrue(formaPagamentoConsultada.isPresent());
        assertEquals(formaPagamento, formaPagamentoConsultada.get());
        verify(formaPagamentoRepository).findByNome(PIX.toUpperCase());
        verify(formaPagamentoEntityMapper).toFormaPagamento(formaPagamentoEntity);
    }

    private FormaPagamento getFormaPagamento(Long id, String nome, Boolean externo) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(id);
        formaPagamento.setNome(nome);
        formaPagamento.setExterno(externo);
        formaPagamento.setAtivo(true);
        return formaPagamento;
    }

    public FormaPagamento toFormaPagamento(FormaPagamentoEntity formaPagamentoEntity) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(formaPagamentoEntity.getId());
        formaPagamento.setNome(formaPagamentoEntity.getNome());
        formaPagamento.setExterno(formaPagamentoEntity.isExterno());
        formaPagamento.setAtivo(formaPagamentoEntity.isAtivo());
        return formaPagamento;
    }

    public FormaPagamentoEntity toFormaPagamentoEntity(FormaPagamento formaPagamento) {
        FormaPagamentoEntity formaPagamentoEntity = new FormaPagamentoEntity();
        formaPagamentoEntity.setId(formaPagamento.getId());
        formaPagamentoEntity.setNome(formaPagamento.getNome());
        formaPagamentoEntity.setExterno(formaPagamento.getExterno());
        formaPagamentoEntity.setAtivo(formaPagamento.getAtivo());
        return formaPagamentoEntity;
    }
}