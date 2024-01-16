package br.com.fiap.fasteats.config.validator;

import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.StatusPagamentoUseCase;
import br.com.fiap.fasteats.core.validator.impl.EmitirComprovantePagamentoValidatorImpl;
import br.com.fiap.fasteats.dataprovider.PedidoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmitirComprovantePagamentoValidatorConfig {
    @Bean
    public EmitirComprovantePagamentoValidatorImpl emitirComprovantePagamentoValidatorImpl(PedidoAdapter pedidoAdapter,
                                                                                           PagamentoUseCase pagamentoUseCase,
                                                                                           StatusPagamentoUseCase statusPagamentoUseCase) {
        return new EmitirComprovantePagamentoValidatorImpl(pedidoAdapter, pagamentoUseCase, statusPagamentoUseCase);
    }
}
