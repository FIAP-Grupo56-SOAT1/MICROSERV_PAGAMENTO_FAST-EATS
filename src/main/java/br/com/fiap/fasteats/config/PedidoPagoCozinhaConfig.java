package br.com.fiap.fasteats.config;

import br.com.fiap.fasteats.core.usecase.impl.PagamentoUseCase;
import br.com.fiap.fasteats.core.usecase.impl.PedidoPagoCozinhaUseCase;
import br.com.fiap.fasteats.dataprovider.ConcluirPagamentoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoPagoCozinhaConfig {
    @Bean
    public PedidoPagoCozinhaUseCase pedidoPagoCozinhaUseCase(PagamentoUseCase pagamentoUseCase,
                                                             ConcluirPagamentoAdapter concluirPagamentoAdapter) {
        return new PedidoPagoCozinhaUseCase(pagamentoUseCase, concluirPagamentoAdapter);
    }
}
