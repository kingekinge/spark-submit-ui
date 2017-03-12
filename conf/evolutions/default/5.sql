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
-- Table structure for table `task_yarn`
--

DROP TABLE IF EXISTS `task_yarn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_yarn` (
  `application_id` varchar(255) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `apptype` varchar(45) DEFAULT NULL,
  `queue` varchar(45) DEFAULT NULL,
  `starttime` bigint(255) DEFAULT NULL,
  `finishtime` bigint(255) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `user` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`application_id`)
)
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_yarn`
--

LOCK TABLES `task_yarn` WRITE;
/*!40000 ALTER TABLE `task_yarn` DISABLE KEYS */;
INSERT INTO `task_yarn` VALUES ('application_1471502301744_0033','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1471946449657,1471946590402,'KILLED','ruilin@staff.weibo.com'),('application_1471502301744_0034','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1471946504666,1471946585370,'KILLED','ruilin@staff.weibo.com'),('application_1471502301744_0035','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1471946909786,1471946953179,'KILLED','ruilin@staff.weibo.com'),('application_1471502301744_0036','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1471947287570,1471948970320,'FAILED','ruilin@staff.weibo.com'),('application_1471502301744_0037','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1471947295991,1471949531415,'FAILED','ruilin@staff.weibo.com'),('application_1471502301744_0038','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1471947303100,1471950749076,'FAILED','xiaopeng4@staff.weibo.com'),('application_1471502301744_0039','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1471947309556,1471950750083,'FAILED','xiaopeng4@staff.weibo.com'),('application_1471502301744_0040','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1471947316769,1471947568409,'KILLED','xiaopeng4@staff.weibo.com'),('application_1471502301744_0041','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1472011603136,1472011658126,'FINISHED','xiaopeng4@staff.weibo.com'),('application_1471502301744_0042','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1472013378221,1472013883316,'FINISHED','xiaopeng4@staff.weibo.com'),('application_1471502301744_0043','mainq','SPARK','default',1472019539676,1472019559186,'KILLED','xiaopeng4@staff.weibo.com'),('application_1471502301744_0060','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1472022481347,1472022496438,'KILLED','xiaopeng4@staff.weibo.com'),('application_1471502301744_0065','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1472034974196,1472035044201,'FINISHED','liangkai1@staff.weibo.com'),('application_1471502301744_0066','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1472097632992,1472097660119,'FINISHED','xiaopeng4@staff.weibo.com'),('application_1471502301744_0067','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1472098940435,1472098970590,'KILLED','xiaopeng4@staff.weibo.com'),('application_1471502301744_0068','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1472099334849,1472099377207,'KILLED','xiaopeng4@staff.weibo.com'),('application_1471502301744_0069','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1472106568677,0,'KILLED','xiaopeng4@staff.weibo.com'),('application_1472438450103_0003','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1472465345416,1472465416934,'KILLED','liangkai1@staff.weibo.com'),('application_1472438450103_0007','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1472468524187,1472468666372,'KILLED','liangkai1@staff.weibo.com'),('application_1472438450103_0008','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1472468547405,1472468780056,'KILLED','liangkai1@staff.weibo.com'),('application_1472438450103_0012','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1472527058867,1472528074691,'KILLED','liangkai1@staff.weibo.com'),('application_1472538323529_0002','org.apache.spark.examples.SparkPi','SPARK','default',1472538510924,1472538633865,'FINISHED','liangkai1@staff.weibo.com'),('application_1472611321353_0002','org.apache.spark.examples.SparkPi','SPARK','default',1472626242201,1472627261211,'FINISHED','liangkai1@staff.weibo.com'),('application_1472611321353_0005','org.apache.spark.examples.SparkPi','SPARK','default',1472629564953,1472630247092,'KILLED','liangkai1@staff.weibo.com'),('application_1472611321353_0007','org.apache.spark.examples.SparkPi','SPARK','default',1472630490656,1472630504270,'KILLED','liangkai1@staff.weibo.com'),('application_1472611321353_0008','org.apache.spark.examples.SparkPi','SPARK','default',1472634980958,1472635029162,'KILLED','liangkai1@staff.weibo.com'),('application_1472611321353_0010','org.apache.spark.examples.SparkPi','SPARK','default',1472635602629,1472635648853,'FINISHED','liangkai1@staff.weibo.com'),('application_1472696197268_0003','mainq','SPARK','default',1472716310460,1472716326724,'KILLED','liangkai1@staff.weibo.com'),('application_1472781969555_0004','org.apache.spark.examples.SparkPi','SPARK','default',1472786604724,1472786630436,'KILLED','liangkai1@staff.weibo.com'),('application_1472781969555_0006','org.apache.spark.examples.SparkPi','SPARK','default',1472797751446,1472797765934,'FINISHED','test'),('application_1472781969555_0010','org.apache.spark.examples.SparkPi','SPARK','default',1472801545593,1472801592767,'FINISHED','xiaopeng4@staff.weibo.com'),('application_1472781969555_0011','org.apache.spark.examples.SparkPi','SPARK','default',1472801708174,1472801757235,'FINISHED','xiaopeng4@staff.weibo.com'),('application_1472781969555_0013','mainq','SPARK','default',1472811931330,1472811960413,'FAILED','kingekinge@163.com'),('application_1473046703088_0002','org.apache.spark.examples.SparkPi','SPARK','default',1473047869535,1473047886895,'FINISHED','kingekinge@163.com'),('application_1473046703088_0003','org.apache.spark.examples.SparkPi','SPARK','default',1473048026253,1473048046056,'FINISHED','kingekinge@163.com'),('application_1473046703088_0004','org.apache.spark.examples.SparkPi','SPARK','default',1473059703874,1473059749354,'FINISHED','kingekinge@163.com'),('application_1473228207769_0002','com.weibo.spark.stream.HDFSWordCount','SPARK','default',1473228348124,1473228362744,'KILLED','jiazheng1@staff.weibo.com'),('application_1473755209768_0002','org.apache.spark.examples.SparkPi','SPARK','default',1473755271296,1473755311887,'KILLED','liangkai1@staff.weibo.com'),('application_1476358590704_0001','org.apache.spark.examples.SparkPi','SPARK','default',1476695507791,1476695585754,'KILLED','liangkai1@staff.weibo.com'),('application_1476931577277_0144','RecommendAtt','SPARK','default',1478759355529,1478759936966,'KILLED','ruilin@staff.weibo.com'),('application_1476931577277_0147','spark.graphx.RecoAttTest','SPARK','default',1478760736045,1478763179368,'FAILED','ruilin@staff.weibo.com'),('application_1477294157293_0013','org.apache.spark.examples.SparkPi','SPARK','default',1478851426609,1478851439599,'KILLED','liangkai1@staff.weibo.com'),('application_1477294157293_0016','org.apache.spark.examples.SparkPi','SPARK','default',1478852162026,1478852174882,'KILLED','liangkai1@staff.weibo.com'),('application_1477994556278_0006','org.apache.spark.examples.SparkPi','SPARK','default',1478852085613,1478852095958,'KILLED','liangkai1@staff.weibo.com');
/*!40000 ALTER TABLE `task_yarn` ENABLE KEYS */;
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
