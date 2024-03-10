package br.com.fiap.fasteats.config;

import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.ReceberPedidoPagoUseCase;
import br.com.fiap.fasteats.dataprovider.ConcluirPagamentoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReceberPedidoPagoConfig {
    @Bean
    public ReceberPedidoPagoUseCase receberPedidoPagoInputPort(PagamentoUseCase pagamentoUseCase,
                                                               ConcluirPagamentoAdapter concluirPagamentoAdapter) {
        return new ReceberPedidoPagoUseCase(pagamentoUseCase, concluirPagamentoAdapter);
    }
}
