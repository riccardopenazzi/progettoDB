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
-- Table structure for table `farciture`
--

DROP TABLE IF EXISTS `farciture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farciture` (
  `pizza` varchar(255) NOT NULL,
  `ingrediente` varchar(255) NOT NULL,
  KEY `pizza` (`pizza`),
  KEY `ingrediente` (`ingrediente`),
  CONSTRAINT `farciture_ibfk_1` FOREIGN KEY (`pizza`) REFERENCES `pizze` (`nome`),
  CONSTRAINT `farciture_ibfk_2` FOREIGN KEY (`ingrediente`) REFERENCES `ingredienti` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farciture`
--

LOCK TABLES `farciture` WRITE;
/*!40000 ALTER TABLE `farciture` DISABLE KEYS */;
INSERT INTO `farciture` VALUES ('Margherita','pomodoro'),('Margherita','mozzarella'),('Cipolla','pomodoro'),('Cipolla','mozzarella'),('Cipolla','cipolla'),('Romana','pomodoro'),('Romana','mozzarella'),('Romana','acciughe'),('Romana','capperi'),('Salamino','pomodoro'),('Salamino','mozzarella'),('Salamino','salame dolce'),('Prosciutto Cotto','pomodoro'),('Prosciutto Cotto','mozzarella'),('Prosciutto Cotto','prosciutto cotto'),('Tonno','pomodoro'),('Tonno','mozzarella'),('Tonno','tonno'),('Bufala','pomodoro'),('Bufala','mozzarella di bufala'),('Capricciosa','pomodoro'),('Capricciosa','mozzarella'),('Capricciosa','carciofi'),('Capricciosa','funghi freschi'),('Capricciosa','prosciutto cotto'),('Funghi Porcini','pomodoro'),('Funghi Porcini','mozzarella'),('Funghi Porcini','funghi porcini'),('Funghi Trifolati','pomodoro'),('Funghi Trifolati','mozzarella'),('Funghi Trifolati','funghi trifolati'),('Funghi Freschi','pomodoro'),('Funghi Freschi','mozzarella'),('Funghi Freschi','funghi freschi'),('Quattro Formaggi','pomodoro'),('Quattro Formaggi','mozzarella'),('Quattro Formaggi','emmenthal'),('Quattro Formaggi','brie'),('Quattro Formaggi','gorgonzola'),('Contadina','pomodoro'),('Contadina','mozzarella'),('Contadina','salsiccia'),('Contadina','patate al forno'),('Contadina','cipolla'),('Montanara','pomodoro'),('Montanara','mozzarella'),('Montanara','speck'),('Montanara','rucola'),('Montanara','scaglie di grana'),('Energica','pomodoro'),('Energica','mozzarella'),('Energica','funghi freschi'),('Energica','prosciutto cotto'),('Energica','rucola'),('Energica','scaglie di grana'),('Esplosiva','pomodoro'),('Esplosiva','mozzarella'),('Esplosiva','patate al forno'),('Esplosiva','funghi porcini'),('Esplosiva','speck'),('Tirolese','pomodoro'),('Tirolese','mozzarella'),('Tirolese','brie'),('Tirolese','speck'),('Montebaldo','pomodoro'),('Montebaldo','mozzarella'),('Montebaldo','radicchio'),('Montebaldo','bruciatini'),('Montebaldo','aceto balsamico');
/*!40000 ALTER TABLE `farciture` ENABLE KEYS */;
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
