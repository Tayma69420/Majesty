<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Projet
 *
 * @ORM\Table(name="projet")
 * @ORM\Entity
 */
class Projet
{
    /**
     * @var int
     *
     * @ORM\Column(name="idprojet", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idprojet;

    /**
     * @var string
     *
     * @ORM\Column(name="titreprojet", type="string", length=150, nullable=false)
     */
    private $titreprojet;

    /**
     * @var float
     *
     * @ORM\Column(name="prixprojet", type="float", precision=100, scale=0, nullable=false)
     */
    private $prixprojet;

    public function getIdprojet(): ?int
    {
        return $this->idprojet;
    }

    public function getTitreprojet(): ?string
    {
        return $this->titreprojet;
    }

    public function setTitreprojet(string $titreprojet): self
    {
        $this->titreprojet = $titreprojet;

        return $this;
    }

    public function getPrixprojet(): ?float
    {
        return $this->prixprojet;
    }

    public function setPrixprojet(float $prixprojet): self
    {
        $this->prixprojet = $prixprojet;

        return $this;
    }


}
