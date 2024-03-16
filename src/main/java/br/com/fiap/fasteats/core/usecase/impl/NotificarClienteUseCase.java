package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.dataprovider.ContatoPadraoOutputPort;
import br.com.fiap.fasteats.core.dataprovider.NotificarClienteOutputPort;
import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PedidoNotFound;
import br.com.fiap.fasteats.core.domain.model.Cliente;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.domain.valueobject.MensagemNotificacao;
import br.com.fiap.fasteats.core.usecase.NotificarClienteInputPort;

public class NotificarClienteUseCase implements NotificarClienteInputPort {
    private final NotificarClienteOutputPort notificarClienteOutputPort;
    private final PedidoOutputPort pedidoOutputPort;
    private final ContatoPadraoOutputPort contatoPadraoOutputPort;

    public NotificarClienteUseCase(NotificarClienteOutputPort notificarClienteOutputPort, PedidoOutputPort pedidoOutputPort, ContatoPadraoOutputPort contatoPadraoOutputPort) {
        this.notificarClienteOutputPort = notificarClienteOutputPort;
        this.pedidoOutputPort = pedidoOutputPort;
        this.contatoPadraoOutputPort = contatoPadraoOutputPort;
    }

    @Override
    public void pagamentoAprovado(Long pedidoId) {
        Cliente cliente = recuperarCliente(pedidoId);
        String titulo = "Fast-Eats - Pagamento Aprovado";
        String mensagem = String.format("%s,%nSeu pagamento do pedido %d foi aprovado!", formatarNome(cliente.getNomeCompleto()), pedidoId);
        notificarCliente(cliente, titulo, mensagem);
    }

    @Override
    public void erroPagamento(Long pedidoId) {
        Cliente cliente = recuperarCliente(pedidoId);
        String titulo = "Fast-Eats - Erro no Pagamento";
        String mensagem = String.format("%s,%nHouve um erro no pagamento do pedido %d.%nPor favor, tente novamente",
                formatarNome(cliente.getNomeCompleto()), pedidoId);
        notificarCliente(cliente, titulo, mensagem);
    }

    private Cliente recuperarCliente(Long pedidoId) {
        Cliente cliente = consultarPedido(pedidoId).getCliente();
        if (cliente != null) return cliente;
        return new Cliente(null, "Cliente Pedido ", pedidoId.toString(), contatoPadraoOutputPort.emailPadrao());
    }

    private void notificarCliente(Cliente cliente, String titulo, String mensagem) {
        MensagemNotificacao mensagemNotificacao = new MensagemNotificacao(cliente.getEmail(), titulo, mensagem);
        notificarClienteOutputPort.notificar(mensagemNotificacao);
    }

    private Pedido consultarPedido(Long pedidoId) {
        return pedidoOutputPort.consultar(pedidoId).orElseThrow(() -> new PedidoNotFound("Pedido não encontrado"));
    }

    public static String formatarNome(String nomeCompleto) {
        StringBuilder nomeFormatado = new StringBuilder();
        String[] palavras = nomeCompleto.split("\\s+");
        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                if (!nomeFormatado.isEmpty()) nomeFormatado.append(" ");
                nomeFormatado.append(Character.toUpperCase(palavra.charAt(0)));
                if (palavra.length() > 1) nomeFormatado.append(palavra.substring(1).toLowerCase());
            }
        }
        return nomeFormatado.toString();
    }
}
