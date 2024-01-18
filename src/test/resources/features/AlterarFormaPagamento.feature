# language: pt
Funcionalidade: Alterar Forma de Pagamento
  Cenario: Altera a Forma de Pagamento
    Dado que o pagamento exista no sistema e não esteja pago
    Quando a forma de pagamento for alterada
    Entao um pagamento com a forma de pagamento alterada deve ser criado