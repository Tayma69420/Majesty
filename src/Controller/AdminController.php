<?php

// src/Controller/AdminController.php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Routing\Annotation\Route;
use App\Entity\Utilisateur;
use Symfony\Component\HttpFoundation\Request;

use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextType;


class AdminController extends AbstractController
{
    /**
     * @Route("/admin_dashboard", name="admin_page")

     */
    public function adminPage(Request $request)
    {
        $entityManager = $this->getDoctrine()->getManager();
        $utilisateurs = $entityManager->getRepository(Utilisateur::class)->findAll();

        // Handle delete button action
        if ($request->isMethod('POST')) {
            $idToDelete = $request->request->get('idToDelete');
            $utilisateurToDelete = $entityManager->getRepository(Utilisateur::class)->find($idToDelete);

            if ($utilisateurToDelete) {
                // Confirm delete action with a popup dialog
                return $this->render('custom/admin.html.twig', [
                    'utilisateurs' => $utilisateurs,
                    'utilisateurToDelete' => $utilisateurToDelete,
                ]);
            } else {
                // Handle invalid id
            }
        }

        return $this->render('custom/admin.html.twig', [
            'utilisateurs' => $utilisateurs,
        ]);
    }

/**
 * @Route("/admin_dashboard/delete", name="admin_page_delete")
 */
public function deleteUtilisateur(Request $request)
{
    $entityManager = $this->getDoctrine()->getManager();
    $idToDelete = $request->request->get('idToDelete');
    $utilisateurToDelete = $entityManager->getRepository(Utilisateur::class)->find($idToDelete);

    if ($utilisateurToDelete) {
        // Check if the user is an admin before deleting
        if ($utilisateurToDelete->getIdrole() == 1) {
            $this->addFlash('warning', 'Vous ne pouvez ni modifier ni supprimer un Admin');
            return $this->redirectToRoute('admin_dashboard');
        }

        // Delete the user from the database
        $entityManager->remove($utilisateurToDelete);
        $entityManager->flush();

        // Redirect back to the admin page with a success message
        $this->addFlash('success', 'Utilisateur supprimé avec succès.');
        return $this->redirectToRoute('admin_dashboard');
    } else {
        // Handle invalid id
    }
}

/**
 * @Route("/admin_dashboard/modifier/{id}", name="admin_modifier_utilisateur")
 */
public function modifierUtilisateur(Request $request, $id)
{
    $entityManager = $this->getDoctrine()->getManager();
    $utilisateur = $entityManager->getRepository(Utilisateur::class)->find($id);

    if (!$utilisateur) {
        throw $this->createNotFoundException('Utilisateur non trouvé');
    }

    // Check if the user is an admin before modifying
    if ($utilisateur->getIdrole() == 1) {
        $this->addFlash('warning', 'Vous ne pouvez ni modifier ni supprimer un Admin');
        return $this->redirectToRoute('admin_dashboard');
    }

    // Create the edit form
    $form = $this->createFormBuilder($utilisateur)
        ->add('nom', TextType::class)
        ->add('prenom', TextType::class)
        ->add('email', EmailType::class)
        ->add('tel', TextType::class)
        ->add('adresse', TextType::class)
        ->add('age', DateType::class, [
            'widget' => 'single_text',
            'format' => 'yyyy-MM-dd',
        ])
        ->add('sexe', ChoiceType::class, [
            'choices' => [
                'Homme' => 'Homme',
                'Femme' => 'Femme',
                'Autre' => 'Autre',
            ],
        ])
        ->add('idrole', ChoiceType::class, [
            'choices' => [
               
                'User' => 2,
                'Freelancer' => 3,
            ],
        ])
        ->add('image', FileType::class, [
            'mapped' => false,
            'required' => false,
        ])
        ->add('Enregistrer', SubmitType::class)
        ->getForm();

    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        $utilisateur = $form->getData();

        // Handle file upload
        $imageFile = $form->get('image')->getData();
        if ($imageFile) {
            $fileName = uniqid().'.'.$imageFile->guessExtension();
            $imageFile->move(
                $this->getParameter('images_directory'),
                $fileName
            );
            $utilisateur->setImage($fileName);
        }

        // Save changes to the database
        $entityManager->flush();

        // Redirect back to the admin page with a success message
        $this->addFlash('success', 'Utilisateur modifié avec succès.');
        return $this->redirectToRoute('admin_dashboard');
    }

    return $this->render('custom/modifieradmin.html.twig', [
        'form' => $form->createView(),
        'utilisateur' => $utilisateur,
    ]);
}


}
