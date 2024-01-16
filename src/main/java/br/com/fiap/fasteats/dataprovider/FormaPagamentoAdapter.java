package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.dataprovider.repository.FormaPagamentoRepository;
import br.com.fiap.fasteats.dataprovider.repository.mapper.FormaPagamentoEntityMapper;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.dataprovider.FormaPagamentoOutputPort;
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
        var formaPagamentoEntity = formaPagamentoEntityMapper.toFormaPagamentoEntity(formaPagamento);
        var formaPagamentoEntitySalvo = formaPagamentoRepository.save(formaPagamentoEntity);
        return formaPagamentoEntityMapper.toFormaPagamento(formaPagamentoEntitySalvo);
    }

    @Override
    public Optional<FormaPagamento> consultar(Long id) {
        return formaPagamentoRepository.findById(id).map(formaPagamentoEntityMapper::toFormaPagamento);
    }

    @Override
    public FormaPagamento atualizar(FormaPagamento formaPagamento) {
        var formaPagamentoEntity = formaPagamentoEntityMapper.toFormaPagamentoEntity(formaPagamento);
        var formaPagamentoAtualizada = formaPagamentoRepository.save(formaPagamentoEntity);
        return formaPagamentoEntityMapper.toFormaPagamento(formaPagamentoAtualizada);
    }

    @Override
    public void deletar(Long id) {
        formaPagamentoRepository.deleteById(id);
    }

    @Override
    public Optional<List<FormaPagamento>> listar() {
        var formaPagamentoEntities = formaPagamentoRepository.findAll();
        var formasPagamento = formaPagamentoEntities.stream()
                .map(formaPagamentoEntityMapper::toFormaPagamento)
                .toList();
        return Optional.of(formasPagamento);
    }

    @Override
    public Optional<FormaPagamento> consultarPorNome(String nome) {
        var formaPagamentoEntity = formaPagamentoRepository.findByNome(nome.toUpperCase());
        var formaPagamento = formaPagamentoEntity.stream()
                .map(formaPagamentoEntityMapper::toFormaPagamento)
                .toList();
        return formaPagamento.stream().findFirst();
    }
}
