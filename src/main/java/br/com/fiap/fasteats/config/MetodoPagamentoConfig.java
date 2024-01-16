package br.com.fiap.fasteats.config;

import br.com.fiap.fasteats.core.usecase.impl.FormaPagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.MetodoPagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.dataprovider.PedidoAdapter;
import br.com.fiap.fasteats.dataprovider.client.MercadoPagoIntegration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetodoPagamentoConfig {
    @Bean
    public MetodoPagamentoUseCase metodoPagamentoUseCase(PagamentoUseCase pagamentoUseCase,
                                                         FormaPagamentoUseCase formaPagamentoUseCase,
                                                         PedidoAdapter pedidoAdapter,
                                                         MercadoPagoIntegration mercadoPagoIntegration) {
        return new MetodoPagamentoUseCase(pagamentoUseCase, formaPagamentoUseCase, pedidoAdapter, mercadoPagoIntegration);
    }
}
