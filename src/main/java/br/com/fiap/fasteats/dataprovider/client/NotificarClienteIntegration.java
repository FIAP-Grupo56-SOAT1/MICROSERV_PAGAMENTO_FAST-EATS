package br.com.fiap.fasteats.dataprovider.client;

import br.com.fiap.fasteats.core.domain.valueobject.MensagemNotificacao;

public interface NotificarClienteIntegration {
    void notificar(MensagemNotificacao mensagemNotificacao);
}
