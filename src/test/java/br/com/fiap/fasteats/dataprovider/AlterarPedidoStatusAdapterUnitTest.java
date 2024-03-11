package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.domain.exception.StatusPedidoNotFound;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.domain.model.StatusPedido;
import br.com.fiap.fasteats.dataprovider.client.PedidoIntegration;
import br.com.fiap.fasteats.dataprovider.client.mapper.PedidoMapper;
import br.com.fiap.fasteats.dataprovider.client.mapper.StatusPedidoMapper;
import br.com.fiap.fasteats.dataprovider.client.response.PedidoResponse;
import br.com.fiap.fasteats.dataprovider.client.response.StatusPedidoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_CANCELADO;
import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_PAGO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Alterar Pedido Status Adapter")
class AlterarPedidoStatusAdapterUnitTest {
    /*@Mock
    private PedidoIntegration pedidoIntegration;
    @Mock
    private StatusPedidoIntegration statusPedidoIntegration;
    @Mock
    private StatusPedidoMapper statusPedidoMapper;
    @Mock
    private PedidoMapper pedidoMapper;
    @InjectMocks
    private AlterarPedidoStatusAdapter alterarPedidoStatusAdapter;
    AutoCloseable openMocks;
    private final Long STATUS_PEDIDO_ID = 1L;
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
    @DisplayName("Deve alterar status do pedido para pago")
    void pago() {
        // Arrange
        StatusPedidoResponse statusPedidoResponse = getStatusPedidoResponse(STATUS_PEDIDO_ID, STATUS_PEDIDO_PAGO);
        PedidoResponse pedidoResponse = getPedidoResponse(PEDIDO_ID, STATUS_PEDIDO_PAGO);
        StatusPedido statusPedido = new StatusPedido();
        statusPedido.setId(statusPedidoResponse.getId());
        statusPedido.setNome(statusPedidoResponse.getNome());


        when(statusPedidoIntegration.consultarPorNome(STATUS_PEDIDO_PAGO)).thenReturn(Optional.of(statusPedidoResponse));
        when(statusPedidoMapper.toStatusPedido(statusPedidoResponse)).thenReturn(statusPedido);
        when(pedidoIntegration.consultar(PEDIDO_ID)).thenReturn(Optional.of(pedidoResponse));
        when(pedidoMapper.toPedido(pedidoResponse)).thenReturn(toPedido(pedidoResponse));

        // Act
        //Optional<Pedido> pedido = alterarPedidoStatusAdapter.pago(PEDIDO_ID);

        // Assert
        //assertTrue(pedido.isPresent());
        //assertEquals(PEDIDO_ID, pedido.get().getId());
        //assertEquals(STATUS_PEDIDO_PAGO, pedido.get().getStatusPedido());
        verify(statusPedidoIntegration).consultarPorNome(STATUS_PEDIDO_PAGO);
        verify(statusPedidoMapper).toStatusPedido(statusPedidoResponse);
        verify(pedidoMapper).toPedido(pedidoResponse);
        verify(pedidoIntegration).atualizarStatus(PEDIDO_ID, STATUS_PEDIDO_ID);
        verify(pedidoIntegration).consultar(PEDIDO_ID);
    }

    @Test
    @DisplayName("Deve apresentar erro ao tentar alterar status pedido devido ao status não encontrado")
    void statusPedidoNaoEncontrado() {
        // Act & Assert
        assertThrows(StatusPedidoNotFound.class, () -> alterarPedidoStatusAdapter.pago(PEDIDO_ID));
        verify(statusPedidoIntegration).consultarPorNome(STATUS_PEDIDO_PAGO);
    }

    @Test
    @DisplayName("Deve alterar status do pedido para cancelado")
    void cancelado() {
        // Arrange
        StatusPedidoResponse statusPedidoResponse = getStatusPedidoResponse(STATUS_PEDIDO_ID, STATUS_PEDIDO_CANCELADO);
        PedidoResponse pedidoResponse = getPedidoResponse(PEDIDO_ID, STATUS_PEDIDO_CANCELADO);

        when(statusPedidoIntegration.consultarPorNome(STATUS_PEDIDO_CANCELADO)).thenReturn(Optional.of(statusPedidoResponse));
        when(statusPedidoMapper.toStatusPedido(statusPedidoResponse)).thenReturn(toStatusPedido(statusPedidoResponse));
        when(pedidoIntegration.consultar(PEDIDO_ID)).thenReturn(Optional.of(pedidoResponse));
        when(pedidoMapper.toPedido(pedidoResponse)).thenReturn(toPedido(pedidoResponse));

        // Act
        //Optional<Pedido> pedido = alterarPedidoStatusAdapter.cancelado(PEDIDO_ID);

        // Assert
        //assertTrue(pedido.isPresent());
        //assertEquals(PEDIDO_ID, pedido.get().getId());
        //assertEquals(STATUS_PEDIDO_CANCELADO, pedido.get().getStatusPedido());
        verify(statusPedidoIntegration).consultarPorNome(STATUS_PEDIDO_CANCELADO);
        verify(statusPedidoMapper).toStatusPedido(statusPedidoResponse);
        verify(pedidoMapper).toPedido(pedidoResponse);
        verify(pedidoIntegration).atualizarStatus(PEDIDO_ID, STATUS_PEDIDO_ID);
        verify(pedidoIntegration).consultar(PEDIDO_ID);
    }

    private StatusPedidoResponse getStatusPedidoResponse(Long id, String nome) {
        StatusPedidoResponse statusPedidoResponse = new StatusPedidoResponse();
        statusPedidoResponse.setId(id);
        statusPedidoResponse.setNome(nome);
        return statusPedidoResponse;
    }

    private PedidoResponse getPedidoResponse(Long pedidoId, String statusPedido) {
        PedidoResponse pedido = new PedidoResponse();
        pedido.setId(pedidoId);
        pedido.setStatusPedido(statusPedido);
        return pedido;
    }

    public StatusPedido toStatusPedido(StatusPedidoResponse statusPedidoResponse) {
        return new StatusPedido(statusPedidoResponse.getId(), statusPedidoResponse.getNome());
    }

    public Pedido toPedido(PedidoResponse pedidoResponse) {
        Pedido pedido = new Pedido();
        pedido.setId(pedidoResponse.getId());
        pedido.setStatusPedido(pedidoResponse.getStatusPedido());
        pedido.setValor(pedidoResponse.getValor());
        pedido.setQrCode(pedidoResponse.getQrCode());
        pedido.setUrlPagamento(pedidoResponse.getUrlPagamento());
        return pedido;
    }*/
}