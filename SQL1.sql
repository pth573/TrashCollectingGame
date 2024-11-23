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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friendship`
--

LOCK TABLES `friendship` WRITE;
/*!40000 ALTER TABLE `friendship` DISABLE KEYS */;
INSERT INTO `friendship` VALUES (1,1,2,1),(2,1,3,0);
/*!40000 ALTER TABLE `friendship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gameresult`
--

DROP TABLE IF EXISTS `gameresult`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
CREATE TABLE `gameroom` (
                            `roomId` int NOT NULL AUTO_INCREMENT,
                            `roomName` varchar(50) DEFAULT NULL,
                            `maxPlayer` int DEFAULT NULL,
                            `currentPlayer` int DEFAULT NULL,
                            `status` enum('WAITING','IN_PROGRESS','FULL') DEFAULT NULL,
                            PRIMARY KEY (`roomId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gameroom`
--

LOCK TABLES `gameroom` WRITE;
/*!40000 ALTER TABLE `gameroom` DISABLE KEYS */;
INSERT INTO `gameroom` VALUES (1,'Room A',4,2,'WAITING'),(2,'Room B',4,3,'IN_PROGRESS');
/*!40000 ALTER TABLE `gameroom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gameroomplayer`
--

DROP TABLE IF EXISTS `gameroomplayer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gameroomplayer`
--

LOCK TABLES `gameroomplayer` WRITE;
/*!40000 ALTER TABLE `gameroomplayer` DISABLE KEYS */;
INSERT INTO `gameroomplayer` VALUES (1,1,1,'2024-10-03 10:00:00',1),(2,2,1,'2024-10-03 10:05:00',1);
/*!40000 ALTER TABLE `gameroomplayer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gameround`
--

DROP TABLE IF EXISTS `gameround`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
CREATE TABLE `gameround` (
                             `roundId` int NOT NULL AUTO_INCREMENT,
                             `roundName` varchar(50) DEFAULT NULL,
                             `startTime` datetime DEFAULT NULL,
                             `endTime` datetime DEFAULT NULL,
                             `timeLimit` int DEFAULT NULL,
                             `difficultyLevel` enum('EASY','MEDIUM','HARD') DEFAULT NULL,
                             `roomId` int DEFAULT NULL,
                             PRIMARY KEY (`roundId`),
                             KEY `roomId` (`roomId`),
                             CONSTRAINT `gameround_ibfk_1` FOREIGN KEY (`roomId`) REFERENCES `gameroom` (`roomId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gameround`
--

LOCK TABLES `gameround` WRITE;
/*!40000 ALTER TABLE `gameround` DISABLE KEYS */;
INSERT INTO `gameround` VALUES (1,'Round 1','2024-10-03 10:20:00','2024-10-03 10:40:00',20,'EASY',1);
/*!40000 ALTER TABLE `gameround` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gamesession`
--

DROP TABLE IF EXISTS `gamesession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
CREATE TABLE `gamesession` (
                               `sessionId` int NOT NULL AUTO_INCREMENT,
                               `startTime` datetime DEFAULT NULL,
                               `endTime` datetime DEFAULT NULL,
                               `roomId` int DEFAULT NULL,
                               PRIMARY KEY (`sessionId`),
                               KEY `roomId` (`roomId`),
                               CONSTRAINT `gamesession_ibfk_1` FOREIGN KEY (`roomId`) REFERENCES `gameroom` (`roomId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gamesession`
--

LOCK TABLES `gamesession` WRITE;
/*!40000 ALTER TABLE `gamesession` DISABLE KEYS */;
INSERT INTO `gamesession` VALUES (1,'2024-10-03 10:15:00','2024-10-03 10:45:00',1);
/*!40000 ALTER TABLE `gamesession` ENABLE KEYS */;
UNLOCK TABLES;

-- Final comments
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40101 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET SQL_NOTES=@OLD_SQL_NOTES */;
