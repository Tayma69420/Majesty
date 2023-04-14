<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

/**
 * Reclamation
 *
 * @ORM\Table(name="reclamation", indexes={@ORM\Index(name="fkuserrec", columns={"iduser"}), @ORM\Index(name="fktyperec", columns={"type"})})
 * @ORM\Entity
 */
class Reclamation
{
    /**
     * @var int
     *
     * @ORM\Column(name="id_reclamation", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idReclamation;

    /**
     * @var string
     *
     * @ORM\Column(name="recla_desc", type="string", length=255, nullable=false)
     */
    private $reclaDesc;

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="daterec", type="date", nullable=false)
     */
    private $daterec;

    /**
     * @var string
     *
     * @ORM\Column(name="titre", type="string", length=50, nullable=false)
     */
    private $titre;

    /**
     * @var \CategorieRec
     *
     * @ORM\ManyToOne(targetEntity="CategorieRec")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="type", referencedColumnName="id")
     * })
     */
    private $type;

    /**
     * @var \Utilisateur
     *
     * @ORM\ManyToOne(targetEntity="Utilisateur")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="iduser", referencedColumnName="iduser")
     * })
     */
    private $iduser;

    public function getIdReclamation(): ?int
    {
        return $this->idReclamation;
    }

    public function getReclaDesc(): ?string
    {
        return $this->reclaDesc;
    }

    public function setReclaDesc(string $reclaDesc): self
    {
        $this->reclaDesc = $reclaDesc;

        return $this;
    }

    public function getDaterec(): ?\DateTimeInterface
    {
        return $this->daterec;
    }

    public function setDaterec(\DateTimeInterface $daterec): self
    {
        $this->daterec = $daterec;

        return $this;
    }

    public function getTitre(): ?string
    {
        return $this->titre;
    }

    public function setTitre(string $titre): self
    {
        $this->titre = $titre;

        return $this;
    }

    public function getType(): ?CategorieRec
    {
        return $this->type;
    }

    public function setType(?CategorieRec $type): self
    {
        $this->type = $type;

        return $this;
    }

    public function getIduser(): ?Utilisateur
    {
        return $this->iduser;
    }

    public function setIduser(?Utilisateur $iduser): self
    {
        $this->iduser = $iduser;

        return $this;
    }


}
