# language: pt
Funcionalidade: Realizar Pagamento

  Cenário: Realizar pagamento com sucesso
    Dado que o pedido com ID 1 está no status "EM_PROCESSAMENTO"
    E a forma de pagamento é PIX
    Quando o pagamento for realizado
    Entao o status do pagamento deve ser "PAGO"
    E deve ser emitido um comprovante de pagamento

  Cenário: Tentar realizar pagamento externo
    Dado que o pedido com ID 1 é tem um pagamento externo e está no status "EM_PROCESSAMENTO"
    E a forma de pagamento é MERCADO_PAGO
    Quando ocorrer a tentativa de pagamento
    Entao uma exceção de regra de negócio deve ser lançada

  Cenário: Tentar realizar pagamento de pedido não pago
    Dado que o pedido com ID 1 está no status "CANCELADO"
    E a forma de pagamento é PIX
    Quando a alteração de status do pedido não for realizado
    Entao uma exceção de regra de negócio deve ser lançada
