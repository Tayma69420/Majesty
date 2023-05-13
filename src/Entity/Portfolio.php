<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Serializer\Annotation\Groups;
/**
 * Portfolio
 *
 * @ORM\Table(name="portfolio")
 * @ORM\Entity
 */
class Portfolio
{
    /**
     * @var int
     *@Groups({"portfolios"})
     * @ORM\Column(name="idportfolio", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idportfolio;

        /**
         * @var string
         *@Groups({"portfolios"})
         * @ORM\Column(name="description", type="string", length=255, nullable=false)
         * @Assert\Length(min=2, max=4)
         */
        private $description;

        /**
         * @var string
         
         * @ORM\Column(name="cv", type="blob", length=65535, nullable=false)
         * @Assert\Length(min=2, max=7)
         */
        private $cv;


    /**
     * @var string
     *@Groups({"portfolios"})
     * @ORM\Column(name="image", type="string", length=255, nullable=false)
     */
    private $image;

    /**
     * @var int|null
     *@Groups({"portfolios"})
     * @ORM\Column(name="iduser", type="integer", nullable=true)
     */
    private $iduser;

    /**
     * @var int|null
     *@Groups({"portfolios"})
     * @ORM\Column(name="idprojet", type="integer", nullable=true)
     */
    private $idprojet;


    
    /**
     * @ORM\Column(type="integer", nullable=true)
     */
    private $rating;
     // Getter and setter for the new "rating" property

     public function getRating(): ?int
     {
         return $this->rating;
     }
 
     public function setRating(?int $rating): self
     {
         $this->rating = $rating;
 
         return $this;
     }

    public function getIdportfolio(): ?int
    {
        return $this->idportfolio;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(?string $description): self
    {
        $this->description = $description;

        return $this;
    }

    public function getCv()
    {
        return $this->cv;
    }

    public function setCv($cv): self
    {
        $this->cv = $cv;

        return $this;
    }

    public function getImage(): ?string
    {
        return $this->image;
    }

    public function setImage(?string $image): self
    {
        $this->image = $image;

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

    public function getIdprojet(): ?int
    {
        return $this->idprojet;
    }

    public function setIdprojet(?int $idprojet): self
    {
        $this->idprojet = $idprojet;

        return $this;
    }

  
    
 /**
     * @ORM\Column(type="integer")
     */
    private $likes;

    /**
     * @ORM\Column(type="integer")
     */
    private $dislikes;

    public function getLikes(): ?int
    {
        return $this->likes;
    }

    public function setLikes(?int $likes): self
    {
        $this->likes = $likes;

        return $this;
    }

    public function getDislikes(): ?int
    {
        return $this->dislikes;
    }

    public function setDislikes(?int $dislikes): self
    {
        $this->dislikes = $dislikes;

        return $this;
    }

}
