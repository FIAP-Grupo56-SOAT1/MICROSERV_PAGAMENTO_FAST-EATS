package br.com.fiap.fasteats.dataprovider.repository.entity;

import br.com.fiap.fasteats.core.constants.StatusMensagemConstants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Notificacao")
@Table(name = "NOTIFICACAO")
public class NotificacaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificacao_id")
    private Long id;

    @Column(nullable = false, name = "cliente_id")
    private Long clienteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status_notificacao")
    private StatusMensagemConstants StatusNotificacao;

    @Column(nullable = false, name = "data_criado")
    private LocalDateTime dataHoraCriado;

    @Column(nullable = false, name = "data_envio")
    private LocalDateTime dataHoraEnvio;
}
