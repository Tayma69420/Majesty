<?php



namespace App\Form;

use App\Entity\Avis;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;

class Avis1Type extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('commentaire', TextareaType::class)
           /* ->add('iduser', EntityType::class, [
                'class' => 'App\Entity\Utilisateur',
                'choice_label' => 'iduser',
                'required' => false,
            ])*/
            ->add('idportfolio', EntityType::class, [
                'class' => 'App\Entity\Portfolio',
                'choice_label' => 'idportfolio',
            ]);
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => Avis::class,
        ]);
    }
}