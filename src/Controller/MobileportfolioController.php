<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;
use Symfony\Component\Serializer\SerializerInterface;
use App\Entity\Portfolio;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Component\HttpFoundation\Request;


class MobileportfolioController extends AbstractController
{
    #[Route('/mobileportfolio', name: 'app_mobileportfolio')]
    public function index(): Response
    {
        return $this->render('mobileportfolio/index.html.twig', [
            'controller_name' => 'MobileportfolioController',
        ]);
    }
  

    //ajouter
    #[Route("/addPortfolioJSON/new", name: "addPortfolioJSON")]
    public function addPortfolioJSON(ManagerRegistry $doctrine, Request $req,NormalizerInterface $Normalizer)
    {

        $em = $this->getDoctrine()->getManager();
        $portfolio = new Portfolio();
        $portfolio->setDescription($req->get('description'));
        $portfolio->setimage($req->get('image'));

        $em->persist($portfolio);
        $em->flush();

        $jsonContent = $Normalizer->normalize($portfolio, 'json', ['groups' => 'portfolios']);
        return new Response(json_encode($jsonContent));
    }

    //affichage
#[Route("/portfoliomobile/list", name: "list")]
public function getPortfolio(SerializerInterface $serializer)
    { 
        $portfolioRepository = $this->getDoctrine()->getRepository(Portfolio::class);
        $portfolios = $portfolioRepository->findAll();
        $json = $serializer->serialize($portfolios, 'json', ['groups' => "portfolios"]);
        return new Response($json);
    }

    //supprimer
    #[Route("/deleteportfolioJSON/{idportfolio}", name: "deleteportfolioJSON")]
    public function deleteportfolioJSON(ManagerRegistry $doctrine, Request $req, $idportfolio, NormalizerInterface $Normalizer)
    {

        $em = $doctrine->getManager();
        $portfolio = $em->getRepository(Portfolio::class)->find($idportfolio);
        $em->remove($portfolio);
        $em->flush();
        $jsonContent = $Normalizer->normalize($portfolio, 'json', ['groups' => 'portfolios']);
        return new Response("portfolio deleted successfully " . json_encode($jsonContent));
    }

    //modifier
    #[Route("/updateuserjson-{id}", name: "updateuser")]
      public function updateuser(ManagerRegistry $doctrine, Request $req, $id, NormalizerInterface $Normalizer)
      {
          $em = $this->getDoctrine()->getManager();

          $portfolio = $em->getRepository(Portfolio::class)->find($id);
      
          if (!$portfolio) {
              return new Response("portfolio not found");
          }
      
          $portfolio->setNom($req->get('description'));
          $portfolio->setPrenom($req->get('image'));
      
          $em->persist($portfolio);
          $em->flush();
      
          $jsonContent = $Normalizer->normalize($portfolio, 'json', ['groups' => 'portfolios']);
          return new Response("portfolio updated successfully " . json_encode($jsonContent));
      }
}
