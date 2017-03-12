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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` int(4) NOT NULL DEFAULT '0'
  PRIMARY KEY (`email`)
)
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('1578326883@qq.com','ruilin','1234',1,0),('detan@staff.sina.com.cn','detan','sdt007',1,1),('jianglong3@staff.weibo.com','jianglong3','Cjlzzu26',0,0),('jiazheng1@staff.sina.com.cn','jhazheng','jiazheng',1,1),('jiazheng1@staff.weibo.com','jiazheng','admin',1,1),('jiazheng8911@126.com','jhazheng','jiazheng',1,1),('kingekinge@163.com','kai','1',1,1),('liangkai1@staff.weibo.com','zhangliangkai','admin',1,1),('liushu1@staff.weibo.com','liushu1','740010494a',0,0),('ruilin@staff.weibo.com','tianruilin','admin',1,1),('spark','spark','spark',1,0),('test','testUser','test',1,1),('wangshun@staff.weibo.com','wangshun','qwerty',1,1),('xiaokang3','xiaokang3','1234',1,1),('xiaokang3@staff.weibo.com','chenxiaokang','1988101cxk',0,0),('xiaopeng4@staff.weibo.com','xiaopeng4','admin',1,1),('zhenfei1@staff.weibo.com','zhuzhenfei','admin',1,1);
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

-- Dump completed on 2017-03-06 13:50:09
