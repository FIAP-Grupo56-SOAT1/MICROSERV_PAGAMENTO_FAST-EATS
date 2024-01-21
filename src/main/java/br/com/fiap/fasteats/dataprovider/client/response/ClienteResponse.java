package br.com.fiap.fasteats.dataprovider.client.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteResponse {
    private String cpf;
    private String primeiroNome;
    private String ultimoNome;
    private String email;
    private Boolean ativo;
}
