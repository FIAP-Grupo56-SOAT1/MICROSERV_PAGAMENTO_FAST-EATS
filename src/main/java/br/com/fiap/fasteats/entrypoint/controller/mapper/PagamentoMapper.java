package br.com.fiap.fasteats.entrypoint.controller.mapper;

import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.entrypoint.controller.response.PagamentoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FormaPagamentoMapper.class,
                                           StatusPagamentoMapper.class})
public interface PagamentoMapper {
    @Mapping(source = "formaPagamento", target = "formaPagamento")
    @Mapping(source = "statusPagamento", target = "statusPagamento")
    PagamentoResponse toPagamentoResponse(Pagamento pagamento);

    List<PagamentoResponse> toPagamentosResponse(List<Pagamento> pagamentos);
}
