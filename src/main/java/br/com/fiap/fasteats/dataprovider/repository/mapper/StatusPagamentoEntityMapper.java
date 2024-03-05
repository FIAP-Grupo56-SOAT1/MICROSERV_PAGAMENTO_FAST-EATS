package br.com.fiap.fasteats.dataprovider.repository.mapper;


import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.dataprovider.repository.entity.StatusPagamentoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusPagamentoEntityMapper {
    StatusPagamento toStatusPagamento(StatusPagamentoEntity statusPagamentoEntity);
    StatusPagamentoEntity toStatusPagamentoEntity(StatusPagamento statusPagamentoEntity);
}
