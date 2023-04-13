<?php

namespace App\Controller;

use App\Entity\Avis;
use App\Form\Avis1Type;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/avis')]
class AvisController extends AbstractController
{
    #[Route('/', name: 'app_avis_index', methods: ['GET'])]
    public function index(EntityManagerInterface $entityManager): Response
    {
        // Get all the Avis objects from the database using the entity manager
        $avis = $entityManager
            ->getRepository(Avis::class)
            ->findAll();

        // Render the index.html.twig template and pass the Avis objects as a variable
        return $this->render('avis/index.html.twig', [
            'avis' => $avis,
        ]);
    }

    #[Route('/new', name: 'app_avis_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        // Create a new Avis object
        $avi = new Avis();
        
        // Create a form based on the Avis1Type form class and the $avi object
        $form = $this->createForm(Avis1Type::class, $avi);
        
        // Handle the form submission
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // If the form was submitted and is valid, save the Avis object to the database
            $entityManager->persist($avi);
            $entityManager->flush();
            
            // Redirect to the index route
            return $this->redirectToRoute('app_avis_index', [], Response::HTTP_SEE_OTHER);
        }

        // Render the new.html.twig template and pass the $avi object and form as variables
        return $this->renderForm('avis/new.html.twig', [
            'avi' => $avi,
            'form' => $form,
        ]);
    }

    #[Route('/{idavis}', name: 'app_avis_show', methods: ['GET'])]
    public function show(Avis $avi): Response
    {
        // Render the show.html.twig template and pass the $avi object as a variable
        return $this->render('avis/show.html.twig', [
            'avi' => $avi,
        ]);
    }

    #[Route('/{idavis}/edit', name: 'app_avis_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Avis $avi, EntityManagerInterface $entityManager): Response
    {
        // Create a form based on the Avis1Type form class and the $avi object
        $form = $this->createForm(Avis1Type::class, $avi);
        
        // Handle the form submission
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // If the form was submitted and is valid, update the Avis object in the database
            $entityManager->flush();

            // Redirect to the index route
            return $this->redirectToRoute('app_avis_index', [], Response::HTTP_SEE_OTHER);
        }

        // Render the edit.html.twig template and pass the $avi object and form as variables
        return $this->renderForm('avis/edit.html.twig', [
            'avi' => $avi,
            'form' => $form,
        ]);
    }

    #[Route('/{idavis}', name: 'app_avis_delete', methods: ['POST'])]
    public function delete(Request $request, Avis $avi, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$avi->getIdavis(), $request->request->get('_token'))) {
            $entityManager->remove($avi);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_avis_index', [], Response::HTTP_SEE_OTHER);
    }

    
    
}
