package br.com.fiap.fasteats.entrypoint.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PagamentoWebhookRequest {

    @JsonProperty("id")
    private String id;
    @JsonProperty("live_mode")
    private boolean liveMode;
    @JsonProperty("type")
    private String type;
    @JsonProperty("date_created")
    private String dateCreated;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("api_version")
    private String apiVersion;
    @JsonProperty("action")
    private String action;
    @JsonProperty("data")
    private PagamentoWebhookDataRequest data;
}
