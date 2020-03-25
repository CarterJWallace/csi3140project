-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 25, 2020 at 01:03 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `csi3140project`
--

-- --------------------------------------------------------

--
-- Table structure for table `factions`
--

CREATE TABLE `factions` (
  `factionID` int(11) NOT NULL,
  `factionName` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `factions`
--

INSERT INTO `factions` (`factionID`, `factionName`) VALUES
(1, 'Amarr'),
(2, 'Caldari'),
(3, 'Gallente'),
(4, 'Minmater'),
(5, 'ORE');

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `typeID` int(11) NOT NULL,
  `factionID` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `Tritanium` int(11) NOT NULL,
  `Pyerite` int(11) NOT NULL,
  `Mexallon` int(11) NOT NULL,
  `Isogen` int(11) NOT NULL,
  `Nocxium` int(11) NOT NULL,
  `Zydrine` int(11) NOT NULL,
  `Megacyte` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`typeID`, `factionID`, `name`, `Tritanium`, `Pyerite`, `Mexallon`, `Isogen`, `Nocxium`, `Zydrine`, `Megacyte`) VALUES
(582, 2, 'Bantam', 22222, 8000, 2444, 500, 2, 4, 0),
(583, 2, 'Condor', 20000, 4444, 2111, 556, 11, 2, 0),
(584, 2, 'Griffin', 13333, 12778, 1667, 222, 56, 22, 6),
(585, 4, 'Slasher', 21111, 5000, 1667, 111, 100, 0, 6),
(586, 4, 'Probe', 15556, 10222, 2778, 111, 78, 34, 6),
(587, 4, 'Rifter', 22778, 6222, 2222, 356, 133, 34, 2),
(589, 1, 'Executioner', 22222, 4444, 1667, 333, 67, 12, 0),
(590, 1, 'Inquisitor', 16667, 12222, 2778, 11, 6, 2, 2),
(591, 1, 'Tormentor', 22222, 2444, 4444, 222, 44, 22, 0),
(592, 3, 'Navitas', 18889, 4667, 3556, 278, 33, 12, 0),
(593, 3, 'Tristan', 23333, 6333, 3000, 333, 78, 22, 2),
(594, 3, 'Incursus', 13333, 11111, 4000, 33, 17, 12, 0),
(597, 1, 'Punisher', 22778, 6111, 2889, 400, 89, 34, 0),
(598, 4, 'Breacher', 17778, 15000, 2778, 111, 2, 4, 2),
(599, 4, 'Burst', 17778, 3333, 4444, 167, 22, 6, 2),
(602, 2, 'Kestrel', 17778, 5556, 2889, 1111, 56, 22, 0),
(603, 2, 'Merlin', 21111, 8889, 3111, 589, 2, 4, 4),
(605, 2, 'Heron', 13333, 11111, 2444, 556, 56, 16, 8),
(607, 3, 'Imicus', 16667, 10556, 2556, 278, 50, 44, 4),
(608, 3, 'Atron', 20556, 3889, 2222, 278, 56, 12, 0),
(609, 3, 'Maulus', 15556, 13333, 2222, 56, 2, 4, 0),
(2161, 1, 'Crucifier', 13333, 12222, 1333, 278, 83, 22, 12),
(3766, 4, 'Vigil', 14444, 5111, 2778, 278, 83, 22, 8),
(29248, 1, 'Magnate', 13889, 6111, 4000, 167, 67, 14, 4),
(32880, 5, 'Venture', 24889, 7444, 744, 444, 50, 22, 0);

-- --------------------------------------------------------

--
-- Table structure for table `ores`
--

CREATE TABLE `ores` (
  `oreID` int(11) NOT NULL,
  `oreName` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `ores`
--

INSERT INTO `ores` (`oreID`, `oreName`) VALUES
(34, 'Tritanium'),
(35, 'Pyerite'),
(36, 'Mexallon'),
(37, 'Isogen'),
(38, 'Nocxium'),
(39, 'Zydrine'),
(40, 'Megacyte');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `factions`
--
ALTER TABLE `factions`
  ADD PRIMARY KEY (`factionID`);

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`typeID`);

--
-- Indexes for table `ores`
--
ALTER TABLE `ores`
  ADD PRIMARY KEY (`oreID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
