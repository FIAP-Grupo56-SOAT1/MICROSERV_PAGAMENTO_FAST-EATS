package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.PagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.dataprovider.repository.PagamentoRepository;
import br.com.fiap.fasteats.dataprovider.repository.entity.PagamentoEntity;
import br.com.fiap.fasteats.dataprovider.repository.mapper.PagamentoEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PagamentoAdapter implements PagamentoOutputPort {
    private final PagamentoRepository pagamentoRepository;
    private final PagamentoEntityMapper pagamentoEntityMapper;

    @Override
    public List<Pagamento> listar() {
        List<PagamentoEntity> listPagamentos = pagamentoRepository.findAll();
        return listPagamentos
                .stream()
                .map(pagamentoEntityMapper::toPagamento)
                .toList();
    }

    @Override
    public Optional<Pagamento> consultar(Long id) {
        return pagamentoRepository.findById(id).map(pagamentoEntityMapper::toPagamento);
    }

    @Override
    public Optional<Pagamento> consultarPorPedidoId(long pedidoId) {
        return pagamentoRepository.findFirstByPedidoIdOrderByDataHoraCriadoDesc(pedidoId).map(pagamentoEntityMapper::toPagamento);
    }

    @Override
    public Pagamento salvarPagamento(Pagamento pagamento) {
        PagamentoEntity pagamentoEntity = pagamentoEntityMapper.toPagamentoEntity(pagamento);
        PagamentoEntity pagamentoEntitySalvo = pagamentoRepository.saveAndFlush(pagamentoEntity);
        pagamentoRepository.flush();
        return pagamentoEntityMapper.toPagamento(pagamentoEntitySalvo);
    }

    @Override
    public Optional<Pagamento> consultarPorIdPagamentoExterno(Long idPagamentoExterno) {
        return pagamentoRepository.findPagamentoByIdPagamentoExterno(idPagamentoExterno).map(pagamentoEntityMapper::toPagamento);
    }
}
