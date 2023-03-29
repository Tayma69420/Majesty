<?php
// src/Controller/RegistrationController.php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Validation;
use Symfony\Component\Validator\Constraints\Email;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\FormError;

use App\Entity\Utilisateur;



class RegistrationController extends AbstractController
{
    /**
     * @Route("/user_registration", name="user_registration")
     */

     private function encryptPassword(string $password): string
{
    return hash('sha256', $password);
}

public function userRegistration(Request $request): Response
{
    $user = new Utilisateur();


    $form = $this->createFormBuilder($user)
    ->add('nom', null, [
        'constraints' => [
            new Assert\NotBlank(['message' => 'Nom doit pas etre vide']),
        ],
    ])
    ->add('prenom', null, [
        'constraints' => [
            new Assert\NotBlank(['message' => 'Prenom doit pas etre vide']),
        ],
    ])
    ->add('email', null, [
        'constraints' => [
           // new Assert\NotBlank(['message' => 'Email doit pas etre vide']),
            new Email(['message' => '']),
        ],
    ])
    ->add('tel', null, [
        'constraints' => [
            new Assert\Regex([
                'pattern' => '/^\+216\d{8}$/',
                'message' => 'Tel doit commencer par +216 et être suivi de 8 chiffres',
            ]),
        ],
    ])
    ->add('adresse', null, [
        'constraints' => [
            new Assert\NotBlank(['message' => 'Adresse doit pas etre vide']),
        ],
    ])
    ->add('age', DateType::class, [
        'widget' => 'single_text',
        'format' => 'yyyy-MM-dd',
    ])
    ->add('passwd', null, [
        'constraints' => [
            new Assert\NotBlank(['message' => 'Password doit pas etre vide']),
            new Assert\Regex([
                'pattern' => '/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/',
                'message' => 'Le mot de passe doit contenir au moins une lettre minuscule, une lettre majuscule, un chiffre et comporter au moins 8 caractères.',
            ]),
        ],
    ]) 
    ->add('image', FileType::class, [
        'label' => 'Image',
        'mapped' => false,
        'required' => false,
    ])
    ->add('sexe', ChoiceType::class, [
        'choices' => [
            'Homme' => 'homme',
            'Femme' => 'femme',
        ],
        'expanded' => true,
        'multiple' => false,
        'constraints' => [
            new Assert\NotBlank(['message' => 'Veuillez sélectionner un sexe']),
        ],
    ])
    ->getForm();

    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        $email = $form->get('email')->getData();
        $entityManager = $this->getDoctrine()->getManager();
        $existingUser = $entityManager->getRepository(Utilisateur::class)->findOneBy(['email' => $email]);

        if ($existingUser) {
            $form->get('email')->addError(new FormError('This email address is already registered.'));
            return $this->render('custom/user_registration.html.twig', [
                'form' => $form->createView(),
            ]);
        }

        $password = $form->get('passwd')->getData();
        $encryptedPassword = $this->encryptPassword($password);
        // handle image upload
        $image = $form['image']->getData();
        $imagePath = $request->request->get('imagePath');

        if ($image && is_uploaded_file($image)) {
            $imageFileName = uniqid().'.'.$image->guessExtension();
            move_uploaded_file($image, $this->getParameter('images_directory').'/'.$imageFileName);
            $user->setImage($imageFileName);
        }

        $user->setPasswd($encryptedPassword);
        $user->setIdRole(2);

        $entityManager->persist($user);
        $entityManager->flush();

        return $this->redirectToRoute('app_login');
    }

    return $this->render('custom/user_registration.html.twig', [
        'form' => $form->createView(),
    ]);
}


    /**
     * @Route("/freelancer_registration", name="freelancer_registration")
     */
    public function freelancerRegistration(Request $request): Response
    {
        $freelancer = new Utilisateur();

        $form = $this->createFormBuilder($freelancer)
    
        ->add('nom', null, [
            'constraints' => [
                new Assert\NotBlank(['message' => 'Nom doit pas etre vide']),
            ],
        ])
        ->add('prenom', null, [
            'constraints' => [
                new Assert\NotBlank(['message' => 'Prenom doit pas etre vide']),
            ],
        ])
        ->add('email', null, [
            'constraints' => [
               // new Assert\NotBlank(['message' => 'Email doit pas etre vide']),
                new Email(['message' => '']),
            ],
        ])
        ->add('tel', null, [
            'constraints' => [
                new Assert\Regex([
                    'pattern' => '/^\+216\d{8}$/',
                    'message' => 'Tel doit commencer par +216 et être suivi de 8 chiffres',
                ]),
            ],
        ])
        ->add('adresse', null, [
            'constraints' => [
                new Assert\NotBlank(['message' => 'Adresse cannot be empty']),
            ],
        ])
        ->add('age', DateType::class, [
            'widget' => 'single_text',
            'format' => 'yyyy-MM-dd',
        ])
        ->add('passwd', null, [
            'constraints' => [
                new Assert\NotBlank(['message' => 'Password cannot be empty']),
                new Assert\Regex([
                    'pattern' => '/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/',
                    'message' => 'Le mot de passe doit contenir au moins une lettre minuscule, une lettre majuscule, un chiffre et comporter au moins 8 caractères.',
                ]),
            ],
        ]) 
        ->add('image', FileType::class, [
            'label' => 'Image',
            'mapped' => false,
            'required' => false,
        ])
        ->add('sexe', ChoiceType::class, [
            'choices' => [
                'Homme' => 'homme',
                'Femme' => 'femme',
            ],
            'expanded' => true,
            'multiple' => false,
            'constraints' => [
                new Assert\NotBlank(['message' => 'Please select a gender']),
            ],
        ])
        ->getForm();
        

    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {

          // handle image upload
       // handle image upload
$image = $form['image']->getData();
$imagePath = $request->request->get('imagePath');

if ($image && is_uploaded_file($image)) {
$imageFileName = uniqid().'.'.$image->guessExtension();
move_uploaded_file($image, $this->getParameter('images_directory').'/'.$imageFileName);
$freelancer->setImage($imageFileName);
}

        $freelancer->setIdRole(3);

      



        $entityManager = $this->getDoctrine()->getManager();

        $entityManager->persist($freelancer);
        $entityManager->flush();
        

        return $this->redirectToRoute('app_login');
    }


        return $this->render('custom/freelancer_registration.html.twig', [
            'form' => $form->createView(),
        ]);
    }

}