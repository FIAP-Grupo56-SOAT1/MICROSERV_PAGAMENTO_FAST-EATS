package br.com.fiap.fasteats.dataprovider.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificarClienteRequest {
    private String email;
    private String titulo;
    private String mensagem;
}
