package br.com.fiap.fasteats.config;


import br.com.fiap.fasteats.core.usecase.impl.AlterarPagamentoStatusUseCase;
import br.com.fiap.fasteats.core.usecase.impl.PagamentoExternoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.core.validator.impl.CancelarPagamentoValidatorImpl;
import br.com.fiap.fasteats.dataprovider.CancelarPagamentoAdapter;
import br.com.fiap.fasteats.dataprovider.MercadoPagoAdapter;
import br.com.fiap.fasteats.dataprovider.RealizarPagamentoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoExternoConfig {
    @Bean
    public PagamentoExternoUseCase pagamentoExternoUseCase(PagamentoUseCase pagamentoUseCase,
                                                           MercadoPagoAdapter mercadoPagoAdapter,
                                                           RealizarPagamentoAdapter realizarPagamentoAdapter,
                                                           AlterarPagamentoStatusUseCase alterarPagamentoStatusUseCase,
                                                           CancelarPagamentoAdapter cancelarPagamentoAdapter,
                                                           CancelarPagamentoValidatorImpl cancelarPagamentoValidatorImpl) {
        return new PagamentoExternoUseCase(pagamentoUseCase, mercadoPagoAdapter,
                                           realizarPagamentoAdapter, alterarPagamentoStatusUseCase,
                                           cancelarPagamentoAdapter, cancelarPagamentoValidatorImpl);
    }
}
