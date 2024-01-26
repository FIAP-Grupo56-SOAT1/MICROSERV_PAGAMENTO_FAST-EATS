# language: pt
Funcionalidade: Emitir Comprovante de Pagamento

  Cenario: Emitir comprovante de pagamento para um pagamento interno
    Dado que o sistema possui um pagamento interno que pode ser emitido comprovante
    Quando eu emitir o comprovante de pagamento para o pedido
    Entao o status do pagamento interno deve ser alterado para pago
    E o pedido deve ser recebido pela cozinha

