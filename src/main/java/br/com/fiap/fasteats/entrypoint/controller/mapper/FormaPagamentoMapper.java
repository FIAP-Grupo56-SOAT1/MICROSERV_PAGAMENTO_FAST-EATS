package br.com.fiap.fasteats.entrypoint.controller.mapper;

import br.com.fiap.fasteats.entrypoint.controller.request.FormaPagamentoRequest;
import br.com.fiap.fasteats.entrypoint.controller.response.FormaPagamentoResponse;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FormaPagamentoMapper {
    @Mapping(target = "id", ignore = true)
    FormaPagamento toFormaPagamento(FormaPagamentoRequest formaPagamentoResponse);

    FormaPagamentoResponse toFormaPagamentoResponse(FormaPagamento formaPagamento);

    List<FormaPagamentoResponse> toFormaPagamentoResponseList(List<FormaPagamento> formaPagamentoList);
}
