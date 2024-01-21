package br.com.fiap.fasteats.core.usecase.impl.bdd.unit;

import br.com.fiap.fasteats.core.dataprovider.PagamentoExternoOutputPort;
import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.impl.CancelarPagamentoExternoUseCase;
import br.com.fiap.fasteats.core.validator.CancelarPagamentoValidator;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CancelarPagamentoExternoSteps {
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private PagamentoExternoOutputPort pagamentoExternoOutputPort;
    @Mock
    private CancelarPagamentoValidator cancelarPagamentoValidator;
    @InjectMocks
    private CancelarPagamentoExternoUseCase cancelarPagamentoExternoUseCase;
    AutoCloseable openMocks;
    final Long PAGAMENTO_EXTERNO_ID = 1L;
    final Long PEDIDO_ID = 1L;
    private Pagamento pagamento;

    @Before
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Quando("o pagamento externo for cancelado")
    public void o_pagamento_externo_for_cancelado() {
        //Arrange
        pagamento = getPagamentoExterno(PAGAMENTO_EXTERNO_ID, PEDIDO_ID);

        when(pagamentoInputPort.consultarPorIdPagamentoExterno(PAGAMENTO_EXTERNO_ID)).thenReturn(pagamento);

        //Act
        cancelarPagamentoExternoUseCase.cancelar(PAGAMENTO_EXTERNO_ID);
    }

    @Entao("deve ser enviada a solicitação de cancelamento para o serviço de pagamento externo")
    public void deve_ser_enviada_a_solicitação_de_cancelamento_para_o_serviço_de_pagamento_externo() {
        verify(cancelarPagamentoValidator).validarCancelarPagamento(pagamento.getPedidoId());
        verify(pagamentoExternoOutputPort).cancelarPagamento(PAGAMENTO_EXTERNO_ID);
    }

    private Pagamento getPagamentoExterno(Long pagamentoId, Long pedidoId) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamentoExterno(2L));
        pagamento.setValor(100.0);
        pagamento.setIdPagamentoExterno(null);
        pagamento.setQrCode(null);
        pagamento.setUrlPagamento(null);
        return pagamento;
    }

    private FormaPagamento getFormaPagamentoExterno(Long formaPagamentoId) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(formaPagamentoId);
        formaPagamento.setNome(MERCADO_PAGO);
        formaPagamento.setExterno(true);
        formaPagamento.setAtivo(true);
        return formaPagamento;
    }
}
