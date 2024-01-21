package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.dataprovider.client.PedidoIntegration;
import br.com.fiap.fasteats.dataprovider.client.mapper.PedidoMapper;
import br.com.fiap.fasteats.dataprovider.client.response.PedidoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_AGUARDANDO_PAGAMENTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Pedido Adapter")
class PedidoAdapterUnitTest {
    @Mock
    private PedidoIntegration pedidoIntegration;
    @Mock
    private PedidoMapper pedidoMapper;
    @InjectMocks
    private PedidoAdapter pedidoAdapter;
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
    @DisplayName("Deve consultar pedido")
    void consultar() {
        // Arrange
        Pedido pedido = getPedido(PEDIDO_ID, STATUS_PEDIDO_AGUARDANDO_PAGAMENTO);
        PedidoResponse pedidoResponse = getPedidoResponse(PEDIDO_ID, STATUS_PEDIDO_AGUARDANDO_PAGAMENTO);

        when(pedidoIntegration.consultar(PEDIDO_ID)).thenReturn(Optional.of(pedidoResponse));
        when(pedidoMapper.toPedido(pedidoResponse)).thenReturn(toPedido(pedidoResponse));

        // Act
        Optional<Pedido> pedidoConsulta = pedidoAdapter.consultar(PEDIDO_ID);

        // Assert
        assertTrue(pedidoConsulta.isPresent());
        assertEquals(pedidoConsulta.get().getId(), pedido.getId());
        assertEquals(pedidoConsulta.get().getStatusPedido(), pedido.getStatusPedido());
        assertEquals(pedidoConsulta.get().getValor(), pedido.getValor());
        verify(pedidoIntegration).consultar(PEDIDO_ID);
    }

    private Pedido getPedido(Long pedidoId, String statusPedido) {
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatusPedido(statusPedido);
        pedido.setValor(100.0);
        pedido.setQrCode("QrCode");
        pedido.setUrlPagamento("UrlPagamento");
        return pedido;
    }

    private PedidoResponse getPedidoResponse(Long pedidoId, String statusPedido) {
        PedidoResponse pedidoResponse = new PedidoResponse();
        pedidoResponse.setId(pedidoId);
        pedidoResponse.setStatusPedido(statusPedido);
        pedidoResponse.setValor(100.0);
        pedidoResponse.setQrCode("QrCode");
        pedidoResponse.setUrlPagamento("UrlPagamento");
        return pedidoResponse;
    }

    public Pedido toPedido(PedidoResponse pedidoResponse) {
        Pedido pedido = new Pedido();
        pedido.setId(pedidoResponse.getId());
        pedido.setStatusPedido(pedidoResponse.getStatusPedido());
        pedido.setValor(pedidoResponse.getValor());
        pedido.setQrCode(pedidoResponse.getQrCode());
        pedido.setUrlPagamento(pedidoResponse.getUrlPagamento());
        return pedido;
    }
}