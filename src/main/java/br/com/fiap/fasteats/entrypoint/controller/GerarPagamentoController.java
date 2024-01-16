package br.com.fiap.fasteats.entrypoint.controller;

import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.GerarPagamentoInputPort;
import br.com.fiap.fasteats.entrypoint.controller.mapper.PagamentoMapper;
import br.com.fiap.fasteats.entrypoint.controller.response.PagamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gerar-pagamento")
@RequiredArgsConstructor
@Tag(name = "Gerar Pagamento", description = "Controller que gerar o pagamento de um pedido")
public class GerarPagamentoController {
    private final GerarPagamentoInputPort gerarPagamentoInputPort;
    private final PagamentoMapper pagamentoMapper;

    @PostMapping("/pedido/{idPedido}/forma-pagamento/{formaPagamentoId}")
    @Operation(summary = "Gerar pagamento de pedido", description = "Gera o pagamento de um pedido.")
    public ResponseEntity<PagamentoResponse> gerarPagamento(@PathVariable("idPedido") final Long idPedido,
                                               @PathVariable("formaPagamentoId") final Long formaPagamentoId) {
        Pagamento pagamento = gerarPagamentoInputPort.gerar(idPedido, formaPagamentoId);
        PagamentoResponse pagamentoResponse = pagamentoMapper.toPagamentoResponse(pagamento);
        return new ResponseEntity<>(pagamentoResponse, HttpStatus.CREATED);
    }
}
