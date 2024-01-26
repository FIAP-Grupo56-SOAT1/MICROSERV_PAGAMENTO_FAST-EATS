# language: pt
Funcionalidade: Pagamento

  Cenario: Listar pagamentos
    Dado que existem pagamentos cadastrados
    Quando eu listar os pagamentos
    Entao a lista de pagamentos deve ser retornada

  Cenario: Consultar pagamento por ID de pedido
    Dado que existe um pagamento com ID de pedido 1
    Quando eu consultar o pagamento por ID de pedido 1
    Entao o pagamento recuperado pelo ID de pedido deve ser retornado

  Cenario: Consultar pagamento por ID de pedido não encontrado
    Dado que não existe um pagamento com ID de pedido 999
    Quando eu tentar consultar o pagamento por ID de pedido 999
    Entao deve ser lançada a exceção PagamentoNotFound para pesquisa por ID de pedido

  Cenario: Criar um novo pagamento
    Dado que eu tenho um novo pagamento
    Quando eu criar o pagamento
    Entao o pagamento deve ser salvo com sucesso

  Cenario: Atualizar um pagamento
    Dado que existe um pagamento com ID 1
    Quando eu atualizar o pagamento
    Entao o pagamento deve ser atualizado com sucesso

  Cenario: Consultar um pagamento por ID
    Dado que existe um pagamento com ID 1
    Quando eu consultar o pagamento por ID 1
    Entao o pagamento deve ser retornado

  Cenario: Consultar um pagamento por ID não encontrado
    Dado que não existe um pagamento com ID 999
    Quando eu tentar consultar o pagamento por ID 999
    Entao deve ser lançada a exceção PagamentoNotFound

  Cenario: Consultar um pagamento por ID de pagamento externo
    Dado que existe um pagamento com ID de pagamento externo 1234567890
    Quando eu consultar o pagamento por ID de pagamento externo 1234567890
    Entao o pagamento recuperado pelo ID de pagamento externo deve ser retornado

  Cenario: Consultar um pagamento por ID de pagamento externo não encontrado
    Dado que não existe um pagamento com ID de pagamento externo 9999999999
    Quando eu tentar consultar o pagamento por ID de pagamento externo 9999999999
    Entao deve ser lançada a exceção PagamentoNotFound para pesquisa por ID de pagamento externo
