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

-- Copiando dados para a tabela fasteatsdb.categorias: ~4 rows (aproximadamente)
INSERT INTO `categorias` (`id`, `nome`, `descricao`, `ativo`) VALUES
	(1, 'LANCHE', 'LANCHE', 1),
	(2, 'BEBIDA', 'BEBIDA', 1),
	(3, 'ACOMPANHAMENTO', 'ACOMPANHAMENTO', 1),
	(4, 'SOBREMESA', 'SOBREMESA', 1);

-- Copiando dados para a tabela fasteatsdb.clientes: ~0 rows (aproximadamente)
INSERT INTO `clientes` (`cpf`, `primeironome`, `ultimonome`, `email`, `ativo`) VALUES
	('29234725897', 'Fiap', 'SOAT1', 'grupo56@fiapl.com', 1);

-- Copiando dados para a tabela fasteatsdb.formapagamentos: ~2 rows (aproximadamente)
INSERT INTO `formapagamentos` (`id`, `nome`, `ativo`) VALUES
	(1, 'PIX', 1),
	(2, 'MERCADO_PAGO', 1);

-- Copiando dados para a tabela fasteatsdb.pagamentos: ~0 rows (aproximadamente)

-- Copiando dados para a tabela fasteatsdb.pedidos: ~0 rows (aproximadamente)

-- Copiando dados para a tabela fasteatsdb.produtos: ~10 rows (aproximadamente)
INSERT INTO `produtos` (`id`, `nome`, `descricao`, `valor`, `ativo`, `categoriaId_fk`, `imagemBase64`, `imagemUrl`) VALUES
	(1, 'X-SALADA', 'X-salada com um hamburger 180g', 20, 1, 1, NULL, NULL),
	(2, 'X-BACON', 'X-sacon com um hamburger 180g', 22, 1, 1, NULL, NULL),
	(3, 'HOT-DOG SIMPLES', 'Hot-dog simples uma salsicha', 10, 1, 1, NULL, NULL),
	(4, 'COCA COLA LT 350ML', 'Coca cola lata 350ml', 6, 1, 2, NULL, NULL),
	(5, 'GUARANA ANTARTICA LT 350ML', 'Guarana antartica lata 350ml', 6, 1, 2, NULL, NULL),
	(6, 'AGUA 500ML', 'Agua garrafa 500ml', 5, 1, 2, NULL, NULL),
	(7, 'BATATA FRITA P', 'Batata frita pequena', 8, 1, 3, NULL, NULL),
	(8, 'BATATA FRITA M', 'Batata frita média', 12, 1, 3, NULL, NULL),
	(9, 'SORVETE CASCÃO UN', 'Sorvete cascão Un', 15, 1, 4, NULL, NULL),
	(10, 'SORVETE CASQUINHA UN', 'Sorvete casquinha Un', 7, 1, 4, NULL, NULL);

-- Copiando dados para a tabela fasteatsdb.produtosdeumpedido: ~3 rows (aproximadamente)
INSERT INTO `produtosdeumpedido` (`pedidoId_fk`, `produtoId_fk`, `quantidade`, `valor`) VALUES
	(9, 1, 9, 20),
	(9, 2, 4, 22),
	(10, 1, 6, 20);

-- Copiando dados para a tabela fasteatsdb.statuspagamento: ~0 rows (aproximadamente)
INSERT INTO `statuspagamento` (`id`, `nome`, `ativo`) VALUES
	(1, 'RECUSADO', 1),
	(2, 'CANCELADO', 1),
	(3, 'PAGO', 1);

-- Copiando dados para a tabela fasteatsdb.statuspedidos: ~0 rows (aproximadamente)
INSERT INTO `statuspedidos` (`id`, `nome`, `ativo`) VALUES
	(1, 'CRIADO', 1),
	(2, 'AGUARDANDO_PAGAMENTO', 1),
	(3, 'PAGO', 1),
	(4, 'RECEBIDO', 1),
	(5, 'EM_PREPARO', 1),
	(6, 'PRONTO', 1),
	(7, 'FINALIZADO', 1),
	(8, 'CANCELADO', 1);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
