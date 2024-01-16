package br.com.fiap.fasteats.config.validator;

import br.com.fiap.fasteats.core.usecase.impl.FormaPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.impl.GerarPagamentoValidatorImpl;
import br.com.fiap.fasteats.dataprovider.PedidoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GerarPagamentoValidatorConfig {
    @Bean
    public GerarPagamentoValidatorImpl gerarPagamentoValidatorImpl(FormaPagamentoUseCase formaPagamentoUseCase,
                                                                   PedidoAdapter pedidoAdapter) {
        return new GerarPagamentoValidatorImpl(formaPagamentoUseCase, pedidoAdapter);
    }
}
