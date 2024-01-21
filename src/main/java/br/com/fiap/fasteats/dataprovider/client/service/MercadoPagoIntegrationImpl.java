package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;
import br.com.fiap.fasteats.dataprovider.client.MercadoPagoIntegration;
import br.com.fiap.fasteats.entrypoint.controller.mapper.PagamentoExternoMapper;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.resources.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
@RequiredArgsConstructor
public class MercadoPagoIntegrationImpl implements MercadoPagoIntegration {
    private final Logger logger = LoggerFactory.getLogger(MercadoPagoIntegrationImpl.class);

    private final PagamentoExternoMapper pagamentoExternoMapper;

    @Value("${pagamento.mercado.pago.email.empresa}")
    private String emailEmpresa;

    @Value("${pagamento.mercado.pago.credencial}")
    private String accessToken;

    @Value("${pagamento.mercado.pago.userid}")
    private String userIdAplicacaoMercadoPago;

    @Value("${pagamento.mercado.pago.tipo.pagamento}")
    private String tipoPagamentoMercadoPago;

    private final PaymentClient client;

    @Override
    public PagamentoExterno enviarSolicitacaoPagamento(Double valorPedido) {
        return criarPagamentoExterno(valorPedido);
    }

    @Override
    public PagamentoExterno consultarPagamento(PagamentoExterno pagamentoExterno) {
        return consultarPagamentoExterno(pagamentoExterno);
    }

    @Override
    public PagamentoExterno cancelarPagamento(Long idPagamentoExterno) {
        return cancelarPagamentoExterno(idPagamentoExterno);
    }

    private PagamentoExterno consultarPagamentoExterno(PagamentoExterno pagamentoExterno) {
        MercadoPagoConfig.setAccessToken(accessToken);
        try {
            Payment payment = client.get(pagamentoExterno.getId());
            String paymentString = payment.toString();
            logger.info("retorno consultar mercado pago {}", paymentString);
            PagamentoExterno pagamentoExternoResponse = pagamentoExternoMapper.toPagamentoExterno(payment);
            if (pagamentoExterno.isSimulacaoPagamento()) {
                pagamentoExternoResponse.setStatus(pagamentoExterno.getStatus());
                logger.info("simulacao de pagamento em andamento {}", pagamentoExternoResponse);
            }
            return pagamentoExternoResponse;
        } catch (Exception ex) {
            throw new RegraNegocioException("Erro ao consultar pagamento externo " + ex.getMessage());
        }
    }

    private PagamentoExterno criarPagamentoExterno(Double valorPedido) {
        MercadoPagoConfig.setAccessToken(accessToken);
        PaymentCreateRequest paymentCreateRequest =
                PaymentCreateRequest.builder()
                        .transactionAmount(BigDecimal.valueOf(valorPedido))
                        .paymentMethodId(tipoPagamentoMercadoPago)
                        .issuerId(userIdAplicacaoMercadoPago)
                        .payer(
                                PaymentPayerRequest.builder()
                                        .email(emailEmpresa)
                                        .build())
                        .build();
        try {
            Payment payment = client.create(paymentCreateRequest);
            String paymentString = payment.toString();
            logger.info("retorno criar pagamento mercado pago {}", paymentString);
            return pagamentoExternoMapper.toPagamentoExterno(payment);
        } catch (Exception ex) {
            throw new RegraNegocioException("Erro ao gerar pagamento externo " + ex.getMessage());
        }
    }

    private PagamentoExterno cancelarPagamentoExterno(Long idPagamentoExterno) {
        MercadoPagoConfig.setAccessToken(accessToken);
        try {
            Payment payment = client.cancel(idPagamentoExterno);
            String paymentString = payment.toString();
            logger.info("retorno cancelar pagamento mercado pago {}", paymentString);
            return pagamentoExternoMapper.toPagamentoExterno(payment);
        } catch (Exception ex) {
            throw new RegraNegocioException("Erro ao cancelar pagamento externo " + ex.getMessage());
        }
    }
}