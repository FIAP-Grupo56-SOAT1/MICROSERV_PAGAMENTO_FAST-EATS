package br.com.fiap.fasteats.config;

import br.com.fiap.fasteats.core.usecase.impl.AlterarPagamentoStatusUseCase;
import br.com.fiap.fasteats.core.usecase.impl.StatusPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.impl.AlterarPagamentoStatusValidatorImpl;
import br.com.fiap.fasteats.dataprovider.PagamentoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlterarPagamentoStatusConfig {
    @Bean
    public AlterarPagamentoStatusUseCase alterarPagamentoStatusUseCase(StatusPagamentoUseCase statusPagamentoUseCase,
                                                                       PagamentoAdapter pagamentoAdapter,
                                                                       AlterarPagamentoStatusValidatorImpl alterarPagamentoStatusValidatorImpl) {

        return new AlterarPagamentoStatusUseCase(statusPagamentoUseCase, pagamentoAdapter, alterarPagamentoStatusValidatorImpl);
    }
}
