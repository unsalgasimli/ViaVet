SQL Kurulumu kolay olmasi icin sifre 0000 digerlerin default birakin Server ismin hospital yapin.
Mysql connectoru jdk-21 dosyasinin icine
https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_windows-x64_bin-sdk.zip DOWNLOAD LINK
FX icin File-->Project Structure-->Libraries + iconu ve sdk_21\lib dosya yeri
                                -->
En ust bolumden run-->Run config--> + iconu Application Modify Options Environment Variables ve add VM options check
                                --> --module-path ".........\javafx-sdk-21.0.1\lib" --add-modules javafx.controls,javafx.fxml 
                                -->App secin 
                                -->--module-path ".........\javafx-sdk-21.0.1\lib" --add-modules javafx.controls,javafx.fxml 
SQL FOR CREATING SCHEME SET NAME TO hospital
CREATE TABLE `appointmentlist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `patientID` varchar(45) NOT NULL,
  `staffID` varchar(45) NOT NULL,
  `date` date NOT NULL,
  `time` varchar(45) NOT NULL,
  `info` varchar(200) NOT NULL,
  `status` varchar(45) NOT NULL,
  `petName` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `historylist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `patientID` varchar(45) NOT NULL,
  `petName` varchar(45) NOT NULL,
  `staffID` varchar(45) NOT NULL,
  `info` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `patientlist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uniqueID` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `middlename` varchar(45) NOT NULL,
  `birthday` varchar(45) NOT NULL,
  `number` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`,`uniqueID`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `petlist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ownerID` varchar(45) NOT NULL,
  `petName` varchar(45) NOT NULL,
  `petAge` varchar(45) NOT NULL,
  `petType` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `stafflist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uniqueID` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `number` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueID_UNIQUE` (`uniqueID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
