package br.com.fiap.fasteats.dataprovider.client.service;

import br.com.fiap.fasteats.core.domain.valueobject.MensagemNotificacao;
import br.com.fiap.fasteats.dataprovider.client.NotificarClienteIntegration;
import br.com.fiap.fasteats.dataprovider.client.exception.AwsSQSException;
import br.com.fiap.fasteats.dataprovider.client.mapper.NotificarClienteMapper;
import br.com.fiap.fasteats.dataprovider.client.request.NotificarClienteRequest;
import com.google.gson.Gson;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificarClienteIntegrationImpl implements NotificarClienteIntegration {
    @Value("${sqs.queue.notificar.cliente}")
    private String filaNotificarCliente;
    private final SqsTemplate sqsTemplate;
    private final NotificarClienteMapper notificarClienteMapper;

    @Override
    public void notificar(MensagemNotificacao mensagemNotificacao) {
        try {
            NotificarClienteRequest req = notificarClienteMapper.toMensagemNotificacaoRequest(mensagemNotificacao);
            String mensagem = new Gson().toJson(req);
            sqsTemplate.send(filaNotificarCliente, mensagem);
            log.info("Notificação de cliente enviada para a fila {} com sucesso!", filaNotificarCliente);
        } catch (Exception ex) {
            String resposta = String.format("Erro na comunicação com a fila %s: %s", filaNotificarCliente, ex.getMessage());
            log.error(resposta);
            throw new AwsSQSException(resposta);
        }
    }
}
