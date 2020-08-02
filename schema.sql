CREATE TABLE `cinema_room` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `rows` int(11) NOT NULL,
  `seats_per_row` int(11) NOT NULL,
  `allowed_distance` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cinema_room_UN` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4

CREATE TABLE `reserved_seat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NOT NULL,
  `row_number` int(11) NOT NULL,
  `seat_number` int(11) NOT NULL,
  `reserved_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `reserved_seat_UN` (`room_id`,`row_number`,`seat_number`),
  CONSTRAINT `reserved_seat_FK` FOREIGN KEY (`room_id`) REFERENCES `cinema_room` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4

CREATE TABLE `reservation_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NOT NULL,
  `room_name` varchar(100) NOT NULL,
  `row_number` int(11) NOT NULL,
  `seat_number` int(11) NOT NULL,
  `reserved_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ticket_clerk` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4