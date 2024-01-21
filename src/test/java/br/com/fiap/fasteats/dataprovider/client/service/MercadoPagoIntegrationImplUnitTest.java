package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;
import br.com.fiap.fasteats.entrypoint.controller.mapper.PagamentoExternoMapper;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import static br.com.fiap.fasteats.dataprovider.constants.StatusMercadoPagoConstants.STATUS_APROVADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - Mercado Pago Integration")
class MercadoPagoIntegrationImplUnitTest {
    @Mock
    private PagamentoExternoMapper pagamentoExternoMapper;

    @Value("${pagamento.mercado.pago.email.empresa}")
    private String emailEmpresa;

    @Value("${pagamento.mercado.pago.credencial}")
    private String accessToken;

    @Value("${pagamento.mercado.pago.userid}")
    private String userIdAplicacaoMercadoPago;

    @Value("${pagamento.mercado.pago.tipo.pagamento}")
    private String tipoPagamentoMercadoPago;
    @Mock
    private PaymentClient paymentClient;
    @InjectMocks
    private MercadoPagoIntegrationImpl mercadoPagoIntegrationImpl;
    private AutoCloseable openMocks;
    private final Long PAGAMENTO_EXTERNO_ID = 1L;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("Deve criar pagamento externo com sucesso")
    void criarPagamentoExterno() throws MPException, MPApiException {
        // Arrange
        Payment payment = new Payment();
        Double valorPedido = 50.0;

        when(paymentClient.create(any(PaymentCreateRequest.class))).thenReturn(payment);
        when(pagamentoExternoMapper.toPagamentoExterno(payment)).thenReturn(new PagamentoExterno());

        // Act
        PagamentoExterno resultado = mercadoPagoIntegrationImpl.enviarSolicitacaoPagamento(valorPedido);

        // Assert
        assertNotNull(resultado);
        verify(paymentClient).create(any(PaymentCreateRequest.class));
        verify(pagamentoExternoMapper).toPagamentoExterno(payment);
    }


    @Test
    @DisplayName("Deve lançar exceção ao criar pagamento externo com erro")
    void criarPagamentoExternoComErro() throws MPException, MPApiException {
        // Arrange
        Double valorPedido = 50.0;

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> mercadoPagoIntegrationImpl.enviarSolicitacaoPagamento(valorPedido));
        verify(paymentClient).create(any());
    }

    @Test
    @DisplayName("Deve consultar pagamento externo com sucesso")
    void consultarPagamentoExternoComSucesso() throws MPException, MPApiException {
        // Arrange
        PagamentoExterno pagamentoExterno = new PagamentoExterno();
        Payment payment = new Payment();
        pagamentoExterno.setId(PAGAMENTO_EXTERNO_ID);
        pagamentoExterno.setStatus(STATUS_APROVADO);
        pagamentoExterno.setUrlPagamento("UrlPagamento");
        pagamentoExterno.setQrCode("QRCode");
        pagamentoExterno.setQrCodeBase64("QRCodeBase64");


        when(paymentClient.get(1L)).thenReturn(payment);
        when(pagamentoExternoMapper.toPagamentoExterno(payment)).thenReturn(pagamentoExterno);

        // Act
        PagamentoExterno resultado = mercadoPagoIntegrationImpl.consultarPagamento(pagamentoExterno);

        // Assert
        assertNotNull(resultado);
        assertEquals("QRCode", resultado.getQrCode());
        assertEquals("UrlPagamento", resultado.getUrlPagamento());
        assertEquals("QRCodeBase64", resultado.getQrCodeBase64());
        verify(paymentClient).get(PAGAMENTO_EXTERNO_ID);
        verify(pagamentoExternoMapper).toPagamentoExterno(payment);
    }

    @Test
    @DisplayName("Deve simular a consultar pagamento externo com sucesso")
    void consultarPagamentoExternoSimuladoComSucesso() throws MPException, MPApiException {
        // Arrange
        PagamentoExterno pagamentoExterno = new PagamentoExterno();
        Payment payment = new Payment();
        pagamentoExterno.setId(PAGAMENTO_EXTERNO_ID);
        pagamentoExterno.setStatus(STATUS_APROVADO);
        pagamentoExterno.setUrlPagamento("UrlPagamento");
        pagamentoExterno.setQrCode("QRCode");
        pagamentoExterno.setQrCodeBase64("QRCodeBase64");
        pagamentoExterno.setSimulacaoPagamento(true);


        when(paymentClient.get(1L)).thenReturn(payment);
        when(pagamentoExternoMapper.toPagamentoExterno(payment)).thenReturn(pagamentoExterno);

        // Act
        PagamentoExterno resultado = mercadoPagoIntegrationImpl.consultarPagamento(pagamentoExterno);

        // Assert
        assertNotNull(resultado);
        assertEquals("QRCode", resultado.getQrCode());
        assertEquals("UrlPagamento", resultado.getUrlPagamento());
        assertEquals("QRCodeBase64", resultado.getQrCodeBase64());
        verify(paymentClient).get(PAGAMENTO_EXTERNO_ID);
        verify(pagamentoExternoMapper).toPagamentoExterno(payment);
    }

    @Test
    @DisplayName("Deve lançar exceção ao consultar pagamento externo com erro")
    void consultarPagamentoExternoComErro() throws MPException, MPApiException {
        // Arrange
        PagamentoExterno pagamentoExterno = new PagamentoExterno();
        pagamentoExterno.setId(PAGAMENTO_EXTERNO_ID);

        when(paymentClient.get(PAGAMENTO_EXTERNO_ID)).thenThrow(new RuntimeException("Erro ao consultar pagamento"));

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> mercadoPagoIntegrationImpl.consultarPagamento(pagamentoExterno));
        verify(paymentClient).get(PAGAMENTO_EXTERNO_ID);
    }

    @Test
    @DisplayName("Deve cancelar pagamento externo com sucesso")
    void cancelarPagamentoExternoComSucesso() throws MPException, MPApiException {
        // Arrange
        PagamentoExterno pagamentoExterno = new PagamentoExterno();
        Payment payment = new Payment();
        pagamentoExterno.setId(PAGAMENTO_EXTERNO_ID);

        when(paymentClient.cancel(PAGAMENTO_EXTERNO_ID)).thenReturn(payment);
        when(pagamentoExternoMapper.toPagamentoExterno(payment)).thenReturn(pagamentoExterno);

        // Act
        PagamentoExterno resultado = mercadoPagoIntegrationImpl.cancelarPagamento(PAGAMENTO_EXTERNO_ID);

        // Assert
        assertNotNull(resultado);
        verify(paymentClient).cancel(PAGAMENTO_EXTERNO_ID);
        verify(pagamentoExternoMapper).toPagamentoExterno(payment);
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar pagamento externo com erro")
    void cancelarPagamentoExternoComErro() throws MPException, MPApiException {
        // Arrange
        when(paymentClient.cancel(PAGAMENTO_EXTERNO_ID)).thenThrow(new RuntimeException("Erro ao cancelar pagamento"));

        // Act & Assert
        assertThrows(RegraNegocioException.class, () -> mercadoPagoIntegrationImpl.cancelarPagamento(PAGAMENTO_EXTERNO_ID));
        verify(paymentClient).cancel(PAGAMENTO_EXTERNO_ID);
    }
}
