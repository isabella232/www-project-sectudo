-- MySQL dump 10.13  Distrib 5.7.30, for Linux (x86_64)
--
-- Host: localhost    Database: demo
-- ------------------------------------------------------
-- Server version	5.7.30-0ubuntu0.16.04.1

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
-- Table structure for table `accountstable`
--

DROP TABLE IF EXISTS `accountstable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accountstable` (
  `account_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userid` int(10) NOT NULL DEFAULT '0',
  `accountname` varchar(45) NOT NULL DEFAULT '',
  `balance` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`account_id`),
  KEY `userid` (`userid`),
  CONSTRAINT `accountstable_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `usertable` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=703203 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accountstable`
--

LOCK TABLES `accountstable` WRITE;
/*!40000 ALTER TABLE `accountstable` DISABLE KEYS */;
INSERT INTO `accountstable` VALUES (10001,1000,'Current',14000),(10002,1000,'Savings',11000),(10003,1000,'Demat',4000),(20001,2000,'Current',25000),(20002,2000,'Savings',20000),(20003,2000,'Demat',5000),(30001,3000,'Current',4000),(30002,3000,'Savings',7000),(30003,3000,'Demat',2000),(40001,4000,'Current',8500),(40002,4000,'Savings',16000),(40003,4000,'Demat',3500),(90001,900,'Current',15000),(90002,900,'Savings',10000),(90003,900,'Demat',5000),(703201,1236,'Savings',1234666),(703202,1236,'Current',200000);
/*!40000 ALTER TABLE `accountstable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `active_logins`
--

DROP TABLE IF EXISTS `active_logins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `active_logins` (
  `fk_user_id` int(10) NOT NULL,
  `session_count` int(100) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`session_count`),
  KEY `fk_user_id` (`fk_user_id`),
  CONSTRAINT `active_logins_ibfk_1` FOREIGN KEY (`fk_user_id`) REFERENCES `susertable` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `active_logins`
--

LOCK TABLES `active_logins` WRITE;
/*!40000 ALTER TABLE `active_logins` DISABLE KEYS */;
/*!40000 ALTER TABLE `active_logins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `branch`
--

DROP TABLE IF EXISTS `branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `branch` (
  `branchname` varchar(45) NOT NULL,
  `Address` varchar(45) NOT NULL,
  `Phone` varchar(10) NOT NULL,
  `Fax` varchar(10) NOT NULL,
  `city` varchar(45) NOT NULL,
  PRIMARY KEY (`branchname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branch`
--

LOCK TABLES `branch` WRITE;
/*!40000 ALTER TABLE `branch` DISABLE KEYS */;
INSERT INTO `branch` VALUES ('Andheri','Saki NAka','8426665','8426666','Mumbai'),('Connaught Place','36 Rajpath','2312101','23109021','Delhi'),('DumDum','Airport Road','1231091','1231090','Kolkata'),('Mambalam','Stadium Road','4316665','4316666','Chennai'),('Parel','Phoenix Mills','9086665','9086666','Mumbai'),('TNagar','Valluvar Kotam','6316665','6316666','Chennai');
/*!40000 ALTER TABLE `branch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emp`
--

DROP TABLE IF EXISTS `emp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `emp` (
  `emp_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `age` int(11) NOT NULL,
  `manager_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`emp_id`),
  KEY `Fk_mgr` (`manager_id`),
  CONSTRAINT `Fk_mgr` FOREIGN KEY (`manager_id`) REFERENCES `emp` (`emp_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emp`
--

LOCK TABLES `emp` WRITE;
/*!40000 ALTER TABLE `emp` DISABLE KEYS */;
INSERT INTO `emp` VALUES (1,'Rakesh',22,NULL),(2,'Sam',33,1),(3,'Bob',33,2),(4,'Peter',22,2);
/*!40000 ALTER TABLE `emp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kycdetails`
--

DROP TABLE IF EXISTS `kycdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kycdetails` (
  `fk_userid` int(10) DEFAULT NULL,
  `fullname` varchar(100) NOT NULL,
  `address` varchar(1000) NOT NULL,
  `pannumber` varchar(50) NOT NULL,
  `emp_type` varchar(100) NOT NULL,
  `designation` varchar(100) NOT NULL,
  `salary` int(100) NOT NULL,
  UNIQUE KEY `fk_userid` (`fk_userid`),
  CONSTRAINT `kycdetails_ibfk_1` FOREIGN KEY (`fk_userid`) REFERENCES `usertable` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kycdetails`
--

LOCK TABLES `kycdetails` WRITE;
/*!40000 ALTER TABLE `kycdetails` DISABLE KEYS */;
INSERT INTO `kycdetails` VALUES (3000,'Carol John','300, Caroline Building, Kerala','NDFR202D','Salaried','Chief Engineer',600000),(1236,'Super Cop','Mumbai','ABC100DB5','Salaried','Manager',1000000),(900,'Sky Rider','Mumbai','ANB4568DF','Salaried','Manager',1000000);
/*!40000 ALTER TABLE `kycdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `settings` (
  `id` int(11) NOT NULL,
  `setting_name` varchar(50) DEFAULT NULL,
  `setting_val` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
INSERT INTO `settings` VALUES (1,'sess_mode','JSE');
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `susertable`
--

DROP TABLE IF EXISTS `susertable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `susertable` (
  `userid` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=1237 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `susertable`
--

LOCK TABLES `susertable` WRITE;
/*!40000 ALTER TABLE `susertable` DISABLE KEYS */;
INSERT INTO `susertable` VALUES (1236,'kalpesh','8ed798c2ac0a041acaa7854b2871e6ace8afb02225e63c44084122e895455b83');
/*!40000 ALTER TABLE `susertable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usertable`
--

DROP TABLE IF EXISTS `usertable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usertable` (
  `userid` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL DEFAULT '',
  `password` varchar(45) DEFAULT '',
  `secure_pass` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=5001 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usertable`
--

LOCK TABLES `usertable` WRITE;
/*!40000 ALTER TABLE `usertable` DISABLE KEYS */;
INSERT INTO `usertable` VALUES (900,'skyrider1','myworld123',NULL),(1000,'skyrider2','myworld456',NULL),(1236,'supcop1',NULL,'d671813c81631abd3c68d226c3f9575fd6a20431cba64aa30293b419c0031933'),(2000,'Bob','bob123',NULL),(3000,'Carol','carol123',NULL),(4000,'supcop2',NULL,'8565f3f4b4a49c10627019b2f46bcdfc588f8701a1161fe5366f2a53282db52b'),(5000,'Eve','eve123',NULL);
/*!40000 ALTER TABLE `usertable` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-08 11:19:19
