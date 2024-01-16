package br.com.fiap.fasteats.entrypoint.controller;


import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.AlterarFormaPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.CancelarPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.RealizarPagamentoInputPort;
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
    private final CancelarPagamentoInputPort cancelarPagamentoInputPort;
    private final AlterarFormaPagamentoInputPort alterarFormaPagamentoInputPort;
    private final PagamentoMapper pagamentoMapper;

    @PostMapping("{idPedido}/realizar-pagamento")
    @Operation(summary = "Realizar pagamento", description = "Realiza o pagamento de um pedido.")
    public ResponseEntity<PagamentoResponse> realizarPagamento(@PathVariable("idPedido") Long idPedido) {
        Pagamento pagamento = realizarPagamentoInputPort.realizarPagamento(idPedido);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.CREATED);
    }

    @GetMapping()
    @Operation(summary = "Listar pagamentos", description = "Retorna todos pagamentos de pedidos.")
    public ResponseEntity<List<PagamentoResponse>> listarPagamentos() {
        List<Pagamento> pagamentos = pagamentoInputPort.listar();
        List<PagamentoResponse> pagamentosResponse = pagamentoMapper.toPagamentosResponse(pagamentos);
        return new ResponseEntity<>(pagamentosResponse, HttpStatus.OK);
    }

    @GetMapping("{idPagamento}")
    @Operation(summary = "Consultar pagamento", description = "Retorna o pagamento do pedido.")
    public ResponseEntity<PagamentoResponse> consultarPagamento(@PathVariable("idPagamento") Long idPagamento) {
        Pagamento pagamento = pagamentoInputPort.consultar(idPagamento);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.OK);
    }

    @GetMapping("{idPagamentoExterno}/consultar-por-id-pagamento-externo")
    @Operation(summary = "Consultar pagamento por id pagamento externo", description = "Retorna o pagamento por id pagamento externo.")
    public ResponseEntity<PagamentoResponse> consultarPorIdPagamentoExterno(@PathVariable("idPagamentoExterno") Long idPagamentoExterno) {
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPagamentoExterno(idPagamentoExterno);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.OK);
    }

    @GetMapping("{idPedido}/consultar-pagamento-por-id-pedido")
    @Operation(summary = "consultar pagamento por id pedido", description = "Retorna o pagamento por id pedido")
    public ResponseEntity<PagamentoResponse> consultarPagamentoPorIdPedido(@PathVariable("idPedido") Long idPedido) {
        Pagamento pagamento = pagamentoInputPort.consultarPorIdPedido(idPedido);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.OK);
    }

    @PutMapping("{idPagamento}/alterar-forma-pagamento")
    @Operation(summary = "Alterar forma de pagamento", description = "Altera a forma de pagamento de um pedido.")
    public ResponseEntity<PagamentoResponse> cancelarPagamento(@PathVariable("idPagamento") Long idPagamento,
                                                               @RequestParam("idFormaPagamento") Long idFormaPagamento) {
        Pagamento pagamento = alterarFormaPagamentoInputPort.alterarFormaPagamento(idPagamento, idFormaPagamento);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.OK);
    }

    @PostMapping("{idPedido}/cancelar-pagamento")
    @Operation(summary = "Cancelar pagamento", description = "Cancela o pagamento de um pedido.")
    public ResponseEntity<PagamentoResponse> cancelarPagamento(@PathVariable("idPedido") Long idPedido) {
        Pagamento pagamento = cancelarPagamentoInputPort.cancelar(idPedido);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.OK);
    }
}
