package br.com.fiap.fasteats.dataprovider.client;

import br.com.fiap.fasteats.core.domain.valueobject.MensagemNotificacaoCliente;

public interface NotificarClienteIntegration {
    void notificar(MensagemNotificacaoCliente mensagemNotificacaoCliente);
}
