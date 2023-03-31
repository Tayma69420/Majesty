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
use Twilio\Rest\Client;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\ButtonType;
use Symfony\Component\HttpFoundation\JsonResponse;
use App\Entity\Utilisateur;
use Symfony\Component\HttpFoundation\Session\SessionInterface;

use Twilio\Exceptions\RestException;


class RegistrationController extends AbstractController
{
    /**
     * @Route("/user_registration", name="user_registration")
     */

     private function encryptPassword(string $password): string
{
    return hash('sha256', $password);
}

public function userRegistration(Request $request, SessionInterface $session): Response
{
    $user = new Utilisateur();
// replace with your own Twilio credentials
$sid = 'AC888b21cc1072373d1fb728a2315dc79f';
$token = '359d14a4b43158e739ae65aab336151f';

// A Twilio phone number you purchased at twilio.com/console
$twilioPhoneNumber = '+12706481625';

$twilioClient = new Client($sid, $token);

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
    ->add('verificationCode', null, [
        'mapped' => false,
        'required' => true,
        'label' => 'Code de vérification',
    ])
    ->add('sendCode', SubmitType::class, [
        'label' => 'Envoyer le code',
        'attr' => ['class' => 'btn btn-primary'],
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
    ->add('submit', SubmitType::class, [
        'label' => 'S\'inscrire',
        'attr' => ['class' => 'btn btn-success'],
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

        if ($form->get('sendCode')->isClicked()) {
            $tel = $form->get('tel')->getData();
            $verificationCode = rand(1000, 9999);
            
            try {
                $twilioPhoneNumber = '+12706481625'; // Your Twilio phone number
                $message = $twilioClient->messages->create($tel, [
                    'from' => $twilioPhoneNumber,
                    'body' => sprintf('Votre code de vérification est %d', $verificationCode),
                ]);
            } catch (RestException $e) {
                // Handle the exception if the message failed to send
                $this->addFlash('error', 'Failed to send verification code');
                return $this->redirectToRoute('user_registration');
            }
            
            // Store the verification code in session
            $session = $request->getSession();

            $session->set('verification_code', $verificationCode);
            dump( $session);
            $session->set('tel', $tel);
        }
        
                // Handle verification code submission
                $submittedVerificationCode = $form->get('verificationCode')->getData();
                if ($submittedVerificationCode) {
                    $sessionVerificationCode = $session->get('verification_code');
                    if ($sessionVerificationCode && $sessionVerificationCode == $submittedVerificationCode) {
                        // Verification successful, proceed with registration
                        $password = $form->get('passwd')->getData();
                        $encryptedPassword = $this->encryptPassword($password);

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
       
    
    

    
}

}return $this->render('custom/user_registration.html.twig', [
    'form' => $form->createView(),
]);


}
  



    /**
     * @Route("/freelancer_registration", name="freelancer_registration")
     */
    public function freelancerRegistration(Request $request, SessionInterface $session): Response
    {
        $Freelancer = new Utilisateur();
        // replace with your own Twilio credentials
        $sid = 'AC888b21cc1072373d1fb728a2315dc79f';
        $token = '359d14a4b43158e739ae65aab336151f';
        
        // A Twilio phone number you purchased at twilio.com/console
        $twilioPhoneNumber = '+12706481625';
        
        $twilioClient = new Client($sid, $token);
        
            $form = $this->createFormBuilder($Freelancer)
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
            ->add('verificationCode', null, [
                'mapped' => false,
                'required' => true,
                'label' => 'Code de vérification',
            ])
            ->add('sendCode', SubmitType::class, [
                'label' => 'Envoyer le code',
                'attr' => ['class' => 'btn btn-primary'],
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
            ->add('submit', SubmitType::class, [
                'label' => 'S\'inscrire',
                'attr' => ['class' => 'btn btn-success'],
            ])
            ->getForm();
        
            $form->handleRequest($request);
        
            if ($form->isSubmitted() && $form->isValid()) {
                $email = $form->get('email')->getData();
                $entityManager = $this->getDoctrine()->getManager();
                $existingUser = $entityManager->getRepository(Utilisateur::class)->findOneBy(['email' => $email]);
        
                if ($existingUser) {
                    $form->get('email')->addError(new FormError('This email address is already registered.'));
                    return $this->render('custom/freelancer_registration.html.twig', [
                        'form' => $form->createView(),
                    ]);
                }
        
                if ($form->get('sendCode')->isClicked()) {
                    $tel = $form->get('tel')->getData();
                    $verificationCode = rand(1000, 9999);
                    
                    try {
                        $twilioPhoneNumber = '+12706481625'; // Your Twilio phone number
                        $message = $twilioClient->messages->create($tel, [
                            'from' => $twilioPhoneNumber,
                            'body' => sprintf('Votre code de vérification est %d', $verificationCode),
                        ]);
                    } catch (RestException $e) {
                        // Handle the exception if the message failed to send
                        $this->addFlash('error', 'Failed to send verification code');
                        return $this->redirectToRoute('freelancer_registration');
                    }
                    
                    // Store the verification code in session
                    $session = $request->getSession();
        
                    $session->set('verification_code', $verificationCode);
                    dump( $session);
                    $session->set('tel', $tel);
                }
                
                        // Handle verification code submission
                        $submittedVerificationCode = $form->get('verificationCode')->getData();
                        if ($submittedVerificationCode) {
                            $sessionVerificationCode = $session->get('verification_code');
                            if ($sessionVerificationCode && $sessionVerificationCode == $submittedVerificationCode) {
                                // Verification successful, proceed with registration
                                $password = $form->get('passwd')->getData();
                                $encryptedPassword = $this->encryptPassword($password);
        
                $password = $form->get('passwd')->getData();
                $encryptedPassword = $this->encryptPassword($password);
                // handle image upload
                $image = $form['image']->getData();
                $imagePath = $request->request->get('imagePath');
        
                if ($image && is_uploaded_file($image)) {
                    $imageFileName = uniqid().'.'.$image->guessExtension();
                    move_uploaded_file($image, $this->getParameter('images_directory').'/'.$imageFileName);
                    $Freelancer->setImage($imageFileName);
                }
        
                $Freelancer->setPasswd($encryptedPassword);
                $Freelancer->setIdRole(3);
        
                $entityManager->persist($Freelancer);
                $entityManager->flush();
        
                return $this->redirectToRoute('app_login');
            }
               
            
            
        
            
        }
        
        }return $this->render('custom/freelancer_registration.html.twig', [
            'form' => $form->createView(),
        ]);
        
        
        }
}