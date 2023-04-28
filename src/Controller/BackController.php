<?php

namespace App\Controller;

use App\Entity\Facture; // Import the Facture entity
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class BackController extends AbstractController
{
    #[Route('/back-b', name: 'app_back')]
    public function index(): Response
    {
        // Get the facture data from the database
        $factureRepository = $this->getDoctrine()->getRepository(Facture::class);
        $factures = $factureRepository->findAll();
    
        return $this->render('back/index.html.twig', [
            'factures' => $factures,
            'test' => 'Hello, World!', // Add this line
        ]);
    }
    
}
