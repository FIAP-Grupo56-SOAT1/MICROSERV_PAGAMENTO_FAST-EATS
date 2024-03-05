package br.com.fiap.fasteats.core.usecase.impl.unit;

import br.com.fiap.fasteats.core.domain.exception.FormaPagamentoNotFound;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.MetodoPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.GerarPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.GerarPagamentoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Gerar Pagamento")
class GerarPagamentoUseCaseUnitTest {
    @Mock
    private FormaPagamentoInputPort formaPagamentoInputPort;
    @Mock
    private GerarPagamentoValidator gerarPagamentoValidator;
    @Mock
    private MetodoPagamentoInputPort metodoPagamentoInputPort;
    @InjectMocks
    private GerarPagamentoUseCase gerarPagamentoUseCase;

    private final Long PAGAMENTO_ID = 1L;
    private  final Long PEDIDO_ID = 1L;
    private final Long FORMA_PAGAMENTO_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve gerar um pagamento interno PIX")
    void gerarInterno() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, PIX, false);
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, false);

        when(formaPagamentoInputPort.consultar(FORMA_PAGAMENTO_ID)).thenReturn(formaPagamento);
        when(metodoPagamentoInputPort.pix(PEDIDO_ID)).thenReturn(pagamento);

        // Act
        Pagamento result = gerarPagamentoUseCase.gerar(PEDIDO_ID, FORMA_PAGAMENTO_ID);

        // Assert
        assertEquals(PIX, result.getFormaPagamento().getNome());
        verify(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        verify(gerarPagamentoValidator).validarFormaPagamento(formaPagamento.getId());
        verify(formaPagamentoInputPort).consultar(FORMA_PAGAMENTO_ID);
        verify(metodoPagamentoInputPort).pix(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve gerar um pagamento externo Mercado Pago")
    void gerarExterno() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, MERCADO_PAGO, true);
        Pagamento pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID, FORMA_PAGAMENTO_ID, true);

        when(formaPagamentoInputPort.consultar(FORMA_PAGAMENTO_ID)).thenReturn(formaPagamento);
        when(metodoPagamentoInputPort.mercadoPago(PEDIDO_ID)).thenReturn(pagamento);

        // Act
        Pagamento result = gerarPagamentoUseCase.gerar(PEDIDO_ID, FORMA_PAGAMENTO_ID);

        // Assert
        assertEquals(MERCADO_PAGO, result.getFormaPagamento().getNome());
        verify(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        verify(gerarPagamentoValidator).validarFormaPagamento(formaPagamento.getId());
        verify(formaPagamentoInputPort).consultar(FORMA_PAGAMENTO_ID);
        verify(metodoPagamentoInputPort).mercadoPago(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar gerar um pagamento com Forma de Pagamento não cadastrada")
    void gerarFormaPagamentoNaoCadastrada() {
        // Arrange
        FormaPagamento formaPagamento = getFormaPagamento(FORMA_PAGAMENTO_ID, "TESTE", false);

        when(formaPagamentoInputPort.consultar(FORMA_PAGAMENTO_ID)).thenReturn(formaPagamento);

        // Act & Assert
        assertThrows(FormaPagamentoNotFound.class, () -> gerarPagamentoUseCase.gerar(PEDIDO_ID, FORMA_PAGAMENTO_ID));
        verify(gerarPagamentoValidator).validarPedidoStatus(PEDIDO_ID);
        verify(gerarPagamentoValidator).validarFormaPagamento(formaPagamento.getId());
        verify(formaPagamentoInputPort).consultar(FORMA_PAGAMENTO_ID);
    }

    private Pagamento getPagamento(Long pagamentoId, Long pedidoId, Long formaPagamentoId, boolean externo) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamento(formaPagamentoId, externo ? MERCADO_PAGO : PIX, externo));
        pagamento.setValor(100.0);
        pagamento.setIdPagamentoExterno(null);
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
}