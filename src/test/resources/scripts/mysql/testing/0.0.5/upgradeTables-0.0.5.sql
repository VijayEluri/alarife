CREATE TABLE `Team` (
  `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) engine=innodb default charset=utf8 collate=utf8_spanish_ci;


-- Create script for the OMG table
CREATE TABLE `OMG` (
  `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,-- the ID is autoincremental
  PRIMARY KEY (`id`)
) engine=innodb default charset=utf8 collate=utf8_spanish_ci;