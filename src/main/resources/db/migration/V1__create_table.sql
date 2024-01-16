CREATE TABLE `forma_pagamento`
(
    `forma_pagamento_id` BIGINT NOT NULL AUTO_INCREMENT,
    `nome`               VARCHAR(100),
    `externo`            TINYINT(1) DEFAULT 0,
    `ativo`              TINYINT(1) DEFAULT 1,
    PRIMARY KEY (`forma_pagamento_id`)
)
    ENGINE=InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `status_pagamento`
(
    `status_pagamento_id` BIGINT NOT NULL AUTO_INCREMENT,
    `nome`                VARCHAR(100) NOT NULL,
    `ativo`               TINYINT(1) DEFAULT 1,
    PRIMARY KEY (`status_pagamento_id`)
)
    ENGINE=InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `pagamento`
(
    `pagamento_id`         BIGINT NOT NULL AUTO_INCREMENT,
    `forma_pagamento_id`   BIGINT NOT NULL,
    `status_pagamento_id`  BIGINT NOT NULL,
    `valor`                DOUBLE NOT NULL,
    `pedido_id`            BIGINT NOT NULL,
    `pagamento_externo_id` BIGINT,
    `url_pagamento`        VARCHAR(1000),
    `qr_code`              VARCHAR(2000),
    `data_criado`          DATETIME DEFAULT CURRENT_TIMESTAMP,
    `data_processamento`   DATETIME,
    `data_finalizado`      DATETIME,
    PRIMARY KEY (`pagamento_id`),
    FOREIGN KEY (`forma_pagamento_id`) REFERENCES `forma_pagamento` (`forma_pagamento_id`),
    FOREIGN KEY (`status_pagamento_id`) REFERENCES `status_pagamento` (`status_pagamento_id`)
)
    ENGINE=InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;