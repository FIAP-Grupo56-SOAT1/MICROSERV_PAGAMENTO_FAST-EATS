package br.com.fiap.fasteats.config.validator;

import br.com.fiap.fasteats.core.validator.impl.RealizarPagamentoValidatorImpl;
import br.com.fiap.fasteats.dataprovider.PedidoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RealizarPagamentoValidatorConfig {
    @Bean
    public RealizarPagamentoValidatorImpl realizarPagamentoValidatorImpl(PedidoAdapter pedidoAdapter) {
        return new RealizarPagamentoValidatorImpl(pedidoAdapter);
    }
}
