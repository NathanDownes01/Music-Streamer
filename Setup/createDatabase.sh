#!/bin/bash
statement="CREATE DATABASE Music; GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' IDENTIFIED BY 'YES' WITH GRANT OPTION; FLUSH PRIVILEGES;"
mysql -u root -pYES -e "${statement}"
statement="CREATE TABLE Songs (Songid INT AUTO_INCREMENT PRIMARY KEY, Title VARCHAR(70) NOT NULL, Artist VARCHAR(50) NOT NULL, Duration INT NOT NULL);" 
mysql -u root -pYES -D Music -e "${statement}"

