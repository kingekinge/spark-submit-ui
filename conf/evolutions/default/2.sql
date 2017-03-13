-- MySQL dump 10.13  Distrib 5.7.12, for osx10.9 (x86_64)
--
-- Host: 10.73.33.41    Database: playdb
-- ------------------------------------------------------
-- Server version	5.6.30-76.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hadoop_metrics`
--

DROP TABLE IF EXISTS `hadoop_metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hadoop_metrics` (
  `ReceivedBytes` bigint(255) NOT NULL,
  `SentBytes` bigint(255) NOT NULL,
  `host` varchar(45) NOT NULL,
  `CapacityUsed` bigint(225) NOT NULL,
  `CapacityRemaining` bigint(255) NOT NULL,
  `CapacityUsedNonDFS` bigint(255) NOT NULL,
  `MemHeapUsedM` double NOT NULL,
  `MemNonHeapUsedM` double NOT NULL,
  `timestamp` bigint(255) NOT NULL
)
/*!40101 SET character_set_client = @saved_cs_client */;


