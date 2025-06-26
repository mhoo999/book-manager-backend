CREATE DATABASE IF NOT EXISTS book_manager;
USE book_manager;

CREATE TABLE `admin` (
	`admin_id`	int	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`account_id`	VARCHAR(255)	NOT NULL,
	`pwd`	VARCHAR(255)	NOT NULL,
	`phone`	VARCHAR(255)	NULL,
	`name`	VARCHAR(255)	NOT NULL,
	`dept`	VARCHAR(255)	NOT NULL,
	`position`	VARCHAR(255)	NOT NULL
);

CREATE TABLE `user` (
	`user_id`	int	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`email`	VARCHAR(255)	NOT NULL UNIQUE,
	`name`	VARCHAR(255)	NOT NULL,
	`phone`	VARCHAR(255)	NULL,
	`pwd`	VARCHAR(255)	NULL,
	`created_at`	datetime	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updated_at`	datetime	NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
	`deleted_at`	datetime	NULL
);

CREATE TABLE `book` (
	`book_id`	int	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`title`	VARCHAR(255)	NOT NULL,
	`author`	VARCHAR(255)	NOT NULL,
	`publisher`	VARCHAR(255)	NOT NULL,
	`published_at`	datetime	NOT NULL,
	`ISBN`	VARCHAR(255)	NOT NULL,
	`description`	longtext	NULL,
	`category`	smallint	NULL,
	`stock`	smallint	NOT NULL
);

CREATE TABLE `book_item` (
	`bookItem_id`	int	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`book_id`	int	NOT NULL,
	`location`	VARCHAR(255)	NOT NULL,
	`status`	bit(1)	NOT NULL,
	`created_at`	datetime	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updated_at`	datetime	NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `notice` (
	`notice_id`	int	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`admin_id`	int	NOT NULL,
	`type`	bit(2)	NOT NULL,
	`created_at`	datetime	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`title`	VARCHAR(255)	NOT NULL,
	`content`	longtext	NOT NULL,
	`views`	int	NULL	DEFAULT 0
);

CREATE TABLE `reply` (
	`reply_id`	int	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`admin_id`	int	NOT NULL,
	`question_id`	int	NOT NULL,
	`content`	VARCHAR(255)	NOT NULL,
	`created_at`	datetime	NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `rent_history` (
	`rental_id`	int	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`user_id`	int	NOT NULL,
	`bookItem_id`	int	NOT NULL,
	`rental_date`	datetime	NULL,
	`expected_return_date`	DATE	NULL,
	`status`	bit(1)	NULL
);

CREATE TABLE `question` (
	`question_id`	int	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`user_id`	int	NOT NULL,
	`question_type`	bit(1)	NOT NULL,
	`title`	VARCHAR(255)	NOT NULL,
	`content`	longtext	NULL,
	`created_at`	datetime	NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `wish` (
	`wish_id`	int	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`user_id`	int	NOT NULL,
	`due_date`	datetime	NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`status`	bit(3)	NOT NULL,
	`book_name`	VARCHAR(255)	NOT NULL,
	`author`	VARCHAR(255)	NOT NULL,
	`publisher`	VARCHAR(255)	NOT NULL,
	`publish_date`	datetime	NULL 
);

ALTER TABLE `book_item` ADD CONSTRAINT `FK_book_to_book_item` FOREIGN KEY (
	`book_id`
)
REFERENCES `book` (
	`book_id`
);

ALTER TABLE `notice` ADD CONSTRAINT `FK_admin_to_notice` FOREIGN KEY (
	`admin_id`
)
REFERENCES `admin` (
	`admin_id`
);

ALTER TABLE `reply` ADD CONSTRAINT `FK_admin_to_reply` FOREIGN KEY (
	`admin_id`
)
REFERENCES `admin` (
	`admin_id`
);

ALTER TABLE `reply` ADD CONSTRAINT `FK_question_to_reply` FOREIGN KEY (
	`question_id`
)
REFERENCES `question` (
	`question_id`
);

ALTER TABLE `rent_history` ADD CONSTRAINT `FK_user_to_rent_history` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`user_id`
);

ALTER TABLE `rent_history` ADD CONSTRAINT `FK_book_item_to_rent_history` FOREIGN KEY (
	`bookItem_id`
)
REFERENCES `book_item` (
	`bookItem_id`
);

ALTER TABLE `question` ADD CONSTRAINT `FK_user_to_question` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`user_id`
);

ALTER TABLE `wish` ADD CONSTRAINT `FK_user_to_wish` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`user_id`
);

