-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Feb 27, 2026 at 07:03 PM
-- Server version: 8.4.3
-- PHP Version: 8.3.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pint`
--

-- --------------------------------------------------------

--
-- Table structure for table `imagenes`
--

CREATE TABLE `imagenes` (
  `id` bigint NOT NULL,
  `enlace` varchar(1000) DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `usuario_nombre` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `imagenes`
--

INSERT INTO `imagenes` (`id`, `enlace`, `titulo`, `usuario_nombre`) VALUES
(1, 'https://i.pinimg.com/originals/f8/9d/de/f89dde131e6f3de0be2fb7a674d78d8f.jpg', 'Dormitorio 1', 'dani'),
(2, 'https://live.staticflickr.com/65535/53526188060_cfb1ac2ee2_b.jpg', 'Dormitorio 2', 'dani'),
(3, 'https://images.squarespace-cdn.com/content/v1/63dde481bbabc6724d988548/cce21c1e-fdd4-4040-ac93-b1df4520d6f8/9.jpeg', 'Dormitorio 3', 'dani'),
(4, 'https://www.decorilla.com/online-decorating/wp-content/uploads/2023/08/Cozy-aesthetic-of-a-rustic-living-room-by-Decorilla-designer-Erica-F.jpg', 'Salón 1', 'dani'),
(5, 'https://images.unsplash.com/photo-1650091507687-5ea34d80e674?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixlib=rb-4.1.0&q=80&w=1080', 'Salón 3', 'dani'),
(6, 'https://i.pinimg.com/1200x/fc/31/05/fc31053e68be6864fee83f8442af60b6.jpg', 'Hatsune Miku', 'art'),
(7, 'https://i.pinimg.com/736x/37/a3/b4/37a3b441c3a60d0901f133dd9a0e24dd.jpg', 'Hu-tao', 'art'),
(8, 'https://i.pinimg.com/1200x/02/f4/2e/02f42eec633c46c02b15979ad887f801.jpg', 'Pikabi', 'art'),
(9, 'https://i.pinimg.com/736x/13/07/58/13075840a64a0580ae9eaf7c4d750316.jpg', ' ', 'art'),
(10, 'https://i.pinimg.com/1200x/df/2b/6c/df2b6ced66a28101a3b4ce1a468e38c1.jpg', ' ', 'art'),
(11, 'https://i.pinimg.com/1200x/6b/aa/73/6baa735d375d312cd4833d80f681ab60.jpg', ' ', 'art'),
(12, 'https://i.pinimg.com/236x/2a/45/bd/2a45bdc7e2b3300fa16182ed9d7c2cfa.jpg', 'pipi', 'art'),
(13, 'https://i.pinimg.com/736x/19/d7/2f/19d72ff11406c3c0f5cbacd44dadf593.jpg', ' ', 'art'),
(14, 'https://i.pinimg.com/736x/42/be/28/42be286bb01acf078d4b8f5887f12f12.jpg', 'Wajolote', 'art'),
(15, 'https://i.pinimg.com/736x/d5/b1/4a/d5b14aea3136e9446e5634f97db064c8.jpg', 'patri', 'art'),
(16, 'https://i.pinimg.com/originals/ea/38/2f/ea382f77e03818842c4d4691eccaf953.gif', 'finger here!', 'art'),
(17, 'https://i.pinimg.com/736x/58/12/41/581241b61e665a573192a0a0361771ec.jpg', 'cat$', 'art'),
(18, 'https://i.pinimg.com/736x/03/c7/74/03c7741e9c7ba105295026d7719169d7.jpg', 'pingui$', 'art'),
(19, 'https://i.pinimg.com/1200x/82/c3/ee/82c3eec445b636be7931499a8f015ecf.jpg', 'カンチェンジュンガ', 'art'),
(20, 'https://i.pinimg.com/736x/21/7a/fe/217afedac216b234d2471bfbae628055.jpg', 'rabit', 'art'),
(21, 'https://i.pinimg.com/1200x/55/17/46/551746a2efa83b0c2d41b9decf3e9a05.jpg', 'panche', 'art'),
(22, 'https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fmedia.tenor.com%2FjRdDJR9EL44AAAAM%2Fpeach-goma.gif&f=1&nofb=1&ipt=a79466c1bb2566b46249941298bfc2f9f0cf91605d325b6de27299a851de6c92', ':3', 'dani'),
(23, 'https://i.pinimg.com/736x/6e/b7/bb/6eb7bbc631bbabb54b040ceeea166098.jpg', 'Magik Raccoon⭐️', 'art'),
(24, 'https://i.pinimg.com/1200x/6c/6c/62/6c6c624247a7b090fde0c297d456ef87.jpg', 'ranita', 'dani'),
(25, 'https://media.tenor.com/yRSnf6wABQ4AAAAj/pato-duck.gif', 'patito', 'dani'),
(27, 'https://i.pinimg.com/736x/ef/12/20/ef1220c02cb5241671760e6ec51459b5.jpg', 'gatito ramdom burlon', 'art'),
(28, 'https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fmedia1.tenor.com%2Fimages%2F73f53e558b557fbaf396da41d322b5fc%2Ftenor.gif%3Fitemid%3D15430082&f=1&nofb=1&ipt=4409e44d0263d7bdf9cdd6bb5086a2edf1b384c42d02f5ea01be42d4a66beb2d', 'assss', 'dani');

-- --------------------------------------------------------

--
-- Table structure for table `usuarios`
--

CREATE TABLE `usuarios` (
  `contrasenya` varchar(255) NOT NULL,
  `correo` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `usuarios`
--

INSERT INTO `usuarios` (`contrasenya`, `correo`, `nombre`) VALUES
('$2a$10$xnpeDf2XGyZZ07JR3j8HE.nt5uKNPvm5w8FOUYRj8hUUkj55j7WYu', 'rosadelalbaxx@gmail.com', 'art'),
('$2a$10$zgs.AjVl66hWxzthIK7f1Oug7TVrYwmDagx7AzcJb0w6BsGwhs1w2', 'illescasdaniel.dev@gmail.com', 'dani');

-- --------------------------------------------------------

--
-- Table structure for table `usuario_favoritos`
--

CREATE TABLE `usuario_favoritos` (
  `imagen_id` bigint NOT NULL,
  `usuario_nombre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `usuario_favoritos`
--

INSERT INTO `usuario_favoritos` (`imagen_id`, `usuario_nombre`) VALUES
(18, 'dani'),
(15, 'dani'),
(7, 'dani'),
(25, 'art'),
(21, 'art'),
(16, 'art'),
(15, 'art'),
(11, 'art'),
(12, 'art'),
(2, 'art'),
(1, 'art'),
(9, 'art');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `imagenes`
--
ALTER TABLE `imagenes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKef39d9exhue7t5gfmjk0l1ak9` (`usuario_nombre`);

--
-- Indexes for table `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`nombre`),
  ADD UNIQUE KEY `UKcdmw5hxlfj78uf4997i3qyyw5` (`correo`);

--
-- Indexes for table `usuario_favoritos`
--
ALTER TABLE `usuario_favoritos`
  ADD KEY `FKtkys0hi795mg4t1s6psfge990` (`imagen_id`),
  ADD KEY `FK5y8y6g1ib78wcfw0xrgrudnvw` (`usuario_nombre`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `imagenes`
--
ALTER TABLE `imagenes`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `imagenes`
--
ALTER TABLE `imagenes`
  ADD CONSTRAINT `FKef39d9exhue7t5gfmjk0l1ak9` FOREIGN KEY (`usuario_nombre`) REFERENCES `usuarios` (`nombre`);

--
-- Constraints for table `usuario_favoritos`
--
ALTER TABLE `usuario_favoritos`
  ADD CONSTRAINT `FK5y8y6g1ib78wcfw0xrgrudnvw` FOREIGN KEY (`usuario_nombre`) REFERENCES `usuarios` (`nombre`) ON DELETE CASCADE,
  ADD CONSTRAINT `FKtkys0hi795mg4t1s6psfge990` FOREIGN KEY (`imagen_id`) REFERENCES `imagenes` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
