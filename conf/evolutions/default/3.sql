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
-- Table structure for table `task_msg`
--

DROP TABLE IF EXISTS `task_msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_msg` (
  `id` varchar(255) NOT NULL,
  `state` varchar(45) DEFAULT NULL,
  `user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_msg`
--

LOCK TABLES `task_msg` WRITE;
/*!40000 ALTER TABLE `task_msg` DISABLE KEYS */;
INSERT INTO `task_msg` VALUES ('app-20160902143142-0000','KILLED','test'),('app-20160907172220-0003','FINISHED','spark'),('app-20161017172415-0001','FINISHED','liangkai1@staff.weibo.com'),('app-20161017173841-0002','FINISHED','liangkai1@staff.weibo.com'),('app-20161018173538-0005','RUNNING','liangkai1@staff.weibo.com'),('app-20161021192350-0008','KILLED','liangkai1@staff.weibo.com'),('app-20161024112557-0009','FINISHED','liangkai1@staff.weibo.com'),('app-20161024143638-0003','FINISHED','liangkai1@staff.weibo.com'),('app-20161024154817-0000','FINISHED','liangkai1@staff.weibo.com'),('app-20161024155208-0001','FINISHED','liangkai1@staff.weibo.com'),('app-20161024155507-0002','FINISHED','liangkai1@staff.weibo.com'),('app-20161024155919-0003','FINISHED','liangkai1@staff.weibo.com'),('app-20161024161117-0004','FINISHED','liangkai1@staff.weibo.com'),('app-20161107144820-0005','FINISHED','liangkai1@staff.weibo.com'),('app-20161107145131-0006','FINISHED','liangkai1@staff.weibo.com'),('app-20161110155841-0007','FINISHED','liangkai1@staff.weibo.com'),('app-20161110155953-0008','KILLED','liangkai1@staff.weibo.com'),('app-20161110160212-0009','KILLED','liangkai1@staff.weibo.com'),('app-20161110160535-0010','KILLED','liangkai1@staff.weibo.com'),('app-20161110161633-0011','FINISHED','liangkai1@staff.weibo.com'),('app-20161110162344-0000','KILLED','liangkai1@staff.weibo.com'),('app-20161110170011-0001','KILLED','liangkai1@staff.weibo.com'),('app-20161110170326-0002','KILLED','liangkai1@staff.weibo.com'),('app-20161111155921-0012','KILLED','liangkai1@staff.weibo.com'),('app-20161111160527-0013','KILLED','liangkai1@staff.weibo.com'),('app-20161111160800-0014','KILLED','liangkai1@staff.weibo.com'),('app-20170209143254-0000','FINISHED','kingekinge@163.com'),('app-20170224173827-0000','FAILED','liangkai1@staff.weibo.com'),('app-20170224174250-0001','FAILED','liangkai1@staff.weibo.com'),('app-20170224174652-0002','KILLED','liangkai1@staff.weibo.com'),('application_1472781969555_0005','FINISHED','test'),('application_1472781969555_0006','FINISHED','test'),('application_1472781969555_0009','FINISHED','xiaopeng4@staff.weibo.com'),('application_1472781969555_0010','FINISHED','xiaopeng4@staff.weibo.com'),('application_1476358590704_0001','KILLED','liangkai1@staff.weibo.com'),('application_1476931577277_0144','KILLED','ruilin@staff.weibo.com'),('application_1476931577277_0147','ACCEPTED','ruilin@staff.weibo.com'),('application_1477294157293_0013','KILLED','liangkai1@staff.weibo.com'),('application_1477294157293_0016','KILLED','liangkai1@staff.weibo.com'),('application_1477994556278_0005','KILLED','liangkai1@staff.weibo.com'),('application_1477994556278_0006','KILLED','liangkai1@staff.weibo.com');
/*!40000 ALTER TABLE `task_msg` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-06 13:50:11
