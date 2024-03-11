package br.com.fiap.fasteats.core.dataprovider;

import br.com.fiap.fasteats.core.domain.valueobject.MensagemNotificacao;

public interface NotificarClienteOutputPort {
    void notificar(MensagemNotificacao mensagemNotificacao);
}
