package br.com.fiap.fasteats.core.domain.model;


public class Pedido {
    private Long id;
    private String statusPedido;
    private Double valor;
    private Long idPagamentoExterno;
    private String qrCode;
    private String urlPagamento;

    public Pedido() {
    }

    public Pedido(Long id, String statusPedido, Double valor, Long idPagamentoExterno, String qrCode, String urlPagamento) {
        this.id = id;
        this.statusPedido = statusPedido;
        this.valor = valor;
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

    public String getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(String statusPedido) {
        this.statusPedido = statusPedido;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
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