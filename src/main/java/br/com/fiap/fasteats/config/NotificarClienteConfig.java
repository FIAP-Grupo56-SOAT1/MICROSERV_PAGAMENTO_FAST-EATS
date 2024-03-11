package br.com.fiap.fasteats.config;

import br.com.fiap.fasteats.core.usecase.impl.NotificarClienteUseCase;
import br.com.fiap.fasteats.dataprovider.ContatoPadraoAdapter;
import br.com.fiap.fasteats.dataprovider.NotificarClienteAdapter;
import br.com.fiap.fasteats.dataprovider.PedidoAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificarClienteConfig {
    @Bean
    public NotificarClienteUseCase notificarClienteUseCase(NotificarClienteAdapter notificarClienteAdapter,
                                                           PedidoAdapter pedidoAdapter,
                                                           ContatoPadraoAdapter contatoPadraoAdapter) {
        return new NotificarClienteUseCase(notificarClienteAdapter, pedidoAdapter, contatoPadraoAdapter);
    }
}
