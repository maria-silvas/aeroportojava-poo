CREATE SCHEMA IF NOT EXISTS `aeroporto`;
USE `aeroporto` ;

-- -----------------------------------------------------
-- Table `aeroporto`.`companhia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aeroporto`.`companhia` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(255) NULL,
  `cnpj` VARCHAR(14) NULL,
  PRIMARY KEY (`id`));
-- -----------------------------------------------------
-- Table `aeroporto`.`aviao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aeroporto`.`aviao` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `marca` VARCHAR(255) NULL,
  `modelo` VARCHAR(255) NULL,
  `prefixo` VARCHAR(7) NULL,
  `capacidade` INT UNSIGNED NULL,
  `companhia_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_aviao_companhia1`
    FOREIGN KEY (`companhia_id`)
    REFERENCES `aeroporto`.`companhia` (`id`));
-- -----------------------------------------------------
-- Table `aeroporto`.`hangar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aeroporto`.`hangar` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `local` VARCHAR(255) NULL,
  `aviao_id` INT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_hangar_aviao1`
    FOREIGN KEY (`aviao_id`)
    REFERENCES `aeroporto`.`aviao` (`id`));
-- -----------------------------------------------------
-- Table `aeroporto`.`pista`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aeroporto`.`pista` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `numero` VARCHAR(255) NULL,
  PRIMARY KEY (`id`));
-- -----------------------------------------------------
-- Table `aeroporto`.`helicoptero`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aeroporto`.`helicoptero` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `marca` VARCHAR(255) NULL,
  `modelo` VARCHAR(255) NULL,
  `cor` VARCHAR(255) NULL,
  `capacidade` INT UNSIGNED NULL,
  PRIMARY KEY (`id`));
-- -----------------------------------------------------
-- Table `aeroporto`.`jato`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aeroporto`.`jato` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `marca` VARCHAR(255) NULL,
  `modelo` VARCHAR(255) NULL,
  `cor` VARCHAR(255) NULL,
  `velocidade` INT UNSIGNED NULL,
  PRIMARY KEY (`id`));
-- -----------------------------------------------------
-- Table `aeroporto`.`voo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aeroporto`.`voo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `numero` VARCHAR(255) NULL,
  `data` VARCHAR(10) NULL,
  `hora` VARCHAR(5) NULL,
  `origem` VARCHAR(3) NULL,
  `destino` VARCHAR(3) NULL,
  `piloto` VARCHAR(255) NULL,
  `copiloto` VARCHAR(255) NULL,
  `observacao` VARCHAR(255) NULL,
  `pista_id` INT NULL,
  `aviao_id` INT NULL,
  `helicoptero_id` INT NULL,
  `jato_id` INT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_voo_pista`
    FOREIGN KEY (`pista_id`)
    REFERENCES `aeroporto`.`pista` (`id`),
  CONSTRAINT `fk_voo_aviao1`
    FOREIGN KEY (`aviao_id`)
    REFERENCES `aeroporto`.`aviao` (`id`),
  CONSTRAINT `fk_voo_helicoptero1`
    FOREIGN KEY (`helicoptero_id`)
    REFERENCES `aeroporto`.`helicoptero` (`id`),
  CONSTRAINT `fk_voo_jato1`
    FOREIGN KEY (`jato_id`)
    REFERENCES `aeroporto`.`jato` (`id`));