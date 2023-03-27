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
            ->add('nom')
            ->add('prenom')
            ->add('email', null, [
                'constraints' => [
                    new Assert\NotBlank([
                        'message' => '',
                    ]),
                    new Assert\Email([
                        'message' => '',
                    ]),
                ],
            ])
            ->add('tel')
            ->add('adresse')
            ->add('age')
            ->add('passwd') 
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
            ])
            ->getForm();
            

        $form->handleRequest($request);

        if ($form->isSubmitted()) {
            
            
            if ($form->isValid()) {

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

                $entityManager = $this->getDoctrine()->getManager();
                $entityManager->persist($user);
                $entityManager->flush();

                return $this->redirectToRoute('app_login');
            }
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
    
        ->add('nom')
        ->add('prenom')
        ->add('email', EmailType::class, [
            
            'constraints' => [
                new Assert\Email([
                    'message' => 'The email "{{ value }}" is not a valid email.',
                ]),
            ],
        ])
        ->add('tel')
        ->add('adresse')
        ->add('age')
        ->add('passwd') 
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
$user->setImage($imageFileName);
}

        $user->setIdRole(3);

      



        $entityManager = $this->getDoctrine()->getManager();

        $entityManager->persist($user);
        $entityManager->flush();
        

        return $this->redirectToRoute('app_login');
    }


        return $this->render('custom/freelancer_registration.html.twig', [
            'form' => $form->createView(),
        ]);
    }

}