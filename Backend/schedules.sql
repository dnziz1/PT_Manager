-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jan 15, 2024 at 06:38 PM
-- Server version: 5.7.41-cll-lve
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `naazshop_appDbfinal`
--

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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

--
-- Indexes for dumped tables
--

--
-- Indexes for table `schedules`
--
ALTER TABLE `schedules`
  ADD PRIMARY KEY (`sId`),
  ADD UNIQUE KEY `id_UNIQUE` (`sId`),
  ADD KEY `tId_idx` (`trainerId`),
  ADD KEY `cId_idx` (`clientId`);

--
-- Constraints for dumped tables
--

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
