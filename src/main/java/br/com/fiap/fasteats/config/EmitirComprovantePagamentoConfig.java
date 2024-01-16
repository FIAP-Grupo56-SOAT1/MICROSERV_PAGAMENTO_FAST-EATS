package br.com.fiap.fasteats.config;

import br.com.fiap.fasteats.core.usecase.impl.EmitirComprovantePagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.core.validator.impl.EmitirComprovantePagamentoValidatorImpl;
import br.com.fiap.fasteats.dataprovider.CozinhaPedidoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmitirComprovantePagamentoConfig {
    @Bean
    public EmitirComprovantePagamentoUseCase emitirComprovantePagamentoUseCase(PagamentoUseCase pagamentoUseCase,
                                                                               CozinhaPedidoAdapter cozinhaPedidoAdapter,
                                                                               EmitirComprovantePagamentoValidatorImpl emitirComprovantePagamentoValidator) {
        return new EmitirComprovantePagamentoUseCase(pagamentoUseCase, cozinhaPedidoAdapter, emitirComprovantePagamentoValidator);
    }
}
