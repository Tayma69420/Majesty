<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;
use Symfony\Component\Serializer\SerializerInterface;
use App\Entity\Panier;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Component\HttpFoundation\Request;
use App\Entity\Projet;



class PaniermobileController extends AbstractController
{
    #[Route('/paniermobile', name: 'app_paniermobile')]
    public function index(): Response
    {
        return $this->render('paniermobile/index.html.twig', [
            'controller_name' => 'PaniermobileController',
        ]);
    }

//affichage
#[Route("/paniermobile/list", name: "listpanier")]
public function getPanier(SerializerInterface $serializer)
    { 
        $panierRepository = $this->getDoctrine()->getRepository(Panier::class);
        $paniers = $panierRepository->findAll();
        $json = $serializer->serialize($paniers, 'json', ['groups' => "paniers"]);
        return new Response($json);
    }

    
//affichage
#[Route("/projetmobile/list", name: "listprojet")]
public function getProjet(SerializerInterface $serializer)
    { 
        $projetRepository = $this->getDoctrine()->getRepository(Projet::class);
        $projets = $projetRepository->findAll();
        $json = $serializer->serialize($projets, 'json', ['groups' => "projets"]);
        return new Response($json);
    }

    //ajouter
    #[Route("/addPanierJSON/new", name: "addPanierJSON")]
    public function addPanierJSON(ManagerRegistry $doctrine, Request $req,NormalizerInterface $Normalizer)
    {

        $em = $this->getDoctrine()->getManager();
        $panier = new Panier();
        $panier->setnom($req->get('nom'));
        $panier->setprix($req->get('prix'));
        $panier->setqnt($req->get('qnt'));
        $panier->setidprojet($req->get('idprojet'));
       
        $em->persist($panier);
        $em->flush();

        $jsonContent = $Normalizer->normalize($panier, 'json', ['groups' => 'paniers']);
        return new Response(json_encode($jsonContent));
    }

//supprimer    

    #[Route("/deletePanierJSON/{idpanier}", name: "deletePanierJSON")]
    public function deletePanierJSON(ManagerRegistry $doctrine, Request $req, $idpanier, NormalizerInterface $Normalizer)
    {

        $em = $doctrine->getManager();
        $panier = $em->getRepository(Panier::class)->find($idpanier);
        $em->remove($panier);
        $em->flush();
        $jsonContent = $Normalizer->normalize($panier, 'json', ['groups' => 'paniers']);
        return new Response("panier deleted successfully " . json_encode($jsonContent));
    }

    #[Route("/incrementQntJSON/{idpanier}", name: "incrementQntJSON")]
    public function incrementQntJSON(ManagerRegistry $doctrine, Request $req, $idpanier, NormalizerInterface $Normalizer)
    {
        $em = $doctrine->getManager();
        $panier = $em->getRepository(Panier::class)->find($idpanier);
        $qnt = $panier->getqnt();
        $panier->setqnt($qnt + 1);
        $em->flush();
        $jsonContent = $Normalizer->normalize($panier, 'json', ['groups' => 'paniers']);
        return new Response(json_encode($jsonContent));
    }
    
    #[Route("/decrementQntJSON/{idpanier}", name: "decrementQntJSON")]
    public function decrementQntJSON(ManagerRegistry $doctrine, Request $req, $idpanier, NormalizerInterface $Normalizer)
    {
        $em = $doctrine->getManager();
        $panier = $em->getRepository(Panier::class)->find($idpanier);
        $qnt = $panier->getqnt();
        if ($qnt > 1) {
            $panier->setqnt($qnt - 1);
            $em->flush();
            $jsonContent = $Normalizer->normalize($panier, 'json', ['groups' => 'paniers']);
            return new Response(json_encode($jsonContent));
        } else {
            $em->remove($panier);
            $em->flush();
            $jsonContent = $Normalizer->normalize($panier, 'json', ['groups' => 'paniers']);
            return new Response("panier deleted successfully " . json_encode($jsonContent));
        }
    }


#[Route("/sendEmail", name: "sendEmail")]
public function SendEmail(ManagerRegistry $doctrine, Request $request, NormalizerInterface $Normalizer)
{
    $entityManager = $doctrine->getManager();
    
    $email = $request->query->get('prix');
    
    $panier = $this->getDoctrine()->getRepository(Panier::class)->findOneBy(['prix' => $email]);
    
    if ($panier) {
        // Create the SwiftMailer transport
        $transport = (new Swift_SmtpTransport('smtp.gmail.com', 587, 'tls'))
            ->setUsername('pidevmajesty@gmail.com')
            ->setPassword('xfbyslhggajvfdjz');
        
        // Create the SwiftMailer instance
        $mailer = new Swift_Mailer($transport);
        
        

        
        // Create the message to send
        $message = (new \Swift_Message('prix='))
            ->setFrom('noreply@example.com')
            ->setTo($email);


            $message->setBody(
                $this->renderView(
                    
                    
                ),
                'text/html'
            );
        

        // Send the message
        $mailer->send($message);
        
        return new Response("Done"); 
    } else {
        return new Response("No");   
    }
}
    

}
