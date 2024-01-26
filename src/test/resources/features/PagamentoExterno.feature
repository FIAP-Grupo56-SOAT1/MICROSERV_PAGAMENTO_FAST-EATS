# language: pt
Funcionalidade: Pagamento Externo

  Cenario: Atualizar Pagamento para Pago por meio de Pagamento Externo
    Dado que o pagamento externo com ID 1 está marcado como "PAGO"
    Quando eu atualizo o status do pagamento por meio de um pagamento externo
    Entao o status do pagamento e do pedido devem ser alterados para pago

  Cenario: Atualizar Pagamento Externo com Status Recusado
    Dado que o pagamento externo com ID 1 está marcado como "RECUSADO"
    Quando eu atualizo o status do pagamento por meio de um pagamento externo
    Entao o status do pagamento deve ser recusado

  Cenario: Atualizar Pagamento Externo com Status Cancelado
    Dado que o pagamento externo com ID 1 está marcado como "CANCELADO"
    Quando eu atualizo o status do pagamento por meio de um pagamento externo
    Entao o status do pagamento e do pedido devem ser alterados para cancelado

  Cenario: Não deve atualizar o status do pagamento externo pois o status não foi mapeado
    Dado que o pagamento externo com ID 1 está marcado como "TESTE"
    Quando eu atualizo o status do pagamento por meio de um pagamento externo
    Entao deve ser retornado um pagamento vazio

  Cenario: Cancelar Pagamento Externo
    Dado que o pagamento externo com ID 1 existe
    Quando eu cancelo o pagamento externo
    Entao o pagamento externo deve ser cancelado com sucesso
