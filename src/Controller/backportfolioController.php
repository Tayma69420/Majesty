<?php

namespace App\Controller;

use App\Entity\Portfolio;
use App\Form\PortfolioType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Mime\Email;
use Symfony\Component\Mailer\Mailer;
use Symfony\Component\Mailer\Bridge\Google\Transport\GmailSmtpTransport;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Component\HttpFoundation\JsonResponse;

#[Route('/backportfolio')]
class backportfolioController extends AbstractController
{
    
    #[Route('', name: 'bapp_portfolio_index', methods: ['GET'])]
    public function index(EntityManagerInterface $entityManager,PaginatorInterface $paginator,Request $request): Response
    {
          
        $portfolios = $entityManager
            ->getRepository(Portfolio::class)
            ->findAll();
            $portfolios = $paginator->paginate(
                $portfolios, /* query NOT result */
                $request->query->getInt('page', 1),
                2
            );

            $searchQuery = $request->query->get('search');

     $queryBuilder = $this->getDoctrine()->getRepository(Portfolio::class)->createQueryBuilder('p');

     if ($searchQuery) {
         $queryBuilder->andWhere('p.description LIKE :searchQuery')
             ->setParameter('searchQuery', '%' . $searchQuery . '%');
     }

     $query = $queryBuilder->getQuery();

     $portfolios = $paginator->paginate($query, $request->query->getInt('page', 1), 10);

     return $this->render('backportfolio/index.html.twig', [
         'portfolios' => $portfolios,
         'searchQuery' => $searchQuery
     ]);

        return $this->render('backportfolio/index.html.twig', [
            'portfolios' => $portfolios,
        ]);
    }

   
    
    #[Route('/{idportfolio}', name: 'bapp_portfolio_show', methods: ['GET'])]
    public function show(Portfolio $portfolio): Response
    {
        
        return $this->render('backportfolio/show.html.twig', [
            'portfolio' => $portfolio,
        ]);
    }

    #[Route('/{idportfolio}/edit', name: 'bapp_portfolio_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Portfolio $portfolio, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(PortfolioType::class, $portfolio);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $image = $request->files->get("image");
    
            if ($image) {
                $image_name = $image->getClientOriginalName();
                $image->move($this->getParameter("images_directory"), $image_name);
                $portfolio->setImage($image_name);
            }
    
            $entityManager->flush();
    
            return $this->redirectToRoute('app_portfolio_index', [], Response::HTTP_SEE_OTHER);
        }
    
        return $this->renderForm('backportfolio/edit.html.twig', [
            'portfolio' => $portfolio,
            'form' => $form,
        ]);
    }
    
    

    #[Route('/{idportfolio}', name: 'bapp_portfolio_delete', methods: ['POST'])]
    public function delete(Request $request, Portfolio $portfolio, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$portfolio->getIdportfolio(), $request->request->get('_token'))) {
            $entityManager->remove($portfolio);
            $entityManager->flush();
        }

        return $this->redirectToRoute('bapp_portfolio_index', [], Response::HTTP_SEE_OTHER);
    }
    

}
