package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.dataprovider.client.CozinhaPedidoIntegration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

@DisplayName("Teste Unitário - Cozinha Pedido Adapter")
class CozinhaPedidoAdapterUnitTest {
    @Mock
    private CozinhaPedidoIntegration cozinhaPedidoIntegration;
    @InjectMocks
    private CozinhaPedidoAdapter cozinhaPedidoAdapter;
    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = org.mockito.MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("Deve receber pedido na cozinha")
    void receberPedido() {
        // Arrange
        final Long PEDIDO_ID = 1L;

        // Act
        cozinhaPedidoAdapter.receberPedido(PEDIDO_ID);

        // Assert
        verify(cozinhaPedidoIntegration).receberPedido(PEDIDO_ID);
    }
}