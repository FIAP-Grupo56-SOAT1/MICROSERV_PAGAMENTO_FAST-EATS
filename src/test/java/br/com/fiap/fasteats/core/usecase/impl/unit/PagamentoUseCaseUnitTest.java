package br.com.fiap.fasteats.core.usecase.impl.unit;

import br.com.fiap.fasteats.core.dataprovider.PagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PagamentoNotFound;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.core.validator.PagamentoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@DisplayName("Teste Unitário - Pagamento")
class PagamentoUseCaseUnitTest {
    @Mock
    private PagamentoOutputPort pagamentoOutputPort;
    @Mock
    private StatusPagamentoInputPort statusPagamentoInputPort;
    @Mock
    private PagamentoValidator pagamentoValidator;
    @InjectMocks
    private PagamentoUseCase pagamentoUseCase;
    private final Long PAGAMENTO_ID = 1L;
    private final Long PAGAMENTO_EXTERNO_ID = 1234567890L;
    private final Long PEDIDO_ID = 1L;
    private final Long FORMA_PAGAMENTO_ID = 1L;
    private final Long STATUS_PAGAMENTO_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar uma lista de pagamentos")
    void listar() {
        // Arrange
        List<Pagamento> pagamentos = List.of(
                getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false),
                getPagamento(PAGAMENTO_ID + 1, PEDIDO_ID, FORMA_PAGAMENTO_ID, false),
                getPagamento(PAGAMENTO_ID + 2, PEDIDO_ID, FORMA_PAGAMENTO_ID, false)
        );

        when(pagamentoOutputPort.listar()).thenReturn(pagamentos);

        // Act
        List<Pagamento> result = pagamentoUseCase.listar();

        // Assert
        assertEquals(pagamentos, result);
        verify(pagamentoOutputPort).listar();
    }

    @Test
    @DisplayName("Deve retornar um pagamento por id do pedido")
    void consultarPorIdPedido() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);

        when(pagamentoOutputPort.consultarPorPedidoId(PEDIDO_ID)).thenReturn(Optional.of(pagamento));

        // Act
        Pagamento result = pagamentoUseCase.consultarPorIdPedido(PEDIDO_ID);

        // Assert
        assertEquals(pagamento, result);
        verify(pagamentoOutputPort).consultarPorPedidoId(pagamento.getPedidoId());
    }

    @Test
    @DisplayName("Deve apresentar erro ao consultar um pagamento por id de pedido não encontrado")
    void consultarPorIdPedidoNaoEncontrado() {
        // Act & Assert
        assertThrows(PagamentoNotFound.class, () -> pagamentoUseCase.consultarPorIdPedido(PEDIDO_ID));
        verify(pagamentoOutputPort).consultarPorPedidoId(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve criar um pagamento")
    void criar() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);
        pagamento.setDataHoraCriado(null);
        pagamento.setStatusPagamento(null);
        pagamento.setDataHoraProcessamento(null);

        when(pagamentoOutputPort.salvarPagamento(pagamento)).thenReturn(pagamento);
        when(statusPagamentoInputPort.consultarPorNome(STATUS_EM_PROCESSAMENTO))
                .thenReturn(getStatusPagamento(STATUS_PAGAMENTO_ID, STATUS_EM_PROCESSAMENTO));

        // Act
        Pagamento result = pagamentoUseCase.criar(pagamento);

        // Assert
        assertNotNull(result.getDataHoraCriado());
        assertNotNull(result.getDataHoraProcessamento());
        assertEquals(STATUS_EM_PROCESSAMENTO, result.getStatusPagamento().getNome());
        verify(pagamentoOutputPort).salvarPagamento(pagamento);
        verify(statusPagamentoInputPort).consultarPorNome(STATUS_EM_PROCESSAMENTO);
    }

    @Test
    @DisplayName("Deve atualizar um pagamento")
    void atualizar() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);

        when(pagamentoOutputPort.salvarPagamento(pagamento)).thenReturn(pagamento);

        // Act
        Pagamento result = pagamentoUseCase.atualizar(pagamento);

        // Assert
        assertEquals(pagamento, result);
        verify(pagamentoValidator).validarAlterarPagamento(pagamento.getId());
        verify(pagamentoOutputPort).salvarPagamento(pagamento);
    }

    @Test
    @DisplayName("Deve consultar um pagamento")
    void consultar() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);

        when(pagamentoOutputPort.consultar(PAGAMENTO_ID)).thenReturn(Optional.of(pagamento));

        // Act
        Pagamento result = pagamentoUseCase.consultar(PAGAMENTO_ID);

        // Assert
        assertEquals(pagamento, result);
        verify(pagamentoOutputPort).consultar(PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao consultar um pagamento não encontrado")
    void consultarNaoEncontrado() {
        // Act & Assert
        assertThrows(PagamentoNotFound.class, () -> pagamentoUseCase.consultar(PAGAMENTO_ID));
        verify(pagamentoOutputPort).consultar(PAGAMENTO_ID);
    }

    @Test
    @DisplayName("Deve consultar um pagamento por id de pagamento externo")
    void consultarPorIdPagamentoExterno() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, true);
        pagamento.setIdPagamentoExterno(PAGAMENTO_EXTERNO_ID);

        when(pagamentoOutputPort.consultarPorIdPagamentoExterno(PAGAMENTO_EXTERNO_ID)).thenReturn(Optional.of(pagamento));

        // Act
        Pagamento result = pagamentoUseCase.consultarPorIdPagamentoExterno(PAGAMENTO_EXTERNO_ID);

        // Assert
        assertEquals(PAGAMENTO_EXTERNO_ID, result.getIdPagamentoExterno());
        verify(pagamentoOutputPort).consultarPorIdPagamentoExterno(PAGAMENTO_EXTERNO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao consultar um pagamento por id de pagamento externo não encontrado")
    void consultarPorIdPagamentoExternoNaoEncontrado() {
        // Act & Assert
        assertThrows(PagamentoNotFound.class, () -> pagamentoUseCase.consultarPorIdPagamentoExterno(PAGAMENTO_EXTERNO_ID));
        verify(pagamentoOutputPort).consultarPorIdPagamentoExterno(PAGAMENTO_EXTERNO_ID);
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