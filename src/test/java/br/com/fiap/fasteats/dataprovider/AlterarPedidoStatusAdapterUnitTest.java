package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.dataprovider.client.PedidoIntegration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

@DisplayName("Teste Unitário - Alterar Pedido Status Adapter")
class AlterarPedidoStatusAdapterUnitTest {
    @Mock
    private PedidoIntegration pedidoIntegration;
    @InjectMocks
    private AlterarPedidoStatusAdapter alterarPedidoStatusAdapter;
    AutoCloseable openMocks;
    private final Long PEDIDO_ID = 1L;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("Deve alterar status do pedido para criado")
    void criado() {
        alterarPedidoStatusAdapter.criado(PEDIDO_ID);
        verify(pedidoIntegration).pedidoCriado(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve alterar status do pedido para aguardando pagamento")
    void aguardandoPagamento() {
        alterarPedidoStatusAdapter.aguardandoPagamento(PEDIDO_ID);
        verify(pedidoIntegration).pedidoAguardandoPagamento(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve alterar status do pedido para pago")
    void pago() {
        alterarPedidoStatusAdapter.pago(PEDIDO_ID);
        verify(pedidoIntegration).pedidoPago(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve alterar status do pedido para cancelado")
    void cancelado() {
        alterarPedidoStatusAdapter.cancelado(PEDIDO_ID);
        verify(pedidoIntegration).pedidoCancelado(PEDIDO_ID);
    }
}