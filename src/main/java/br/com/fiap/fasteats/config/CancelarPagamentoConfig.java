package br.com.fiap.fasteats.config;

import br.com.fiap.fasteats.core.dataprovider.CancelarPedidoOutputPort;
import br.com.fiap.fasteats.core.usecase.impl.AlterarPagamentoStatusUseCase;
import br.com.fiap.fasteats.core.usecase.impl.CancelarPagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.core.validator.impl.CancelarPagamentoValidatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CancelarPagamentoConfig {
    @Bean
    public CancelarPagamentoUseCase cancelarPagamentoUseCase(PagamentoUseCase pagamentoUseCase,
                                                             AlterarPagamentoStatusUseCase alterarPagamentoStatusUseCase,
                                                             CancelarPedidoOutputPort cancelarPedidoOutputPort,
                                                             CancelarPagamentoValidatorImpl cancelarPagamentoValidatorImpl) {
        return new CancelarPagamentoUseCase(pagamentoUseCase, alterarPagamentoStatusUseCase,
                cancelarPedidoOutputPort, cancelarPagamentoValidatorImpl);
    }
}
