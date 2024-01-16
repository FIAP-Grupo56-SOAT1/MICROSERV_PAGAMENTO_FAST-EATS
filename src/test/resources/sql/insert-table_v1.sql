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

-- Copiando dados para a tabela fasteatsdb.clientes: ~1 rows (aproximadamente)
INSERT INTO `clientes` (`id`, `primeironome`, `ultimonome`, `email`, `cpf`, `ativo`) VALUES
	(1, 'Pedro', 'Paulo', 'zerp@gmail.com', '29234725897', 1);

-- Copiando dados para a tabela fasteatsdb.formapagamentos: ~1 rows (aproximadamente)
INSERT INTO `formapagamentos` (`id`, `nome`, `ativo`) VALUES
	(1, 'PIX_QRCODE', 1);

-- Copiando dados para a tabela fasteatsdb.pedidos: ~2 rows (aproximadamente)
INSERT INTO `pedidos` (`id`, `clienteId_fk`, `ativo`, `statusPedidoId_fk`, `formaPagamentoId_fk`, `statusPagamentoId_fk`) VALUES
	(1, 1, 1, 1, 1, 2),
	(2, NULL, 1, 9, 1, 2);

-- Copiando dados para a tabela fasteatsdb.produtos: ~6 rows (aproximadamente)
INSERT INTO `produtos` (`id`, `nome`, `descricao`, `valor`, `ativo`, `tipoProdutoId_fk`) VALUES
	(1, 'X-SALADA', 'X-Salada com um Hamburger 180g', 20, 1, 1),
	(2, 'X-BACON', 'X-Bacon com um Hamburger 180g', 22, 1, 1),
	(3, 'HOT-DOG SIMPLES', 'Hot-Dog Simples uma Salsicha', 10, 1, 1),
	(4, 'COCA COLA LT 350ML', 'Coca Cola lata 350ml', 6, 1, 2),
	(5, 'GUARANA ANTARTICA LT 350ML', 'Guarana Antartica lata 350ml', 6, 1, 2),
	(6, 'AGUA 500ML', 'Agua garrafa 500ml', 5, 1, 2);

-- Copiando dados para a tabela fasteatsdb.produtosdeumpedido: ~2 rows (aproximadamente)
INSERT INTO `produtosdeumpedido` (`id`, `pedidoId_fk`, `produtoId_fk`, `quantidade`, `valor`, `desconto`, `ativo`) VALUES
	(1, 1, 1, 1, 20, 0, 1),
	(2, 2, 6, 2, 5, 0, 1);

-- Copiando dados para a tabela fasteatsdb.statuspagamento: ~2 rows (aproximadamente)
INSERT INTO `statuspagamento` (`id`, `nome`, `ativo`) VALUES
	(1, 'PAGO', 1),
	(2, 'AGUARDANDO_PAGAMENTO', 1);

-- Copiando dados para a tabela fasteatsdb.statuspedidos: ~10 rows (aproximadamente)
INSERT INTO `statuspedidos` (`id`, `nome`, `ativo`) VALUES
	(1, 'SELECAO_ITENS', 1),
	(2, 'AGUARDANDO_PAGAMENTO', 1),
	(3, 'PAGO', 1),
	(4, 'PAGAMENTO_RECUSADO', 1),
	(5, 'CANCELADO', 1),
	(6, 'AGUARDANDO_PAGAMENTO', 1),
	(7, 'AGUARDANDO_PREPARO_COZINHA', 1),
	(8, 'PRONTO', 1),
	(9, 'FINALIZADO', 1),
	(10, 'CANCELADO', 1);

-- Copiando dados para a tabela fasteatsdb.tipoprodutos: ~4 rows (aproximadamente)
INSERT INTO `tipoprodutos` (`id`, `nome`, `descricao`, `ativo`) VALUES
	(1, 'LANCHE', 'LANCHE', 1),
	(2, 'BEBIDA', 'BEBIDA', 1),
	(3, 'ACOMPANHAMENTO', 'ACOMPANHAMENTO', 1),
	(4, 'SOBREMESA', 'SOBREMESA', 1);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
