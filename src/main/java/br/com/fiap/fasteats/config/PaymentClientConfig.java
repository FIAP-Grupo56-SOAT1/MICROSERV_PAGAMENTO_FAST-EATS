package br.com.fiap.fasteats.config;

import com.mercadopago.client.payment.PaymentClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentClientConfig {
    @Bean
    public PaymentClient paymentClient() {
        return new PaymentClient();
    }
}
