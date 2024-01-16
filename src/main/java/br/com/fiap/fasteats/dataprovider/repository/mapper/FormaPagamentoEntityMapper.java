package br.com.fiap.fasteats.dataprovider.repository.mapper;

import br.com.fiap.fasteats.dataprovider.repository.entity.FormaPagamentoEntity;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FormaPagamentoEntityMapper {
    FormaPagamento toFormaPagamento(FormaPagamentoEntity formaPagamentoEntity);
    FormaPagamentoEntity toFormaPagamentoEntity(FormaPagamento formaPagamentoEntity);
}
