package br.com.fiap.fasteats.config;


import br.com.fiap.fasteats.core.usecase.impl.FormaPagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.GerarPagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.MetodoPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.impl.GerarPagamentoValidatorImpl;
import br.com.fiap.fasteats.dataprovider.PagamentoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GerarPagamentoConfig {
    @Bean
    public GerarPagamentoUseCase gerarPagamentoUseCase(FormaPagamentoUseCase formaPagamentoUseCase,
                                                       GerarPagamentoValidatorImpl gerarPagamentoValidatorImpl,
                                                       MetodoPagamentoUseCase metodoPagamentoUseCase,
                                                       PagamentoAdapter pagamentoAdapter) {
        return new GerarPagamentoUseCase(formaPagamentoUseCase, gerarPagamentoValidatorImpl,
                metodoPagamentoUseCase, pagamentoAdapter);
    }
}
