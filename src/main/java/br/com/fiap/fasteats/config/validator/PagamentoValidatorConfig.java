package br.com.fiap.fasteats.config.validator;

import br.com.fiap.fasteats.core.usecase.impl.StatusPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.impl.PagamentoValidatorImpl;
import br.com.fiap.fasteats.dataprovider.PagamentoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoValidatorConfig {
    @Bean
    public PagamentoValidatorImpl pagamentoValidatorImpl(PagamentoAdapter pagamentoAdapter,
                                                         StatusPagamentoUseCase statusPagamentoUseCase) {
        return new PagamentoValidatorImpl(pagamentoAdapter, statusPagamentoUseCase);
    }
}
