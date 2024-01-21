package br.com.fiap.fasteats.core.validator.impl;

import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PedidoNotFound;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_AGUARDANDO_PAGAMENTO;
import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_PAGO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Realizar Pagamento Validator")
class RealizarPagamentoValidatorImplUnitTest {
    @Mock
    private PedidoOutputPort pedidoOutputPort;
    @InjectMocks
    private RealizarPagamentoValidatorImpl realizarPagamentoValidatorImpl;
    private AutoCloseable openMocks;
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
    @DisplayName("Deve validar status pedido")
    void validarStatusPedido() {
        // Arrange
        Pedido pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_AGUARDANDO_PAGAMENTO);

        when(pedidoOutputPort.consultar(PEDIDO_ID)).thenReturn(java.util.Optional.of(pedido));

        // Act
        realizarPagamentoValidatorImpl.validarStatusPedido(PEDIDO_ID);

        // Assert
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao validar status pedido com pedido não encontrado")
    void validarStatusPedidoNaoEncontrado() {
        // Act & Assert
        assertThrows(PedidoNotFound.class, () -> realizarPagamentoValidatorImpl.validarStatusPedido(PEDIDO_ID));
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao validar status pedido com pedido já pago")
    void validarStatusPedidoJaPago() {
        // Arrange
        Pedido pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_PAGO);

        when(pedidoOutputPort.consultar(PEDIDO_ID)).thenReturn(java.util.Optional.of(pedido));

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> realizarPagamentoValidatorImpl.validarStatusPedido(PEDIDO_ID));
        verify(pedidoOutputPort).consultar(PEDIDO_ID);
    }

    private Pedido getPedido(Long pedidoId, String statusPedido) {
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatusPedido(statusPedido);
        return pedido;
    }
}