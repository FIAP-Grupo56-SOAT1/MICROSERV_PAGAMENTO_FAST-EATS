package br.com.fiap.fasteats.dataprovider.repository.mapper;


import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.dataprovider.repository.entity.PagamentoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StatusPagamentoEntityMapper.class,
                                           FormaPagamentoEntityMapper.class})
public interface PagamentoEntityMapper {
    Pagamento toPagamento(PagamentoEntity pagamentoEntity);
    PagamentoEntity toPagamentoEntity(Pagamento pagamento);
}
