-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: progetto_db
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `assegnazioni`
--

DROP TABLE IF EXISTS `assegnazioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assegnazioni` (
  `utente` int NOT NULL,
  `ordine1` int NOT NULL,
  `ordine2` int DEFAULT NULL,
  `ordine3` int DEFAULT NULL,
  `orario` datetime NOT NULL,
  `contatore` int DEFAULT '1',
  `orarioEffettivoOrdine1` datetime DEFAULT NULL,
  `orarioEffettivoOrdine2` datetime DEFAULT NULL,
  `orarioEffettivoOrdine3` datetime DEFAULT NULL,
  PRIMARY KEY (`utente`,`orario`),
  KEY `ordine1` (`ordine1`),
  KEY `ordine2` (`ordine2`),
  KEY `ordine3` (`ordine3`),
  KEY `orario` (`orario`),
  KEY `orarioEffettivoOrdine1` (`orarioEffettivoOrdine1`),
  KEY `orarioEffettivoOrdine2` (`orarioEffettivoOrdine2`),
  KEY `orarioEffettivoOrdine3` (`orarioEffettivoOrdine3`),
  CONSTRAINT `assegnazioni_ibfk_1` FOREIGN KEY (`utente`) REFERENCES `utenti` (`codUtente`),
  CONSTRAINT `assegnazioni_ibfk_2` FOREIGN KEY (`ordine1`) REFERENCES `ordini` (`codOrdine`),
  CONSTRAINT `assegnazioni_ibfk_3` FOREIGN KEY (`ordine2`) REFERENCES `ordini` (`codOrdine`),
  CONSTRAINT `assegnazioni_ibfk_4` FOREIGN KEY (`ordine3`) REFERENCES `ordini` (`codOrdine`),
  CONSTRAINT `assegnazioni_ibfk_5` FOREIGN KEY (`orario`) REFERENCES `tempi` (`orario`),
  CONSTRAINT `assegnazioni_ibfk_6` FOREIGN KEY (`orarioEffettivoOrdine1`) REFERENCES `tempi` (`orario`),
  CONSTRAINT `assegnazioni_ibfk_7` FOREIGN KEY (`orarioEffettivoOrdine2`) REFERENCES `tempi` (`orario`),
  CONSTRAINT `assegnazioni_ibfk_8` FOREIGN KEY (`orarioEffettivoOrdine3`) REFERENCES `tempi` (`orario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assegnazioni`
--

LOCK TABLES `assegnazioni` WRITE;
/*!40000 ALTER TABLE `assegnazioni` DISABLE KEYS */;
/*!40000 ALTER TABLE `assegnazioni` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-09 11:17:08
