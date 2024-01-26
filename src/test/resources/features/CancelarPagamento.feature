# language: pt
Funcionalidade: Cancelar Pagamento

  Cenario: Cancelar um pagamento interno
    Dado que o pagamento interno exista no sistema
    Quando o pagamento interno for cancelado
    Entao o status do pagamento interno deve ser alterado para cancelado
    E o pedido deve ser cancelado
    E o pagamento interno deve ser marcado como cancelado

  Cenario: Tentar cancelar um pagamento externo
    Dado que o pagamento externo exista no sistema
    Quando eu tento cancelar o pagamento externo
    Entao deve gerar um erro de regra de negócio
