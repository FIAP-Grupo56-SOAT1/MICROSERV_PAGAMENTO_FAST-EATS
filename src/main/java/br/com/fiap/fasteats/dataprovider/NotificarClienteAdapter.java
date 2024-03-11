package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.NotificarClienteOutputPort;
import br.com.fiap.fasteats.core.domain.valueobject.MensagemNotificacao;
import br.com.fiap.fasteats.dataprovider.client.NotificarClienteIntegration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificarClienteAdapter implements NotificarClienteOutputPort {
    private final NotificarClienteIntegration notificarClienteIntegration;

    @Override
    public void notificar(MensagemNotificacao mensagemNotificacao) {
        notificarClienteIntegration.notificar(mensagemNotificacao);
    }
}
