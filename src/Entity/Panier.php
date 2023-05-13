<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;
/**
 * Panier
 *
 * @ORM\Table(name="panier")
 * @ORM\Entity
 */
class Panier
{
    /**
     * @var int
     *@Groups({"paniers"})
     * @ORM\Column(name="idpanier", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idpanier;

    /**
     * @var string
     * *@Groups({"paniers"})
     * @ORM\Column(name="nom", type="string", length=255, nullable=false)
     */
    private $nom;

    /**
     * @var float
     * *@Groups({"paniers"})
     * @ORM\Column(name="prix", type="float", precision=10, scale=0, nullable=false)
     */
    private $prix;

    /**
     * @var int
     * *@Groups({"paniers"})
     * @ORM\Column(name="idprojet", type="integer", nullable=false)
     */
    private $idprojet;

    /**
     * @var int
     * *@Groups({"paniers"})
     * @ORM\Column(name="qnt", type="integer", nullable=false)
     */
    private $qnt;

    /**
     * @var int|null
     * *@Groups({"paniers"})
     * @ORM\Column(name="iduser", type="integer", nullable=true)
     */
    private $iduser;

    public function getIdpanier(): ?int
    {
        return $this->idpanier;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(?string $nom): self
    {
        $this->nom = $nom;

        return $this;
    }

    public function getPrix(): ?float
    {
        return $this->prix;
    }

    public function setPrix(?float $prix): self
    {
        $this->prix = $prix;

        return $this;
    }

    public function getIdprojet(): ?int
    {
        return $this->idprojet;
    }

    public function setIdprojet(?int $idprojet): self
    {
        $this->idprojet = $idprojet;

        return $this;
    }

    public function getQnt(): ?int
    {
        return $this->qnt;
    }

    public function setQnt(?int $qnt): self
    {
        $this->qnt = $qnt;

        return $this;
    }

    public function getIduser(): ?int
    {
        return $this->iduser;
    }

    public function setIduser(?int $iduser): self
    {
        $this->iduser = $iduser;

        return $this;
    }



}
