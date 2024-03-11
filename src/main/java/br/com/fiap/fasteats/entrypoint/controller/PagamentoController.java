package br.com.fiap.fasteats.entrypoint.controller;


import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.*;
import br.com.fiap.fasteats.entrypoint.controller.mapper.PagamentoMapper;
import br.com.fiap.fasteats.entrypoint.controller.response.PagamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pagamentos")
@RequiredArgsConstructor
@Tag(name = "Pagamentos", description = "Controller que gerencia as ações possíveis do pagamento de um pedido")
public class PagamentoController {
    private final PagamentoInputPort pagamentoInputPort;
    private final RealizarPagamentoInputPort realizarPagamentoInputPort;
    private final ReprocessarPagamentoInputPort reprocessarPagamentoInputPort;
    private final CancelarPagamentoInputPort cancelarPagamentoInputPort;
    private final AlterarFormaPagamentoInputPort alterarFormaPagamentoInputPort;
    private final PagamentoMapper pagamentoMapper;

    @PostMapping("{pedidoId}/realizar-pagamento")
    @Operation(summary = "Realizar pagamento", description = "Realiza o pagamento de um pedido.")
    public ResponseEntity<PagamentoResponse> realizarPagamento(@PathVariable("pedidoId") Long pedidoId) {
        Pagamento pagamento = realizarPagamentoInputPort.pagar(pedidoId);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.OK);
    }

    @GetMapping()
    @Operation(summary = "Listar pagamentos", description = "Retorna todos pagamentos de pedidos.")
    public ResponseEntity<List<PagamentoResponse>> listarPagamentos() {
        List<Pagamento> pagamentos = pagamentoInputPort.listar();
        List<PagamentoResponse> pagamentosResponse = pagamentoMapper.toPagamentosResponse(pagamentos);
        return new ResponseEntity<>(pagamentosResponse, HttpStatus.OK);
    }

    @GetMapping("{pagamentoId}")
    @Operation(summary = "Consultar pagamento", description = "Retorna o pagamento do pedido.")
    public ResponseEntity<PagamentoResponse> consultarPagamento(@PathVariable("pagamentoId") Long pagamentoId) {
        Pagamento pagamento = pagamentoInputPort.consultar(pagamentoId);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.OK);
    }

    @GetMapping("{pagamentoExternoId}/consultar-por-id-pagamento-externo")
    @Operation(summary = "Consultar pagamento por id pagamento externo", description = "Retorna o pagamento por id pagamento externo.")
    public ResponseEntity<PagamentoResponse> consultarPorIdPagamentoExterno(@PathVariable("pagamentoExternoId") Long pagamentoExternoId) {
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPagamentoExterno(pagamentoExternoId);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.OK);
    }

    @GetMapping("{pedidoId}/consultar-pagamento-por-id-pedido")
    @Operation(summary = "consultar pagamento por id pedido", description = "Retorna o pagamento por id pedido")
    public ResponseEntity<PagamentoResponse> consultarPagamentoPorIdPedido(@PathVariable("pedidoId") Long pedidoId) {
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(pedidoId);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.OK);
    }

    @PutMapping("{pagamentoId}/alterar-forma-pagamento")
    @Operation(summary = "Alterar forma de pagamento", description = "Altera a forma de pagamento de um pedido.")
    public ResponseEntity<PagamentoResponse> cancelarPagamento(@PathVariable("pagamentoId") Long pagamentoId,
                                                               @RequestParam("formaPagamentoId") Long formaPagamentoId) {
        Pagamento pagamento = alterarFormaPagamentoInputPort.alterarFormaPagamento(pagamentoId, formaPagamentoId);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.OK);
    }

    @PostMapping("{pedidoId}/cancelar-pagamento")
    @Operation(summary = "Cancelar pagamento", description = "Cancela o pagamento de um pedido.")
    public ResponseEntity<PagamentoResponse> cancelarPagamento(@PathVariable("pedidoId") Long pedidoId) {
        Pagamento pagamento = cancelarPagamentoInputPort.cancelar(pedidoId);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.OK);
    }
    
    @PostMapping("{pedidoId}/reprocessar-pagamento")
    @Operation(summary = "Reprocessar pagamento", description = "Reprocessa o pagamento de um pedido.")
    public ResponseEntity<Void> reprocessarPagamento(@PathVariable("pedidoId") Long pedidoId) {
        reprocessarPagamentoInputPort.reprocessar(pedidoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
