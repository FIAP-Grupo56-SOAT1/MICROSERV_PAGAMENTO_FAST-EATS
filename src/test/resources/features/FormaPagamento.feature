# language: pt
Funcionalidade: Forma de Pagamento

  Cenario: Criar uma nova forma de pagamento
    Dado que uma nova forma de pagamento "PIX" deve ser criada
    Quando eu chamar o método de criação da forma de pagamento
    Então a forma de pagamento criada deve estar ativa e com o nome em maiúsculas

  Cenario: Criar uma nova forma de pagamento externa
    Dado que uma nova forma de pagamento externa "PIX" deve ser criada
    Quando eu chamar o método de criação da forma de pagamento
    Então a forma de pagamento criada deve ser marcada como externa

  Cenario: Tentar criar uma forma de pagamento duplicada
    Dado que uma forma de pagamento "PIX" já existe
    Quando eu tentar criar uma nova forma de pagamento com o mesmo nome
    Então deve lançar uma exceção de regra de negócio

  Cenario: Consultar uma forma de pagamento
    Dado que uma forma de pagamento com ID 1 existe
    Quando eu consultar a forma de pagamento pelo ID
    Então devo receber a forma de pagamento esperada

  Cenario: Tentar consultar uma forma de pagamento inexistente
    Dado que uma forma de pagamento com ID 999 não existe
    Quando eu tentar consultar a forma de pagamento pelo ID 999
    Então deve lançar uma exceção de forma de pagamento não encontrada

  Cenario: Atualizar uma forma de pagamento
    Dado que uma forma de pagamento com ID 1 existe
    Quando eu atualizar a forma de pagamento
    Então a forma de pagamento deve estar ativa e com o nome em maiúsculas

  Cenario: Tentar atualizar uma forma de pagamento inexistente
    Dado que uma forma de pagamento com ID 999 não existe
    Quando eu tentar atualizar a forma de pagamento
    Então deve lançar uma exceção de forma de pagamento não encontrada

  Cenario: Deletar uma forma de pagamento
    Dado que uma forma de pagamento com ID 1 existe
    Quando eu deletar a forma de pagamento
    Então a forma de pagamento deve ser removida

  Cenario: Tentar deletar uma forma de pagamento inexistente
    Dado que uma forma de pagamento com ID 999 não existe
    Quando eu tentar deletar a forma de pagamento
    Então deve lançar uma exceção de forma de pagamento não encontrada

  Cenario: Listar todas as formas de pagamento
    Dado que existem formas de pagamento cadastradas
    Quando eu listar todas as formas de pagamento
    Então devo receber a lista de formas de pagamento

  Cenario: Tentar listar formas de pagamento quando não existem
    Dado que não existem formas de pagamento cadastradas
    Quando eu tentar listar todas as formas de pagamento
    Então deve lançar uma exceção de formas de pagamento não encontradas

  Cenario: Consultar uma forma de pagamento pelo nome
    Dado que uma forma de pagamento com nome "PIX" existe
    Quando eu consultar a forma de pagamento pelo nome "PIX"
    Então devo receber a forma de pagamento "PIX" como resultado

  Cenario: Tentar consultar uma forma de pagamento pelo nome inexistente
    Dado que uma forma de pagamento com nome "PIX" não existe
    Quando eu consultar a forma de pagamento pelo nome "PIX"
    Então deve lançar uma exceção de forma de pagamento não encontrada