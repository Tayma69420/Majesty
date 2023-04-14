<?php

namespace App\Controller;

use App\Entity\Facture;
use App\Entity\Panier;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;

class FactureController extends AbstractController
{
   
    #[Route('/facture', name: 'app_facture_add')]
    public function app_add_facture(Request $request): Response
    {
        $entityManager = $this->getDoctrine()->getManager();
        $cartItems = $entityManager->getRepository(Panier::class)->findAll();
        
        $total = 0;
        foreach ($cartItems as $item) {
            $subtotal = $item->getQnt() * $item->getPrix();
            $total += $subtotal;
        }
        // Create a new Facture entity
        $facture = new Facture();

        // Create the form builder
        $form = $this->createFormBuilder($facture)
            ->add('cardnumber') // Add fields based on your database schema
            ->add('expirationdate')
            ->add('securitycode',PasswordType::class)
            ->add('firstname')
            ->add('lastname')
            ->getForm();

        // Handle form submission
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Set the total from the "panier" table
$entityManager = $this->getDoctrine()->getManager();
$cartItems = $entityManager->getRepository(Panier::class)->findAll();

$total = 0;
foreach ($cartItems as $item) {
    $subtotal = $item->getQnt() * $item->getPrix();
    $total += $subtotal;
   
}


            $facture->setTotal($total);

            // Persist the Facture entity
            $entityManager->persist($facture);

            // Remove the Panier table
            $connection = $entityManager->getConnection();
            $platform = $connection->getDatabasePlatform();
            $connection->executeStatement($platform->getTruncateTableSQL('panier', true /* whether to cascade */));

            // Flush changes to the database
            $entityManager->flush();

            // Redirect to success page or do whatever you want
            return $this->redirectToRoute('app_projet');
        }
        

        return $this->render('facture/index.html.twig', [
           'form' => $form->createView(),
           'total' => $total,
        ]);
    }
}