package br.com.fiap.fasteats.entrypoint.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PagamentoWebhookDataRequest {

    @JsonProperty("id")
    @NotNull
    @NotEmpty
    private String id;

}
