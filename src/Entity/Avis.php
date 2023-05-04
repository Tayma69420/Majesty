<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
/**
 * Avis
 *
 * @ORM\Table(name="avis", indexes={@ORM\Index(name="idportfolio", columns={"idportfolio"})})
 * @ORM\Entity
 */
class Avis
{
    /**
     * @var int
     *
     * @ORM\Column(name="idavis", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idavis;

    /**
     * @var string
     *
     * @ORM\Column(name="commentaire", type="string", length=255, nullable=false)
     */
    private $commentaire;

    /**
     * @var int|null
     *
     * @ORM\Column(name="iduser", type="integer", nullable=true)
     */
    private  $iduser;

    /**
     * @var \Portfolio
     *
     * @ORM\ManyToOne(targetEntity="Portfolio")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="idportfolio", referencedColumnName="idportfolio")
     * })
     */
    private $idportfolio;


    public function __construct()
    {
        $this->Portfolio = new ArrayCollection();
    }

    public function getIdavis(): ?int
    {
        return $this->idavis;
    }

    public function getCommentaire(): ?string
    {
        return $this->commentaire;
    }

    public function setCommentaire(string $commentaire): self
    {
        $this->commentaire = $commentaire;

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

    public function getIdportfolio(): ?Portfolio
    {
        return $this->idportfolio;
    }

    public function setIdportfolio(?Portfolio $idportfolio): self
    {
        $this->idportfolio = $idportfolio;

        return $this;
    }

    
}
