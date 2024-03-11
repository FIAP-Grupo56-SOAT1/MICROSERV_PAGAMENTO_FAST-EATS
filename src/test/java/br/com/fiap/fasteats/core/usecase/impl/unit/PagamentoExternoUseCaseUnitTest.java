package br.com.fiap.fasteats.core.usecase.impl.unit;

import br.com.fiap.fasteats.core.dataprovider.CancelarPagamentoOutputPort;
import br.com.fiap.fasteats.core.dataprovider.PagamentoExternoOutputPort;
import br.com.fiap.fasteats.core.dataprovider.RealizarPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.PagamentoExternoUseCase;
import br.com.fiap.fasteats.core.validator.CancelarPagamentoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@DisplayName("Teste Unitário - Pagamento Externo")
class PagamentoExternoUseCaseUnitTest {
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private PagamentoExternoOutputPort pagamentoExternoOutputPort;
    @Mock
    private RealizarPagamentoOutputPort realizarPagamentoOutputPort;
    @Mock
    private AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    @Mock
    private CancelarPagamentoOutputPort cancelarPagamentoOutputPort;
    @Mock
    private CancelarPagamentoValidator cancelarPagamentoValidator;
    @InjectMocks
    private PagamentoExternoUseCase pagamentoExternoUseCase;
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
    @DisplayName("Deve atualizar o status do pagamento externo para pago")
    void atualizarPagamentoParaPago() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, PAGAMENTO_EXTERNO_ID, true);
        PagamentoExterno pagamentoExterno = getPagamentoExterno(PAGAMENTO_EXTERNO_ID, "qrCode", "urlPagamento");
        Pagamento pagamentoAtualizadoExterno = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, FORMA_PAGAMENTO_ID, true);
        pagamentoAtualizadoExterno.setStatusPagamento(getStatusPagamento(2L, STATUS_PAGO));

        when(pagamentoInputPort.consultarPorIdPagamentoExterno(pagamento.getIdPagamentoExterno())).thenReturn(pagamento);
        when(pagamentoExternoOutputPort.recuperarPagamentoDePagamentoExterno(pagamentoExterno)).thenReturn(pagamentoAtualizadoExterno);
        when(realizarPagamentoOutputPort.realizarPagamento(pagamento.getId(), pagamento.getPedidoId())).thenReturn(pagamentoAtualizadoExterno);

        // Act
        Pagamento result = pagamentoExternoUseCase.atualizarPagamento(pagamentoExterno);

        // Assert
        assertEquals(STATUS_PAGO, result.getStatusPagamento().getNome());
        verify(pagamentoInputPort).consultarPorIdPagamentoExterno(pagamento.getIdPagamentoExterno());
        verify(pagamentoExternoOutputPort).recuperarPagamentoDePagamentoExterno(pagamentoExterno);
        verify(realizarPagamentoOutputPort).realizarPagamento(pagamento.getId(), pagamento.getPedidoId());
    }

    @Test
    @DisplayName("Deve atualizar o status do pagamento externo para recusado")
    void atualizarPagamentoParaRecusado() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, PAGAMENTO_EXTERNO_ID, true);
        PagamentoExterno pagamentoExterno = getPagamentoExterno(PAGAMENTO_EXTERNO_ID, "qrCode", "urlPagamento");
        Pagamento pagamentoAtualizadoExterno = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, FORMA_PAGAMENTO_ID, true);
        pagamentoAtualizadoExterno.setStatusPagamento(getStatusPagamento(2L, STATUS_RECUSADO));

        when(pagamentoInputPort.consultarPorIdPagamentoExterno(pagamento.getIdPagamentoExterno())).thenReturn(pagamento);
        when(pagamentoExternoOutputPort.recuperarPagamentoDePagamentoExterno(pagamentoExterno)).thenReturn(pagamentoAtualizadoExterno);
        when(alterarPagamentoStatusInputPort.recusado(pagamento.getPedidoId())).thenReturn(pagamentoAtualizadoExterno);

        // Act
        Pagamento result = pagamentoExternoUseCase.atualizarPagamento(pagamentoExterno);

        // Assert
        assertEquals(STATUS_RECUSADO, result.getStatusPagamento().getNome());
        verify(pagamentoInputPort).consultarPorIdPagamentoExterno(pagamento.getIdPagamentoExterno());
        verify(pagamentoExternoOutputPort).recuperarPagamentoDePagamentoExterno(pagamentoExterno);
        verify(alterarPagamentoStatusInputPort).recusado(pagamento.getPedidoId());
    }

    @Test
    @DisplayName("Deve atualizar o status do pagamento externo para cancelado")
    void atualizarPagamentoParaCancelado() {
        // Arrange
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, PAGAMENTO_EXTERNO_ID, true);
        PagamentoExterno pagamentoExterno = getPagamentoExterno(PAGAMENTO_EXTERNO_ID, "qrCode", "urlPagamento");
        Pagamento pagamentoAtualizadoExterno = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, FORMA_PAGAMENTO_ID, true);
        pagamentoAtualizadoExterno.setStatusPagamento(getStatusPagamento(2L, STATUS_CANCELADO));

        when(pagamentoInputPort.consultarPorIdPagamentoExterno(pagamento.getIdPagamentoExterno())).thenReturn(pagamento);
        when(pagamentoExternoOutputPort.recuperarPagamentoDePagamentoExterno(pagamentoExterno)).thenReturn(pagamentoAtualizadoExterno);
        when(cancelarPagamentoOutputPort.cancelar(pagamento.getId(), pagamento.getPedidoId())).thenReturn(pagamentoAtualizadoExterno);

        // Act
        Pagamento result = pagamentoExternoUseCase.atualizarPagamento(pagamentoExterno);

        // Assert
        assertEquals(STATUS_CANCELADO, result.getStatusPagamento().getNome());
        verify(pagamentoInputPort).consultarPorIdPagamentoExterno(pagamento.getIdPagamentoExterno());
        verify(pagamentoExternoOutputPort).recuperarPagamentoDePagamentoExterno(pagamentoExterno);
        verify(cancelarPagamentoValidator).validarCancelarPagamento(pagamento.getPedidoId());
        verify(cancelarPagamentoOutputPort).cancelar(pagamento.getId(), pagamento.getPedidoId());
    }

    @Test
    @DisplayName("Não deve atualizar o status do pagamento externo pois o status não foi mapeado")
    void atualizarParaStatusPagamentoNaoMapeado() {
        // Arrange
        String statusPagamentoNaoMapeado = "TESTE";
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, PAGAMENTO_EXTERNO_ID, true);
        PagamentoExterno pagamentoExterno = getPagamentoExterno(PAGAMENTO_EXTERNO_ID, "qrCode", "urlPagamento");
        Pagamento pagamentoAtualizadoExterno = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, FORMA_PAGAMENTO_ID, true);
        pagamentoAtualizadoExterno.setStatusPagamento(getStatusPagamento(2L, statusPagamentoNaoMapeado));

        when(pagamentoInputPort.consultarPorIdPagamentoExterno(pagamento.getIdPagamentoExterno())).thenReturn(pagamento);
        when(pagamentoExternoOutputPort.recuperarPagamentoDePagamentoExterno(pagamentoExterno)).thenReturn(pagamentoAtualizadoExterno);

        // Act
        Pagamento result = pagamentoExternoUseCase.atualizarPagamento(pagamentoExterno);

        // Assert
        assertNull(result.getStatusPagamento());
        verify(pagamentoInputPort).consultarPorIdPagamentoExterno(pagamento.getIdPagamentoExterno());
        verify(pagamentoExternoOutputPort).recuperarPagamentoDePagamentoExterno(pagamentoExterno);
    }

    @Test
    @DisplayName("Deve cancelar o pagamento externo")
    void cancelarPagamentoExterno() {
        // Act
        pagamentoExternoUseCase.cancelarPagamentoExterno(PAGAMENTO_EXTERNO_ID);

        // Assert
        verify(pagamentoExternoOutputPort).cancelarPagamento(PAGAMENTO_EXTERNO_ID);
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
        pagamentoExterno.setMensagem("mensagem");
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