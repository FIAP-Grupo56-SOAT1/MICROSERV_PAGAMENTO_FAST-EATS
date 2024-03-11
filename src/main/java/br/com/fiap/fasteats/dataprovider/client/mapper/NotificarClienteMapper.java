package br.com.fiap.fasteats.dataprovider.client.mapper;

import br.com.fiap.fasteats.core.domain.valueobject.MensagemNotificacao;
import br.com.fiap.fasteats.dataprovider.client.request.NotificarClienteRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificarClienteMapper {
    NotificarClienteRequest toMensagemNotificacaoRequest(MensagemNotificacao mensagemNotificacao);
}
