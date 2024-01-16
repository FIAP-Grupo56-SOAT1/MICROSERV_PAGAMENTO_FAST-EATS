package br.com.fiap.fasteats.dataprovider.client.mapper;

import br.com.fiap.fasteats.core.domain.model.StatusPedido;
import br.com.fiap.fasteats.dataprovider.client.response.StatusPedidoResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatusPedidoMapper {
    StatusPedido toStatusPedido(StatusPedidoResponse statusPedidoResponse);
    List<StatusPedido> toStatusPedidoList(List<StatusPedidoResponse> statusPedidoResponseList);
}
