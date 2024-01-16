package br.com.fiap.fasteats.entrypoint.controller;

import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.entrypoint.controller.mapper.StatusPagamentoMapper;
import br.com.fiap.fasteats.entrypoint.controller.response.StatusPagamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("status-pagamentos")
@RequiredArgsConstructor
@Tag(name = "Status de Pagamento", description = "Controller que gerencia os status de pagemento de um produto")
public class StatusPagamentoController {
    private final StatusPagamentoInputPort statusPagamentoInputPort;
    private final StatusPagamentoMapper statusPagamentoMapper;

    @GetMapping("{id}")
    @Operation(summary = "Consultar status pagamento por ID", description = "Retorna o status pagamento.")
    public ResponseEntity<StatusPagamentoResponse> consultarStatusPagamento(@PathVariable("id") final Long id) {
        StatusPagamento statusPagamento = statusPagamentoInputPort.consultar(id);
        StatusPagamentoResponse statusPagamentoResponse = statusPagamentoMapper.toStatusPagamentoResponse(statusPagamento);
        return ResponseEntity.ok().body(statusPagamentoResponse);
    }

    @GetMapping("consultar-por-nome/{nome}")
    @Operation(summary = "Consultar forma de pagamento por nome", description = "Retorna o status pagamento por nome.")
    public ResponseEntity<StatusPagamentoResponse> consultarStatusPagamentoPorNome(@PathVariable("nome") final String nome) {
        StatusPagamento statusPagamento = statusPagamentoInputPort.consultarPorNome(nome);
        StatusPagamentoResponse statusPagamentoResponse = statusPagamentoMapper.toStatusPagamentoResponse(statusPagamento);
        return ResponseEntity.ok().body(statusPagamentoResponse);
    }

    @GetMapping
    @Operation(summary = "Listar status pagamento", description = "Retorna o status pagamento.")
    public ResponseEntity<List<StatusPagamentoResponse>> listarStatusPedidos() {
        List<StatusPagamento> listStatusPagamento = statusPagamentoInputPort.listar();
        List<StatusPagamentoResponse> statusPagamentoResponses = new ArrayList<>();

        for (StatusPagamento statusPagamento : listStatusPagamento) {
            StatusPagamentoResponse statusPagamentoResponse = statusPagamentoMapper.toStatusPagamentoResponse(statusPagamento);
            statusPagamentoResponses.add(statusPagamentoResponse);
        }

        return ResponseEntity.ok().body(statusPagamentoResponses);
    }
}
