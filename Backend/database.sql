-- phpMyAdmin SQL Dump
-- version 3.4.10.1
-- http://www.phpmyadmin.net
--
-- Machine: localhost
-- Genereertijd: 30 jan 2014 om 13:27
-- Serverversie: 5.5.21
-- PHP-Versie: 5.3.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `dibbit_wvt`
--

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `boodschappenlijst`
--

CREATE TABLE IF NOT EXISTS `boodschappenlijst` (
  `boodschappenlijst_id` int(11) NOT NULL AUTO_INCREMENT,
  `gebruiker_id` int(11) NOT NULL,
  `naam` varchar(50) NOT NULL,
  PRIMARY KEY (`boodschappenlijst_id`,`gebruiker_id`),
  UNIQUE KEY `naam` (`naam`),
  KEY `gebruiker_id` (`gebruiker_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=83 ;

--
-- Gegevens worden uitgevoerd voor tabel `boodschappenlijst`
--

INSERT INTO `boodschappenlijst` (`boodschappenlijst_id`, `gebruiker_id`, `naam`) VALUES
(82, 11, 'Dagelijks'),
(62, 6, 'Kerst'),
(71, 10, 'Sjakie'),
(50, 6, 'test'),
(51, 5, 'testje'),
(48, 1, 'tsdes'),
(80, 6, 'weekend');

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `boodschappenlijstregel`
--

CREATE TABLE IF NOT EXISTS `boodschappenlijstregel` (
  `regel_id` int(11) NOT NULL AUTO_INCREMENT,
  `boodschappenlijst_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `aantal` int(11) NOT NULL,
  PRIMARY KEY (`regel_id`),
  UNIQUE KEY `boodschappenlijst_id_2` (`boodschappenlijst_id`,`product_id`),
  KEY `boodschappenlijst_id` (`boodschappenlijst_id`),
  KEY `product_id` (`product_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=87 ;

--
-- Gegevens worden uitgevoerd voor tabel `boodschappenlijstregel`
--

INSERT INTO `boodschappenlijstregel` (`regel_id`, `boodschappenlijst_id`, `product_id`, `aantal`) VALUES
(17, 48, 1, 12),
(18, 48, 2, 45),
(32, 62, 1, 5),
(33, 62, 6, 300),
(34, 62, 7, 600),
(79, 50, 1, 1),
(80, 50, 2, 2),
(81, 50, 3, 1),
(82, 62, 5, 3);

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `categorieen`
--

CREATE TABLE IF NOT EXISTS `categorieen` (
  `categorie_id` int(11) NOT NULL AUTO_INCREMENT,
  `naam` varchar(50) NOT NULL,
  PRIMARY KEY (`categorie_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Gegevens worden uitgevoerd voor tabel `categorieen`
--

INSERT INTO `categorieen` (`categorie_id`, `naam`) VALUES
(1, 'groente'),
(2, 'vlees'),
(3, 'zuivel'),
(4, 'frisdrank'),
(5, 'fruit'),
(6, 'drank'),
(7, 'vleeswaren');

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `gebruikers`
--

CREATE TABLE IF NOT EXISTS `gebruikers` (
  `gebruiker_id` int(11) NOT NULL AUTO_INCREMENT,
  `naam` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `wachtwoord` varchar(20) NOT NULL,
  PRIMARY KEY (`gebruiker_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Gegevens worden uitgevoerd voor tabel `gebruikers`
--

INSERT INTO `gebruikers` (`gebruiker_id`, `naam`, `email`, `wachtwoord`) VALUES
(1, 'jens', 'jensboer@gmail.com', 'test'),
(5, 'Test', 'test@test.nl', 'test'),
(6, 'yeah', 'test@test.com', 'test'),
(10, 'Sjakie', 'sjakie@gmail.com', 'test'),
(11, 'Tester', 'test2@test.nl', 'test');

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `productcategorie`
--

CREATE TABLE IF NOT EXISTS `productcategorie` (
  `product_id` int(11) NOT NULL,
  `categorie_id` int(11) NOT NULL,
  PRIMARY KEY (`product_id`,`categorie_id`),
  KEY `categorie_id` (`categorie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Gegevens worden uitgevoerd voor tabel `productcategorie`
--

INSERT INTO `productcategorie` (`product_id`, `categorie_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(6, 5),
(3, 6),
(5, 6),
(7, 7);

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `producten`
--

CREATE TABLE IF NOT EXISTS `producten` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `naam` varchar(50) NOT NULL,
  `gewichtingram` decimal(8,2) NOT NULL,
  `prijs` decimal(6,2) NOT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Gegevens worden uitgevoerd voor tabel `producten`
--

INSERT INTO `producten` (`product_id`, `naam`, `gewichtingram`, `prijs`) VALUES
(1, 'bloemkool', 200.00, 1.00),
(2, 'slavink', 400.00, 3.00),
(3, 'melk', 1000.00, 1.50),
(4, 'cola', 1000.00, 1.00),
(5, 'bier', 550.00, 5.50),
(6, 'appels', 65.00, 0.50),
(7, 'salami', 300.00, 1.25);

--
-- Beperkingen voor gedumpte tabellen
--

--
-- Beperkingen voor tabel `boodschappenlijst`
--
ALTER TABLE `boodschappenlijst`
  ADD CONSTRAINT `boodschappenlijst_ibfk_1` FOREIGN KEY (`gebruiker_id`) REFERENCES `gebruikers` (`gebruiker_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Beperkingen voor tabel `boodschappenlijstregel`
--
ALTER TABLE `boodschappenlijstregel`
  ADD CONSTRAINT `boodschappenlijstregel_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `producten` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `boodschappenlijstregel_ibfk_3` FOREIGN KEY (`boodschappenlijst_id`) REFERENCES `boodschappenlijst` (`boodschappenlijst_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Beperkingen voor tabel `productcategorie`
--
ALTER TABLE `productcategorie`
  ADD CONSTRAINT `productcategorie_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `producten` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `productcategorie_ibfk_2` FOREIGN KEY (`categorie_id`) REFERENCES `categorieen` (`categorie_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
