--
-- Table structure for table `classes`
--

DROP TABLE IF EXISTS `classes`;
CREATE TABLE `classes` (
  `classID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `duration` int NOT NULL,
  `maxOccupancy` int NOT NULL,
  `notes` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`classID`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `classes`
--

LOCK TABLES `classes` WRITE;
INSERT INTO `classes` VALUES (1,'Aerobics',60,5,'Aerobics for beginners'),(2,'Spin (B)',30,10,'Exercise Bike workout for beginners '),(3,'Spin (A)',60,10,'Exercise Bike workout for advanced'),(4,'Yoga',60,20,'Yoga exervise for all levels'),(5,'Zumba',60,15,'Dance workouts for all levels');
UNLOCK TABLES;

--
-- Table structure for table `class_schedules`
--

DROP TABLE IF EXISTS `class_schedules`;
CREATE TABLE `class_schedules` (
  `scheduleID` int NOT NULL AUTO_INCREMENT,
  `classID` int DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `trainerID` int DEFAULT NULL,
  PRIMARY KEY (`scheduleID`),
  UNIQUE KEY `idx_unique_class` (`classID`,`startDate`,`endDate`,`trainerID`),
  KEY `trainerID` (`trainerID`),
  CONSTRAINT `classID` FOREIGN KEY (`classID`) REFERENCES `classes` (`classID`),
  CONSTRAINT `trainerID` FOREIGN KEY (`trainerID`) REFERENCES `trainers` (`trainerID`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `class_schedules`
--

LOCK TABLES `class_schedules` WRITE;
UNLOCK TABLES;

--
-- Table structure for table `class_timeslots`
--

DROP TABLE IF EXISTS `class_timeslots`;
CREATE TABLE `class_timeslots` (
  `timeslotID` int NOT NULL AUTO_INCREMENT,
  `scheduleID` int NOT NULL,
  `startTime` datetime NOT NULL,
  PRIMARY KEY (`timeslotID`),
  UNIQUE KEY `idx_unique_timeslot` (`scheduleID`,`startTime`),
  CONSTRAINT `scheduleID` FOREIGN KEY (`scheduleID`) REFERENCES `class_schedules` (`scheduleID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `class_timeslots`
--

LOCK TABLES `class_timeslots` WRITE;
UNLOCK TABLES;

--
-- Table structure for table `class_bookings`
--

DROP TABLE IF EXISTS `class_bookings`;
CREATE TABLE `class_bookings` (
  `classBookingID` int NOT NULL AUTO_INCREMENT,
  `timeslotID` int NOT NULL,
  `clientID` int NOT NULL,
  PRIMARY KEY (`classBookingID`),
  UNIQUE KEY `idx_unique_booking` (`timeslotID`,`clientID`),
  KEY `clientID` (`clientID`),
  CONSTRAINT `clientID` FOREIGN KEY (`clientID`) REFERENCES `clients` (`clientID`),
  CONSTRAINT `timeslotID` FOREIGN KEY (`timeslotID`) REFERENCES `class_timeslots` (`timeslotID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `class_bookings`
--

LOCK TABLES `class_bookings` WRITE;
UNLOCK TABLES;

