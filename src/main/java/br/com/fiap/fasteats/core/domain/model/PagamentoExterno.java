package br.com.fiap.fasteats.core.domain.model;


public class PagamentoExterno {
    private Long id;
    private String status;
    private boolean simulacaoPagamento;
    private String mensagem;
    private String qrCode;
    private String qrCodeBase64;
    private String urlPagamento;

    public PagamentoExterno() {
    }

    public PagamentoExterno(Long id) {
        this.id = id;
    }

    public PagamentoExterno(Long id, String status, boolean simulacaoPagamento) {
        this.id = id;
        this.status = status;
        this.simulacaoPagamento = simulacaoPagamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSimulacaoPagamento() {
        return simulacaoPagamento;
    }

    public void setSimulacaoPagamento(boolean simulacaoPagamento) {
        this.simulacaoPagamento = simulacaoPagamento;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCodeBase64() {
        return qrCodeBase64;
    }

    public void setQrCodeBase64(String qrCodeBase64) {
        this.qrCodeBase64 = qrCodeBase64;
    }

    public String getUrlPagamento() {
        return urlPagamento;
    }

    public void setUrlPagamento(String urlPagamento) {
        this.urlPagamento = urlPagamento;
    }
}
