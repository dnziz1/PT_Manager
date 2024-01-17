-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 17, 2024 at 03:22 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `appDB`
--

-- --------------------------------------------------------

--
-- Table structure for table `clients`
--

CREATE TABLE `clients` (
  `cId` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(45) NOT NULL,
  `firstName` varchar(20) NOT NULL,
  `lastName` varchar(20) NOT NULL,
  `displayName` varchar(20) DEFAULT NULL,
  `DoB` datetime NOT NULL,
  `phoneNumber` int(11) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `pictureId` int(11) DEFAULT NULL,
  `paymentDetails` varchar(45) DEFAULT NULL,
  `messageInbox` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`messageInbox`))
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `clients`
--

INSERT INTO `clients` (`cId`, `username`, `password`, `firstName`, `lastName`, `displayName`, `DoB`, `phoneNumber`, `address`, `pictureId`, `paymentDetails`, `messageInbox`) VALUES
(767, 'ali', 'ali', 'ali', 'ali', 'ali', '2024-01-15 09:36:47', 8876786, 'dgk', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

CREATE TABLE `message` (
  `mId` varchar(45) NOT NULL,
  `senderId` int(11) NOT NULL,
  `receiverId` int(11) NOT NULL,
  `message` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `message`
--

INSERT INTO `message` (`mId`, `senderId`, `receiverId`, `message`) VALUES
('m001', 116, 767, 'hello world');

-- --------------------------------------------------------

--
-- Table structure for table `schedules`
--

CREATE TABLE `schedules` (
  `sId` int(11) NOT NULL,
  `trainerId` int(11) NOT NULL,
  `clientId` int(11) DEFAULT NULL,
  `eventDate` varchar(20) NOT NULL,
  `startTime` varchar(20) NOT NULL,
  `endTime` varchar(20) NOT NULL,
  `title` varchar(45) NOT NULL,
  `details` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `schedules`
--

INSERT INTO `schedules` (`sId`, `trainerId`, `clientId`, `eventDate`, `startTime`, `endTime`, `title`, `details`) VALUES
(0, 116, 767, '2024-01-15 00:00:00', '2009-00-00 00:00:00', '2011-00-00 00:00:00', 'Training Session', 'Details of the training session'),
(1, 116, 767, '2024-01-15 00:00:00', '2009-00-00 00:00:00', '2011-00-00 00:00:00', 'Training Session 1', 'Details of the training session'),
(2, 116, 767, '2024-01-15 00:00:00', '2009-00-00 00:00:00', '2011-00-00 00:00:00', 'Training Session 1', 'Details of the training session'),
(3, 116, 767, '2024-01-20 00:00:00', '2011-00-00 00:00:00', '0001-00-00 00:00:00', 'Training Session', 'Details of the training session'),
(4, 116, 767, '2024-01-20', '12:00:00', '1:00:00', 'Training Session', 'Details of the training session'),
(6, 116, 767, '2024-01-16', '19:27:00', '20:27:00', 'Training Session 1', 'Details of the training session'),
(111, 116, 767, '2024-01-15 09:39:40', '2024-01-15 09:39:40', '2024-01-15 09:39:40', 'helo world', 'helooooooooo');

-- --------------------------------------------------------

--
-- Table structure for table `trainers`
--

CREATE TABLE `trainers` (
  `tId` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(45) NOT NULL,
  `firstName` varchar(20) NOT NULL,
  `lastName` varchar(20) NOT NULL,
  `displayName` varchar(20) NOT NULL,
  `DoB` datetime NOT NULL,
  `phoneNumber` int(11) NOT NULL,
  `address` varchar(45) DEFAULT NULL,
  `webpage` varchar(45) DEFAULT NULL,
  `pictureId` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `certifications` varchar(100) DEFAULT NULL,
  `specialities` varchar(100) DEFAULT NULL,
  `availability` datetime DEFAULT NULL,
  `messageInbox` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`messageInbox`))
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `trainers`
--

INSERT INTO `trainers` (`tId`, `username`, `password`, `firstName`, `lastName`, `displayName`, `DoB`, `phoneNumber`, `address`, `webpage`, `pictureId`, `description`, `certifications`, `specialities`, `availability`, `messageInbox`) VALUES
(116, 'anser', 'anser', 'anser', 'ali', 'anser', '2024-01-15 09:25:36', 247085009, 'dgk', NULL, NULL, 'trainer is here', NULL, NULL, NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `clients`
--
ALTER TABLE `clients`
  ADD PRIMARY KEY (`cId`),
  ADD UNIQUE KEY `id_UNIQUE` (`cId`),
  ADD UNIQUE KEY `username_UNIQUE` (`username`),
  ADD UNIQUE KEY `phoneNumber_UNIQUE` (`phoneNumber`),
  ADD UNIQUE KEY `pictureId_UNIQUE` (`pictureId`);

--
-- Indexes for table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`mId`),
  ADD KEY `id_idx1` (`senderId`,`receiverId`),
  ADD KEY `receiver_fk` (`receiverId`);

--
-- Indexes for table `schedules`
--
ALTER TABLE `schedules`
  ADD PRIMARY KEY (`sId`),
  ADD UNIQUE KEY `id_UNIQUE` (`sId`),
  ADD KEY `tId_idx` (`trainerId`),
  ADD KEY `cId_idx` (`clientId`);

--
-- Indexes for table `trainers`
--
ALTER TABLE `trainers`
  ADD PRIMARY KEY (`tId`),
  ADD UNIQUE KEY `id_UNIQUE` (`tId`),
  ADD UNIQUE KEY `phoneNumber_UNIQUE` (`phoneNumber`),
  ADD UNIQUE KEY `pictureId_UNIQUE` (`pictureId`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `receiver_fk` FOREIGN KEY (`receiverId`) REFERENCES `clients` (`cId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sender_fk` FOREIGN KEY (`senderId`) REFERENCES `trainers` (`tId`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `schedules`
--
ALTER TABLE `schedules`
  ADD CONSTRAINT `cId` FOREIGN KEY (`clientId`) REFERENCES `clients` (`cId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tId` FOREIGN KEY (`trainerId`) REFERENCES `trainers` (`tId`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
