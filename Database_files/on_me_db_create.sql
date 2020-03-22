CREATE TABLE `on_me`.`Users_table` (
  `id_User` varchar(45) NOT NULL,
  `first_name_user` varchar(45) NOT NULL,
  `last_name_user` varchar(45) NOT NULL,
  `address_user` varchar(45) NOT NULL,
  `email_user` varchar(45) NOT NULL,
  `password_user` varchar(255) NOT NULL,
  PRIMARY KEY (`id_User`),
  UNIQUE KEY `id_User_UNIQUE` (`id_User`)
);

CREATE TABLE `on_me`.`Drinks_table` (
  `id_drinks` int(11) NOT NULL AUTO_INCREMENT,
  `name_drink` varchar(255) NOT NULL,
  `description_drink` varchar(255) NOT NULL,
  `fk_id_User` varchar(45) NOT NULL,
  PRIMARY KEY (`id_drinks`),
  UNIQUE KEY `id_drinks_UNIQUE` (`id_drinks`),
  KEY `fk_id_User_idx` (`fk_id_User`),
  CONSTRAINT `fk_id_User_drinks` FOREIGN KEY (`fk_id_User`) REFERENCES `Users_table` (`id_User`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `on_me`.`Fav_bars_table` (
  `id_bar` int(11) NOT NULL AUTO_INCREMENT,
  `name_bar` varchar(255) NOT NULL,
  `address_bar` varchar(255) NOT NULL,
  `fk_id_User` varchar(45) NOT NULL,
  `fk_id_restuarant` int(11) NOT NULL,
  PRIMARY KEY (`id_bar`),
  UNIQUE KEY `id_bar_UNIQUE` (`id_bar`),
  KEY `fk_id_User_idx` (`fk_id_User`),
  KEY `fk_id_restuarant_bars_idx` (`fk_id_restuarant`),
  CONSTRAINT `fk_id_User_bars` FOREIGN KEY (`fk_id_User`) REFERENCES `Users_table` (`id_User`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_id_restuarant_bars` FOREIGN KEY (`fk_id_restuarant`) REFERENCES `Restaurants_table` (`id_restaurant`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `on_me`.`Friends_table` (
  `id_of_freinds` int(11) NOT NULL,
  `fk_id_user` varchar(45) NOT NULL,
  `list_of_friends` varchar(255) NOT NULL,
  PRIMARY KEY (`id_of_freinds`),
  UNIQUE KEY `id_of_freinds_UNIQUE` (`id_of_freinds`),
  KEY `fk_id_user_friends_idx` (`fk_id_user`),
  CONSTRAINT `fk_id_user_friends` FOREIGN KEY (`fk_id_user`) REFERENCES `Users_table` (`id_User`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `on_me`.`Restaurants_table` (
  `id_restaurant` int(11) NOT NULL AUTO_INCREMENT,
  `name_restaurant` varchar(255) NOT NULL,
  `address_resturant` varchar(255) NOT NULL,
  `phone_restaurant` varchar(45) NOT NULL,
  `hours_restaurant` varchar(45) NOT NULL,
  PRIMARY KEY (`id_restaurant`),
  UNIQUE KEY `id_restaurant_UNIQUE` (`id_restaurant`)
);

CREATE TABLE `on_me`.`Transactions_table` (
  `id_transaction` int(11) NOT NULL AUTO_INCREMENT,
  `fk_recipient_restaurant` int(11) NOT NULL,
  `fk_sender_users` varchar(255) NOT NULL,
  `amount_transaction` decimal(9,2) NOT NULL,
  `date_time_transaction` datetime NOT NULL,
  PRIMARY KEY (`id_transaction`),
  UNIQUE KEY `id_transaction_UNIQUE` (`id_transaction`),
  KEY `fk_sender_id_User_idx` (`fk_sender_users`),
  KEY `fk_recipient_id_bar_idx` (`fk_recipient_restaurant`),
  CONSTRAINT `fk_recipient_restuarant_transaction` FOREIGN KEY (`fk_recipient_restaurant`) REFERENCES `Restaurants_table` (`id_restaurant`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_sender_user_transaction` FOREIGN KEY (`fk_sender_users`) REFERENCES `Users_table` (`id_User`) ON DELETE NO ACTION ON UPDATE CASCADE
);

