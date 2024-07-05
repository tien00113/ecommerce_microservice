-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: microservice
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `total_item` int NOT NULL,
  `total_price` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (1,0,297000,1);
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_item`
--

DROP TABLE IF EXISTS `cart_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` bigint NOT NULL,
  `product_id` bigint DEFAULT NULL,
  `quantity` int NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `cart_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1uobyhgl1wvgt1jpccia8xxs3` (`cart_id`),
  CONSTRAINT `FK1uobyhgl1wvgt1jpccia8xxs3` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_item`
--

LOCK TABLES `cart_item` WRITE;
/*!40000 ALTER TABLE `cart_item` DISABLE KEYS */;
INSERT INTO `cart_item` VALUES (6,198000,2,2,1,1),(7,99000,4,1,1,1);
/*!40000 ALTER TABLE `cart_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `level` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK46ccwnsi9409t36lurvtyljak` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,1,'Quần',NULL),(3,2,'Quần Short',1),(4,1,'Áo',NULL);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `message` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` bigint NOT NULL,
  `product_id` bigint DEFAULT NULL,
  `quantity` int NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt4dc2r9nbvbujrljv3e23iibt` (`order_id`),
  CONSTRAINT `FKt4dc2r9nbvbujrljv3e23iibt` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,198198000,2,2002,1,1),(2,990000,2,10,1,2),(3,99000,2,1,1,3),(4,99000,2,1,1,4),(5,99000,2,1,1,5);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `delivery_date_time` datetime(6) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `payment_status` enum('FAILED','NEW','ROLLBACK','SUCCESS') DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `status` enum('FAILED','NEW','ROLLBACK','SUCCESS') DEFAULT NULL,
  `stock_status` enum('FAILED','NEW','ROLLBACK','SUCCESS') DEFAULT NULL,
  `total_price` bigint NOT NULL,
  `update_status_at` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'hà nội','2024-06-26 21:31:11.050339',NULL,'kafka fiox','240626CLTSAOMKO','VNPay','NEW','0908765431','ROLLBACK','NEW',198198000,'2024-06-26 21:31:11.050339',1),(2,'hà nội','2024-06-26 21:40:03.770067',NULL,'kafka fiox','240626CLTEWHYFE','VNPay','NEW','0908765431','SUCCESS','NEW',990000,'2024-06-26 21:40:03.770067',1),(3,'hà nội','2024-06-28 15:26:15.659375',NULL,'kafka fiox','240628CLTFZERUK','VNPay','SUCCESS','0908765431','SUCCESS','SUCCESS',99000,'2024-06-28 15:26:15.660371',1),(4,'hà nội','2024-06-28 15:56:02.643544',NULL,'kafka fiox','240628CLTUGWZAQ','VNPay','SUCCESS','0908765431','SUCCESS','SUCCESS',99000,'2024-06-28 15:56:02.643544',1),(5,'hà nội','2024-06-28 16:50:22.365879',NULL,'kafka fiox','240628CLTOFCBXI','VNPay','SUCCESS','0908765431','SUCCESS','SUCCESS',99000,'2024-06-28 16:50:22.365879',1);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `create_at` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `start_payment` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,198198000,'14480371','20240626213328','VNPay','240626CLTSAOMKO','SUCCESS',NULL),(2,990000,'14480384','20240626214227','VNPay','240626CLTEWHYFE','SUCCESS',NULL),(3,99000,'14483803','20240628152650','VNPay','240628CLTFZERUK','SUCCESS',NULL),(4,99000,'14483884','20240628155647','VNPay','240628CLTUGWZAQ','SUCCESS',NULL),(5,99000,'14484010','20240628165053','VNPay','240628CLTOFCBXI','SUCCESS',NULL);
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process_redis`
--

DROP TABLE IF EXISTS `process_redis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `process_redis` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `last_updated_at` datetime(6) DEFAULT NULL,
  `process_name` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process_redis`
--

LOCK TABLES `process_redis` WRITE;
/*!40000 ALTER TABLE `process_redis` DISABLE KEYS */;
INSERT INTO `process_redis` VALUES (1,'2024-06-27 23:01:24.910989','checkForUpdates','COMPLETED','2024-06-27 23:01:24.910989'),(2,'2024-06-27 17:21:26.250512','scanFullProducts','COMPLETED','2024-06-27 16:40:36.489000'),(3,'2024-06-27 16:40:36.494001','processQueue','COMPLETED','2024-06-27 16:40:36.494001'),(4,'2024-06-27 16:40:36.497563','processQueue','COMPLETED','2024-06-27 16:40:36.497563');
/*!40000 ALTER TABLE `process_redis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` bigint DEFAULT NULL,
  `stock` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1mtsbur82frn64de7balymq9s` (`category_id`),
  CONSTRAINT `FK1mtsbur82frn64de7balymq9s` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,_binary '','2024-06-26 14:57:29.043202','This is a sample product description.','Product 222 updated',100000,20,'2024-06-27 17:07:07.919392',4),(2,_binary '','2024-06-26 14:57:50.961495','This is a sample product description.','Product 222 updated',100000,0,'2024-06-27 15:37:16.852746',1),(3,_binary '','2024-06-26 14:57:54.511959','This is a sample product description.','Product 222 updated',100000,0,'2024-06-27 15:27:39.427362',1),(4,_binary '','2024-06-26 14:57:58.212407','This is a sample product description.','Product 222 updated',100000,0,'2024-06-27 15:27:42.555925',1),(5,_binary '','2024-06-26 14:58:02.197973','This is a sample product description.','Product 222 updated',100000,0,'2024-06-27 15:27:45.502606',1),(6,_binary '','2024-06-26 14:58:05.946080','This is a sample product description.','Product 222 updated',100000,0,'2024-06-27 17:06:46.161402',3),(7,_binary '','2024-06-26 14:58:09.651380','This is a sample product description.','Product 222 updated',100000,0,'2024-06-27 15:37:01.171186',1),(8,_binary '','2024-06-26 14:59:41.629135','This is a sample product description.','Product 222 updated',100000,0,'2024-06-27 17:07:02.394661',4),(9,_binary '','2024-06-26 15:00:09.950270','This is a sample product description.','Product 222 updated',100000,0,'2024-06-27 17:06:56.192503',3),(10,_binary '','2024-06-26 15:00:16.350679','This is a sample product description.','Product 222 updated',100000,0,'2024-06-27 15:37:09.934914',1),(11,_binary '','2024-06-26 15:00:19.945933','This is an example product description.','Quần số 11',99000,0,'2024-06-26 15:00:19.945933',1),(12,_binary '','2024-06-26 15:00:25.062377','This is an example product description.','Quần số 12',99000,0,'2024-06-26 15:00:25.062377',1),(13,_binary '','2024-06-27 11:21:40.474379','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:40.474379',1),(14,_binary '','2024-06-27 11:21:41.788409','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:41.788409',1),(15,_binary '','2024-06-27 11:21:42.738304','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:42.738304',1),(16,_binary '','2024-06-27 11:21:43.581313','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:43.581313',1),(17,_binary '','2024-06-27 11:21:44.434943','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:44.434943',1),(18,_binary '','2024-06-27 11:21:45.218907','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:45.218907',1),(19,_binary '','2024-06-27 11:21:45.986195','This is a sample product description.','Product 222 updated',100000,0,'2024-06-27 17:07:12.203116',4),(20,_binary '','2024-06-27 11:21:46.841007','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:46.841007',1),(21,_binary '','2024-06-27 11:21:47.716312','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:47.716312',1),(22,_binary '','2024-06-27 11:21:48.552268','This is a sample product description.','Product 222 updated',100000,0,'2024-06-27 23:01:24.910989',3),(23,_binary '','2024-06-27 11:21:49.407393','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:49.407393',1),(24,_binary '','2024-06-27 11:21:50.293040','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:50.293040',1),(25,_binary '','2024-06-27 11:21:51.105783','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:51.105783',1),(26,_binary '','2024-06-27 11:21:51.944717','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:51.944717',1),(27,_binary '','2024-06-27 11:21:52.754449','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:52.754449',1),(28,_binary '','2024-06-27 11:21:53.618278','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:53.618278',1),(29,_binary '','2024-06-27 11:21:54.487876','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:54.487876',1),(30,_binary '','2024-06-27 11:21:55.354314','This is an example product description.','Quần số 12',99000,0,'2024-06-27 11:21:55.354314',1),(31,_binary '','2024-06-28 14:16:39.510991','This is an example product description.','Quần số 12',99000,0,'2024-06-28 14:16:39.510991',1),(32,_binary '','2024-07-02 10:32:08.714746','This is an example product description.','Quần số 12',99000,0,'2024-07-02 10:32:08.714746',1),(33,_binary '','2024-07-02 15:04:48.088325','This is an example product description.','Quần số 92',99000,0,'2024-07-02 15:04:48.088325',1),(34,_binary '','2024-07-02 15:10:44.333054','This is an example product description.','Quần số 92',99000,0,'2024-07-02 15:10:44.333054',1),(35,_binary '','2024-07-02 15:26:10.967783','This is an example product description.','đây là quần quan 98',99000,0,'2024-07-02 15:26:10.967783',1);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_variant`
--

DROP TABLE IF EXISTS `product_variant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_variant` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `color` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `price` bigint NOT NULL,
  `quantity` int NOT NULL,
  `size` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgrbbs9t374m9gg43l6tq1xwdj` (`product_id`),
  CONSTRAINT `FKgrbbs9t374m9gg43l6tq1xwdj` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_variant`
--

LOCK TABLES `product_variant` WRITE;
/*!40000 ALTER TABLE `product_variant` DISABLE KEYS */;
INSERT INTO `product_variant` VALUES (1,'Red','2024-06-26 14:57:29.043202','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 17:07:07.921392',1),(2,'Red','2024-06-26 14:57:29.043202','https://example.com/red_l.jpg',99000,2,'L','2024-06-27 17:07:07.921392',1),(3,'Blue','2024-06-26 14:57:29.043202','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 17:07:07.921392',1),(4,'Red','2024-06-26 14:57:50.961495','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 15:37:16.854746',2),(5,'Red','2024-06-26 14:57:50.961495','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 15:37:16.854746',2),(6,'Blue','2024-06-26 14:57:50.961495','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 15:37:16.854746',2),(7,'Red','2024-06-26 14:57:54.512961','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 15:27:39.428363',3),(8,'Red','2024-06-26 14:57:54.512961','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 15:27:39.428363',3),(9,'Blue','2024-06-26 14:57:54.512961','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 15:27:39.428363',3),(10,'Red','2024-06-26 14:57:58.212407','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 15:27:42.556938',4),(11,'Red','2024-06-26 14:57:58.212407','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 15:27:42.556938',4),(12,'Blue','2024-06-26 14:57:58.212407','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 15:27:42.556938',4),(13,'Red','2024-06-26 14:58:02.197973','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 15:27:45.503608',5),(14,'Red','2024-06-26 14:58:02.197973','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 15:27:45.503608',5),(15,'Blue','2024-06-26 14:58:02.197973','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 15:27:45.503608',5),(16,'Red','2024-06-26 14:58:05.946080','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 17:06:46.162401',6),(17,'Red','2024-06-26 14:58:05.946612','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 17:06:46.162401',6),(18,'Blue','2024-06-26 14:58:05.946612','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 17:06:46.162401',6),(19,'Red','2024-06-26 14:58:09.651380','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 15:37:01.172189',7),(20,'Red','2024-06-26 14:58:09.651380','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 15:37:01.172189',7),(21,'Blue','2024-06-26 14:58:09.651380','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 15:37:01.172189',7),(22,'Red','2024-06-26 14:59:41.629135','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 17:07:02.396658',8),(23,'Red','2024-06-26 14:59:41.629135','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 17:07:02.396658',8),(24,'Blue','2024-06-26 14:59:41.629135','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 17:07:02.396658',8),(25,'Red','2024-06-26 15:00:09.950270','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 17:06:56.194505',9),(26,'Red','2024-06-26 15:00:09.950270','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 17:06:56.194505',9),(27,'Blue','2024-06-26 15:00:09.950270','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 17:06:56.194505',9),(28,'Red','2024-06-26 15:00:16.350679','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 15:37:09.935914',10),(29,'Red','2024-06-26 15:00:16.350679','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 15:37:09.935914',10),(30,'Blue','2024-06-26 15:00:16.350679','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 15:37:09.935914',10),(31,'Red','2024-06-26 15:00:19.945933','https://example.com/red_m.jpg',99000,10,'M','2024-06-26 15:00:19.945933',11),(32,'Red','2024-06-26 15:00:19.945933','https://example.com/red_l.jpg',99000,15,'L','2024-06-26 15:00:19.945933',11),(33,'Blue','2024-06-26 15:00:19.945933','https://example.com/blue_m.jpg',99000,8,'M','2024-06-26 15:00:19.945933',11),(34,'Red','2024-06-26 15:00:25.062377','https://example.com/red_m.jpg',99000,10,'M','2024-06-26 15:00:25.062377',12),(35,'Red','2024-06-26 15:00:25.062377','https://example.com/red_l.jpg',99000,15,'L','2024-06-26 15:00:25.062377',12),(36,'Blue','2024-06-26 15:00:25.062377','https://example.com/blue_m.jpg',99000,8,'M','2024-06-26 15:00:25.062377',12),(37,'Red','2024-06-27 11:21:40.474379','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:40.474379',13),(38,'Red','2024-06-27 11:21:40.474379','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:40.474379',13),(39,'Blue','2024-06-27 11:21:40.474379','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:40.474379',13),(40,'Red','2024-06-27 11:21:41.788409','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:41.788409',14),(41,'Red','2024-06-27 11:21:41.788409','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:41.788409',14),(42,'Blue','2024-06-27 11:21:41.788409','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:41.788409',14),(43,'Red','2024-06-27 11:21:42.738304','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:42.738304',15),(44,'Red','2024-06-27 11:21:42.738304','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:42.738304',15),(45,'Blue','2024-06-27 11:21:42.738304','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:42.738304',15),(46,'Red','2024-06-27 11:21:43.581313','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:43.581313',16),(47,'Red','2024-06-27 11:21:43.581313','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:43.581313',16),(48,'Blue','2024-06-27 11:21:43.581313','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:43.581313',16),(49,'Red','2024-06-27 11:21:44.434943','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:44.434943',17),(50,'Red','2024-06-27 11:21:44.434943','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:44.434943',17),(51,'Blue','2024-06-27 11:21:44.434943','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:44.434943',17),(52,'Red','2024-06-27 11:21:45.218907','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:45.218907',18),(53,'Red','2024-06-27 11:21:45.218907','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:45.218907',18),(54,'Blue','2024-06-27 11:21:45.218907','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:45.218907',18),(55,'Red','2024-06-27 11:21:45.986195','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 17:07:12.204116',19),(56,'Red','2024-06-27 11:21:45.986195','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 17:07:12.204116',19),(57,'Blue','2024-06-27 11:21:45.986195','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 17:07:12.204116',19),(58,'Red','2024-06-27 11:21:46.841007','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:46.841007',20),(59,'Red','2024-06-27 11:21:46.841007','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:46.841007',20),(60,'Blue','2024-06-27 11:21:46.841007','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:46.841007',20),(61,'Red','2024-06-27 11:21:47.716312','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:47.716312',21),(62,'Red','2024-06-27 11:21:47.716312','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:47.716312',21),(63,'Blue','2024-06-27 11:21:47.716312','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:47.716312',21),(64,'Red','2024-06-27 11:21:48.552268','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 23:01:24.918434',22),(65,'Red','2024-06-27 11:21:48.552268','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 23:01:24.918434',22),(66,'Blue','2024-06-27 11:21:48.552268','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 23:01:24.918434',22),(67,'Red','2024-06-27 11:21:49.407393','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:49.407393',23),(68,'Red','2024-06-27 11:21:49.407393','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:49.407393',23),(69,'Blue','2024-06-27 11:21:49.407393','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:49.407393',23),(70,'Red','2024-06-27 11:21:50.293040','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:50.293040',24),(71,'Red','2024-06-27 11:21:50.293040','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:50.293040',24),(72,'Blue','2024-06-27 11:21:50.293040','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:50.293040',24),(73,'Red','2024-06-27 11:21:51.105783','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:51.105783',25),(74,'Red','2024-06-27 11:21:51.105783','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:51.105783',25),(75,'Blue','2024-06-27 11:21:51.105783','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:51.105783',25),(76,'Red','2024-06-27 11:21:51.944717','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:51.944717',26),(77,'Red','2024-06-27 11:21:51.944717','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:51.944717',26),(78,'Blue','2024-06-27 11:21:51.944717','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:51.944717',26),(79,'Red','2024-06-27 11:21:52.754449','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:52.754449',27),(80,'Red','2024-06-27 11:21:52.754449','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:52.754449',27),(81,'Blue','2024-06-27 11:21:52.754449','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:52.754449',27),(82,'Red','2024-06-27 11:21:53.618278','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:53.618278',28),(83,'Red','2024-06-27 11:21:53.618278','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:53.618278',28),(84,'Blue','2024-06-27 11:21:53.618278','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:53.618278',28),(85,'Red','2024-06-27 11:21:54.487876','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:54.487876',29),(86,'Red','2024-06-27 11:21:54.487876','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:54.487876',29),(87,'Blue','2024-06-27 11:21:54.487876','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:54.487876',29),(88,'Red','2024-06-27 11:21:55.354314','https://example.com/red_m.jpg',99000,10,'M','2024-06-27 11:21:55.354314',30),(89,'Red','2024-06-27 11:21:55.354314','https://example.com/red_l.jpg',99000,15,'L','2024-06-27 11:21:55.354314',30),(90,'Blue','2024-06-27 11:21:55.354314','https://example.com/blue_m.jpg',99000,8,'M','2024-06-27 11:21:55.354314',30),(91,'Red','2024-06-28 14:16:39.510991','https://example.com/red_m.jpg',99000,10,'M','2024-06-28 14:16:39.510991',31),(92,'Red','2024-06-28 14:16:39.510991','https://example.com/red_l.jpg',99000,15,'L','2024-06-28 14:16:39.510991',31),(93,'Blue','2024-06-28 14:16:39.510991','https://example.com/blue_m.jpg',99000,8,'M','2024-06-28 14:16:39.510991',31),(94,'Red','2024-07-02 10:32:08.715272','https://example.com/red_m.jpg',99000,10,'M','2024-07-02 10:32:08.715272',32),(95,'Red','2024-07-02 10:32:08.715272','https://example.com/red_l.jpg',99000,15,'L','2024-07-02 10:32:08.715272',32),(96,'Blue','2024-07-02 10:32:08.715272','https://example.com/blue_m.jpg',99000,8,'M','2024-07-02 10:32:08.715272',32),(97,'Red','2024-07-02 15:04:48.088325','https://example.com/red_m.jpg',99000,10,'M','2024-07-02 15:04:48.088325',33),(98,'Red','2024-07-02 15:04:48.088325','https://example.com/red_l.jpg',99000,15,'L','2024-07-02 15:04:48.088325',33),(99,'Blue','2024-07-02 15:04:48.088325','https://example.com/blue_m.jpg',99000,8,'M','2024-07-02 15:04:48.088325',33),(100,'Red','2024-07-02 15:10:44.333054','https://example.com/red_m.jpg',99000,10,'M','2024-07-02 15:10:44.333054',34),(101,'Red','2024-07-02 15:10:44.333054','https://example.com/red_l.jpg',99000,15,'L','2024-07-02 15:10:44.333054',34),(102,'Blue','2024-07-02 15:10:44.333054','https://example.com/blue_m.jpg',99000,8,'M','2024-07-02 15:10:44.333054',34),(103,'Red','2024-07-02 15:26:10.967783','https://example.com/red_m.jpg',99000,10,'M','2024-07-02 15:26:10.967783',35),(104,'Red','2024-07-02 15:26:10.967783','https://example.com/red_l.jpg',99000,15,'L','2024-07-02 15:26:10.967783',35),(105,'Blue','2024-07-02 15:26:10.967783','https://example.com/blue_m.jpg',99000,8,'M','2024-07-02 15:26:10.967783',35);
/*!40000 ALTER TABLE `product_variant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) DEFAULT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `rating` int NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiyof1sindb9qiqr9o8npj8klt` (`product_id`),
  CONSTRAINT `FKiyof1sindb9qiqr9o8npj8klt` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `roles` enum('ADMIN','USER') DEFAULT NULL,
  KEY `FKhfh9dx7w3ubf1co1vdev94g3f` (`user_id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,'ADMIN');
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin20@gmail.com','Nguyễn','ABC','$2a$10$gApdJHoFKocPnYXNKoQ0tO9wFKKKD2lwRZYLxaLVuUcNmvX7rTA.a',NULL,'admin20');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-04 21:07:29
