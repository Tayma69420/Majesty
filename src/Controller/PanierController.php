<?php

namespace App\Controller;

use App\Entity\Panier;
use App\Entity\Projet;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\ORM\EntityManagerInterface; 
use Symfony\Component\HttpFoundation\Session\SessionInterface ;
class PanierController extends AbstractController
{
    #[Route('/panier', name: 'app_panier_index')]
    public function index(SessionInterface $session): Response
    {
        // Get the cart items from the database
        $panierRepository = $this->getDoctrine()->getRepository(Panier::class);
        $cartItems = $panierRepository->findAll();
    
        $total = 0;
        $panierCount = 0;
        foreach ($cartItems as $item) {
            $subtotal = $item->getQnt() * $item->getPrix();
            $total += $subtotal;
            $panierCount += $item->getQnt();
        }
    
        // Store the panierCount variable in the session
        $session->set('panierCount', $panierCount);
    
        return $this->render('panier/index.html.twig', [
            'cartItems' => $cartItems,
            'total' => $total,
            'panierCount' => $panierCount, // Add the count to the template variables
        ]);
    }
    
    
    
    #[Route('/panier/ajouter/{id}', name: 'app_panier_ajouter')]
    public function ajouter(Request $request, int $id): Response
    {
        // Get the project from the database
        $projetRepository = $this->getDoctrine()->getRepository(Projet::class);
        $projet = $projetRepository->find($id);
    
        // Get the cart items from the database
        $panierRepository = $this->getDoctrine()->getRepository(Panier::class);
        $cartItems = $panierRepository->findBy(['idprojet' => $projet->getIdprojet()]);
    
        // If there is already an item in the cart with the same idprojet, update its quantity
        if ($cartItems) {
            $cartItem = $cartItems[0];
            $cartItem->setQnt($cartItem->getQnt() + 1);
        } else {
            // Create a new item for the cart
            $cartItem = new Panier();
            $cartItem->setNom($projet->getTitreprojet());
            $cartItem->setPrix($projet->getPrixprojet());
            $cartItem->setQnt(1);
            $cartItem->setIdprojet($projet->getIdprojet());
        }
    
        // Add the item to the database
        $entityManager = $this->getDoctrine()->getManager();
        $entityManager->persist($cartItem);
        $entityManager->flush();
    
        // Redirect back to the project list
        return $this->redirectToRoute('app_projet');
    }
    
/**
 * @Route("/panier/incrementer/{id}", name="app_panier_incrementer")
 */
public function incrementerPanier(EntityManagerInterface $entityManager, Projet $projet, int $id): Response

{
    // Get the cart item from the database
    $panierRepository = $this->getDoctrine()->getRepository(Panier::class);
    $projet = $panierRepository->findOneBy(['idprojet' => $id]);

    if (!$projet) {
        throw $this->createNotFoundException('No project found for id '.$projet->getIdprojet());
    }

    $projet->setQnt($projet->getQnt() + 1);
    $entityManager->flush();

    return $this->redirectToRoute('app_panier_index');
}

/**
 * @Route("/panier/decrementer/{id}", name="app_panier_decrementer")
 */
public function decrementerPanier(EntityManagerInterface $entityManager, Projet $projet, int $id): Response
{
    // Get the cart item from the database
    $panierRepository = $this->getDoctrine()->getRepository(Panier::class);
    $projet = $panierRepository->findOneBy(['idprojet' => $id]);

    if (!$projet) {
        throw $this->createNotFoundException('No project found for id '.$projet->getIdprojet());
    }

    $projet->setQnt($projet->getQnt() - 1);

    if ($projet->getQnt() < 1) {
        $entityManager->remove($projet);
    } else {
        $entityManager->flush();
    }

    return $this->redirectToRoute('app_panier_index');
}
/**
 * @Route("/panier/supprimer/{id}", name="app_panier_supprimer")
 */
public function supprimer(Request $request, int $id): Response
{
    // Get the cart item from the database
    $panierRepository = $this->getDoctrine()->getRepository(Panier::class);
    $cartItem = $panierRepository->findOneBy(['idprojet' => $id]);

    // If the cart item exists, remove it from the database
    if ($cartItem) {
        $entityManager = $this->getDoctrine()->getManager();
        $entityManager->remove($cartItem);
        $entityManager->flush();
    }

    // Redirect back to the cart page
    return $this->redirectToRoute('app_panier_index');
}


}
