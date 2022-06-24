DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `hibernate_sequence` WRITE;
INSERT INTO `hibernate_sequence` VALUES (1);
UNLOCK TABLES;

DROP TABLE IF EXISTS `photo`;
CREATE TABLE `photo` (
  `attachedphoto` binary(16) NOT NULL,
  `review_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`attachedphoto`),
  KEY `FKnx36dfpxbxmxifyiq7yvhiohn` (`review_id`),
  CONSTRAINT `FKnx36dfpxbxmxifyiq7yvhiohn` FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
LOCK TABLES `photo` WRITE;
UNLOCK TABLES;
DROP TABLE IF EXISTS `place`;

CREATE TABLE `place` (
  `place_id` binary(16) NOT NULL,
  PRIMARY KEY (`place_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

UNLOCK TABLES;
DROP TABLE IF EXISTS `point_log`;
CREATE TABLE `point_log` (
  `id` bigint NOT NULL,
  `point` varchar(255) DEFAULT NULL,
  `user_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiwxuemytacp89p9qppgaj84y6` (`user_id`),
  CONSTRAINT `FKiwxuemytacp89p9qppgaj84y6` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


LOCK TABLES `point_log` WRITE;
UNLOCK TABLES;
DROP TABLE IF EXISTS `review`;

CREATE TABLE `review` (
  `review_id` binary(16) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `rating` varchar(255) DEFAULT NULL,
  `place_id` binary(16) DEFAULT NULL,
  `user_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`review_id`),
  KEY `review_id` (`review_id`),
  KEY `FKn429agmmvh298piqrnnd4gbfg` (`place_id`),
  KEY `FKiyf57dy48lyiftdrf7y87rnxi` (`user_id`),
  CONSTRAINT `FKiyf57dy48lyiftdrf7y87rnxi` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKn429agmmvh298piqrnnd4gbfg` FOREIGN KEY (`place_id`) REFERENCES `place` (`place_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` binary(16) NOT NULL,
  `point` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `user_places`;
CREATE TABLE `user_places` (
  `user_user_id` binary(16) NOT NULL,
  `places_place_id` binary(16) NOT NULL,
  UNIQUE KEY `UK_ppgwd4sjw2akgjkajvxmcb9kq` (`places_place_id`),
  KEY `FKh907mgcf1lpl3jr47d2tul8tx` (`user_user_id`),
  CONSTRAINT `FKh907mgcf1lpl3jr47d2tul8tx` FOREIGN KEY (`user_user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKlgbpsd8ujqeelj2l6988yfnrg` FOREIGN KEY (`places_place_id`) REFERENCES `place` (`place_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


# 초기 user 및 place 가 존재해야함
INSERT INTO user (user_id, point) VALUES
       (UNHEX(REPLACE('3ede0ef2-92b7-4817-a5f3-0c575361f745', "-","")),0);
       
INSERT INTO place (place_id) VALUES
       (UNHEX(REPLACE('2e4baf1c-5acb-4efb-a1af-eddada31b00f', "-","")));
