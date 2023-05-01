<?php

namespace App\Controller;

use App\Entity\Utilisateur;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;
use Symfony\Component\Serializer\SerializerInterface;
use Symfony\Component\Serializer\Annotation\Groups;


class UsermobileController extends AbstractController
{
    #[Route('/usermobile', name: 'app_usermobile')]
    public function index(): Response
    {
        return $this->render('usermobile/index.html.twig', [
            'controller_name' => 'UsermobileController',
        ]);
    }
    #[Route("/user-list", name: "list")]
    public function listUsers( SerializerInterface $serializer)
    {
        $entityManager = $this->getDoctrine()->getManager();
        $utilisateurs = $entityManager->getRepository(Utilisateur::class)->findAll();

        $json = $serializer->serialize($utilisateurs, 'json', ['groups' => "utilisateur"]);
        return new Response($json);
    }
      //ajouter
      #[Route("/user/new", name: "adduser")]
      public function adduser(ManagerRegistry $doctrine, Request $req,NormalizerInterface $Normalizer)
      {
  
        $entityManager = $this->getDoctrine()->getManager();

          $utilisateur = new Utilisateur();
          $utilisateur->setNom($req->get('nom'));
          $utilisateur->setPrenom($req->get('prenom'));
          $utilisateur->setEmail($req->get('email'));
          $utilisateur->setTel($req->get('tel'));
          $utilisateur->setAdresse($req->get('adresse'));
          $utilisateur->setAge($req->get('age'));
          $utilisateur->setPasswd($req->get('passwd'));
          $utilisateur->setIdrole($req->get('id_role'));
          $utilisateur->setSexe($req->get('sexe'));
          $utilisateur->setImage($req->get('image'));
          $entityManager->persist($utilisateur);
          $entityManager->flush();
  
          $jsonContent = $Normalizer->normalize($utilisateur, 'json', ['groups' => 'utilisateurs']);
          return new Response(json_encode($jsonContent));
      }
     
     
      #[Route("/updateuserjson-{id}", name: "updateuser")]
      public function updateuser(ManagerRegistry $doctrine, Request $req, $id, NormalizerInterface $Normalizer)
      {
          $em = $this->getDoctrine()->getManager();

          $utilisateur = $em->getRepository(Utilisateur::class)->find($id);
      
          if (!$utilisateur) {
              return new Response("User not found");
          }
      
          $utilisateur->setNom($req->get('nom'));
          $utilisateur->setPrenom($req->get('prenom'));
          $utilisateur->setEmail($req->get('email'));
          $utilisateur->setTel($req->get('tel'));
          $utilisateur->setAdresse($req->get('adresse'));
          $utilisateur->setAge($req->get('age'));
          $utilisateur->setPasswd($req->get('passwd'));
          $utilisateur->setImage($req->get('image'));
          $utilisateur->setSexe($req->get('sexe'));
      
          $em->persist($utilisateur);
          $em->flush();
      
          $jsonContent = $Normalizer->normalize($utilisateur, 'json', ['groups' => 'utilisateur']);
          return new Response("User updated successfully " . json_encode($jsonContent));
      }
      
  
  
      #[Route("/deleteuserjson-{id}", name: "deleteuserjson")]
      public function deleteuserjson(ManagerRegistry $doctrine, Request $req, $id, NormalizerInterface $Normalizer)
      {
  
          $em = $doctrine->getManager();
          $utilisateur = $em->getRepository(Utilisateur::class)->find($id);
          $em->remove($utilisateur);
          $em->flush();
          $jsonContent = $Normalizer->normalize($utilisateur, 'json', ['groups' => 'utilisateur']);
          return new Response("User deleted successfully " . json_encode($jsonContent));
      }
}
