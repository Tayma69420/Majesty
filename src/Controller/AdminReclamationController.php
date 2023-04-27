<?php

namespace App\Controller;
use App\Repository\ReclamationRepository;
use App\Entity\Reclamation;
use App\Form\ReclamationType;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * @Route("/admin/reclamation")
 */

class AdminReclamationController extends AbstractController
{
    /**
     * @Route("/", name="app_admin_reclamation_index")
     */
    public function index(ReclamationRepository $reclamationRepository): Response
    {
        $reclamations = $reclamationRepository->findAll();
    
        return $this->render('admin/admin_reclamation/index.html.twig', [
            'reclamations' => $reclamations,
        ]);
    }

    /**
     * @Route("/{idReclamation}", name="app_admin_reclamation_show", methods={"GET"})
     */
    public function show(Reclamation $reclamation): Response
    {
        return $this->render('admin/admin_reclamation/show.html.twig', [
            'reclamation' => $reclamation,
        ]);
    }
}
