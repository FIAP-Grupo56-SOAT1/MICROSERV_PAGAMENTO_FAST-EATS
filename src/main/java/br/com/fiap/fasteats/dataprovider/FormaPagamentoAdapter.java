package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.FormaPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.dataprovider.repository.FormaPagamentoRepository;
import br.com.fiap.fasteats.dataprovider.repository.entity.FormaPagamentoEntity;
import br.com.fiap.fasteats.dataprovider.repository.mapper.FormaPagamentoEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FormaPagamentoAdapter implements FormaPagamentoOutputPort {
    private final FormaPagamentoRepository formaPagamentoRepository;
    private final FormaPagamentoEntityMapper formaPagamentoEntityMapper;

    @Override
    public FormaPagamento criar(FormaPagamento formaPagamento) {
        FormaPagamentoEntity formaPagamentoEntity = formaPagamentoEntityMapper.toFormaPagamentoEntity(formaPagamento);
        FormaPagamentoEntity formaPagamentoEntitySalva = formaPagamentoRepository.save(formaPagamentoEntity);
        return formaPagamentoEntityMapper.toFormaPagamento(formaPagamentoEntitySalva);
    }

    @Override
    public Optional<FormaPagamento> consultar(Long id) {
        return formaPagamentoRepository.findById(id).map(formaPagamentoEntityMapper::toFormaPagamento);
    }

    @Override
    public FormaPagamento atualizar(FormaPagamento formaPagamento) {
        FormaPagamentoEntity formaPagamentoEntity = formaPagamentoEntityMapper.toFormaPagamentoEntity(formaPagamento);
        FormaPagamentoEntity formaPagamentoAtualizada = formaPagamentoRepository.save(formaPagamentoEntity);
        return formaPagamentoEntityMapper.toFormaPagamento(formaPagamentoAtualizada);
    }

    @Override
    public void deletar(Long id) {
        formaPagamentoRepository.deleteById(id);
    }

    @Override
    public Optional<List<FormaPagamento>> listar() {
        List<FormaPagamentoEntity> formaPagamentoEntities = formaPagamentoRepository.findAll();
        List<FormaPagamento> formasPagamento = formaPagamentoEntities.stream()
                .map(formaPagamentoEntityMapper::toFormaPagamento)
                .toList();
        return Optional.of(formasPagamento);
    }

    @Override
    public Optional<FormaPagamento> consultarPorNome(String nome) {
        List<FormaPagamentoEntity> formaPagamentoEntity = formaPagamentoRepository.findByNome(nome.toUpperCase());
        List<FormaPagamento> formaPagamento = formaPagamentoEntity.stream()
                .map(formaPagamentoEntityMapper::toFormaPagamento)
                .toList();
        return formaPagamento.stream().findFirst();
    }
}
