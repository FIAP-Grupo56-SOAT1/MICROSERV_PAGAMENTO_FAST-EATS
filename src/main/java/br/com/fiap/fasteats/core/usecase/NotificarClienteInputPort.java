package br.com.fiap.fasteats.core.usecase;

public interface NotificarClienteInputPort {
    void pagamentoAprovado(Long pedidoId);
    void erroPagamento(Long pedidoId);
}
