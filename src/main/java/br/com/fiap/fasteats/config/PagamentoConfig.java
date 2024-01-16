package br.com.fiap.fasteats.config;


import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.StatusPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.impl.PagamentoValidatorImpl;
import br.com.fiap.fasteats.dataprovider.PagamentoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoConfig {
    @Bean
    public PagamentoUseCase pagamentoUseCase(PagamentoAdapter pagamentoAdapter,
                                             StatusPagamentoUseCase statusPagamentoUseCase,
                                             PagamentoValidatorImpl pagamentoValidatorImpl) {
        return new PagamentoUseCase(pagamentoAdapter, statusPagamentoUseCase, pagamentoValidatorImpl);
    }
}
