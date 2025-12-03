-- proiect_paw.`user` definition

CREATE TABLE `user` (
                        `id_user` int(11) NOT NULL AUTO_INCREMENT,
                        `username` varchar(100) NOT NULL,
                        `email` varchar(100) NOT NULL,
                        `password` varchar(100) NOT NULL,
                        `is_admin` tinyint(1) NOT NULL,
                        PRIMARY KEY (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `statistic` (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `action` varchar(100) NOT NULL,
                             `argument` varchar(100) DEFAULT NULL,
                             `counter` int(11) NOT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
