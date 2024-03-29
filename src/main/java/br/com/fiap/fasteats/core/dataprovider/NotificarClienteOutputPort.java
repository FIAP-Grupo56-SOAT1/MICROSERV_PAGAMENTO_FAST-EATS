package br.com.fiap.fasteats.core.dataprovider;

import br.com.fiap.fasteats.core.domain.valueobject.MensagemNotificacaoCliente;

public interface NotificarClienteOutputPort {
    void notificar(MensagemNotificacaoCliente mensagemNotificacaoCliente);
}
