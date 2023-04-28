<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * Facture
 *
 * @ORM\Table(name="facture")
 * @ORM\Entity
 */
class Facture
{
    /**
     * @var int
     *
     * @ORM\Column(name="idfac", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $idfac;

  /**
     * @var string
     *
     * @ORM\Column(name="cardnumber", type="string", length=50, nullable=false)
     * @Assert\NotBlank()
     * @Assert\Length(max=16)
     * @Assert\Regex(
     *     pattern="/^\d{16}$/",
     *     message="Card number must be 16 digits"
     * )
     */
    private $cardnumber;

    /**
     * @var string
     *
     * @ORM\Column(name="expirationdate", type="string", length=5, nullable=false)
     * @Assert\NotBlank()
     * @Assert\Length(max=5)
     * @Assert\Regex(
     *     pattern="/^(0[1-9]|1[0-2])\/\d{2}$/",
     *     message="Expiration date must be in format MM/YY"
     * )
     * 
     */
    private $expirationdate;

    /**
     * @var int
     *
     * @ORM\Column(name="securitycode", type="integer", nullable=false)
     * @Assert\NotBlank()
     * @Assert\Type(type="integer")
     * @Assert\Length(max=3)
     */
    private $securitycode;

    /**
     * @var string
     *
     * @ORM\Column(name="firstname", type="string", length=255, nullable=false)
     * 
     
     
     */
    private $firstname;

    /**
     * @var string
     *
     * @ORM\Column(name="lastname", type="string", length=255, nullable=false)

     */
    private $lastname;

    /**
     * @var float
     *
     * @ORM\Column(name="total", type="float", precision=10, scale=0, nullable=false)
     */
    private $total;

    /**
     * @var int|null
     *
     * @ORM\Column(name="iduser", type="integer", nullable=true)
     */
    private $iduser;

    public function getIdfac(): ?int
    {
        return $this->idfac;
    }

    public function getCardnumber(): ?string
    {
        return $this->cardnumber;
    }

    public function setCardnumber(string $cardnumber): self
    {
        $this->cardnumber = $cardnumber;

        return $this;
    }

    public function getExpirationdate(): ?string
    {
        return $this->expirationdate;
    }

    public function setExpirationdate(string $expirationdate): self
    {
        $this->expirationdate = $expirationdate;

        return $this;
    }

    public function getSecuritycode(): ?int
    {
        return $this->securitycode;
    }

    public function setSecuritycode(int $securitycode): self
    {
        $this->securitycode = $securitycode;

        return $this;
    }

    public function getFirstname(): ?string
    {
        return $this->firstname;
    }

    public function setFirstname(string $firstname): self
    {
        $this->firstname = $firstname;

        return $this;
    }

    public function getLastname(): ?string
    {
        return $this->lastname;
    }

    public function setLastname(string $lastname): self
    {
        $this->lastname = $lastname;

        return $this;
    }

    public function getTotal(): ?float
    {
        return $this->total;
    }

    public function setTotal(float $total): self
    {
        $this->total = $total;

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
