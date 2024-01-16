package br.com.fiap.fasteats.core.domain.model;

import java.time.LocalDateTime;

public class Pagamento {
    private Long id;
    private Double valor;
    private FormaPagamento formaPagamento;
    private StatusPagamento statusPagamento;
    private Long pedidoId;
    private LocalDateTime dataHoraCriado;
    private LocalDateTime dataHoraProcessamento;
    private LocalDateTime dataHoraFinalizado;
    private Long idPagamentoExterno;
    private String qrCode;
    private String urlPagamento;

    public Pagamento() {
    }

    public Pagamento(Long id,
                     Double valor,
                     FormaPagamento formaPagamento,
                     StatusPagamento statusPagamento,
                     Long pedidoId,
                     LocalDateTime dataHoraCriado,
                     LocalDateTime dataHoraProcessamento,
                     LocalDateTime dataHoraFinalizado,
                     Long idPagamentoExterno,
                     String qrCode,
                     String urlPagamento) {
        this.id = id;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.statusPagamento = statusPagamento;
        this.pedidoId = pedidoId;
        this.dataHoraCriado = dataHoraCriado;
        this.dataHoraProcessamento = dataHoraProcessamento;
        this.dataHoraFinalizado = dataHoraFinalizado;
        this.idPagamentoExterno = idPagamentoExterno;
        this.qrCode = qrCode;
        this.urlPagamento = urlPagamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public LocalDateTime getDataHoraCriado() {
        return dataHoraCriado;
    }

    public void setDataHoraCriado(LocalDateTime dataHoraCriado) {
        this.dataHoraCriado = dataHoraCriado;
    }

    public LocalDateTime getDataHoraProcessamento() {
        return dataHoraProcessamento;
    }

    public void setDataHoraProcessamento(LocalDateTime dataHoraProcessamento) {
        this.dataHoraProcessamento = dataHoraProcessamento;
    }

    public LocalDateTime getDataHoraFinalizado() {
        return dataHoraFinalizado;
    }

    public void setDataHoraFinalizado(LocalDateTime dataHoraFinalizado) {
        this.dataHoraFinalizado = dataHoraFinalizado;
    }

    public Long getIdPagamentoExterno() {
        return idPagamentoExterno;
    }

    public void setIdPagamentoExterno(Long idPagamentoExterno) {
        this.idPagamentoExterno = idPagamentoExterno;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getUrlPagamento() {
        return urlPagamento;
    }

    public void setUrlPagamento(String urlPagamento) {
        this.urlPagamento = urlPagamento;
    }
}
