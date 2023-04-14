<?php

namespace App\Controller;

use App\Entity\Projet;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

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
}
