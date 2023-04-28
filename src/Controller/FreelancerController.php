<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\IsGranted;
use Symfony\Component\HttpFoundation\Session\SessionInterface ;
use App\Entity\Utilisateur;

class FreelancerController extends AbstractController
{
    /**
     * @Route("/admin_page", name="admin_page")
     */
    public function freelancerPage()
    {
        return $this->render('custom/freelancerfront.html.twig');
    }

    /**
     * @Route("/settings-fr", name="freelancer_settings")
     */
    public function freelancerSettings()
    {
        {
            $user = $session->get('user');
        
            if ($user->getIs2faEnabled() === false) {
                $message = 'Vous n\'avez pas votre Google Authentication active. Cliquez ici pour l\'activer.';
            } else {
                $message = null;
            }
        
            if ($user->getIs2faEnabled() === true) {
                $message = 'Votre Google Authentication est activée. Cliquez ici pour la désactiver.';
            }
        
            if (isset($_POST['disable2fa'])) {
                // Generate a code and email it to the user
                $code = rand(100000, 999999);
                $message = (new \Swift_Message('Code de désactivation 2FA'))
                    ->setFrom('your_email@example.com')
                    ->setTo($user->getEmail())
                    ->setBody(
                        $this->renderView(
                            'email/disable_2fa.html.twig',
                            ['code' => $code]
                        ),
                        'text/html'
                    );
        
                $mailer->send($message);
        
                // Store the code in the session
                $session->set('2fa_disable_code', $code);
        
                // Redirect to a page to enter the code
                return $this->redirectToRoute('enter_2fa_disable_code');
            }
        
            return $this->render('custom/settingsuser.html.twig', [
                'message' => $message,
            ]);
        }}
/**
 * @Route("/update-image-fr", name="freelancer_update_image", methods={"POST"})
 */
public function updateFreelancerImage(Request $request, SessionInterface $session)
{
    $userId = $session->get('user');
    dump($userId);
    $entityManager = $this->getDoctrine()->getManager();
    $user = $entityManager->getRepository(Utilisateur::class)->find($userId);

    $imageFile = $request->files->get('image_file');

    // Handle the case when no image file is uploaded
    if (!$imageFile) {
        // Return an error response or redirect back to the settings page with an error message
        return $this->redirectToRoute('freelancer_settings');
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

    return $this->redirectToRoute('freelancer_settings');
}

/**
 * @Route("/update-password-fr", name="freelancer_update_password", methods={"POST"})
 */
public function updatefreelancerPassword(Request $request, SessionInterface $session)
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

    return $this->redirectToRoute('freelancer_settings');
}

     
private function encryptPassword(string $password): string
{
    return hash('sha256', $password);
}

}