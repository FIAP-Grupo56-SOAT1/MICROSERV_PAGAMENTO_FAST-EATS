package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.FormaPagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.FormaPagamentoNotFound;
import br.com.fiap.fasteats.core.domain.exception.RegraNegocioException;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;

import java.util.List;

public class FormaPagamentoUseCase implements FormaPagamentoInputPort {
    private final FormaPagamentoOutputPort formaPagamentoOutputPort;

    public FormaPagamentoUseCase(FormaPagamentoOutputPort formaPagamentoOutputPort) {
        this.formaPagamentoOutputPort = formaPagamentoOutputPort;
    }

    @Override
    public FormaPagamento criar(FormaPagamento formaPagamento) {
        validarCriar(formaPagamento);
        formaPagamento.setNome(formaPagamento.getNome().toUpperCase());
        formaPagamento.setAtivo(true);
        return formaPagamentoOutputPort.criar(formaPagamento);
    }

    @Override
    public FormaPagamento consultar(Long id) {
        return formaPagamentoOutputPort.consultar(id).orElseThrow(() -> new FormaPagamentoNotFound("Forma de pagamento não encontrada"));
    }

    @Override
    public FormaPagamento atualizar(FormaPagamento formaPagamento) {
        checarFormaPagamentoExistente(formaPagamento.getId());
        if (formaPagamento.getAtivo() == null) formaPagamento.setAtivo(true);
        formaPagamento.setNome(formaPagamento.getNome().toUpperCase());
        return formaPagamentoOutputPort.atualizar(formaPagamento);
    }

    @Override
    public void deletar(Long id) {
        checarFormaPagamentoExistente(id);
        formaPagamentoOutputPort.deletar(id);
    }

    @Override
    public List<FormaPagamento> listar() {
        return formaPagamentoOutputPort.listar().orElseThrow(() -> new FormaPagamentoNotFound("Nenhuma forma de pagamento encontrada"));
    }

    @Override
    public FormaPagamento consultarPorNome(String nome) {
        return formaPagamentoOutputPort.consultarPorNome(nome.toUpperCase()).orElseThrow(() -> new FormaPagamentoNotFound("Forma de pagamento não encontrada"));
    }

    private void validarCriar(FormaPagamento formaPagamento) {
        if (formaPagamentoOutputPort.consultarPorNome(formaPagamento.getNome().toUpperCase()).isPresent())
            throw new RegraNegocioException("Forma de pagamento já cadastrada");
    }

    private void checarFormaPagamentoExistente(Long formaPagamentoId) {
        if (!existeFormaPagamento(formaPagamentoId))
            throw new FormaPagamentoNotFound("Forma de pagamento não cadastrada");
    }

    private boolean existeFormaPagamento(Long formaPagamentoId) {
        return formaPagamentoOutputPort.consultar(formaPagamentoId).isPresent();
    }
}
