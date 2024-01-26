package br.com.fiap.fasteats.entrypoint.controller;

import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.usecase.FormaPagamentoInputPort;
import br.com.fiap.fasteats.entrypoint.controller.mapper.FormaPagamentoMapper;
import br.com.fiap.fasteats.entrypoint.controller.request.FormaPagamentoRequest;
import br.com.fiap.fasteats.entrypoint.controller.response.FormaPagamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("forma-pagamento")
@RequiredArgsConstructor
@Tag(name = "Forma de Pagamento", description = "Controller que gerencia as possíveis formas de pagamento de um pedido")
public class FormaPagamentoController {
    private final FormaPagamentoInputPort formaPagamentoInputPort;
    private final FormaPagamentoMapper formaPagamentoMapper;

    @PostMapping
    @Operation(summary = "Criar forma de pagamento", description = "Cria uma nova forma de pagamento de um pedido.")
    public ResponseEntity<FormaPagamentoResponse> criarFormaPagamento(@Valid @RequestBody FormaPagamentoRequest formaPagamentoRequest) {
        FormaPagamento formaPagamento = formaPagamentoMapper.toFormaPagamento(formaPagamentoRequest);
        FormaPagamento formaPagamentoCriado = formaPagamentoInputPort.criar(formaPagamento);
        FormaPagamentoResponse formaPagamentoResponse = formaPagamentoMapper.toFormaPagamentoResponse(formaPagamentoCriado);
        return ResponseEntity.ok().body(formaPagamentoResponse);
    }

    @PatchMapping("{id}")
    @Operation(summary = "Atualizar forma de pagamento", description = "Atualiza a forma de pagamento de um pedido.")
    public ResponseEntity<FormaPagamentoResponse> atualizarFormaPagamento(@PathVariable("id") final Long id, @RequestBody FormaPagamentoRequest formaPagamentoRequest) {
        FormaPagamento formaPagamento = formaPagamentoMapper.toFormaPagamento(formaPagamentoRequest);
        formaPagamento.setId(id);
        FormaPagamento formaPagamentoAtualizado = formaPagamentoInputPort.atualizar(formaPagamento);
        FormaPagamentoResponse formaPagamentoResponse = formaPagamentoMapper.toFormaPagamentoResponse(formaPagamentoAtualizado);
        return ResponseEntity.ok().body(formaPagamentoResponse);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletar forma de pagamento", description = "Deleta um meio de pagamento de pedidos.")
    public ResponseEntity<Void> deletarFormaPagamento(@PathVariable("id") final Long id) {
        formaPagamentoInputPort.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    @Operation(summary = "Consultar forma de pagamento por ID", description = "Retorna a forma de pagamento de pedido.")
    public ResponseEntity<FormaPagamentoResponse> consultarFormaPagamento(@PathVariable("id") final Long id) {
        FormaPagamento formaPagamento = formaPagamentoInputPort.consultar(id);
        FormaPagamentoResponse formaPagamentoResponse = formaPagamentoMapper.toFormaPagamentoResponse(formaPagamento);
        return ResponseEntity.ok().body(formaPagamentoResponse);
    }

    @GetMapping("consultar-por-nome/{nome}")
    @Operation(summary = "Consultar forma de pagamento por nome", description = "Retorna a forma de pagamento de pedido por nome.")
    public ResponseEntity<FormaPagamentoResponse> consultarFormaPagamentoPorNome(@PathVariable("nome") final String nome) {
        FormaPagamento formaPagamento = formaPagamentoInputPort.consultarPorNome(nome);
        FormaPagamentoResponse formaPagamentoResponse = formaPagamentoMapper.toFormaPagamentoResponse(formaPagamento);
        return ResponseEntity.ok().body(formaPagamentoResponse);
    }

    @GetMapping
    @Operation(summary = "Listar forma de pagamento", description = "Retorna todas as formas de pagamento.")
    public ResponseEntity<List<FormaPagamentoResponse>> listarFormaPagamentos() {
        List<FormaPagamento> formaPagamento = formaPagamentoInputPort.listar();
        List<FormaPagamentoResponse> formaPagamentoResponse = formaPagamentoMapper.toFormaPagamentoResponseList(formaPagamento);
        return ResponseEntity.ok().body(formaPagamentoResponse);
    }
}
