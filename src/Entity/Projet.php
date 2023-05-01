<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
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
     * 
     */
    private $idprojet;

    /**
     * @var string
     *@Assert\NotBlank()
     * @ORM\Column(name="titreprojet", type="string", length=150, nullable=false)
     */
    private $titreprojet;

    /**
     * @var float
     *@Assert\NotBlank()
     * @ORM\Column(name="prixprojet", type="float", precision=100, scale=0, nullable=false)
     */
    private $prixprojet;
    /**
     * @var string
     *@ORM\Column(type="string", length=255, nullable=true)
     */
    private $type;

    public function getType(): ?string
    {
        return $this->type;
    }
    public function setType(string $type): self
    {
        $this->type = $type;

        return $this;
    }

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
