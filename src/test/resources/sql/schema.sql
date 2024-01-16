-- --------------------------------------------------------
-- Servidor:                     localhost
-- Versão do servidor:           8.0.33 - MySQL Community Server - GPL
-- OS do Servidor:               Linux
-- HeidiSQL Versão:              12.4.0.6659
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Copiando estrutura para tabela fasteatsdb.categorias
DROP TABLE IF EXISTS `categorias`;
CREATE TABLE IF NOT EXISTS `categorias` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) DEFAULT NULL,
  `descricao` varchar(400) DEFAULT NULL,
  `ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Exportação de dados foi desmarcado.

-- Copiando estrutura para tabela fasteatsdb.clientes
DROP TABLE IF EXISTS `clientes`;
CREATE TABLE IF NOT EXISTS `clientes` (
  `cpf` varchar(11) NOT NULL,
  `primeironome` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ultimonome` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ativo` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`cpf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Exportação de dados foi desmarcado.

-- Copiando estrutura para tabela fasteatsdb.formapagamentos
DROP TABLE IF EXISTS `formapagamentos`;
CREATE TABLE IF NOT EXISTS `formapagamentos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) DEFAULT NULL,
  `ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Exportação de dados foi desmarcado.

-- Copiando estrutura para tabela fasteatsdb.pagamentos
DROP TABLE IF EXISTS `pagamentos`;
CREATE TABLE IF NOT EXISTS `pagamentos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `formaPagamentoId_fk` bigint NOT NULL,
  `statusPagamentoId_fk` bigint NOT NULL,
  `pedidoId_fk` bigint NOT NULL,
  `dataHoraCriado` datetime DEFAULT NULL,
  `dataHoraProcessamento` datetime DEFAULT NULL,
  `dataHoraFinalizado` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pedidoId_fk` (`pedidoId_fk`),
  KEY `formaPagamentoId_fk` (`formaPagamentoId_fk`),
  KEY `statusPagamentoId_fk` (`statusPagamentoId_fk`),
  CONSTRAINT `pagamentos_ibfk_1` FOREIGN KEY (`pedidoId_fk`) REFERENCES `pedidos` (`id`),
  CONSTRAINT `pagamentos_ibfk_2` FOREIGN KEY (`formaPagamentoId_fk`) REFERENCES `formapagamentos` (`id`),
  CONSTRAINT `pagamentos_ibfk_3` FOREIGN KEY (`statusPagamentoId_fk`) REFERENCES `statuspagamento` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Exportação de dados foi desmarcado.

-- Copiando estrutura para tabela fasteatsdb.pedidos
DROP TABLE IF EXISTS `pedidos`;
CREATE TABLE IF NOT EXISTS `pedidos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `clienteId_fk` varchar(11) DEFAULT NULL,
  `ativo` tinyint(1) DEFAULT NULL,
  `statusPedidoId_fk` bigint DEFAULT NULL,
  `dataHoraCriado` datetime DEFAULT NULL,
  `dataHoraRecebimento` datetime DEFAULT NULL,
  `dataHoraFinalizado` datetime DEFAULT NULL,
  `valor` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `clienteId_fk` (`clienteId_fk`),
  KEY `statusPedidoId_fk` (`statusPedidoId_fk`),
  CONSTRAINT `pedidos_ibfk_1` FOREIGN KEY (`clienteId_fk`) REFERENCES `clientes` (`cpf`),
  CONSTRAINT `pedidos_ibfk_2` FOREIGN KEY (`statusPedidoId_fk`) REFERENCES `statuspedidos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Exportação de dados foi desmarcado.

-- Copiando estrutura para tabela fasteatsdb.produtos
DROP TABLE IF EXISTS `produtos`;
CREATE TABLE IF NOT EXISTS `produtos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) NOT NULL,
  `descricao` varchar(400) DEFAULT NULL,
  `valor` double NOT NULL,
  `ativo` tinyint(1) NOT NULL,
  `categoriaId_fk` bigint NOT NULL,
  `imagemBase64` longtext,
  `imagemUrl` varchar(400) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tipoProdutoId_fk` (`categoriaId_fk`),
  CONSTRAINT `produtos_ibfk_1` FOREIGN KEY (`categoriaId_fk`) REFERENCES `categorias` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Exportação de dados foi desmarcado.

-- Copiando estrutura para tabela fasteatsdb.produtosdeumpedido
DROP TABLE IF EXISTS `produtosdeumpedido`;
CREATE TABLE IF NOT EXISTS `produtosdeumpedido` (
  `pedidoId_fk` bigint NOT NULL,
  `produtoId_fk` bigint NOT NULL,
  `quantidade` int NOT NULL,
  `valor` double NOT NULL,
  PRIMARY KEY (`pedidoId_fk`,`produtoId_fk`),
  KEY `pedidoId_fk` (`pedidoId_fk`),
  KEY `produtoId_fk` (`produtoId_fk`),
  CONSTRAINT `produtosdeumpedido_ibfk_1` FOREIGN KEY (`pedidoId_fk`) REFERENCES `pedidos` (`id`),
  CONSTRAINT `produtosdeumpedido_ibfk_2` FOREIGN KEY (`produtoId_fk`) REFERENCES `produtos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Exportação de dados foi desmarcado.

-- Copiando estrutura para tabela fasteatsdb.statuspagamento
DROP TABLE IF EXISTS `statuspagamento`;
CREATE TABLE IF NOT EXISTS `statuspagamento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) DEFAULT NULL,
  `ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Exportação de dados foi desmarcado.

-- Copiando estrutura para tabela fasteatsdb.statuspedidos
DROP TABLE IF EXISTS `statuspedidos`;
CREATE TABLE IF NOT EXISTS `statuspedidos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) DEFAULT NULL,
  `ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Exportação de dados foi desmarcado.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
