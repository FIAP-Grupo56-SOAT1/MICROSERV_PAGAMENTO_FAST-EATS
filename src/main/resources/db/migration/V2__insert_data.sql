INSERT INTO `forma_pagamento` (`forma_pagamento_id`, `nome`, `externo`,`ativo`)
VALUES (1, 'PIX', 0, 1),
       (2, 'MERCADO_PAGO', 1, 1);

INSERT INTO `status_pagamento` (`status_pagamento_id`, `nome`, `ativo`)
VALUES (1, 'RECUSADO', 1),
       (2, 'CANCELADO', 1),
       (3, 'PAGO', 1),
       (4, 'EM_PROCESSAMENTO', 1),
       (5, 'AGUARDANDO_PAGAMENTO_PEDIDO', 1),
       (6, 'CONCLUIDO', 1);