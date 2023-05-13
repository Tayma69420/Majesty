<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Serializer\Annotation\Groups;
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
     **@Groups({"projets"})
     * @ORM\Column(name="idprojet", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     * 
     */
    private $idprojet;

    /**
     * @var string
     * *@Groups({"projets"})
     *@Assert\NotBlank()
     * @ORM\Column(name="titreprojet", type="string", length=150, nullable=false)
     */
    private $titreprojet;

    /**
     * @var float
     * *@Groups({"projets"})
     *@Assert\NotBlank()
     * @ORM\Column(name="prixprojet", type="float", precision=100, scale=0, nullable=false)
     */
    private $prixprojet;
    /**
     * @var string
     * *@Groups({"projets"})
     *@ORM\Column(type="string", length=255, nullable=true)
     */
    private $type;
        /**
     * @var string|null
     * *@Groups({"projets"})
     * @ORM\Column(name="imageproj", type="string", length=250, nullable=true)
     */
    private $imageproj;

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

    public function getImageproj(): ?string
    {
        return $this->imageproj;
    }

    public function setImageproj(?string $imageproj): self
    {
        $this->imageproj = $imageproj;

        return $this;
    }
}
