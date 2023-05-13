<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;

use Symfony\Component\Serializer\Annotation\Groups;


/**
 * Utilisateur
 *
 * @ORM\Table(name="utilisateur")
 * @ORM\Entity

 */
class Utilisateur
{

public function getfaSecretKey(): ?string
{
    return $this->faSecretKey;
}

public function setfaSecretKey(?string $faSecretKey): self
{
    $this->faSecretKey = $faSecretKey;

    return $this;
}

public function getIs2faEnabled(): bool
{
    return $this->is2faEnabled;
}

public function setIs2faEnabled(bool $is2faEnabled): self
{
    $this->is2faEnabled = $is2faEnabled;

    return $this;
}

    /**
     * @var int
     *
     * @ORM\Column(name="iduser", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     *   @Groups({"utilisateur"})
     */
    private $iduser;

    /**
     * @var string|null
     *
     * @ORM\Column(name="nom", type="string", length=50, nullable=true)
     *   @Groups({"utilisateur"})
     */
    private $nom;

    /**
     * @var string|null
     *
     * @ORM\Column(name="prenom", type="string", length=50, nullable=true)
     *   @Groups({"utilisateur"})
     */
    private $prenom;

    /**
     * @var string|null
     *
     * @ORM\Column(name="email", type="string", length=50, nullable=true)
     *   @Groups({"utilisateur"})
     */
    private $email;

    /**
     * @var string|null
     *
     * @ORM\Column(name="tel", type="string", length=50, nullable=true)
     *   @Groups({"utilisateur"})
     */
    private $tel;

    /**
     * @var string|null
     *  @Groups({"utilisateur"})
     * @ORM\Column(name="adresse", type="string", length=100, nullable=true)
     */
    private $adresse;

    /**
     * @var \DateTime|null
     *  @Groups({"utilisateur"})
     * @ORM\Column(name="age", type="date", nullable=true)
     */
    private $age;

    /**
     * @var string|null
     *  @Groups({"utilisateur"})
     * @ORM\Column(name="passwd", type="string", length=250, nullable=true)
     */
    private $passwd;

    /**
     * @var int|null
     *  @Groups({"utilisateur"})
     * @ORM\Column(name="id_role", type="integer", nullable=true)
     */
    private $idRole;

    /**
     * @var string
     *  @Groups({"utilisateur"})
     * @ORM\Column(name="sexe", type="string", length=55, nullable=false)
     */
    private $sexe;

    /**
     * @var string|null
     *  @Groups({"utilisateur"})
     * @ORM\Column(name="image", type="string", length=250, nullable=true)
     */
    private $image;
    /**
     * @var string|null
     *  @Groups({"utilisateur"})
     * @ORM\Column(name="activationToken", type="string", length=250, nullable=true)
     */
    private $activationToken;

    
        /**
 * @ORM\Column(type="string", length=255, nullable=true)
 *   @Groups({"utilisateur"})
 */
private $faSecretKey;

/**
 * @ORM\Column(type="boolean")
 *    @Groups({"utilisateur"})
 */
private $is2faEnabled = false;
/**
 * @ORM\Column(type="boolean")
 *    @Groups({"utilisateur"})
 */
private $isDisabled  = false;


    public function getIduser(): ?int
    {
        return $this->iduser;
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

    public function getPrenom(): ?string
    {
        return $this->prenom;
    }

    public function setPrenom(?string $prenom): self
    {
        $this->prenom = $prenom;

        return $this;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(?string $email): self
    {
        $this->email = $email;

        return $this;
    }

    public function getTel(): ?string
    {
        return $this->tel;
    }

    public function setTel(?string $tel): self
    {
        $this->tel = $tel;

        return $this;
    }

    public function getAdresse(): ?string
    {
        return $this->adresse;
    }

    public function setAdresse(?string $adresse): self
    {
        $this->adresse = $adresse;

        return $this;
    }

    public function getAge(): ?\DateTimeInterface
    {
        return $this->age;
    }

    public function setAge(?\DateTimeInterface $age): self
    {
        $this->age = $age;

        return $this;
    }

    public function getPasswd(): ?string
    {
        return $this->passwd;
    }

    public function setPasswd(?string $passwd): self
    {
        $this->passwd = $passwd;

        return $this;
    }

    public function getIdRole(): ?int
    {
        return $this->idRole;
    }

    public function setIdRole(?int $idRole): self
    {
        $this->idRole = $idRole;

        return $this;
    }

    public function getSexe(): ?string
    {
        return $this->sexe;
    }

    public function setSexe(?string $sexe): self
    {
        if (is_string($sexe)) {
            $this->sexe = $sexe;
        }
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

    public function getActivationToken(): ?string
    {
        return $this->activationToken;
    }

    public function setActivationToken(?string $activationToken): self
    {
        $this->activationToken = $activationToken;

        return $this;
    }
  

  

    public function isIs2faEnabled(): ?bool
    {
        return $this->is2faEnabled;
    }
    public function getIsDisabled(): bool
    {
        return $this->isDisabled;
    }

    public function setIsDisabled(bool $isDisabled): void
    {
        $this->isDisabled = $isDisabled;
    }

}

