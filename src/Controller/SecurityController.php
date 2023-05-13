<?php


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
use App\Controller\Response;
use Symfony\Component\HttpFoundation\JsonResponse ;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;



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
                // If the user is disabled, redirect them to a page that tells them their account is disabled
                if ($utilisateur->getIsDisabled() === true) {
                    $session->set('disabled_user_email', $email);
                    return $this->redirectToRoute('account_disabled');
                }
                
                
    
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
                        return $this->redirectToRoute('app_home');
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

/**
 * @Route("/account-disabled", name="app_account_disabled")
 */
public function accountDisabled(SessionInterface $session, EntityManagerInterface $em)
{
    $disabledUserEmail = $session->get('disabled_user_email');
    $user = $em->getRepository(Utilisateur::class)->findOneBy(['email' => $disabledUserEmail]);

    return $this->render('custom/account_disabled.html.twig', [
        'disabledUserEmail' => $disabledUserEmail,
        'userId' => $user ? $user->getIduser() : null,
    ]);
}


/**
 * @Route("/send-activation-link-{id}", name="send_activation_link")
 */
public function sendActivationLink(Request $request, EntityManagerInterface $em, \Swift_Mailer $mailer, int $id)
{
    $transport = (new Swift_SmtpTransport('smtp.gmail.com', 587, 'tls'))
        ->setUsername('pidevmajesty@gmail.com')
        ->setPassword('xfbyslhggajvfdjz');
    $mailer = new Swift_Mailer($transport);

    // Find the user with the given id
    $user = $em->getRepository(Utilisateur::class)->find($id);

    // Generate a token to include in the activation link
    $token = bin2hex(random_bytes(32));

    // Update the user's token in the database
    $user->setActivationToken($token);
    $em->persist($user);
    $em->flush();

    // Generate the activation link
    $activationLink = $this->generateUrl('activate_account', ['token' => $token], UrlGeneratorInterface::ABSOLUTE_URL);

    // Send the activation email
    $message = (new \Swift_Message('Account Activation'))
        ->setFrom('noreply@example.com')
        ->setTo($user->getEmail())
        ->setBody(
            $this->renderView(
                'emails/activation.html.twig',
                ['activationLink' => $activationLink]
            ),
            'text/html'
        );
    $mailer->send($message);

    // Redirect to the login page with a success message
    $this->addFlash('success', 'An activation link has been sent to your email address.');
    return $this->redirectToRoute('app_login');
}



/**
 * @Route("/activate-account-{token}", name="activate_account")
 */
public function activateAccount(Request $request, EntityManagerInterface $em, $token)
{
    // find the user with the given activation token
    $user = $em->getRepository(Utilisateur::class)->findOneBy(['activationToken' => $token]);

    // if no user was found with the given token, show an error message
    if (!$user) {
        throw $this->createNotFoundException('The user was not found.');
    }

    // activate the user's account
    $user->setIsDisabled(false);
    $user->setActivationToken(null);
    $em->persist($user);
    $em->flush();

    // redirect the user to the login page
    return $this->redirectToRoute('app_login');
}








/**
 * @Route("/front/index", name="Test")
 */
    public function index()
    {
        return $this->render('custom/test.html.twig');
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