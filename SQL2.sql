CREATE DATABASE  IF NOT EXISTS `gametrashcollecting` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gametrashcollecting`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: gametrashcollecting
-- ------------------------------------------------------
-- Server version	8.0.38

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
-- Table structure for table `friendship`
--

DROP TABLE IF EXISTS `friendship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friendship` (
  `friendshipId` int NOT NULL AUTO_INCREMENT,
  `user1Id` int DEFAULT NULL,
  `user2Id` int DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`friendshipId`),
  KEY `user1Id` (`user1Id`),
  KEY `user2Id` (`user2Id`),
  CONSTRAINT `friendship_ibfk_1` FOREIGN KEY (`user1Id`) REFERENCES `user` (`userId`),
  CONSTRAINT `friendship_ibfk_2` FOREIGN KEY (`user2Id`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friendship`
--

LOCK TABLES `friendship` WRITE;
/*!40000 ALTER TABLE `friendship` DISABLE KEYS */;
INSERT INTO `friendship` VALUES (1,1,2,1),(2,1,3,0),(3,4,6,1),(4,4,7,1),(5,6,4,1),(6,7,4,1);
/*!40000 ALTER TABLE `friendship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gameresult`
--

DROP TABLE IF EXISTS `gameresult`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gameresult` (
  `resultId` int NOT NULL AUTO_INCREMENT,
  `pointsScored` int DEFAULT NULL,
  `playTime` datetime DEFAULT NULL,
  `userId` int DEFAULT NULL,
  `roundId` int DEFAULT NULL,
  PRIMARY KEY (`resultId`),
  KEY `userId` (`userId`),
  KEY `roundId` (`roundId`),
  CONSTRAINT `gameresult_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`),
  CONSTRAINT `gameresult_ibfk_2` FOREIGN KEY (`roundId`) REFERENCES `gameround` (`roundId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gameresult`
--

LOCK TABLES `gameresult` WRITE;
/*!40000 ALTER TABLE `gameresult` DISABLE KEYS */;
INSERT INTO `gameresult` VALUES (1,600,'2024-10-03 10:50:00',1,1);
/*!40000 ALTER TABLE `gameresult` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gameroom`
--

DROP TABLE IF EXISTS `gameroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gameroom` (
  `roomId` int NOT NULL AUTO_INCREMENT,
  `roomName` varchar(50) DEFAULT NULL,
  `maxPlayer` int DEFAULT NULL,
  `currentPlayer` int DEFAULT NULL,
  `status` enum('WAITING','IN_PROGRESS','FULL') DEFAULT NULL,
  PRIMARY KEY (`roomId`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gameroom`
--

LOCK TABLES `gameroom` WRITE;
/*!40000 ALTER TABLE `gameroom` DISABLE KEYS */;
INSERT INTO `gameroom` VALUES (1,'Room A',2,3,'FULL'),(2,'Room B',2,1,'IN_PROGRESS'),(3,'Room-f89549a5',2,1,'WAITING'),(4,'Room-19f16707',2,1,'WAITING'),(5,'Room-26dc397e',2,1,'WAITING'),(6,'Room-907cdfd1',2,1,'WAITING'),(7,'Room-e1f4c61d',2,1,'WAITING'),(8,'Room-5eb9e88e',2,1,'WAITING'),(9,'Room-aa6c3358',2,1,'WAITING'),(10,'Room-21999397',2,1,'WAITING'),(11,'Room-543c832c',2,1,'WAITING'),(12,'Room-0a890c7b',2,1,'WAITING'),(13,'Room-3940c5ab',2,1,'WAITING'),(14,'Room-9d61ee40',2,1,'WAITING'),(15,'Room-1009a81b',2,1,'WAITING'),(16,'Room-f212ce37',2,2,'IN_PROGRESS'),(17,'Room-ca374b89',2,1,'WAITING'),(18,'Room-aa3f6235',2,1,'WAITING'),(19,'Room-45f8d154',2,1,'WAITING'),(20,'Room-e3e97551',2,1,'WAITING'),(21,'Room-94f339be',2,2,'FULL'),(22,'Room-9be2a2ed',2,2,'FULL'),(23,'Room-b8d4c7d6',2,2,'FULL'),(24,'Room-58f3311a',2,2,'FULL'),(25,'Room-d0b2b94f',2,1,'WAITING'),(26,'Room-c1f4f18f',2,2,'FULL'),(27,'Room-083312e5',2,2,'FULL'),(28,'Room-ecfd802c',2,2,'FULL'),(29,'Room-5c92757f',2,2,'FULL'),(30,'Room-c6200bf3',2,2,'FULL'),(31,'Room-30fdac2b',2,2,'FULL'),(32,'Room-b6a25e84',2,2,'FULL'),(33,'Room-7354e865',2,2,'FULL'),(34,'Room-2c89e72c',2,2,'FULL'),(35,'Room-3af91667',2,2,'FULL'),(36,'Room-4a433b76',2,2,'FULL'),(37,'Room-8af6a567',2,2,'FULL'),(38,'Room-fa4cfe09',2,2,'FULL'),(39,'Room-7c5592d0',2,2,'FULL'),(40,'Room-ee1a0ec8',2,1,'WAITING'),(41,'Room-e5bed57a',2,1,'WAITING'),(42,'Room-2243c9cf',2,2,'FULL'),(43,'Room-d7331eac',2,2,'FULL'),(44,'Room-a119a3be',2,2,'FULL'),(45,'Room-f2e92d49',2,2,'FULL'),(46,'Room-3e09d4a6',2,2,'FULL'),(47,'Room-40e99ece',2,2,'FULL'),(48,'Room-39f28f52',2,2,'FULL'),(49,'Room-0a539801',2,2,'FULL'),(50,'Room-817af621',2,1,'WAITING'),(51,'Room-4f7146f8',2,2,'FULL'),(52,'Room-02c10cd0',2,2,'FULL'),(53,'Room-4e32759d',2,2,'FULL'),(54,'Room-e1b5dcfe',2,2,'FULL'),(55,'Room-5f1a1c84',2,1,'WAITING'),(56,'Room-76413a4d',2,1,'WAITING'),(57,'Room-56d8a6c4',2,1,'WAITING'),(58,'Room-69cc6694',2,1,'WAITING'),(59,'Room-910c0b51',2,1,'WAITING'),(60,'Room-2e61e98c',2,2,'FULL'),(61,'Room-227c7cf1',2,2,'FULL'),(62,'Room-c5f519af',2,2,'FULL'),(63,'Room-1be5c91e',2,2,'FULL'),(64,'Room-ae15e530',2,2,'FULL'),(65,'Room-591a5d11',2,2,'FULL'),(66,'Room-229a41fb',2,2,'FULL'),(67,'Room-338b9c69',2,2,'FULL'),(68,'Room-975c981c',2,2,'FULL'),(69,'Room-934663ec',2,2,'FULL'),(70,'Room-4f61b15d',2,2,'FULL'),(71,'Room-1713c04f',2,2,'FULL'),(72,'Room-fe1c8963',2,2,'FULL'),(73,'Room-69c4a0c1',2,1,'WAITING'),(74,'Room-c5dbd252',2,1,'WAITING'),(75,'Room-3d61e947',2,1,'WAITING'),(76,'Room-69afeb32',2,1,'WAITING');
/*!40000 ALTER TABLE `gameroom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gameroomplayer`
--

DROP TABLE IF EXISTS `gameroomplayer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gameroomplayer` (
  `roomPlayerId` int NOT NULL AUTO_INCREMENT,
  `userId` int DEFAULT NULL,
  `roomId` int DEFAULT NULL,
  `joinedTime` datetime DEFAULT NULL,
  `isActive` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`roomPlayerId`),
  KEY `userId` (`userId`),
  KEY `roomId` (`roomId`),
  CONSTRAINT `gameroomplayer_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`),
  CONSTRAINT `gameroomplayer_ibfk_2` FOREIGN KEY (`roomId`) REFERENCES `gameroom` (`roomId`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gameroomplayer`
--

LOCK TABLES `gameroomplayer` WRITE;
/*!40000 ALTER TABLE `gameroomplayer` DISABLE KEYS */;
INSERT INTO `gameroomplayer` VALUES (1,1,1,'2024-10-03 10:00:00',1),(2,2,1,'2024-10-03 10:05:00',1),(3,4,14,NULL,NULL),(4,4,15,NULL,1),(5,4,16,NULL,1),(6,4,1,NULL,1),(7,4,1,NULL,1),(8,4,1,NULL,1),(9,4,17,NULL,1),(10,4,18,NULL,1),(11,4,19,NULL,1),(12,4,20,NULL,1),(13,4,21,NULL,1),(14,6,21,NULL,1),(15,4,22,NULL,1),(16,6,22,NULL,1),(17,4,23,NULL,1),(18,6,23,NULL,1),(19,4,24,NULL,1),(20,6,24,NULL,1),(21,4,25,NULL,1),(22,6,25,NULL,1),(23,4,26,NULL,1),(24,6,26,NULL,1),(25,4,27,NULL,1),(26,6,27,NULL,1),(27,4,28,NULL,1),(28,6,28,NULL,1),(29,4,29,NULL,1),(30,6,29,NULL,1),(31,4,30,NULL,1),(32,6,30,NULL,1),(33,4,31,NULL,1),(34,6,31,NULL,1),(35,4,32,NULL,1),(36,6,32,NULL,1),(37,4,33,NULL,1),(38,6,33,NULL,1),(39,4,34,NULL,1),(40,6,34,NULL,1),(41,4,35,NULL,1),(42,6,35,NULL,1),(43,4,36,NULL,1),(44,6,36,NULL,1),(45,4,37,NULL,1),(46,6,37,NULL,1),(47,4,38,NULL,1),(48,6,38,NULL,1),(49,4,39,NULL,1),(50,6,39,NULL,1),(51,4,1,NULL,1),(52,4,40,NULL,1),(53,6,40,NULL,1),(54,4,41,NULL,1),(55,4,42,NULL,1),(56,6,42,NULL,1),(57,4,43,NULL,1),(58,6,43,NULL,1),(59,4,44,NULL,1),(60,6,44,NULL,1),(61,4,45,NULL,1),(62,6,45,NULL,1),(63,4,46,NULL,1),(64,6,46,NULL,1),(65,4,47,NULL,1),(66,6,47,NULL,1),(67,4,48,NULL,1),(68,6,48,NULL,1),(69,4,49,NULL,1),(70,6,49,NULL,1),(71,4,50,NULL,1),(72,4,51,NULL,1),(73,6,51,NULL,1),(74,4,52,NULL,1),(75,6,52,NULL,1),(76,4,53,NULL,1),(77,6,53,NULL,1),(78,4,54,NULL,1),(79,6,54,NULL,1),(80,4,55,NULL,1),(81,4,56,NULL,1),(82,6,57,NULL,1),(83,6,58,NULL,1),(84,6,59,NULL,1),(85,4,60,NULL,1),(86,6,60,NULL,1),(87,4,61,NULL,1),(88,6,61,NULL,1),(89,4,62,NULL,1),(90,6,62,NULL,1),(91,4,63,NULL,1),(92,6,63,NULL,1),(93,4,64,NULL,1),(94,6,64,NULL,1),(95,4,65,NULL,1),(96,6,65,NULL,1),(97,4,66,NULL,1),(98,6,66,NULL,1),(99,4,67,NULL,1),(100,6,67,NULL,1),(101,4,68,NULL,1),(102,6,68,NULL,1),(103,4,69,NULL,1),(104,6,69,NULL,1),(105,4,70,NULL,1),(106,6,70,NULL,1),(107,4,71,NULL,1),(108,6,71,NULL,1),(109,4,72,NULL,1),(110,6,72,NULL,1),(111,4,73,NULL,1),(112,4,74,NULL,1),(113,4,75,NULL,1),(114,4,76,NULL,1);
/*!40000 ALTER TABLE `gameroomplayer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gameround`
--

DROP TABLE IF EXISTS `gameround`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gameround` (
  `roundId` int NOT NULL AUTO_INCREMENT,
  `roundName` varchar(50) DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `timeLimit` int DEFAULT NULL,
  `difficultyLevel` enum('EASY','MEDIUM','HARD') DEFAULT NULL,
  `roomId` int DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`roundId`),
  KEY `roomId` (`roomId`),
  CONSTRAINT `gameround_ibfk_1` FOREIGN KEY (`roomId`) REFERENCES `gameroom` (`roomId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gameround`
--

LOCK TABLES `gameround` WRITE;
/*!40000 ALTER TABLE `gameround` DISABLE KEYS */;
INSERT INTO `gameround` VALUES (1,'Round 1','2024-10-03 10:20:00','2024-10-03 10:40:00',20,'EASY',1,'/img/map_tmp.png');
/*!40000 ALTER TABLE `gameround` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gamesession`
--

DROP TABLE IF EXISTS `gamesession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gamesession` (
  `sessionId` int NOT NULL AUTO_INCREMENT,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `roomId` int DEFAULT NULL,
  PRIMARY KEY (`sessionId`),
  KEY `roomId` (`roomId`),
  CONSTRAINT `gamesession_ibfk_1` FOREIGN KEY (`roomId`) REFERENCES `gameroom` (`roomId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gamesession`
--

LOCK TABLES `gamesession` WRITE;
/*!40000 ALTER TABLE `gamesession` DISABLE KEYS */;
INSERT INTO `gamesession` VALUES (1,'2024-10-03 10:15:00','2024-10-03 10:45:00',1);
/*!40000 ALTER TABLE `gamesession` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gamesessionplayer`
--

DROP TABLE IF EXISTS `gamesessionplayer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gamesessionplayer` (
  `sessionPlayerId` int NOT NULL AUTO_INCREMENT,
  `sessionId` int DEFAULT NULL,
  `userId` int DEFAULT NULL,
  `score` int DEFAULT NULL,
  PRIMARY KEY (`sessionPlayerId`),
  KEY `sessionId` (`sessionId`),
  KEY `userId` (`userId`),
  CONSTRAINT `gamesessionplayer_ibfk_1` FOREIGN KEY (`sessionId`) REFERENCES `gamesession` (`sessionId`),
  CONSTRAINT `gamesessionplayer_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gamesessionplayer`
--

LOCK TABLES `gamesessionplayer` WRITE;
/*!40000 ALTER TABLE `gamesessionplayer` DISABLE KEYS */;
/*!40000 ALTER TABLE `gamesessionplayer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roundtrashitem`
--

DROP TABLE IF EXISTS `roundtrashitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roundtrashitem` (
  `roundTrashId` int NOT NULL AUTO_INCREMENT,
  `roundId` int DEFAULT NULL,
  `trashId` int DEFAULT NULL,
  PRIMARY KEY (`roundTrashId`),
  KEY `roundId` (`roundId`),
  KEY `trashId` (`trashId`),
  CONSTRAINT `roundtrashitem_ibfk_1` FOREIGN KEY (`roundId`) REFERENCES `gameround` (`roundId`),
  CONSTRAINT `roundtrashitem_ibfk_2` FOREIGN KEY (`trashId`) REFERENCES `trashitem` (`itemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roundtrashitem`
--

LOCK TABLES `roundtrashitem` WRITE;
/*!40000 ALTER TABLE `roundtrashitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `roundtrashitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trashitem`
--

DROP TABLE IF EXISTS `trashitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trashitem` (
  `itemId` int NOT NULL AUTO_INCREMENT,
  `itemName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`itemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trashitem`
--

LOCK TABLES `trashitem` WRITE;
/*!40000 ALTER TABLE `trashitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `trashitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userId` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `totalPoints` int DEFAULT NULL,
  `status` enum('ONLINE','OFFLINE','PLAYING') DEFAULT NULL,
  `lastLogin` datetime DEFAULT NULL,
  `currentRoomId` int DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userId`),
  KEY `currentRoomId` (`currentRoomId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'alice','pass123',1200,'ONLINE','2024-10-03 10:30:00',NULL,NULL),(2,'bob','pass456',1500,'OFFLINE','2024-10-02 09:00:00',NULL,NULL),(3,'charlie','pass789',1000,'PLAYING','2024-10-04 11:00:00',1,NULL),(4,'123','123',1000,'ONLINE','2024-10-23 22:02:29',NULL,'/img/user.png'),(5,'Username','hang572003',NULL,NULL,NULL,NULL,NULL),(6,'1234','1234',0,'ONLINE','2024-10-23 17:22:24',NULL,'/img/user22.png'),(7,'12345','12345',0,'ONLINE',NULL,NULL,NULL),(8,'123456','123',0,'ONLINE','2024-10-17 16:28:29',NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-23 22:22:29
