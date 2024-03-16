package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.dataprovider.client.exception.AwsSQSException;
import br.com.fiap.fasteats.dataprovider.client.request.CozinhaReceberPedidoRequest;
import com.google.gson.Gson;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - Cozinha Pedido Integration")
class CozinhaPedidoIntegrationImplUnitTest {
    @Mock
    private SqsTemplate sqsTemplate;
    @Mock
    private AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @InjectMocks
    private CozinhaPedidoIntegrationImpl cozinhaPedidoIntegration;
    AutoCloseable openMocks;
    private static final Long PEDIDO_ID = 1L;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("Deve receber um pedido na cozinha")
    void receberPedido() {
        // Arrange
        String jsonReq = new Gson().toJson(new CozinhaReceberPedidoRequest(PEDIDO_ID));

        // Act
        cozinhaPedidoIntegration.receberPedido(PEDIDO_ID);

        // Assert
        verify(sqsTemplate).send(any(), eq(jsonReq));
    }

    @Test
    @DisplayName("Deve lançar exceção ao falhar ao receber um pedido na cozinha")
    void falharAoReceberPedido() {
        // Arrange
        String jsonReq = new Gson().toJson(new CozinhaReceberPedidoRequest(PEDIDO_ID));
        doThrow(new RuntimeException("Erro ao tentar receber pedido")).when(sqsTemplate).send(any(), eq(jsonReq));

        // Act & Assert
        assertThrows(AwsSQSException.class, () -> cozinhaPedidoIntegration.receberPedido(PEDIDO_ID));
    }
}