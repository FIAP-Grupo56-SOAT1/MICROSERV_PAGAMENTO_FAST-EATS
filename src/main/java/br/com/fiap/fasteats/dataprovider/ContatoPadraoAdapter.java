package br.com.fiap.fasteats.dataprovider;

import br.com.fiap.fasteats.core.dataprovider.ContatoPadraoOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContatoPadraoAdapter implements ContatoPadraoOutputPort {
    @Value("${contato.email-padrao.pagamento-pedido}")
    private String emailPadrao;

    @Override
    public String emailPadrao() {
        return emailPadrao;
    }
}
