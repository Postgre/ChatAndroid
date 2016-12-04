-- --------------------------------------------------------
-- Host:                         52.53.151.16
-- Versión del servidor:         5.7.15-log - MySQL Community Server (GPL)
-- SO del servidor:              Win64
-- HeidiSQL Versión:             9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Volcando estructura de base de datos para colonosswiii
CREATE DATABASE IF NOT EXISTS `colonosswiii` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `colonosswiii`;


-- Volcando estructura para tabla colonosswiii.academic_period
CREATE TABLE IF NOT EXISTS `academic_period` (
  `code_academic_period` int(11) NOT NULL AUTO_INCREMENT,
  `date_start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date_finish` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `number_academic_period` int(11) NOT NULL,
  PRIMARY KEY (`code_academic_period`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla colonosswiii.academic_period: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `academic_period` DISABLE KEYS */;
INSERT INTO `academic_period` (`code_academic_period`, `date_start`, `date_finish`, `number_academic_period`) VALUES
	(1, '2016-09-11 14:23:27', '2016-11-11 00:00:00', 2);
/*!40000 ALTER TABLE `academic_period` ENABLE KEYS */;


-- Volcando estructura para tabla colonosswiii.course
CREATE TABLE IF NOT EXISTS `course` (
  `code_course` int(11) NOT NULL AUTO_INCREMENT,
  `number_course` int(11) NOT NULL,
  `name_course` varchar(50) NOT NULL,
  `date_course` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `state_course` tinyint(1) NOT NULL,
  `id_teacher` int(11) DEFAULT NULL,
  `code_subject` int(11) NOT NULL,
  `code_curriculum` int(11) NOT NULL,
  `code_academic_period` int(11) NOT NULL,
  PRIMARY KEY (`code_course`),
  KEY `id_teacher` (`id_teacher`),
  KEY `code_subject` (`code_subject`),
  KEY `code_curriculum` (`code_curriculum`),
  KEY `code_academic_period` (`code_academic_period`),
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`id_teacher`) REFERENCES `person` (`id`),
  CONSTRAINT `course_ibfk_2` FOREIGN KEY (`code_subject`) REFERENCES `detail_curriculum` (`code_subject`),
  CONSTRAINT `course_ibfk_3` FOREIGN KEY (`code_curriculum`) REFERENCES `detail_curriculum` (`code_curriculum`),
  CONSTRAINT `course_ibfk_4` FOREIGN KEY (`code_academic_period`) REFERENCES `academic_period` (`code_academic_period`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla colonosswiii.course: ~4 rows (aproximadamente)
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` (`code_course`, `number_course`, `name_course`, `date_course`, `state_course`, `id_teacher`, `code_subject`, `code_curriculum`, `code_academic_period`) VALUES
	(1, 1, 'Software 2', '2016-09-11 14:24:44', 1, NULL, 1, 1, 1),
	(2, 1, 'D.S.I.F', '2016-09-11 14:26:12', 1, NULL, 2, 2, 1),
	(3, 1, 'Mate', '2016-09-11 14:26:43', 1, NULL, 3, 1, 1),
	(4, 1, 'Mate', '2016-09-11 14:27:00', 1, NULL, 3, 3, 1);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;


-- Volcando estructura para tabla colonosswiii.curriculum
CREATE TABLE IF NOT EXISTS `curriculum` (
  `code_curriculum` int(11) NOT NULL AUTO_INCREMENT,
  `name_curriculum` varchar(50) NOT NULL,
  `state` tinyint(1) NOT NULL,
  `code_program` int(11) NOT NULL,
  PRIMARY KEY (`code_curriculum`),
  KEY `code_program` (`code_program`),
  CONSTRAINT `curriculum_ibfk_1` FOREIGN KEY (`code_program`) REFERENCES `program` (`code_program`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla colonosswiii.curriculum: ~3 rows (aproximadamente)
/*!40000 ALTER TABLE `curriculum` DISABLE KEYS */;
INSERT INTO `curriculum` (`code_curriculum`, `name_curriculum`, `state`, `code_program`) VALUES
	(1, 'Pensum 2007 SIS', 1, 1),
	(2, 'Pensum 2014 SIS', 1, 1),
	(3, 'Pensum 2007 LIM', 1, 2);
/*!40000 ALTER TABLE `curriculum` ENABLE KEYS */;


-- Volcando estructura para tabla colonosswiii.detail_curriculum
CREATE TABLE IF NOT EXISTS `detail_curriculum` (
  `code_subject` int(11) NOT NULL DEFAULT '0',
  `code_curriculum` int(11) NOT NULL DEFAULT '0',
  `state_subject_curriculum` tinyint(1) NOT NULL,
  `semester` int(11) NOT NULL,
  PRIMARY KEY (`code_subject`,`code_curriculum`),
  KEY `code_curriculum` (`code_curriculum`),
  CONSTRAINT `detail_curriculum_ibfk_1` FOREIGN KEY (`code_subject`) REFERENCES `subject` (`code_subject`),
  CONSTRAINT `detail_curriculum_ibfk_2` FOREIGN KEY (`code_curriculum`) REFERENCES `curriculum` (`code_curriculum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla colonosswiii.detail_curriculum: ~4 rows (aproximadamente)
/*!40000 ALTER TABLE `detail_curriculum` DISABLE KEYS */;
INSERT INTO `detail_curriculum` (`code_subject`, `code_curriculum`, `state_subject_curriculum`, `semester`) VALUES
	(1, 1, 1, 7),
	(2, 2, 1, 6),
	(3, 1, 1, 1),
	(3, 3, 1, 2);
/*!40000 ALTER TABLE `detail_curriculum` ENABLE KEYS */;


-- Volcando estructura para tabla colonosswiii.enrollment
CREATE TABLE IF NOT EXISTS `enrollment` (
  `id_student` int(11) NOT NULL,
  `code_course` int(11) NOT NULL,
  `state` tinyint(4) NOT NULL,
  `date_enrollment` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_student`,`code_course`),
  KEY `FK_enrollment_course` (`code_course`),
  CONSTRAINT `FK_enrollment_course` FOREIGN KEY (`code_course`) REFERENCES `course` (`code_course`),
  CONSTRAINT `FK_enrollment_person` FOREIGN KEY (`id_student`) REFERENCES `person` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla colonosswiii.enrollment: ~6 rows (aproximadamente)
/*!40000 ALTER TABLE `enrollment` DISABLE KEYS */;
INSERT INTO `enrollment` (`id_student`, `code_course`, `state`, `date_enrollment`) VALUES
	(1, 1, 1, '2016-09-11 14:27:20'),
	(1, 3, 1, '2016-09-11 14:27:54'),
	(1, 4, 1, '2016-09-12 12:08:30'),
	(2, 2, 1, '2016-09-11 14:27:43'),
	(2, 4, 1, '2016-09-11 14:28:15'),
	(4, 2, 1, '2016-09-11 14:27:32');
/*!40000 ALTER TABLE `enrollment` ENABLE KEYS */;


-- Volcando estructura para tabla colonosswiii.person
CREATE TABLE IF NOT EXISTS `person` (
  `id` int(11) NOT NULL,
  `code` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `number_phone` int(11) DEFAULT NULL,
  `location_semester` int(11) DEFAULT NULL,
  `salary` double DEFAULT NULL,
  `code_user` int(11) NOT NULL,
  `code_program` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `code_user` (`code_user`),
  KEY `FK_person_program` (`code_program`),
  CONSTRAINT `FK_person_program` FOREIGN KEY (`code_program`) REFERENCES `program` (`code_program`),
  CONSTRAINT `person_ibfk_1` FOREIGN KEY (`code_user`) REFERENCES `user` (`code_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla colonosswiii.person: ~5 rows (aproximadamente)
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` (`id`, `code`, `name`, `last_name`, `number_phone`, `location_semester`, `salary`, `code_user`, `code_program`) VALUES
	(1, 123, 'Jhon Esteban', 'Munar Gomez', 312, NULL, NULL, 1, 1),
	(2, 1231, 'Sandy Tatiana', 'Cardenas Anacona', 31313, NULL, NULL, 2, 1),
	(3, 3131, 'Jorge Luis', 'Valencia Correa', 313, NULL, NULL, 3, 2),
	(4, 311, 'Angelica', 'Paladinez Guenis', 1412, NULL, NULL, 4, 1),
	(5, 199, 'Edwin', 'Millan', 1231, NULL, 4, 5, 1);
/*!40000 ALTER TABLE `person` ENABLE KEYS */;


-- Volcando estructura para tabla colonosswiii.person_role
CREATE TABLE IF NOT EXISTS `person_role` (
  `id_person` int(11) NOT NULL DEFAULT '0',
  `code_role` int(11) NOT NULL DEFAULT '0',
  `state` tinyint(1) NOT NULL,
  PRIMARY KEY (`id_person`,`code_role`),
  KEY `code_role` (`code_role`),
  CONSTRAINT `person_role_ibfk_1` FOREIGN KEY (`id_person`) REFERENCES `person` (`id`),
  CONSTRAINT `person_role_ibfk_2` FOREIGN KEY (`code_role`) REFERENCES `role` (`code_role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla colonosswiii.person_role: ~5 rows (aproximadamente)
/*!40000 ALTER TABLE `person_role` DISABLE KEYS */;
INSERT INTO `person_role` (`id_person`, `code_role`, `state`) VALUES
	(1, 1, 1),
	(2, 1, 1),
	(3, 1, 1),
	(4, 1, 1),
	(5, 2, 1);
/*!40000 ALTER TABLE `person_role` ENABLE KEYS */;


-- Volcando estructura para tabla colonosswiii.program
CREATE TABLE IF NOT EXISTS `program` (
  `code_program` int(11) NOT NULL AUTO_INCREMENT,
  `name_program` varchar(50) NOT NULL,
  PRIMARY KEY (`code_program`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla colonosswiii.program: ~2 rows (aproximadamente)
/*!40000 ALTER TABLE `program` DISABLE KEYS */;
INSERT INTO `program` (`code_program`, `name_program`) VALUES
	(1, 'Ingenieria de sistema'),
	(2, 'Licenciatura en matematicas');
/*!40000 ALTER TABLE `program` ENABLE KEYS */;


-- Volcando estructura para tabla colonosswiii.role
CREATE TABLE IF NOT EXISTS `role` (
  `code_role` int(11) NOT NULL AUTO_INCREMENT,
  `name_role` varchar(50) NOT NULL,
  PRIMARY KEY (`code_role`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla colonosswiii.role: ~2 rows (aproximadamente)
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`code_role`, `name_role`) VALUES
	(1, 'Estudiante'),
	(2, 'Docente');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;


-- Volcando estructura para tabla colonosswiii.subject
CREATE TABLE IF NOT EXISTS `subject` (
  `code_subject` int(11) NOT NULL AUTO_INCREMENT,
  `name_subject` varchar(50) NOT NULL,
  PRIMARY KEY (`code_subject`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla colonosswiii.subject: ~3 rows (aproximadamente)
/*!40000 ALTER TABLE `subject` DISABLE KEYS */;
INSERT INTO `subject` (`code_subject`, `name_subject`) VALUES
	(1, 'Ingenieria de Software 2'),
	(2, 'Diseño de Sistema de Informacion'),
	(3, 'Matematicas I');
/*!40000 ALTER TABLE `subject` ENABLE KEYS */;


-- Volcando estructura para tabla colonosswiii.user
CREATE TABLE IF NOT EXISTS `user` (
  `code_user` int(11) NOT NULL AUTO_INCREMENT,
  `name_user` varchar(50) NOT NULL,
  `password` varbinary(50) DEFAULT NULL,
  PRIMARY KEY (`code_user`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla colonosswiii.user: ~5 rows (aproximadamente)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`code_user`, `name_user`, `password`) VALUES
	(1, 'Jhon', NULL),
	(2, 'Sandy', NULL),
	(3, 'Jorge Valencia', NULL),
	(4, 'Angelica Paladinez', NULL),
	(5, 'Millan', NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
