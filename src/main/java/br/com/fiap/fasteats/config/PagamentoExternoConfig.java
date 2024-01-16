package br.com.fiap.fasteats.config;


import br.com.fiap.fasteats.core.usecase.impl.AlterarPagamentoStatusUseCase;
import br.com.fiap.fasteats.core.usecase.impl.EmitirComprovantePagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.PagamentoExternoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.core.validator.impl.CancelarPagamentoValidatorImpl;
import br.com.fiap.fasteats.dataprovider.AlterarPedidoStatusAdapter;
import br.com.fiap.fasteats.dataprovider.MercadoPagoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoExternoConfig {
    @Bean
    public PagamentoExternoUseCase pagamentoExternoUseCase(PagamentoUseCase pagamentoUseCase,
                                                           MercadoPagoAdapter mercadoPagoAdapter,
                                                           EmitirComprovantePagamentoUseCase emitirComprovantePagamentoUseCase,
                                                           AlterarPagamentoStatusUseCase alterarPagamentoStatusUseCase,
                                                           AlterarPedidoStatusAdapter alterarPedidoStatusAdapter,
                                                           CancelarPagamentoValidatorImpl cancelarPagamentoValidatorImpl) {
        return new PagamentoExternoUseCase(pagamentoUseCase, mercadoPagoAdapter,
                                           emitirComprovantePagamentoUseCase, alterarPagamentoStatusUseCase,
                                           alterarPedidoStatusAdapter, cancelarPagamentoValidatorImpl);
    }
}
