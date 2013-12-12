/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `apsi_lab2`
--
CREATE DATABASE IF NOT EXISTS `apsi_lab2` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `apsi_lab2`;

-- --------------------------------------------------------
--
-- Tabellenstruktur für Tabelle `companies`
--
DROP TABLE IF EXISTS `companies`;
CREATE TABLE IF NOT EXISTS `companies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `name` varchar(20) NOT NULL,
  `address` varchar(100) NOT NULL,
  `postcode` varchar(4) NOT NULL,
  `town` varchar(100) NOT NULL,
  `hashcode` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- --------------------------------------------------------
--
-- Benutzer erstellen und Rechte geben
--
GRANT USAGE ON *.* TO 'apsi'@'localhost' IDENTIFIED BY PASSWORD '*BBD0AF153A74AD4046FEA574B94545E98FA01C99';
GRANT SELECT, INSERT, UPDATE, DELETE ON `apsi\_lab2`.* TO 'apsi'@'localhost';