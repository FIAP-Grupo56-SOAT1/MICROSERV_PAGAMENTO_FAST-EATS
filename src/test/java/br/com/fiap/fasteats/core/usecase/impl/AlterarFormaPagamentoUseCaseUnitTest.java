package br.com.fiap.fasteats.core.usecase.impl;

import br.com.fiap.fasteats.core.domain.model.FormaPagamento;
import br.com.fiap.fasteats.core.domain.model.Pagamento;
import br.com.fiap.fasteats.core.usecase.GerarPagamentoInputPort;
import br.com.fiap.fasteats.core.usecase.PagamentoInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.MERCADO_PAGO;
import static br.com.fiap.fasteats.core.constants.FormaPagamentoConstants.PIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - Alterar Forma de Pagamento")
class AlterarFormaPagamentoUseCaseUnitTest {
    @Mock
    private PagamentoInputPort pagamentoInputPort;
    @Mock
    private GerarPagamentoInputPort gerarPagamentoInputPort;
    @InjectMocks
    private AlterarFormaPagamentoUseCase alterarFormaPagamentoUseCase;
    private final Long PAGAMENTO_ID = 1L;
    private final Long PEDIDO_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve alterar a forma de pagamento de um pagamento")
    void alterarFormaPagamentoInterno() {
        //Arrange
        boolean pagamentoExterno = false;
        Pagamento pagamentoAnterior = getPagamento(PAGAMENTO_ID, PEDIDO_ID, pagamentoExterno);
        Pagamento pagamentoAlterado = getPagamento(PAGAMENTO_ID, PEDIDO_ID, pagamentoExterno);
        Long pagamentoId = pagamentoAlterado.getId();
        Long formaPagamentoId = pagamentoAlterado.getFormaPagamento().getId();
        Long pedidoId = pagamentoAlterado.getPedidoId();

        when(pagamentoInputPort.consultar(pagamentoId)).thenReturn(pagamentoAnterior);
        when(gerarPagamentoInputPort.gerar(pedidoId, formaPagamentoId)).thenReturn(pagamentoAlterado);

        //Act
        Pagamento pagamentoAtualizado = alterarFormaPagamentoUseCase.alterarFormaPagamento(pagamentoId, formaPagamentoId);

        //Assert
        assertEquals(pagamentoAlterado.getId(), pagamentoAtualizado.getId());
        assertEquals(formaPagamentoId, pagamentoAtualizado.getFormaPagamento().getId());
    }

    @Test
    @DisplayName("Deve alterar a forma de pagamento de um pagamento para uma forma de pagamento externa")
    void alterarFormaPagamentoExterna() {
        //Arrange
        boolean pagamentoExterno = true;
        Pagamento pagamentoAnterior = getPagamento(PAGAMENTO_ID, PEDIDO_ID, pagamentoExterno);
        Pagamento pagamentoAlterado = getPagamento(PAGAMENTO_ID, PEDIDO_ID, pagamentoExterno);
        Long pagamentoId = pagamentoAlterado.getId();
        Long formaPagamentoId = pagamentoAlterado.getFormaPagamento().getId();
        Long pedidoId = pagamentoAlterado.getPedidoId();

        when(pagamentoInputPort.consultar(pagamentoId)).thenReturn(pagamentoAnterior);
        when(gerarPagamentoInputPort.gerar(pedidoId, formaPagamentoId)).thenReturn(pagamentoAlterado);

        //Act
        Pagamento pagamentoAtualizado = alterarFormaPagamentoUseCase.alterarFormaPagamento(pagamentoId, formaPagamentoId);

        //Assert
        assertEquals(pagamentoAlterado.getId(), pagamentoAtualizado.getId());
        assertEquals(formaPagamentoId, pagamentoAtualizado.getFormaPagamento().getId());
    }

    private Pagamento getPagamento(Long pagamentoId, Long pedidoId, boolean externo) {
        Pagamento pagamento = new Pagamento();
        Long formaPagamentoId = externo ? 1L : 2L;
        pagamento.setId(pagamentoId);
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(getFormaPagamento(formaPagamentoId, externo ? MERCADO_PAGO : PIX, externo));
        pagamento.setValor(100.0);
        pagamento.setIdPagamentoExterno(null);
        pagamento.setQrCode(null);
        pagamento.setUrlPagamento(null);
        return pagamento;
    }

    private FormaPagamento getFormaPagamento(Long formaPagamentoId, String nome, Boolean externo) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(formaPagamentoId);
        formaPagamento.setNome(nome);
        formaPagamento.setExterno(externo);
        formaPagamento.setAtivo(true);
        return formaPagamento;
    }
}