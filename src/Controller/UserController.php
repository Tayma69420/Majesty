<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\IsGranted;
use Symfony\Component\HttpFoundation\Session\SessionInterface ;
use App\Entity\Utilisateur;
use PragmaRX\Google2FAQRCode\Google2FAQRCode;
use PragmaRX\Google2FA\Google2FA;
use BaconQrCode\Renderer\ImageRenderer;
use BaconQrCode\Writer\ImageWriter;
use Symfony\Component\HttpFoundation\Response;
use BaconQrCode\Encoder\QrCode;
use BaconQrCode\Common\Mode;
use BaconQrCode\Encoder\Encoder;
use BaconQrCode\Renderer\Image\SvgImageBackEnd;
use App\Services\QrcodeService;









class UserController extends AbstractController
{
   /**
 * @Route("/user", name="user_page")
 */
public function userPage(SessionInterface $session)
{
    $user = $session->get('user');
   

    
        return $this->render('home/profile.html.twig', [
            'controller_name' => 'UserController',
        ]);
    }




/**
 * @Route("/settings", name="user_settings")
 */
public function userSettings(SessionInterface $session)
{
    $user = $session->get('user');

    if ($user->getIs2faEnabled() === false) {
        $message = 'Vous n\'avez pas votre Google Authentication active. Cliquez ici pour l\'activer.';
    } else {
        $message = null;
    }

    return $this->render('custom/settingsuser.html.twig', [
        'message' => $message,
    ]);
}

/**
 * @Route("/update-image", name="user_update_image", methods={"POST"})
 */
public function updateUserImage(Request $request, SessionInterface $session)
{
    $userId = $session->get('user');
    dump($userId);
    $entityManager = $this->getDoctrine()->getManager();
    $user = $entityManager->getRepository(Utilisateur::class)->find($userId);

    $imageFile = $request->files->get('image_file');

    // Handle the case when no image file is uploaded
    if (!$imageFile) {
        // Return an error response or redirect back to the settings page with an error message
        return $this->redirectToRoute('user_settings');
    }

    // Generate a unique filename for the uploaded file
    $newFilename = uniqid().'.'.$imageFile->getClientOriginalExtension();

    // Move the uploaded file to a location on the server
    $imageFile->move(
        $this->getParameter('images_directory'), // the target directory defined in services.yaml
        $newFilename
    );

    // Update the user's profile with the new image filename
    $user->setImage($newFilename);

    // Persist the changes to the database
    $entityManager->flush();

    // Update the user object stored in the session with the new image filename
    $session->set('user', $user);

    return $this->redirectToRoute('user_settings');
}

/**
 * @Route("/update-password", name="user_update_password", methods={"POST"})
 */
public function updateUserPassword(Request $request, SessionInterface $session)
{
    $userId = $session->get('user');
    $entityManager = $this->getDoctrine()->getManager();
    $user = $entityManager->getRepository(Utilisateur::class)->find($userId);

    $oldPassword = $request->request->get('old_password');
    $newPassword = $request->request->get('new_password');
    $confirmNewPassword = $request->request->get('confirm_new_password');

    if ($this->encryptPassword($oldPassword) !== $user->getPasswd()) {
        // Handle incorrect old password
    }

    if ($newPassword !== $confirmNewPassword) {
        // Handle password confirmation mismatch
    }

    // Update the user's password
    $user->setPasswd($this->encryptPassword($newPassword));
    
    // Persist the changes to the database
    $entityManager->flush();

    // Update the user entity object stored in the session with the new password
    $session->set('user', $user);

    return $this->redirectToRoute('user_settings');
}

     
private function encryptPassword(string $password): string
{
    return hash('sha256', $password);
}
/**
 * @Route("/logout", name="user_logout")
 */
public function logout(SessionInterface $session)
{
    // Remove all session data
    $session->clear();

    return $this->redirectToRoute('app_home'); // Redirect to home page after logout
}
/**
 * @Route("/enable2fa", name="user_enable_2fa", methods={"GET"})
 */
public function enable2fa(SessionInterface $session, QrcodeService $qrcodeService)
{
    $userId = $session->get('user');
    $entityManager = $this->getDoctrine()->getManager();
    $user = $entityManager->getRepository(Utilisateur::class)->find($userId);

    // Generate a secret key for the user
    $google2fa = new Google2FA();
    $secret = $google2fa->generateSecretKey();

    // Generate a QR code for the secret key
    $google2faQr = new Google2FAQRCode();
    $qrCodeUrl = $google2faQr->getQRCodeUrl(
        'Majesty Website', // Name of your application
        $user->getEmail(),
        $secret
    );
    $qrCodeData = $qrcodeService->qrcode($qrCodeUrl);

    // Store the secret key and set 2FA enabled in the user's record
    $user->setfaSecretKey($secret);
    $user->setIs2faEnabled(true);
    $entityManager = $this->getDoctrine()->getManager();
    $entityManager->persist($user);
    $entityManager->flush();
    
    
    // Render the 2FA setup page with the QR code
    return $this->render('custom/2fa_setup.html.twig', [
        
        'qr_code_data' => $qrCodeData,
        
    ]);
}

}
