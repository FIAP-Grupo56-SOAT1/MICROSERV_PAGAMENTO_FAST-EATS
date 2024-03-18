package br.com.fiap.fasteats.entrypoint.queue;

import br.com.fiap.fasteats.core.dataprovider.AlterarPedidoStatusOutputPort;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.AlterarPagamentoStatusInputPort;
import br.com.fiap.fasteats.core.usecase.NotificarClienteInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.ReceberPedidoPagoInputPort;
import br.com.fiap.fasteats.dataprovider.client.response.PagamentoPedidoResponse;
import com.google.gson.Gson;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.STATUS_EM_PROCESSAMENTO;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.cloud.aws.sqs.enabled", havingValue = "true", matchIfMissing = true)
public class PagamentoPedidoIntegration {
    @Value("${sqs.queue.pagamento.receber.pedido.pago}")
    private String filaPagamentoReceberPedidoPago;
    @Value("${sqs.queue.pagamento.erro.pagamento-pedido}")
    private String filaPagamentoErroPagamentoPedido;
    @Value("${sqs.queue.pagamento.erro.pedido.cancelar}")
    private String filaPagamentoErroPedidoCancelado;
    private final ReceberPedidoPagoInputPort receberPedidoPagoInputPort;
    private final AlterarPedidoStatusOutputPort alterarPedidoStatusOutputPort;
    private final AlterarPagamentoStatusInputPort alterarPagamentoStatusInputPort;
    private final PagamentoInputPort pagamentoInputPort;
    private final NotificarClienteInputPort notificarClienteInputPort;

    @SqsListener("${sqs.queue.pagamento.receber.pedido.pago}")
    public void receber(String mensagem) {
        Long pedidoId = pedidoIdFromJson(mensagem);
        try {
            receberPedidoPagoInputPort.receber(pedidoId);
            notificarClienteInputPort.pagamentoAprovado(pedidoId);
            String mensagemSucesso = String.format("Pedido %d pago recebido da fila %s com sucesso!", pedidoId, filaPagamentoReceberPedidoPago);
            log.info(mensagemSucesso);
        } catch (Exception ex) {
            String mensagemErro = String.format("Erro ao processar receber pedido %d da fila %s : {}", pedidoId, filaPagamentoReceberPedidoPago);
            log.error(mensagemErro, ex.getMessage());
            alterarPedidoStatusOutputPort.aguardandoPagamento(pedidoId);
            Long pagamentoId = pagamentoInputPort.consultarPorIdPedido(pedidoId).getId();
            alterarPagamentoStatusInputPort.emProcessamento(pagamentoId);
            notificarClienteInputPort.erroPagamento(pedidoId);
        }
    }

    @SqsListener("${sqs.queue.pagamento.erro.pagamento-pedido}")
    public void erroPagamentoPedido(String mensagem) {
        Long pedidoId = pedidoIdFromJson(mensagem);
        try {
            Long pagamentoId = pagamentoInputPort.consultarPorIdPedido(pedidoId).getId();
            alterarPagamentoStatusInputPort.emProcessamento(pagamentoId);
            notificarClienteInputPort.erroPagamento(pedidoId);
            String mensagemSucesso = String.format("O pagamento %d retornou para o status %s por erro no pagamento do pedido %d, mensagem da fila %s!",
                    pagamentoId, STATUS_EM_PROCESSAMENTO, pedidoId, filaPagamentoErroPagamentoPedido);
            log.info(mensagemSucesso);
        } catch (Exception ex) {
            String mensagemErro = String.format("Erro ao processar mensagem do pedido %d da fila %s : {}", pedidoId, filaPagamentoErroPagamentoPedido);
            log.error(mensagemErro, ex.getMessage());
            throw ex;
        }
    }

    @SqsListener("${sqs.queue.pagamento.erro.pedido.cancelar}")
    public void erroCancelarPedido(String mensagem) {
        Long pedidoId = pedidoIdFromJson(mensagem);
        try {
            Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(pedidoId);
            if (!pagamento.getStatusPagamento().getNome().equals(STATUS_EM_PROCESSAMENTO)) {
                alterarPagamentoStatusInputPort.emProcessamento(pedidoId);
            }
            String mensagemSucesso = String.format("Pedido %d foi recebido da fila %s com sucesso!", pedidoId, filaPagamentoErroPedidoCancelado);
            log.info(mensagemSucesso);
        } catch (Exception ex) {
            String mensagemErro = String.format("Erro ao processar mensagem do pedido %d da fila %s : {}", pedidoId, filaPagamentoErroPedidoCancelado);
            log.error(mensagemErro, ex.getMessage());
            throw ex;
        }
    }

    private Long pedidoIdFromJson(String mensagem) {
        PagamentoPedidoResponse pedidoPagoResponse = new Gson().fromJson(mensagem, PagamentoPedidoResponse.class);
        return pedidoPagoResponse.getPedidoId();
    }
}
