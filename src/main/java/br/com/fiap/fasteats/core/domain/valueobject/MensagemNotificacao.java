package br.com.fiap.fasteats.core.domain.valueobject;

public class MensagemNotificacao {
    private String email;
    private String titulo;
    private String mensagem;

    public MensagemNotificacao(String email, String titulo, String mensagem) {
        this.email = email;
        this.titulo = titulo;
        this.mensagem = mensagem;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
