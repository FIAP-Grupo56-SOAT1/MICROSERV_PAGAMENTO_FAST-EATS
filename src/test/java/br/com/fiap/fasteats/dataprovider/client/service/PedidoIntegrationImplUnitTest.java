package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.dataprovider.client.response.PedidoResponse;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - Pedido Integration")
class PedidoIntegrationImplUnitTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private SqsTemplate sqsTemplate;
    @InjectMocks
    private PedidoIntegrationImpl pedidoIntegrationImpl;
    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void consultar() {
        Long pedidoId = 1L;
        when(restTemplate.getForObject(anyString(), eq(PedidoResponse.class), eq(pedidoId))).thenReturn(new PedidoResponse());
        pedidoIntegrationImpl.consultar(pedidoId);
        verify(restTemplate).getForObject(anyString(), eq(PedidoResponse.class), eq(pedidoId));
    }

    @Test
    void pedidoCriado() {
        Long pedidoId = 1L;
        pedidoIntegrationImpl.pedidoCriado(pedidoId);
        verify(sqsTemplate).send(any(), anyString());
    }

    @Test
    void pedidoAguardandoPagamento() {
        Long pedidoId = 1L;
        pedidoIntegrationImpl.pedidoAguardandoPagamento(pedidoId);
        verify(sqsTemplate).send(any(), anyString());
    }

    @Test
    void pedidoPago() {
        Long pedidoId = 1L;
        pedidoIntegrationImpl.pedidoPago(pedidoId);
        verify(sqsTemplate).send(any(), anyString());
    }

    @Test
    void pedidoCancelado() {
        Long pedidoId = 1L;
        pedidoIntegrationImpl.pedidoCancelado(pedidoId);
        verify(sqsTemplate).send(any(), anyString());
    }
}