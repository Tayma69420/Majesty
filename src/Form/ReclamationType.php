<?php

namespace App\Form;
use App\Entity\Utilisateur;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use App\Entity\Reclamation; 
use App\Entity\CategorieRec;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
class ReclamationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('reclaDesc',TextType::class)
            ->add('daterec')
            ->add('titre',TextType::class)
            ->add('type',TextType::class)
            ->add('idcat',EntityType::class,
            ['class'=>CategorieRec::class,
                'choice_label'=>'type',
                'label'=>'Categorie'
            ])
            ->add('iduser',EntityType::class,
            ['class'=>Utilisateur::class,
                'choice_label'=>'nom',
                'label'=>'User'
            ]) 
            ->add('rating',ChoiceType::class,[
                'choices'=>[
                    '1'=>'1',
                    '2'=>'2',
                    '3'=>'3',
                    '4'=>'4',
                    '5'=>'5',
                ]
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Reclamation::class,
        ]);
    }
    
}
