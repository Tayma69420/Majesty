<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\IsGranted;
use Symfony\Component\HttpFoundation\Session\SessionInterface ;
use App\Entity\Utilisateur;

class UserController extends AbstractController
{
    /**
     * @Route("/admin_page", name="admin_page")
     */
    public function userPage()
    {
        return $this->render('custom/front.html.twig');
    }

    /**
     * @Route("/settings", name="user_settings")
     */
    public function userSettings()
    {
        return $this->render('custom/settingsuser.html.twig');
   
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

}