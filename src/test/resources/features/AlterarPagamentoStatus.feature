# language: pt
Funcionalidade: Alterar Status do Pagamento

  Cenario: Alterar o status do pagamento para recusado
    Dado que o pagamento exista no sistema
    Quando o status do pagamento for alterado para recusado
    Entao o status do pagamento deve ser alterado para recusado

  Cenario: Alterar o status do pagamento para cancelado
    Dado que o pagamento exista no sistema
    Quando o status do pagamento for alterado para cancelado
    Entao o status do pagamento deve ser alterado para cancelado
    E a data e hora de finalizacao do pagamento deve ser preenchida

  Cenario: Alterar o status do pagamento para pago
    Dado que o pagamento exista no sistema
    Quando o status do pagamento for alterado para pago
    Entao o status do pagamento deve ser alterado para pago
    E a data e hora de finalizacao do pagamento deve ser preenchida