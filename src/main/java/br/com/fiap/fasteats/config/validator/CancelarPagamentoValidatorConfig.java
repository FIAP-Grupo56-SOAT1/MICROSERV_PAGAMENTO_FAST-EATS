package br.com.fiap.fasteats.config.validator;

import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.StatusPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.impl.CancelarPagamentoValidatorImpl;
import br.com.fiap.fasteats.dataprovider.PedidoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CancelarPagamentoValidatorConfig {
    @Bean
    public CancelarPagamentoValidatorImpl cancelarPagamentoValidatorImpl(PedidoAdapter pedidoAdapter,
                                                                         PagamentoUseCase pagamentoUseCase,
                                                                         StatusPagamentoUseCase statusPagamentoUseCase) {
        return new CancelarPagamentoValidatorImpl(pedidoAdapter, pagamentoUseCase, statusPagamentoUseCase);
    }
}
