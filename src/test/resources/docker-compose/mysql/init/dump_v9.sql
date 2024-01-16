-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: fasteatsdb
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) DEFAULT NULL,
  `descricao` varchar(400) DEFAULT NULL,
  `ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES (1,'LANCHE','LANCHE',1),(2,'BEBIDA','BEBIDA',1),(3,'ACOMPANHAMENTO','ACOMPANHAMENTO',1),(4,'SOBREMESA','SOBREMESA',1);
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `cpf` varchar(11) NOT NULL,
  `primeironome` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ultimonome` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ativo` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`cpf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES ('29234725897','Fiap','SOAT1','grupo56@fiapl.com',1);
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formapagamentos`
--

DROP TABLE IF EXISTS `formapagamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `formapagamentos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) DEFAULT NULL,
  `ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formapagamentos`
--

LOCK TABLES `formapagamentos` WRITE;
/*!40000 ALTER TABLE `formapagamentos` DISABLE KEYS */;
INSERT INTO `formapagamentos` VALUES (1,'PIX',1),(2,'MERCADO_PAGO',1);
/*!40000 ALTER TABLE `formapagamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagamentos`
--

DROP TABLE IF EXISTS `pagamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagamentos` (
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagamentos`
--

LOCK TABLES `pagamentos` WRITE;
/*!40000 ALTER TABLE `pagamentos` DISABLE KEYS */;
/*!40000 ALTER TABLE `pagamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos` (
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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produtos`
--

DROP TABLE IF EXISTS `produtos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produtos` (
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produtos`
--

LOCK TABLES `produtos` WRITE;
/*!40000 ALTER TABLE `produtos` DISABLE KEYS */;
INSERT INTO `produtos` VALUES (1,'X-SALADA','X-salada com um hamburger 180g',20,1,1,NULL,NULL),(2,'X-BACON','X-sacon com um hamburger 180g',22,1,1,NULL,NULL),(3,'HOT-DOG SIMPLES','Hot-dog simples uma salsicha',10,1,1,NULL,NULL),(4,'COCA COLA LT 350ML','Coca cola lata 350ml',6,1,2,NULL,NULL),(5,'GUARANA ANTARTICA LT 350ML','Guarana antartica lata 350ml',6,1,2,NULL,NULL),(6,'AGUA 500ML','Agua garrafa 500ml',5,1,2,NULL,NULL),(7,'BATATA FRITA P','Batata frita pequena',8,1,3,NULL,NULL),(8,'BATATA FRITA M','Batata frita média',12,1,3,NULL,NULL),(9,'SORVETE CASCÃO UN','Sorvete cascão Un',15,1,4,NULL,NULL),(10,'SORVETE CASQUINHA UN','Sorvete casquinha Un',7,1,4,NULL,NULL);
/*!40000 ALTER TABLE `produtos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produtosdeumpedido`
--

DROP TABLE IF EXISTS `produtosdeumpedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produtosdeumpedido` (
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produtosdeumpedido`
--

LOCK TABLES `produtosdeumpedido` WRITE;
/*!40000 ALTER TABLE `produtosdeumpedido` DISABLE KEYS */;
INSERT INTO `produtosdeumpedido` VALUES (9,1,9,20),(9,2,4,22),(10,1,6,20);
/*!40000 ALTER TABLE `produtosdeumpedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statuspagamento`
--

DROP TABLE IF EXISTS `statuspagamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statuspagamento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) DEFAULT NULL,
  `ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statuspagamento`
--

LOCK TABLES `statuspagamento` WRITE;
/*!40000 ALTER TABLE `statuspagamento` DISABLE KEYS */;
INSERT INTO `statuspagamento` VALUES (1,'RECUSADO',1),(2,'CANCELADO',1),(3,'PAGO',1);
/*!40000 ALTER TABLE `statuspagamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statuspedidos`
--

DROP TABLE IF EXISTS `statuspedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statuspedidos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) DEFAULT NULL,
  `ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statuspedidos`
--

LOCK TABLES `statuspedidos` WRITE;
/*!40000 ALTER TABLE `statuspedidos` DISABLE KEYS */;
INSERT INTO `statuspedidos` VALUES (1,'CRIADO',1),(2,'AGUARDANDO_PAGAMENTO',1),(3,'PAGO',1),(4,'RECEBIDO',1),(5,'EM_PREPARO',1),(6,'PRONTO',1),(7,'FINALIZADO',1),(8,'CANCELADO',1);
/*!40000 ALTER TABLE `statuspedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'fasteatsdb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-08 11:59:37
