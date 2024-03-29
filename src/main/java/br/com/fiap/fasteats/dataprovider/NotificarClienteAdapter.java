package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.NotificarClienteOutputPort;
import br.com.fiap.fasteats.core.domain.valueobject.MensagemNotificacaoCliente;
import br.com.fiap.fasteats.dataprovider.client.NotificarClienteIntegration;
import br.com.fiap.fasteats.dataprovider.repository.NotificacaoRepository;
import br.com.fiap.fasteats.dataprovider.repository.entity.NotificacaoEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificarClienteAdapter implements NotificarClienteOutputPort {
    private final NotificarClienteIntegration notificarClienteIntegration;
    private final NotificacaoRepository notificacaoRepository;

    @Override
    @Transactional
    public void notificar(MensagemNotificacaoCliente mensagemNotificacaoCliente) {
        NotificacaoEntity notificacaoEntity = new NotificacaoEntity();
        notificacaoEntity.setClienteId(mensagemNotificacaoCliente.getClienteId());
        notificacaoEntity.setStatusNotificacao(mensagemNotificacaoCliente.getStatus());
        notificacaoEntity.setDataHoraCriado(LocalDateTime.now());
        notificarClienteIntegration.notificar(mensagemNotificacaoCliente);
        notificacaoEntity.setDataHoraEnvio(LocalDateTime.now());
        notificacaoRepository.save(notificacaoEntity);
    }
}
