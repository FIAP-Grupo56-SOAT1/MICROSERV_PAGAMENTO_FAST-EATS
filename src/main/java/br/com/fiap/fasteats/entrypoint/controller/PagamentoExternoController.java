package br.com.fiap.fasteats.entrypoint.controller;


import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.PagamentoExterno;
import br.com.fiap.fasteats.core.usecase.PagamentoExternoInputPort;
import br.com.fiap.fasteats.entrypoint.controller.mapper.PagamentoMapper;
import br.com.fiap.fasteats.entrypoint.controller.request.PagamentoWebhookRequest;
import br.com.fiap.fasteats.entrypoint.controller.request.StatusPagamentoRequest;
import br.com.fiap.fasteats.entrypoint.controller.response.PagamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pagamento-externo")
@RequiredArgsConstructor
@Tag(name = "Pagamento Externo", description = "Controller que gerencia as ações possíveis de integracao com pagamento externo")
public class PagamentoExternoController {
    public static final boolean SIMULACAO_PAGAMENTO = true;
    private final PagamentoExternoInputPort pagamentoExternoInputPort;
    private final PagamentoMapper pagamentoMapper;

    @PostMapping("mercadopago-webhook-simulacao")
    @Operation(summary = "Webhook para simular retorno pagamento externo", description = "Webhook para simular retorno de pagamento externo")
    public ResponseEntity<PagamentoResponse> webhookPagamentoSimulacao(@RequestParam(value = "idPagamentoExterno") Long idPagamentoExterno,
                                                                       @RequestParam("statusMercadoPago") StatusPagamentoRequest statusMercadoPago) {
        PagamentoExterno pagamentoExterno = new PagamentoExterno(idPagamentoExterno, statusMercadoPago.name().toLowerCase(), SIMULACAO_PAGAMENTO);
        Pagamento pagamento = pagamentoExternoInputPort.atualizarPagamento(pagamentoExterno);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.OK);
    }

    @PostMapping("mercadopago-webhook")
    @Operation(summary = "Webhook Pagamento", description = "Webhook para retorno de pagamento")
    public ResponseEntity<PagamentoResponse> webhookPagamento(@Valid @RequestBody PagamentoWebhookRequest pagamentoWebhookRequest) {
        PagamentoExterno pagamentoExterno = new PagamentoExterno(Long.valueOf(pagamentoWebhookRequest.getData().getId()));
        Pagamento pagamento = pagamentoExternoInputPort.atualizarPagamento(pagamentoExterno);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.OK);
    }

    @PostMapping("mercadopago/{idPagamentoExterno}/cancelar")
    @Operation(summary = "Cancelar pagamento do Mercado Pago", description = "Cancela um pagamento que a forma de pagamento é Mercado Pago")
    public ResponseEntity<Void> cancelarPagamento(@PathVariable(value = "idPagamentoExterno") Long idPagamentoExterno) {
        pagamentoExternoInputPort.cancelarPagamentoExterno(idPagamentoExterno);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
