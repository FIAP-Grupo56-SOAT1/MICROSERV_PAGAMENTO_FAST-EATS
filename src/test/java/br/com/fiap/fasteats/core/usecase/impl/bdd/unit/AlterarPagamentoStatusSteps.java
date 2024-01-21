package br.com.fiap.fasteats.core.usecase.impl.bdd.unit;

import br.com.fiap.fasteats.core.dataprovider.PagamentoOutputPort;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.domain.model.StatusPagamento;
import br.com.fiap.fasteats.core.usecase.StatusPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.AlterarPagamentoStatusUseCase;
import br.com.fiap.fasteats.core.validator.AlterarPagamentoStatusValidator;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static br.com.fiap.fasteats.core.constants.StatusPagamentoConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AlterarPagamentoStatusSteps {
    @Mock
    private StatusPagamentoInputPort statusPagamentoInputPort;
    @Mock
    private PagamentoOutputPort pagamentoOutputPort;
    @Mock
    private AlterarPagamentoStatusValidator alterarPagamentoStatusValidator;
    @InjectMocks
    private AlterarPagamentoStatusUseCase alterarPagamentoStatusUseCase;
    private final Long PAGAMENTO_ID = 1L;
    private final Long PEDIDO_ID = 1L;
    private Pagamento pagamento;
    private Pagamento resultado;
    AutoCloseable openMocks;

    @Before
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Dado("que o pagamento exista no sistema")
    public void que_o_pagamento_exista_no_sistema() {
        pagamento = getPagamento(PAGAMENTO_ID, PEDIDO_ID);
        when(pagamentoOutputPort.consultar(pagamento.getId())).thenReturn(java.util.Optional.of(pagamento));
    }

    @Quando("o status do pagamento for alterado para recusado")
    public void o_status_do_pagamento_for_alterado_para_recusado() {
        StatusPagamento statusPagamentoRecusado = getStatusPagamento(2L, STATUS_RECUSADO);
        Pagamento pagamentoRecusado = getPagamento(PAGAMENTO_ID, PEDIDO_ID);
        pagamentoRecusado.setStatusPagamento(statusPagamentoRecusado);

        when(statusPagamentoInputPort.consultarPorNome(STATUS_RECUSADO)).thenReturn(statusPagamentoRecusado);
        when(pagamentoOutputPort.salvarPagamento(pagamento)).thenReturn(pagamentoRecusado);

        resultado = alterarPagamentoStatusUseCase.recusado(PAGAMENTO_ID);
    }

    @Entao("o status do pagamento deve ser alterado para recusado")
    public void o_status_do_pagamento_deve_ser_alterado_para_recusado() {
        assertNotNull(resultado);
        assertEquals(STATUS_RECUSADO, resultado.getStatusPagamento().getNome());
        verify(alterarPagamentoStatusValidator).validarRecusado(pagamento.getId());
        verify(pagamentoOutputPort).salvarPagamento(pagamento);
    }

    @Quando("o status do pagamento for alterado para cancelado")
    public void o_status_do_pagamento_for_alterado_para_cancelado() {
        StatusPagamento statusPagamentoCancelado = getStatusPagamento(3L, STATUS_CANCELADO);
        Pagamento pagamentoCancelado = getPagamento(PAGAMENTO_ID, PEDIDO_ID);
        pagamentoCancelado.setStatusPagamento(statusPagamentoCancelado);
        pagamentoCancelado.setDataHoraFinalizado(LocalDateTime.now());

        when(statusPagamentoInputPort.consultarPorNome(STATUS_CANCELADO)).thenReturn(statusPagamentoCancelado);
        when(pagamentoOutputPort.salvarPagamento(pagamento)).thenReturn(pagamentoCancelado);

        resultado = alterarPagamentoStatusUseCase.cancelado(PAGAMENTO_ID);
    }

    @Entao("o status do pagamento deve ser alterado para cancelado")
    public void o_status_do_pagamento_deve_ser_alterado_para_cancelado() {
        assertNotNull(resultado);
        assertEquals(STATUS_CANCELADO, resultado.getStatusPagamento().getNome());
        verify(alterarPagamentoStatusValidator).validarCancelado(pagamento.getId());
        verify(pagamentoOutputPort).salvarPagamento(pagamento);
    }

    @Quando("o status do pagamento for alterado para pago")
    public void o_status_do_pagamento_for_alterado_para_pago() {
        StatusPagamento statusPagamentoPago = getStatusPagamento(4L, STATUS_PAGO);
        Pagamento pagamentoPago = getPagamento(PAGAMENTO_ID, PEDIDO_ID);
        pagamentoPago.setStatusPagamento(statusPagamentoPago);
        pagamentoPago.setDataHoraFinalizado(LocalDateTime.now());

        when(statusPagamentoInputPort.consultarPorNome(STATUS_PAGO)).thenReturn(statusPagamentoPago);
        when(pagamentoOutputPort.salvarPagamento(pagamento)).thenReturn(pagamentoPago);

        resultado = alterarPagamentoStatusUseCase.pago(PAGAMENTO_ID);
    }

    @Entao("o status do pagamento deve ser alterado para pago")
    public void o_status_do_pagamento_deve_ser_alterado_para_pago() {
        assertNotNull(resultado);
        assertEquals(STATUS_PAGO, resultado.getStatusPagamento().getNome());
        verify(alterarPagamentoStatusValidator).validarPago(pagamento.getId());
        verify(pagamentoOutputPort).salvarPagamento(pagamento);
    }

    @Entao("a data e hora de finalizacao do pagamento deve ser preenchida")
    public void a_data_e_hora_de_finalizacao_do_pagamento_deve_ser_preenchida() {
        assertNotNull(resultado.getDataHoraFinalizado());
    }

    private Pagamento getPagamento(Long pagamentoId, Long pedidoId) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamentoInterno(1L));
        pagamento.setStatusPagamento(getStatusPagamento(1L, STATUS_EM_PROCESSAMENTO));
        pagamento.setValor(100.0);
        pagamento.setIdPagamentoExterno(null);
        pagamento.setQrCode(null);
        pagamento.setUrlPagamento(null);
        return pagamento;
    }

    private FormaPagamento getFormaPagamentoInterno(Long formaPagamentoId) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(formaPagamentoId);
        formaPagamento.setNome(PIX);
        formaPagamento.setExterno(false);
        formaPagamento.setAtivo(true);
        return formaPagamento;
    }

    private StatusPagamento getStatusPagamento(Long statusPagamentoId, String nomeStatusPagamento) {
        StatusPagamento statusPagamento = new StatusPagamento();
        statusPagamento.setId(statusPagamentoId);
        statusPagamento.setNome(nomeStatusPagamento);
        return statusPagamento;
    }
}
