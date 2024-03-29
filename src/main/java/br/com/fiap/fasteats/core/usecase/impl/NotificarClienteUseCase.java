package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.constants.StatusMensagemConstants;
import br.com.fiap.fasteats.core.dataprovider.ContatoPadraoOutputPort;
import br.com.fiap.fasteats.core.dataprovider.NotificarClienteOutputPort;
import br.com.fiap.fasteats.core.dataprovider.PedidoOutputPort;
import br.com.fiap.fasteats.core.domain.exception.PedidoNotFound;
import br.com.fiap.fasteats.core.domain.model.Cliente;
import br.com.fiap.fasteats.core.domain.model.Pedido;
import br.com.fiap.fasteats.core.domain.valueobject.MensagemNotificacaoCliente;
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
        if (Boolean.FALSE.equals(cliente.getAtivo())) return;
        String titulo = "Fast-Eats - Pagamento Aprovado";
        String mensagem = String.format("%s,%nSeu pagamento do pedido %d foi aprovado!", formatarNome(cliente.getNomeCompleto()), pedidoId);
        notificarCliente(cliente, titulo, mensagem, StatusMensagemConstants.SUCESSO_PAGAMENTO);
    }

    @Override
    public void erroPagamento(Long pedidoId) {
        Cliente cliente = recuperarCliente(pedidoId);
        if (Boolean.FALSE.equals(cliente.getAtivo())) return;
        String titulo = "Fast-Eats - Erro no Pagamento";
        String mensagem = String.format("%s,%nHouve um erro no pagamento do pedido %d.%nPor favor, tente novamente",
                formatarNome(cliente.getNomeCompleto()), pedidoId);
        notificarCliente(cliente, titulo, mensagem, StatusMensagemConstants.ERRO_PAGAMENTO);
    }

    private Cliente recuperarCliente(Long pedidoId) {
        Cliente cliente = consultarPedido(pedidoId).getCliente();
        if (cliente != null) return cliente;
        return new Cliente(null, null, "Cliente Pedido ", pedidoId.toString(), contatoPadraoOutputPort.emailPadrao(), true);
    }

    private void notificarCliente(Cliente cliente, String titulo, String mensagem, StatusMensagemConstants status) {
        MensagemNotificacaoCliente mensagemNotificacaoCliente = new MensagemNotificacaoCliente(cliente.getId() ,cliente.getEmail(), titulo, mensagem, status);
        notificarClienteOutputPort.notificar(mensagemNotificacaoCliente);
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
