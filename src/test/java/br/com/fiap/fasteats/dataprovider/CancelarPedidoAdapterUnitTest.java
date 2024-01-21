package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.AlterarPedidoStatusOutputPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

@DisplayName("Teste Unitário - Cancelar Pedido Adapter")
class CancelarPedidoAdapterUnitTest {
    @Mock
    private AlterarPedidoStatusOutputPort alterarPedidoStatusOutputPort;
    @InjectMocks
    private CancelarPedidoAdapter cancelarPedidoAdapter;
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
    @DisplayName("Deve cancelar pedido")
    void cancelar() {
        // Arrange
        final Long PEDIDO_ID = 1L;

        // Act
        cancelarPedidoAdapter.cancelar(PEDIDO_ID);

        // Assert
        verify(alterarPedidoStatusOutputPort).cancelado(PEDIDO_ID);
    }
}