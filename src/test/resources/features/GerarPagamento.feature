# language: pt
Funcionalidade: Gerar Pagamento

  Cenario: Gerar um pagamento interno PIX
    Dado um pedido com ID 1
    E uma forma de pagamento PIX com ID 1
    Quando tento gerar o pagamento
    Entao devo obter um pagamento interno PIX

  Cenario: Gerar um pagamento externo Mercado Pago
    Dado um pedido com ID 1
    E uma forma de pagamento Mercado Pago com ID 1
    Quando tento gerar o pagamento
    Entao devo obter um pagamento externo Mercado Pago

  Cenario: Tentar gerar pagamento com forma de pagamento nao cadastrada
    Dado um pedido com ID 1
    E uma forma de pagamento nao cadastrada com ID 1
    Quando tento gerar o pagamento
    Entao devo receber uma mensagem de erro FormaPagamentoNotFound


