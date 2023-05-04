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
use Symfony\Component\HttpFoundation\Session\SessionInterface ;



#[Route('/portfolio')]
class PortfolioController extends AbstractController
{
    
    #[Route('', name: 'app_portfolio_index', methods: ['GET'])]
    public function index(SessionInterface $session, EntityManagerInterface $entityManager, PaginatorInterface $paginator, Request $request): Response
    {
        $user = $session->get('user');
        $queryBuilder = $entityManager->getRepository(Portfolio::class)->createQueryBuilder('p');
        $queryBuilder->where('p.iduser = :iduser')
                    ->setParameter('iduser', $user->getIduser());
    
        $searchQuery = $request->query->get('search');
        if ($searchQuery) {
            $queryBuilder->andWhere('p.description LIKE :searchQuery')
                         ->setParameter('searchQuery', '%' . $searchQuery . '%');
        }
    
        $query = $queryBuilder->getQuery();
        $portfolios = $paginator->paginate($query, $request->query->getInt('page', 1), 10);
    
        return $this->render('portfolio/index.html.twig', [
            'portfolios' => $portfolios,
            'searchQuery' => $searchQuery
        ]);
    }
    

    #[Route('-new', name: 'app_portfolio_new', methods: ['GET', 'POST'])]
    public function new(SessionInterface $session,Request $request, EntityManagerInterface $entityManager): Response
    {
   
        $user = $session->get('user');
        $portfolio = new Portfolio();
        $form = $this->createForm(PortfolioType::class, $portfolio);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            $image = $request->files->get("image");
    
            if ($image) {
                $image_name = $image->getClientOriginalName();
                $image->move($this->getParameter("images_directory"), $image_name);
                $portfolio->setImage($image_name);
            }
            $email = (new Email())
                ->from('symfonycopte822@gmail.com')
                ->To($user->getEmail())
                ->subject('Car Rental Reservation Confirmation')
                ->text('portfolio aded');
            $transport = new GmailSmtpTransport('pidevmajesty@gmail.com', 'xfbyslhggajvfdjz');
            $mailer = new Mailer($transport);
            $mailer->send($email);
          
            $portfolio->setIduser($user->getIduser());
            $entityManager->persist($portfolio);
            $entityManager->flush();
    
            return $this->redirectToRoute('app_portfolio_index', [], Response::HTTP_SEE_OTHER);
        }
    
        return $this->renderForm('portfolio/new.html.twig', [
            'portfolio' => $portfolio,
            'form' => $form,
        ]);
    }
    
    #[Route('-{idportfolio}', name: 'app_portfolio_show', methods: ['GET'])]
    public function show(SessionInterface $session,Portfolio $portfolio): Response
    {
        $user = $session->get('user');
        return $this->render('portfolio/show.html.twig', [
            'portfolio' => $portfolio,
        ]);
    }

    #[Route('-{idportfolio}-edit', name: 'app_portfolio_edit', methods: ['GET', 'POST'])]
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
    
        return $this->renderForm('portfolio/edit.html.twig', [
            'portfolio' => $portfolio,
            'form' => $form,
        ]);
    }
    
    

    #[Route('-{idportfolio}', name: 'app_portfolio_delete', methods: ['POST'])]
    public function delete(Request $request, Portfolio $portfolio, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$portfolio->getIdportfolio(), $request->request->get('_token'))) {
            $entityManager->remove($portfolio);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_portfolio_index', [], Response::HTTP_SEE_OTHER);
    }
    
    public function updateVotes(Request $request): JsonResponse
    {
        $entityManager = $this->getDoctrine()->getManager();
        $id = $request->request->get('id');
        $type = $request->request->get('type');
        $portfolio = $entityManager->getRepository(Portfolio::class)->find($id);
        if (!$portfolio) {
            throw $this->createNotFoundException('Portfolio not found');
        }
        if ($type === 'like') {
            $portfolio->setLikes($portfolio->getLikes() + 1);
        } elseif ($type === 'dislike') {
            $portfolio->setDislikes($portfolio->getDislikes() + 1);
        }
        $entityManager->flush();
        return new JsonResponse('success');
    }

    
    public function update_portfolio(Request $request): JsonResponse
    {
        $id = $request->request->get('idportfolio'); // replace with the ID of the entity you want to update
        $rating = $request->request->get('rating');

        $entityManager = $this->getDoctrine()->getManager();
        $entity = $entityManager->getRepository(Portfolio::class)->find($id);

        if (!$entity) {
            throw $this->createNotFoundException('No entity found for idportfolio ' . $id);
        }

        $entity->setRating($rating);
        $entityManager->flush();

        return new JsonResponse(['success' => true]);
    }

}
