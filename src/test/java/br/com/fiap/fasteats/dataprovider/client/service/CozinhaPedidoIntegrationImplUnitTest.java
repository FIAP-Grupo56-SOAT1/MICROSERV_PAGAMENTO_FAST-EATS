package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
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

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_PAGO;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    @DisplayName("Deve tratar a falhar ao receber um pedido na cozinha")
    void tratarErroReceberPedido() {
        // Arrange
        String jsonReq = new Gson().toJson(new CozinhaReceberPedidoRequest(PEDIDO_ID));
        Pagamento pagamento = new Pagamento();
        pagamento.setId(1L);
        Pagamento pagamentoPago = new Pagamento();
        pagamentoPago.setId(1L);
        pagamentoPago.setStatusPagamento(new StatusPagamento(1L, STATUS_PAGO, true));

        when(pagamentoInputPort.consultarPorIdPedido(PEDIDO_ID)).thenReturn(pagamento);
        when(alterarPagamentoStatusInputPort.pago(pagamento.getId())).thenReturn(pagamentoPago);

        // Act
        cozinhaPedidoIntegration.erroReceberPedido(jsonReq);

        // Assert
        assertEquals(STATUS_PAGO, pagamentoPago.getStatusPagamento().getNome());
        verify(pagamentoInputPort).consultarPorIdPedido(PEDIDO_ID);
        verify(alterarPagamentoStatusInputPort).pago(pagamento.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao falhar ao tratar a falha ao receber um pedido na cozinha")
    void falharAoTratarErroReceberPedido() {
        // Arrange
        String jsonReq = new Gson().toJson(new CozinhaReceberPedidoRequest(PEDIDO_ID));
        doThrow(new RuntimeException("Erro ao tentar tratar erro ao receber pedido")).when(pagamentoInputPort).consultarPorIdPedido(PEDIDO_ID);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> cozinhaPedidoIntegration.erroReceberPedido(jsonReq));
    }
}