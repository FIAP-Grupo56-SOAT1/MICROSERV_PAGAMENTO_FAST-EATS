package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.dataprovider.client.exception.MicroservicoPedidoException;
import br.com.fiap.fasteats.dataprovider.client.response.ClienteResponse;
import br.com.fiap.fasteats.dataprovider.client.response.PedidoResponse;
import br.com.fiap.fasteats.dataprovider.client.response.ProdutoPedidoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.com.fiap.fasteats.core.constants.StatusPedidoConstants.STATUS_PEDIDO_AGUARDANDO_PAGAMENTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - Pedido Integration")
class PedidoIntegrationImplUnitTest {
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private PedidoIntegrationImpl pedidoIntegrationImpl;
    AutoCloseable openMocks;
    private final Long PEDIDO_ID = 1L;
    private final Long NOVO_STATUS_ID = 2L;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("Deve consultar pedido com sucesso")
    void consultar() {
        // Arrange
        PedidoResponse pedidoResponse = getPedidoResponse(PEDIDO_ID, STATUS_PEDIDO_AGUARDANDO_PAGAMENTO);
        when(restTemplate.getForObject(anyString(), eq(PedidoResponse.class), eq(PEDIDO_ID))).thenReturn(pedidoResponse);

        // Act
        Optional<PedidoResponse> resultado = pedidoIntegrationImpl.consultar(PEDIDO_ID);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(pedidoResponse, resultado.get());
    }

    @Test
    @DisplayName("Deve lançar exceção ao consultar pedido")
    void consultarException() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(PedidoResponse.class), eq(PEDIDO_ID))).thenThrow(new RuntimeException("Erro na comunicação"));

        // Act & Assert
        assertThrows(MicroservicoPedidoException.class, () -> pedidoIntegrationImpl.consultar(PEDIDO_ID));
    }

    @Test
    @DisplayName("Deve atualizar status do pedido com sucesso")
    void atualizarStatus() {
        // Act
       pedidoIntegrationImpl.atualizarStatus(PEDIDO_ID, NOVO_STATUS_ID);

        // Assert
        verify(restTemplate).put(anyString(), isNull(), eq(PEDIDO_ID), eq(NOVO_STATUS_ID));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar status do pedido")
    void atualizarStatusException() {
        // Arrange
        doThrow(new RuntimeException("Erro na comunicação")).when(restTemplate)
                .put(anyString(), isNull(), eq(PEDIDO_ID), eq(NOVO_STATUS_ID));

        // Act & Assert
        assertThrows(MicroservicoPedidoException.class, () -> pedidoIntegrationImpl.atualizarStatus(PEDIDO_ID, NOVO_STATUS_ID));
    }

    private PedidoResponse getPedidoResponse(Long pedidoId, String statusPedido) {
        PedidoResponse pedidoResponse = new PedidoResponse();
        pedidoResponse.setId(pedidoId);
        pedidoResponse.setCliente(getClienteResponse());
        pedidoResponse.setStatusPedido(statusPedido);
        pedidoResponse.setDataHoraCriado(LocalDateTime.MIN);
        pedidoResponse.setDataHoraRecebimento(LocalDateTime.now());
        pedidoResponse.setDataHoraFinalizado(LocalDateTime.MAX);
        pedidoResponse.setValor(100.0);
        pedidoResponse.setQrCode("QrCode");
        pedidoResponse.setUrlPagamento("UrlPagamento");
        pedidoResponse.setProdutos(List.of(
                getProdutoPedidoResponse(1L, "Produto 1", 10.0),
                getProdutoPedidoResponse(2L, "Produto 2", 20.0),
                getProdutoPedidoResponse(3L, "Produto 3", 30.0)

        ));
        return pedidoResponse;
    }

    private ClienteResponse getClienteResponse() {
        ClienteResponse clienteResponse = new ClienteResponse();
        clienteResponse.setCpf("12345678901");
        clienteResponse.setPrimeiroNome("Cliente");
        clienteResponse.setUltimoNome("Teste");
        clienteResponse.setEmail("teste@teste.com");
        clienteResponse.setAtivo(true);
        return clienteResponse;
    }

    private ProdutoPedidoResponse getProdutoPedidoResponse(Long produtoId, String nomeProduto, Double valorProduto) {
        ProdutoPedidoResponse produtoPedidoResponse = new ProdutoPedidoResponse();
        produtoPedidoResponse.setIdProduto(produtoId);
        produtoPedidoResponse.setNomeProduto(nomeProduto);
        produtoPedidoResponse.setDescricaoProduto("teste");
        produtoPedidoResponse.setQuantidade(1);
        produtoPedidoResponse.setValor(valorProduto);
        return produtoPedidoResponse;
    }
}