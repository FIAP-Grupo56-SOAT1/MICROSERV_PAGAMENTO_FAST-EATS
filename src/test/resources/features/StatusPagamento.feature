# language: pt
Funcionalidade: Status Pagamento

  Cenario: Consultar status de pagamento
    Dado que existe um status de pagamento ID 1 cadastrado
    Quando eu consultar o status de pagamento ID 1
    Entao o status de pagamento deve ser retornado

  Cenario: Consultar status de pagamento não encontrado
    Dado que não existe um status de pagamento com ID 999 cadastrado
    Quando eu tentar consultar o status de pagamento com ID 999
    Entao deve ser lançada a exceção StatusPagametoNotFound

  Cenario: Listar status de pagamento
    Dado que existem status de pagamento cadastrados
    Quando eu listar os status de pagamento
    Entao a lista de status de pagamento deve ser retornada

  Cenario: Consultar status de pagamento por nome
    Dado que existe um status de pagamento com nome "STATUS_EM_PROCESSAMENTO" cadastrado
    Quando eu consultar o status de pagamento com nome "STATUS_EM_PROCESSAMENTO"
    Entao o status de pagamento com o nome pesquisado deve ser retornado

  Cenario: Consultar status de pagamento por nome não encontrado
    Dado que não existe um status de pagamento com nome "STATUS_INEXISTENTE" cadastrado
    Quando eu tentar consultar o status de pagamento com nome "STATUS_INEXISTENTE"
    Entao deve ser lançada a exceção StatusPagametoNotFound para consulta por nome
