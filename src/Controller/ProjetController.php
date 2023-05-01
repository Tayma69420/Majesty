<?php

namespace App\Controller;

use App\Entity\Categorie;
use App\Entity\Projet;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;

class ProjetController extends AbstractController
{
    #[Route('/projet', name: 'app_projet')]
    public function index(Request $request): Response
    {
        // Get all projects from the database
        $projetRepository = $this->getDoctrine()->getRepository(Projet::class);
        $projets = $projetRepository->findAll();

        // Render the Twig template and pass the projects to it
        return $this->render('projet/index.html.twig', [
            'projets' => $projets,
        ]);
    }


    #[Route('/projet-admin', name: 'app_projet_admin')]
    public function indexadmin(Request $request): Response
    {
        // Get all projects from the database
        $projetRepository = $this->getDoctrine()->getRepository(Projet::class);
        $projets = $projetRepository->findAll();

        // Render the Twig template and pass the projects to it
        return $this->render('projet/backprojet.html.twig', [
            'projets' => $projets,
        ]);
    }
    #[Route('/projet-new', name: 'app_projet_new')]
    public function new(Request $request): Response
    {
        // Create a new project entity
        $projet = new Projet();

        // Get all categories from the database
        $categorieRepository = $this->getDoctrine()->getRepository(Categorie::class);
        $categories = $categorieRepository->findAll();


      // Create a form for the project with a dropdown field for categories
      $form = $this->createFormBuilder($projet)
      ->add('titreprojet')
      ->add('prixprojet')
      ->add('type', ChoiceType::class, [
          'choices' => array_combine(
              array_map(fn(Categorie $c) => $c->getNomcat(), $categories),
              array_map(fn(Categorie $c) => $c->getIdcat(), $categories)
          ),
      ])
      ->getForm();
  
  $form->handleRequest($request);
  
  if ($form->isSubmitted() && $form->isValid()) {
      // Get the selected category id
      $selectedCategoryId = $form->get('type')->getData();
  
      // Get the category entity by its id
      $selectedCategory = $categorieRepository->find($selectedCategoryId);
  
      // Set the project's type to the selected category
      $projet->setType($selectedCategory->getNomcat());

  
      // Save the project to the database
      $entityManager = $this->getDoctrine()->getManager();
      $entityManager->persist($projet);
      $entityManager->flush();
  
      // Redirect back to the index page
      return $this->redirectToRoute('app_projet');
  }
  

        // Render the form template
        return $this->render('projet/create.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/projet-{id}-edit', name: 'app_projet_edit')]
    public function edit(Request $request, Projet $projet): Response
    {
        // Get all categories from the database
        $categorieRepository = $this->getDoctrine()->getRepository(Categorie::class);
        $categories = $categorieRepository->findAll();
    
        // Create a form for the project with a dropdown field for categories
        $form = $this->createFormBuilder($projet)
            ->add('titreprojet')
            ->add('prixprojet')
            ->add('type', ChoiceType::class, [
                'choices' => array_combine(
                    array_map(fn(Categorie $c) => $c->getNomcat(), $categories),
                    array_map(fn(Categorie $c) => $c->getIdcat(), $categories)
                ),
            ])
            ->getForm();
    
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            // Get the selected category id
            $selectedCategoryId = $form->get('type')->getData();
    
            // Get the category entity by its id
            $selectedCategory = $categorieRepository->find($selectedCategoryId);
    
            // Set the project's type to the selected category
            $projet->setType($selectedCategory->getNomcat());
    
            // Save the project to the database
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->flush();
    
            // Redirect back to the index page
            return $this->redirectToRoute('app_projet_admin');
        }
    
        // Render the form template
        return $this->render('projet/edit.html.twig', [
            'form' => $form->createView(),
        ]);
    }
    #[Route('/projet-{id}-delete', name: 'app_projet_delete')]
public function delete(Request $request, Projet $projet): Response
{
    $entityManager = $this->getDoctrine()->getManager();
    $entityManager->remove($projet);
    $entityManager->flush();

    $this->addFlash('success', 'Projet supprimé avec succès.');

    return $this->redirectToRoute('app_projet_admin');

}
}