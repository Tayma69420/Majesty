<?php

// src/Controller/SecurityController.php
// src/Controller/SecurityController.php

namespace App\Controller;

use App\Entity\Utilisateur;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Session\SessionInterface;

class SecurityController extends AbstractController
{
    /**
     * @Route("/login", name="app_login")
     */

     
     public function login(Request $request, EntityManagerInterface $em, SessionInterface $session)
     {
         $errorMessage = null;
     
         if ($request->isMethod('POST')) {
             $email = $request->request->get('_username');
             $password = $request->request->get('_password');
     
             // Find the user with the given email
             $utilisateur = $em->getRepository(Utilisateur::class)
                 ->findOneBy(['email' => $email]);
     
             // If no user was found or the password is incorrect
             if (!$utilisateur || $utilisateur->getPasswd() !== $this->encryptPassword($password)) {
                 $errorMessage = 'Invalid email or password.';
             } else {
                 // If the email and password are correct, log the user in
                 // ...
     
                 // Store the user object in the session
                 $session->set('user', $utilisateur);
     
                 // Get the user's idRole
                 $idRole = $utilisateur->getIdRole();
     
                 // Redirect the user to the appropriate page based on idRole
                 switch ($idRole) {
                    case 1:
                        return $this->redirectToRoute('admin_dashboard');
                    case 2:
                        return $this->redirectToRoute('user_dashboard');
                    case 3:
                        return $this->redirectToRoute('freelancer_dashboard');
                    default:
                        $errorMessage = 'Invalid user role.';
                        break;
                 }
             }
         }
     
         return $this->render('custom/connect.html.twig', [
             'errorMessage' => $errorMessage,
         ]);
         
     }
     
     private function encryptPassword(string $password): string
     {
         return hash('sha256', $password);
     }
     
    

}
