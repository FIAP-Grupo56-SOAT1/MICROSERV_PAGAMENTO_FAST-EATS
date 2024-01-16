package br.com.fiap.fasteats.entrypoint.controller.mapper;

import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.entrypoint.controller.response.StatusPagamentoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusPagamentoMapper {
    StatusPagamentoResponse toStatusPagamentoResponse(StatusPagamento statusPagamento);
}
