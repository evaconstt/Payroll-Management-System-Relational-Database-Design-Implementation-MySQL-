-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: hy360_payroll_database
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `child`
--

DROP TABLE IF EXISTS `child`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `child` (
  `child_id` int(11) NOT NULL AUTO_INCREMENT,
  `birth_date` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`child_id`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dedomena_katavolwn_misthodosias`
--

DROP TABLE IF EXISTS `dedomena_katavolwn_misthodosias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dedomena_katavolwn_misthodosias` (
  `pay_date` varchar(100) DEFAULT NULL,
  `pay_amount` int(11) DEFAULT NULL,
  `dkm_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`dkm_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3932 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dedomena_misthodosias`
--

DROP TABLE IF EXISTS `dedomena_misthodosias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dedomena_misthodosias` (
  `dm_id` int(11) NOT NULL AUTO_INCREMENT,
  `staff_type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`dm_id`)
) ENGINE=InnoDB AUTO_INCREMENT=161 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `name` varchar(100) DEFAULT NULL,
  `home_address` varchar(100) DEFAULT NULL,
  `start_date` varchar(100) DEFAULT NULL,
  `marriage_status` varchar(100) DEFAULT NULL,
  `phone_number` varchar(100) DEFAULT NULL,
  `iban` varchar(100) DEFAULT NULL,
  `bank_name` varchar(100) DEFAULT NULL,
  `id_employee` int(11) NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(100) DEFAULT NULL,
  `children_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_employee`)
) ENGINE=InnoDB AUTO_INCREMENT=191 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `employee_found`
--

DROP TABLE IF EXISTS `employee_found`;
/*!50001 DROP VIEW IF EXISTS `employee_found`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `employee_found` AS SELECT 
 1 AS `name`,
 1 AS `home_address`,
 1 AS `start_date`,
 1 AS `marriage_status`,
 1 AS `phone_number`,
 1 AS `iban`,
 1 AS `bank_name`,
 1 AS `id_employee`,
 1 AS `dept_name`,
 1 AS `pay_date`,
 1 AS `pay_amount`,
 1 AS `dkm_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `epidoma`
--

DROP TABLE IF EXISTS `epidoma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `epidoma` (
  `epidoma_id` int(11) NOT NULL AUTO_INCREMENT,
  `ep_type` varchar(100) DEFAULT NULL,
  `ep_amount` double DEFAULT NULL,
  PRIMARY KEY (`epidoma_id`)
) ENGINE=InnoDB AUTO_INCREMENT=321 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `has_child`
--

DROP TABLE IF EXISTS `has_child`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `has_child` (
  `child_id` int(11) DEFAULT NULL,
  `id_employee` int(11) DEFAULT NULL,
  KEY `has_child_child_FK` (`child_id`),
  KEY `has_child_employee_FK_1` (`id_employee`),
  CONSTRAINT `has_child_child_FK` FOREIGN KEY (`child_id`) REFERENCES `child` (`child_id`),
  CONSTRAINT `has_child_employee_FK` FOREIGN KEY (`id_employee`) REFERENCES `employee` (`id_employee`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `has_ep`
--

DROP TABLE IF EXISTS `has_ep`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `has_ep` (
  `dm_id` int(11) DEFAULT NULL,
  `epidoma_id` int(11) DEFAULT NULL,
  KEY `has_ep_dedomena_misthodosias_FK` (`dm_id`),
  KEY `has_ep_epidoma_FK` (`epidoma_id`),
  CONSTRAINT `has_ep_dedomena_misthodosias_FK` FOREIGN KEY (`dm_id`) REFERENCES `dedomena_misthodosias` (`dm_id`),
  CONSTRAINT `has_ep_epidoma_FK` FOREIGN KEY (`epidoma_id`) REFERENCES `epidoma` (`epidoma_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `has_misth`
--

DROP TABLE IF EXISTS `has_misth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `has_misth` (
  `id_employee` int(11) DEFAULT NULL,
  `dm_id` int(11) DEFAULT NULL,
  KEY `has_misth_employee_FK` (`id_employee`),
  KEY `has_misth_dedomena_misthodosias_FK` (`dm_id`),
  CONSTRAINT `has_misth_dedomena_misthodosias_FK` FOREIGN KEY (`dm_id`) REFERENCES `dedomena_misthodosias` (`dm_id`),
  CONSTRAINT `has_misth_employee_FK` FOREIGN KEY (`id_employee`) REFERENCES `employee` (`id_employee`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `has_simv`
--

DROP TABLE IF EXISTS `has_simv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `has_simv` (
  `dm_id` int(11) DEFAULT NULL,
  `contract_id` int(11) DEFAULT NULL,
  KEY `has_simv_dedomena_misthodosias_FK` (`dm_id`),
  KEY `has_simv_simvasi_FK` (`contract_id`),
  CONSTRAINT `has_simv_dedomena_misthodosias_FK` FOREIGN KEY (`dm_id`) REFERENCES `dedomena_misthodosias` (`dm_id`),
  CONSTRAINT `has_simv_simvasi_FK` FOREIGN KEY (`contract_id`) REFERENCES `simvasi` (`contract_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pay`
--

DROP TABLE IF EXISTS `pay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pay` (
  `id_employee` int(11) DEFAULT NULL,
  `dkm_id` int(11) DEFAULT NULL,
  KEY `pay_employee_FK` (`id_employee`),
  KEY `pay_dedomena_katavolwn_misthodosias_FK` (`dkm_id`),
  CONSTRAINT `pay_dedomena_katavolwn_misthodosias_FK` FOREIGN KEY (`dkm_id`) REFERENCES `dedomena_katavolwn_misthodosias` (`dkm_id`),
  CONSTRAINT `pay_employee_FK` FOREIGN KEY (`id_employee`) REFERENCES `employee` (`id_employee`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `salary_status_for_didaktikos`
--

DROP TABLE IF EXISTS `salary_status_for_didaktikos`;
/*!50001 DROP VIEW IF EXISTS `salary_status_for_didaktikos`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `salary_status_for_didaktikos` AS SELECT 
 1 AS `id_employee`,
 1 AS `name`,
 1 AS `pay_amount`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `simvasi`
--

DROP TABLE IF EXISTS `simvasi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `simvasi` (
  `contract_id` int(11) NOT NULL AUTO_INCREMENT,
  `end_date_SIMB` varchar(100) DEFAULT NULL,
  `Start_date_SIMB` varchar(100) DEFAULT NULL,
  `salary_amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`contract_id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `sum_misthodosia_didaktikos`
--

DROP TABLE IF EXISTS `sum_misthodosia_didaktikos`;
/*!50001 DROP VIEW IF EXISTS `sum_misthodosia_didaktikos`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `sum_misthodosia_didaktikos` AS SELECT 
 1 AS `sum(dkm.pay_amount)`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'hy360_payroll_database'
--

--
-- Final view structure for view `employee_found`
--

/*!50001 DROP VIEW IF EXISTS `employee_found`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `employee_found` AS select `e`.`name` AS `name`,`e`.`home_address` AS `home_address`,`e`.`start_date` AS `start_date`,`e`.`marriage_status` AS `marriage_status`,`e`.`phone_number` AS `phone_number`,`e`.`iban` AS `iban`,`e`.`bank_name` AS `bank_name`,`e`.`id_employee` AS `id_employee`,`e`.`dept_name` AS `dept_name`,`dkm`.`pay_date` AS `pay_date`,`dkm`.`pay_amount` AS `pay_amount`,`dkm`.`dkm_id` AS `dkm_id` from ((`employee` `e` join `dedomena_katavolwn_misthodosias` `dkm`) join `pay` `p`) where `p`.`id_employee` = `e`.`id_employee` and `p`.`dkm_id` = `dkm`.`dkm_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `salary_status_for_didaktikos`
--

/*!50001 DROP VIEW IF EXISTS `salary_status_for_didaktikos`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `salary_status_for_didaktikos` AS select `e`.`id_employee` AS `id_employee`,`e`.`name` AS `name`,`dkm`.`pay_amount` AS `pay_amount` from ((((`employee` `e` join `dedomena_katavolwn_misthodosias` `dkm`) join `dedomena_misthodosias` `dm`) join `pay` `p`) join `has_misth` `m`) where `e`.`id_employee` = `p`.`id_employee` and `dkm`.`dkm_id` = `p`.`dkm_id` and `m`.`id_employee` = `e`.`id_employee` and `dm`.`dm_id` = `m`.`dm_id` and `dm`.`staff_type` = 'Didaktikos' */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sum_misthodosia_didaktikos`
--

/*!50001 DROP VIEW IF EXISTS `sum_misthodosia_didaktikos`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `sum_misthodosia_didaktikos` AS select sum(`dkm`.`pay_amount`) AS `sum(dkm.pay_amount)` from `dedomena_katavolwn_misthodosias` `dkm` where `dkm`.`dkm_id` = (select `p`.`dkm_id` from `pay` `p` where `p`.`id_employee` = (select `e`.`id_employee` from `employee` `e` where `e`.`id_employee` = (select `has_misth`.`id_employee` from `has_misth` where `has_misth`.`dm_id` = (select `dm`.`dm_id` from `dedomena_misthodosias` `dm` where `dm`.`staff_type` = 'Didaktikos')))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-25 21:33:13