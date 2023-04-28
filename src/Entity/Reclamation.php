<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * Reclamation
 *
 * @ORM\Table(name="reclamation", indexes={@ORM\Index(name="fkuserrec", columns={"iduser"}), @ORM\Index(name="idcat", columns={"idcat"})})
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
     * @Assert\NotBlank(message="Description cannot be blank.")
     * @Assert\Length(min=2, max=7)
     */
    private $reclaDesc;

    /**
     * @var \DateTime
     *
     * @ORM\Column(name="daterec", type="date", nullable=false)
     */
    private $daterec;

     /**
     * @var \int
     *
     * @ORM\Column(name="rating", type="integer")
     */
    private $rating;

    /**
     * @var string
     *
     * @ORM\Column(name="titre", type="string", length=50, nullable=false)
     * @Assert\Length(min=2, max=7)
     */
    private $titre;

    /**
     * @var string|null
     *
     * @ORM\Column(name="type", type="string", length=255, nullable=true, options={"default"="NULL"})
     * @Assert\Length(min=2, max=20)
     */
    private $type;

    /**
     * @var \CategorieRec
     *
     * @ORM\ManyToOne(targetEntity="CategorieRec")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idcat", referencedColumnName="id")
     * })
     */
    private $idcat;

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

    public function getType(): ?string
    {
        return $this->type;
    }

    public function setType(?string $type): self
    {
        $this->type = $type;

        return $this;
    }

    public function getIdcat(): ?CategorieRec
    {
        return $this->idcat;
    }

    public function setIdcat(?CategorieRec $idcat): self
    {
        $this->idcat = $idcat;

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
    public function getRating(): int
    {
        return $this->rating;
    }

    public function setRating(int $rating)
    {
        $this->rating = $rating;

       
    }
    


}
