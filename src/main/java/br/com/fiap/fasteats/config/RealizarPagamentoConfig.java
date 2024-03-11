package br.com.fiap.fasteats.config;


import br.com.fiap.fasteats.core.usecase.impl.FormaPagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.RealizarPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.impl.RealizarPagamentoValidatorImpl;
import br.com.fiap.fasteats.dataprovider.RealizarPagamentoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RealizarPagamentoConfig {
    @Bean
    public RealizarPagamentoUseCase realizarPagamentoUseCase(FormaPagamentoUseCase formaPagamentoUseCase,
                                                             RealizarPagamentoAdapter realizarPagamentoAdapter,
                                                             PagamentoUseCase pagamentoUseCase,
                                                             RealizarPagamentoValidatorImpl realizarPagamentoValidatorImpl) {

        return new RealizarPagamentoUseCase(formaPagamentoUseCase, realizarPagamentoAdapter,
                pagamentoUseCase, realizarPagamentoValidatorImpl);
    }
}
