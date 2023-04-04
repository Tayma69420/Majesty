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
use Swift_SmtpTransport;
use Swift_Mailer;
use PragmaRX\Google2FAQRCode\Google2FAQRCode;
use PragmaRX\Google2FA\Google2FA;






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
                $session->set('user', $utilisateur);
    
                // If the email and password are correct, check if 2FA is enabled for the user
                if ($utilisateur->getIs2faEnabled()) {
                    // If 2FA is enabled, check if the user has entered a valid code
                    $google2fa = new Google2FA();
                    $code = $request->request->get('code');
                    if (!$code) {
                        $errorMessage = 'Please enter an authentication code.';
                        // Render the login page with an error message
                        return $this->render('custom/connect.html.twig', [
                            'errorMessage' => $errorMessage,
                        ]);
                    }
                    $validCode = $google2fa->verifyKey($utilisateur->getfaSecretKey(), $code);
                    if (!$validCode) {
                        $errorMessage = 'Invalid authentication code.';
                        // Render the login page with an error message
                        return $this->render('custom/connect.html.twig', [
                            'errorMessage' => $errorMessage,
                        ]);
                    }
                } else {
                    // If 2FA is not enabled, check if the user has entered a secret key to enable it
                    $secretKey = $request->request->get('secret_key');
                    if ($secretKey) {
                        $google2fa = new Google2FA();
                        $validKey = $google2fa->verifyKey($secretKey);
                        if ($validKey) {
                            // If the secret key is valid, update the fa_secret_key for the user
                            $utilisateur->setfaSecretKey($secretKey);
                            $utilisateur->setIs2faEnabled(true);
                        } else {
                            $errorMessage = 'Invalid secret key.';
                            // Render the login page with an error message
                            return $this->render('custom/connect.html.twig', [
                                'errorMessage' => $errorMessage,
                            ]);
                        }
                    }
                }
    
    
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
/**
 * @Route("/forgot-password", name="app_forgot_password")
 */
public function forgotPassword(Request $request, \Swift_Mailer $mailer)
{
    $transport = (new Swift_SmtpTransport('smtp.gmail.com', 587, 'tls'))
        ->setUsername('pidevmajesty@gmail.com')
        ->setPassword('xfbyslhggajvfdjz');
    $mailer = new Swift_Mailer($transport);

    // Handle the form submission
    if ($request->isMethod('POST')) {
        // Get the email address from the form
        $email = $request->request->get('email');

        // Find the user with the given email
        $utilisateur = $this->getDoctrine()->getRepository(Utilisateur::class)->findOneBy(['email' => $email]);

        if (!$utilisateur) {
            // Redirect the user to an error page
            return $this->redirectToRoute('app_forgot_password_error');
        }

        // Generate a new password and set it for the user
        $newPassword = substr(md5(rand()), 0, 8);
        $utilisateur->setPasswd($this->encryptPassword($newPassword));

        // Update the user in the database
        $entityManager = $this->getDoctrine()->getManager();
        $entityManager->persist($utilisateur);
        $entityManager->flush();

        // Create the message to send
        $message = (new \Swift_Message('Mot de passe oublié'))
            ->setFrom('noreply@example.com')
            ->setTo($email);

        // Add the message body with a design and user's name
        $message->setBody(
            $this->renderView(
                'custom/email_template.html.twig',
                ['nom' => $utilisateur->getNom(), 'prenom' => $utilisateur->getPrenom(), 'new_password' => $newPassword]
            ),
            'text/html'
        );

        // Send the message
        $mailer->send($message);

        // Redirect the user to a confirmation page
        return $this->redirectToRoute('app_forgot_password_confirm');
    }

    // Render the form
    return $this->render('custom/forgot_password.html.twig');
}


    /**
 * @Route("/forgot-password/confirm", name="app_forgot_password_confirm")
 */
public function forgotPasswordConfirm()
{
    return $this->render('custom/password_reset_confirmation.html.twig');
}


/**
 * @Route("/enable-2fa", name="enable_2fa")
 */
public function enable2fa(EntityManagerInterface $em, SessionInterface $session)
{
    // Generate a new secret key for the user
    $google2fa = new Google2FA();
    $secretKey = $google2fa->generateSecretKey();
    
    // Update the user's secret key in the database
    $utilisateur = $session->get('user');
    $utilisateur->setfaSecretKey($secretKey);
    $em->persist($utilisateur);
    $em->flush();

    // Generate a QR code for the user to scan with their 2FA app
    $google2faQRCode = new Google2FAQRCode();
    $qrCodeUrl = $google2faQRCode->getQRCodeUrl(
        'My Company',
        $utilisateur->getIduser(),
        $secretKey
    );

    // Render the enable 2FA page
    return $this->render('custom/2fa_setup.html.twig', [
        'qrCodeUrl' => $qrCodeUrl,
        'secretKey' => $secretKey,
    ]);
}


}