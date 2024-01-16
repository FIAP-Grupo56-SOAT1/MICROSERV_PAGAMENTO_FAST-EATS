package br.com.fiap.fasteats.config;

import br.com.fiap.fasteats.core.usecase.impl.AlterarFormaPagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.GerarPagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlterarFormaPagamentoConfig {
    @Bean
    public AlterarFormaPagamentoUseCase alterarFormaPagamentoUseCase(PagamentoUseCase pagamentoUseCase,
                                                                     GerarPagamentoUseCase gerarPagamentoUseCase) {
        return new AlterarFormaPagamentoUseCase(pagamentoUseCase, gerarPagamentoUseCase);
    }
}
