# language: pt
Funcionalidade: Cancelar Pagamento Externo

  Cenario: Cancelar pagamento externo
    Dado que o pagamento exista no sistema
    Quando o pagamento externo for cancelado
    Entao deve ser enviada a solicitação de cancelamento para o serviço de pagamento externo