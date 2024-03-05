package br.com.fiap.fasteats.config;

import br.com.fiap.fasteats.core.dataprovider.StatusPagamentoOutputPort;
import br.com.fiap.fasteats.core.usecase.impl.StatusPagamentoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatusPagamentoConfig {
    @Bean
    public StatusPagamentoUseCase crudStatusPagamentoUseCase(StatusPagamentoOutputPort statusPagamentoOutputPort) {
        return new StatusPagamentoUseCase(statusPagamentoOutputPort);
    }
}
