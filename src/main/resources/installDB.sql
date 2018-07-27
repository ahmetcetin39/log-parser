CREATE TABLE `logs` (
  `log_id` INT(10) NOT NULL AUTO_INCREMENT,
  `request_time` TIMESTAMP NOT NULL,
  `ip` VARCHAR(15) NOT NULL,
  `request_method` VARCHAR(100) NOT NULL,
  `response_code` VARCHAR(5) NOT NULL,
  `agent` VARCHAR(1000) NOT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `comment` (
  `comment_id` INT(10) NOT NULL AUTO_INCREMENT,
  `ip` VARCHAR(15) NOT NULL,
  `description` VARCHAR(1000) NOT NULL,
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;