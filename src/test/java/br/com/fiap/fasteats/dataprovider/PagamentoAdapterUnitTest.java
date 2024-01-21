package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.dataprovider.repository.PagamentoRepository;
import br.com.fiap.fasteats.dataprovider.repository.entity.PagamentoEntity;
import br.com.fiap.fasteats.dataprovider.repository.mapper.PagamentoEntityMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_EM_PROCESSAMENTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Pagamento Adapter")
class PagamentoAdapterUnitTest {
    @Mock
    private PagamentoRepository pagamentoRepository;
    @Mock
    private PagamentoEntityMapper pagamentoEntityMapper;
    @InjectMocks
    private PagamentoAdapter pagamentoAdapter;
    AutoCloseable openMocks;
    private final Long PAGAMENTO_ID = 1L;
    private final Long PEDIDO_ID = 1L;
    private final Long FORMA_PAGAMENTO_ID = 1L;
    private final Long STATUS_PAGAMENTO_ID = 1L;

    @BeforeEach
    void setUp() {
        openMocks = org.mockito.MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("Deve listar pagamentos")
    void listar() {
        // Arrange
        List<Pagamento> listPagamentos = List.of(
                getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID),
                getPagamento(PAGAMENTO_ID + 1, PEDIDO_ID + 1, FORMA_PAGAMENTO_ID + 1)
        );

        List<PagamentoEntity> listPagamentosEntities = listPagamentos.stream()
                .map(pagamentoEntityMapper::toPagamentoEntity)
                .toList();

        when(pagamentoRepository.findAll()).thenReturn(listPagamentosEntities);
        when(pagamentoEntityMapper.toPagamento(listPagamentosEntities.get(0))).thenReturn(listPagamentos.get(0));
        when(pagamentoEntityMapper.toPagamento(listPagamentosEntities.get(1))).thenReturn(listPagamentos.get(1));

        // Act
        List<Pagamento> listPagamentosRetornados = pagamentoAdapter.listar();

        // Assert
        assertEquals(listPagamentos.size(), listPagamentosRetornados.size());
        verify(pagamentoRepository).findAll();
    }

    @Test
    @DisplayName("Deve consultar pagamento")
    void consultar() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID);
        PagamentoEntity pagamentoEntity = toPagamentoEntity(pagamento);

        when(pagamentoRepository.findById(PAGAMENTO_ID)).thenReturn(Optional.ofNullable(pagamentoEntity));
        when(pagamentoEntityMapper.toPagamento(pagamentoEntity)).thenReturn(pagamento);

        // Act
        Optional<Pagamento> pagamentoRetornado = pagamentoAdapter.consultar(PAGAMENTO_ID);

        // Assert
        assertTrue(pagamentoRetornado.isPresent());
        assertEquals(pagamento, pagamentoRetornado.get());
        verify(pagamentoRepository).findById(PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve consultar pagamento por pedido id")
    void consultarPorPedidoId() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID);
        PagamentoEntity pagamentoEntity = toPagamentoEntity(pagamento);

        when(pagamentoRepository.findFirstByPedidoIdOrderByDataHoraCriadoDesc(PEDIDO_ID)).thenReturn(Optional.ofNullable(pagamentoEntity));
        when(pagamentoEntityMapper.toPagamento(pagamentoEntity)).thenReturn(pagamento);

        // Act
        Optional<Pagamento> pagamentoRetornado = pagamentoAdapter.consultarPorPedidoId(PEDIDO_ID);

        // Assert
        assertTrue(pagamentoRetornado.isPresent());
        assertEquals(pagamento, pagamentoRetornado.get());
        verify(pagamentoRepository).findFirstByPedidoIdOrderByDataHoraCriadoDesc(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve salvar pagamento")
    void salvarPagamento() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID);
        PagamentoEntity pagamentoEntity = toPagamentoEntity(pagamento);

        when(pagamentoEntityMapper.toPagamentoEntity(pagamento)).thenReturn(pagamentoEntity);
        when(pagamentoRepository.saveAndFlush(pagamentoEntity)).thenReturn(pagamentoEntity);
        when(pagamentoEntityMapper.toPagamento(pagamentoEntity)).thenReturn(pagamento);

        // Act
        Pagamento pagamentoRetornado = pagamentoAdapter.salvarPagamento(pagamento);

        // Assert
        assertEquals(pagamento, pagamentoRetornado);
        verify(pagamentoEntityMapper).toPagamentoEntity(pagamento);
        verify(pagamentoRepository).saveAndFlush(pagamentoEntity);
        verify(pagamentoEntityMapper).toPagamento(pagamentoEntity);
    }

    @Test
    @DisplayName("Deve consultar pagamento por id pagamento externo")
    void consultarPorIdPagamentoExterno() {
        // Arrange
        final long PAGAMENTO_EXTERNO_ID = 1234L;
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID);
        pagamento.setIdPagamentoExterno(PAGAMENTO_EXTERNO_ID);
        PagamentoEntity pagamentoEntity = toPagamentoEntity(pagamento);


        when(pagamentoRepository.findPagamentoByIdPagamentoExterno(PAGAMENTO_EXTERNO_ID)).thenReturn(Optional.ofNullable(pagamentoEntity));
        when(pagamentoEntityMapper.toPagamento(pagamentoEntity)).thenReturn(pagamento);

        // Act
        Optional<Pagamento> pagamentoRetornado = pagamentoAdapter.consultarPorIdPagamentoExterno(PAGAMENTO_EXTERNO_ID);

        // Assert
        assertTrue(pagamentoRetornado.isPresent());
        assertEquals(pagamento, pagamentoRetornado.get());
        verify(pagamentoRepository).findPagamentoByIdPagamentoExterno(PAGAMENTO_EXTERNO_ID);
    }

    private Pagamento getPagamento(Long pagamentoId, Long pedidoId, Long formaPagamentoId) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamento(formaPagamentoId));
        pagamento.setStatusPagamento(getStatusPagamento(STATUS_PAGAMENTO_ID));
        pagamento.setValor(100.0);
        pagamento.setDataHoraCriado(LocalDateTime.now());
        pagamento.setQrCode(null);
        pagamento.setUrlPagamento(null);
        return pagamento;
    }

    private FormaPagamento getFormaPagamento(Long formaPagamentoId) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(formaPagamentoId);
        formaPagamento.setNome(PIX);
        formaPagamento.setExterno(false);
        formaPagamento.setAtivo(true);
        return formaPagamento;
    }

    private StatusPagamento getStatusPagamento(Long statusPagamentoId) {
        StatusPagamento statusPagamento = new StatusPagamento();
        statusPagamento.setId(statusPagamentoId);
        statusPagamento.setNome(STATUS_EM_PROCESSAMENTO);
        return statusPagamento;
    }

    public PagamentoEntity toPagamentoEntity(Pagamento pagamento) {
        PagamentoEntity pagamentoEntity = new PagamentoEntity();
        pagamentoEntity.setId(pagamento.getId());
        pagamentoEntity.setValor(pagamento.getValor());
        pagamentoEntity.setPedidoId(pagamento.getPedidoId());
        pagamentoEntity.setDataHoraCriado(pagamento.getDataHoraCriado());
        pagamentoEntity.setDataHoraProcessamento(pagamento.getDataHoraProcessamento());
        pagamentoEntity.setDataHoraFinalizado(pagamento.getDataHoraFinalizado());
        pagamentoEntity.setIdPagamentoExterno(pagamento.getIdPagamentoExterno());
        pagamentoEntity.setUrlPagamento(pagamento.getUrlPagamento());
        pagamentoEntity.setQrCode(pagamento.getQrCode());
        return pagamentoEntity;
    }
}