-- fasteatspagamentodb.notificacao definition

CREATE TABLE `notificacao` (
                               `notificacao_id` bigint NOT NULL AUTO_INCREMENT,
                               `status_notificacao` varchar(255) NOT NULL,
                               `cliente_id` bigint NOT NULL,
                               `data_criado` datetime(6) NOT NULL,
                               `data_envio` datetime(6) NOT NULL,
                               PRIMARY KEY (`notificacao_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;