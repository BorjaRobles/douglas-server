-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Apr 09, 2017 at 07:00 AM
-- Server version: 5.6.33
-- PHP Version: 7.0.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `douglas`
--

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `section`
--

CREATE TABLE `section` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `test`
--

CREATE TABLE `test` (
  `id` bigint(20) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  `section_id` bigint(20) DEFAULT NULL,
  `test_status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `test_result`
--

CREATE TABLE `test_result` (
  `id` bigint(20) NOT NULL,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  `steps` longtext,
  `test_id` bigint(20) DEFAULT NULL,
  `test_result_status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `test_step`
--

CREATE TABLE `test_step` (
  `id` bigint(20) NOT NULL,
  `action` varchar(255) DEFAULT NULL,
  `metaContent` varchar(255) DEFAULT NULL,
  `metaLocationX` bigint(20) DEFAULT NULL,
  `metaLocationY` bigint(20) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `suggestion` longtext,
  `test_id` bigint(20) DEFAULT NULL,
  `test_step_status` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Indexes for dumped tables
--

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `section`
--
ALTER TABLE `section`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK7q6t8flbua6luc093ttflx7u3` (`product_id`);

--
-- Indexes for table `test`
--
ALTER TABLE `test`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKlr83btx6fy4tst6uwlix2eguj` (`section_id`);

--
-- Indexes for table `test_result`
--
ALTER TABLE `test_result`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKef3e8k7fgvkj4mox0lxrkf8hh` (`test_id`);

--
-- Indexes for table `test_step`
--
ALTER TABLE `test_step`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK1eu3xk9ulu1p77fagir0oums6` (`test_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `section`
--
ALTER TABLE `section`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `test`
--
ALTER TABLE `test`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `test_result`
--
ALTER TABLE `test_result`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `test_step`
--
ALTER TABLE `test_step`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `section`
--
ALTER TABLE `section`
  ADD CONSTRAINT `FK7q6t8flbua6luc093ttflx7u3` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`);

--
-- Constraints for table `test`
--
ALTER TABLE `test`
  ADD CONSTRAINT `FKlr83btx6fy4tst6uwlix2eguj` FOREIGN KEY (`section_id`) REFERENCES `section` (`id`);

--
-- Constraints for table `test_result`
--
ALTER TABLE `test_result`
  ADD CONSTRAINT `FKef3e8k7fgvkj4mox0lxrkf8hh` FOREIGN KEY (`test_id`) REFERENCES `test` (`id`);

--
-- Constraints for table `test_step`
--
ALTER TABLE `test_step`
  ADD CONSTRAINT `FK1eu3xk9ulu1p77fagir0oums6` FOREIGN KEY (`test_id`) REFERENCES `test` (`id`);
