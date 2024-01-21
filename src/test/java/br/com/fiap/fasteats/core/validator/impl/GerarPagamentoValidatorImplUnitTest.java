package br.com.fiap.fasteats.core.validator.impl;

import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_CANCELADO;
import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_CRIADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Gerar Pagamento Validator")
class GerarPagamentoValidatorImplUnitTest {
    @Mock
    private FormaPagamentoInputPort formaPagamentoInputPort;
    @Mock
    private PedidoOutputPort pedidoOutputPort;
    @InjectMocks
    private GerarPagamentoValidatorImpl gerarPagamentoValidatorImpl;
    AutoCloseable openMocks;
    private final Long PEDIDO_ID = 1L;

    @BeforeEach
    void setUp() {
        openMocks = org.mockito.MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("Deve validar pedido status")
    void validarPedidoStatus() {
        // Arrange
        Pedido pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_CRIADO);

        when(pedidoOutputPort.consultar(PEDIDO_ID)).thenReturn(java.util.Optional.of(pedido));

        // Act
        gerarPagamentoValidatorImpl.validarPedidoStatus(PEDIDO_ID);

        // Assert
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao validar pedido status com pedido cancelado")
    void validarPedidoStatusPedidoCancelado() {
        // Arrange
        Pedido pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_CANCELADO);

        when(pedidoOutputPort.consultar(PEDIDO_ID)).thenReturn(java.util.Optional.of(pedido));

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> gerarPagamentoValidatorImpl.validarPedidoStatus(PEDIDO_ID));
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve validar forma pagamento")
    void validarFormaPagamento() {
        // Arrange
        Long formaPagamentoId = 1L;
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(formaPagamentoId);
        formaPagamento.setNome(PIX);
        formaPagamento.setAtivo(true);
        formaPagamento.setExterno(false);

        when(formaPagamentoInputPort.consultar(formaPagamentoId)).thenReturn(formaPagamento);

        // Act
        gerarPagamentoValidatorImpl.validarFormaPagamento(formaPagamentoId);

        // Assert
        verify(formaPagamentoInputPort).consultar(formaPagamentoId);
    }

    private Pedido getPedido(Long pedidoId, String statusPedido) {
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatusPedido(statusPedido);
        return pedido;
    }
}