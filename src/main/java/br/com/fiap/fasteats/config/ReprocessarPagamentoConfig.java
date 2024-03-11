package br.com.fiap.fasteats.config;

import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.ReprocessarPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.impl.RealizarPagamentoValidatorImpl;
import br.com.fiap.fasteats.dataprovider.RealizarPagamentoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReprocessarPagamentoConfig {
    @Bean
    public ReprocessarPagamentoUseCase reprocessarPagamentoUseCase(RealizarPagamentoAdapter realizarPagamentoAdapter,
                                                                   PagamentoUseCase pagamentoUseCase,
                                                                   RealizarPagamentoValidatorImpl realizarPagamentoValidatorImpl) {
        return new ReprocessarPagamentoUseCase(realizarPagamentoAdapter, pagamentoUseCase, realizarPagamentoValidatorImpl);
    }
}
