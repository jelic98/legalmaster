-- phpMyAdmin SQL Dump
-- version 4.4.10
-- http://www.phpmyadmin.net
--
-- Host: localhost:8889
-- Generation Time: Jul 23, 2016 at 03:00 AM
-- Server version: 5.5.42-log
-- PHP Version: 7.0.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `legal`
--

-- --------------------------------------------------------

--
-- Table structure for table `korisnik`
--

CREATE TABLE `korisnik` (
  `ime` varchar(50) NOT NULL,
    `kod` varchar(10) NOT NULL,
	  `aktivan` varchar(1) NOT NULL
	  ) ENGINE=InnoDB DEFAULT CHARSET=latin1;

