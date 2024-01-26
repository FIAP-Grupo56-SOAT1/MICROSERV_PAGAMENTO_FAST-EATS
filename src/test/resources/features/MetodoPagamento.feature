# language: pt
Funcionalidade: Método de Pagamento

  Cenário: Gerar um pagamento interno PIX com sucesso
    Dado que existe um pedido com ID 1 no sistema
    Quando o método de pagamento PIX é acionado para o pedido 1
    Entao deve ser gerado um pagamento interno PIX com sucesso

  Cenário: Tentar gerar um pagamento interno PIX com pedido não encontrado
    Dado que não existe um pedido com ID 1 no sistema
    Quando o método de pagamento PIX é acionado para o pedido 1
    Entao deve ser lançada a exceção PedidoNotFound

  Cenário: Gerar um pagamento externo Mercado Pago com sucesso
    Dado que existe um pedido com ID 1 no sistema
    Quando o método de pagamento Mercado Pago é acionado para o pedido 1
    Entao deve ser gerado um pagamento externo Mercado Pago com sucesso

  Cenário: Tentar gerar um pagamento externo Mercado Pago com pedido não encontrado
    Dado que não existe um pedido com ID 1 no sistema
    Quando o método de pagamento Mercado Pago é acionado para o pedido 1
    Entao deve ser lançada a exceção PedidoNotFound

  Cenário: Tentar gerar um pagamento externo Mercado Pago com falha na geração do pagamento externo
    Dado que existe um pedido com ID 1 no sistema
    Quando o método de pagamento Mercado Pago deve ser acionado para o pedido 1
    Entao deve ser lançada a exceção PagamentoExternoException
