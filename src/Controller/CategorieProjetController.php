<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Entity\Categorie;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;

class CategorieProjetController extends AbstractController
{
    #[Route('/categorie-projet', name: 'app_categorie_projet')]
    public function index(Request $request): Response
    { $categorie = new Categorie();
    
        // create the form
        $form = $this->createFormBuilder($categorie)
            ->add('nomcat', TextType::class)
            ->add('save', SubmitType::class, ['label' => 'Create Category'])
            ->getForm();
    
        // handle the form submission
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($categorie);
            $entityManager->flush();
            
            // redirect to the index page, or wherever you want to go after creating a new category
            return $this->redirectToRoute('app_categorie_projet');
        }
        
        // render the form template
        return $this->render('categorie_projet/index.html.twig', [
            'form' => $form->createView(),
        ]);
    }
}
