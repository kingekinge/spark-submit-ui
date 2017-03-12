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
-- Table structure for table `task_standalone`
--

DROP TABLE IF EXISTS `task_standalone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_standalone` (
  `app_id` varchar(255) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `cores` int(11) DEFAULT NULL,
  `memoryperslave` int(10) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `starttime` bigint(255) DEFAULT NULL,
  `duration` bigint(255) DEFAULT NULL,
  `user` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`app_id`)
)
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_standalone`
--

LOCK TABLES `task_standalone` WRITE;
/*!40000 ALTER TABLE `task_standalone` DISABLE KEYS */;
INSERT INTO `task_standalone` VALUES ('app-20160829173743-0025','HFDSWordCount',3,2048,'KILLED',1472463463782,90232,'liangkai1@staff.weibo.com'),('app-20160829173804-0026','HFDSWordCount',3,2048,'KILLED',1472463484692,64499,'xiaopeng4@staff.weibo.com'),('app-20160829173832-0027','HFDSWordCount',3,2048,'KILLED',1472463512961,26254,'xiaopeng4@staff.weibo.com'),('app-20160829173845-0028','HFDSWordCount',3,2048,'KILLED',1472463525986,7753,'xiaopeng4@staff.weibo.com'),('app-20160829174701-0029','HFDSWordCount',3,2048,'KILLED',1472464021743,56121,'xiaopeng4@staff.weibo.com'),('app-20160829175148-0030','HFDSWordCount',3,2048,'KILLED',1472464308237,25954,'liangkai1@staff.weibo.com'),('app-20160829181256-0031','HFDSWordCount',3,2048,'KILLED',1472465576250,55398,'liangkai1@staff.weibo.com'),('app-20160829190518-0032','HFDSWordCount',3,2048,'KILLED',1472468718083,66501,'liangkai1@staff.weibo.com'),('app-20160830110513-0000','HFDSWordCount',3,2048,'KILLED',1472526313908,52093,'liangkai1@staff.weibo.com'),('app-20160830110605-0001','HFDSWordCount',3,2048,'KILLED',1472526365637,84312,'liangkai1@staff.weibo.com'),('app-20160830111644-0002','HFDSWordCount',3,2048,'KILLED',1472527004613,40609,'liangkai1@staff.weibo.com'),('app-20160830113932-0003','HFDSWordCount',3,2048,'KILLED',1472528372009,119364,'liangkai1@staff.weibo.com'),('app-20160830114518-0005','Spark Pi',2,2048,'FINISHED',1472528718815,5187,'liangkai1@staff.weibo.com'),('app-20160830114548-0006','Spark Pi',2,2048,'FINISHED',1472528748599,5407,'liangkai1@staff.weibo.com'),('app-20160902143142-0000','Spark Pi',4,1024,'KILLED',1472797902597,46277,'test'),('app-20160902180244-0000','Spark Pi',2,2048,'FINISHED',1472810564668,26964,'kingekinge@163.com'),('app-20160905120752-0000','Spark Pi',2,2048,'FINISHED',1473048472240,25834,'kingekinge@163.com'),('app-20160905150833-0004','Spark Pi',2,2048,'FINISHED',1473059313160,26822,'kingekinge@163.com'),('app-20160905151042-0005','Spark Pi',2,2048,'FINISHED',1473059442755,25565,'kingekinge@163.com'),('app-20160907135634-0000','HFDSWordCount',4,2048,'KILLED',1473227794987,12654,'jiazheng1@staff.weibo.com'),('app-20160907155656-0000','Spark Pi',4,2048,'FINISHED',1473235016274,40725,'liangkai1@staff.weibo.com'),('app-20160907155814-0001','Spark Pi',4,2048,'FINISHED',1473235094858,42180,'liangkai1@staff.weibo.com'),('app-20160907165415-0002','Spark Pi',4,2048,'FINISHED',1473238455372,38533,'spark'),('app-20160907172220-0003','Spark Pi',4,2048,'FINISHED',1473240140070,43125,'spark'),('app-20161017171351-0000','Spark Pi',3,2048,'FINISHED',1476695631587,523969,'liangkai1@staff.weibo.com'),('app-20161017172415-0001','Spark Pi',3,2048,'FINISHED',1476696255120,513347,'liangkai1@staff.weibo.com'),('app-20161017173841-0002','Spark Pi',3,2048,'FINISHED',1476697121999,489395,'liangkai1@staff.weibo.com'),('app-20161017175505-0003','Spark Pi',3,2048,'FINISHED',1476698105589,850647,'liangkai1@staff.weibo.com'),('app-20161017181857-0004','Spark Pi',3,2048,'FINISHED',1476699537632,766427,'liangkai1@staff.weibo.com'),('app-20161018173538-0005','Spark Pi',4,2048,'FINISHED',1476783338727,735988,'liangkai1@staff.weibo.com'),('app-20161021192350-0008','Spark Pi',4,0,'KILLED',1477049030412,291519,'liangkai1@staff.weibo.com'),('app-20161024112557-0009','Spark Pi',4,2048,'FINISHED',1477279557797,38138,'liangkai1@staff.weibo.com'),('app-20161024143638-0003','Spark Pi',4,2048,'FINISHED',1477290998310,168676,'liangkai1@staff.weibo.com'),('app-20161024154817-0000','Spark Pi',4,2048,'FINISHED',1477295297150,165778,'liangkai1@staff.weibo.com'),('app-20161024155208-0001','Spark Pi',4,2048,'FINISHED',1477295528495,162544,'liangkai1@staff.weibo.com'),('app-20161024155507-0002','Spark Pi',4,2048,'FINISHED',1477295707898,165112,'liangkai1@staff.weibo.com'),('app-20161024155919-0003','Spark Pi',4,2048,'FINISHED',1477295959802,167510,'liangkai1@staff.weibo.com'),('app-20161024161117-0004','Spark Pi',4,2048,'FINISHED',1477296677713,173410,'liangkai1@staff.weibo.com'),('app-20161107144820-0005','Spark Pi',4,2048,'FINISHED',1478501300354,164982,'liangkai1@staff.weibo.com'),('app-20161107145131-0006','Spark Pi',4,2048,'FINISHED',1478501491543,165027,'liangkai1@staff.weibo.com'),('app-20161110155841-0007','Spark Pi',4,2048,'FINISHED',1478764721852,427,'liangkai1@staff.weibo.com'),('app-20161110155953-0008','Spark Pi',4,2048,'KILLED',1478764793862,202619,'liangkai1@staff.weibo.com'),('app-20161110160212-0009','Spark Pi',4,2048,'KILLED',1478764932786,61728,'liangkai1@staff.weibo.com'),('app-20161110160535-0010','Spark Pi',4,2048,'KILLED',1478765135510,202289,'liangkai1@staff.weibo.com'),('app-20161110161633-0011','Spark Pi',4,2048,'FINISHED',1478765793685,390745,'liangkai1@staff.weibo.com'),('app-20161110162344-0000','Spark Pi',4,2048,'KILLED',1478766224980,56655,'liangkai1@staff.weibo.com'),('app-20161110170011-0001','Spark Pi',4,2048,'KILLED',1478768411600,170592,'liangkai1@staff.weibo.com'),('app-20161110170326-0002','Spark Pi',4,2048,'KILLED',1478768606366,6915,'liangkai1@staff.weibo.com'),('app-20161111155921-0012','Spark Pi',3,2048,'KILLED',1478851161187,38955,'liangkai1@staff.weibo.com'),('app-20161111160527-0013','Spark Pi',3,2048,'KILLED',1478851527456,22366,'liangkai1@staff.weibo.com'),('app-20161111160800-0014','Spark Pi',3,2048,'KILLED',1478851680031,109084,'liangkai1@staff.weibo.com'),('app-20161223155534-0000','Spark Pi',4,1024,'KILLED',1482479734800,114290,'kingekinge@163.com'),('app-20161223155804-0001','Spark Pi',4,1024,'FINISHED',1482479884207,370054,'kingekinge@163.com'),('app-20170209143254-0000','Spark Pi',4,1024,'FINISHED',1486621974405,190814,'kingekinge@163.com'),('app-20170224173827-0000','Spark Pi',4,2048,'FAILED',1487929107087,109,'liangkai1@staff.weibo.com'),('app-20170224174250-0001','Spark Pi',6,0,'FAILED',1487929370569,73,'liangkai1@staff.weibo.com'),('app-20170224174652-0002','Spark Pi',4,2048,'KILLED',1487929612805,30969,'liangkai1@staff.weibo.com');
/*!40000 ALTER TABLE `task_standalone` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-06 13:50:01
