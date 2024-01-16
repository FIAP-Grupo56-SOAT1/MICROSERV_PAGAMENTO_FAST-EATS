package br.com.fiap.fasteats.config;


import br.com.fiap.fasteats.core.usecase.impl.*;
import br.com.fiap.fasteats.core.validator.impl.RealizarPagamentoValidatorImpl;
import br.com.fiap.fasteats.dataprovider.AlterarPedidoStatusAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RealizarPagamentoConfig {
    @Bean
    public RealizarPagamentoUseCase realizarPagamentoUseCase(FormaPagamentoUseCase formaPagamentoUseCase,
                                                             AlterarPagamentoStatusUseCase alterarPagamentoStatusUseCase,
                                                             AlterarPedidoStatusAdapter alterarPedidoStatusAdapter,
                                                             PagamentoUseCase pagamentoUseCase,
                                                             EmitirComprovantePagamentoUseCase emitirComprovantePagamentoUseCase,
                                                             RealizarPagamentoValidatorImpl realizarPagamentoValidatorImpl) {

        return new RealizarPagamentoUseCase(formaPagamentoUseCase, alterarPagamentoStatusUseCase,
                alterarPedidoStatusAdapter, pagamentoUseCase,
                emitirComprovantePagamentoUseCase, realizarPagamentoValidatorImpl);
    }
}
