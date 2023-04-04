<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20230402132416 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE messenger_messages (id BIGINT AUTO_INCREMENT NOT NULL, body LONGTEXT NOT NULL, headers LONGTEXT NOT NULL, queue_name VARCHAR(190) NOT NULL, created_at DATETIME NOT NULL, available_at DATETIME NOT NULL, delivered_at DATETIME DEFAULT NULL, INDEX IDX_75EA56E0FB7336F0 (queue_name), INDEX IDX_75EA56E0E3BD61CE (available_at), INDEX IDX_75EA56E016BA31DB (delivered_at), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE panier CHANGE idpanier idpanier INT AUTO_INCREMENT NOT NULL');
        $this->addSql('ALTER TABLE projet CHANGE idprojet idprojet INT AUTO_INCREMENT NOT NULL');
        $this->addSql('ALTER TABLE reclamation CHANGE iduser iduser INT DEFAULT NULL, CHANGE type type INT DEFAULT NULL');
        $this->addSql('ALTER TABLE role CHANGE id_role id_role INT AUTO_INCREMENT NOT NULL');
        $this->addSql('ALTER TABLE utilisateur CHANGE email email VARCHAR(255) NOT NULL');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('DROP TABLE messenger_messages');
        $this->addSql('ALTER TABLE panier CHANGE idpanier idpanier INT NOT NULL');
        $this->addSql('ALTER TABLE projet CHANGE idprojet idprojet INT NOT NULL');
        $this->addSql('ALTER TABLE reclamation CHANGE type type INT NOT NULL, CHANGE iduser iduser INT NOT NULL');
        $this->addSql('ALTER TABLE role CHANGE id_role id_role INT NOT NULL');
        $this->addSql('ALTER TABLE utilisateur CHANGE email email VARCHAR(50) DEFAULT NULL');
    }
}
