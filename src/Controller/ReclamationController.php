<?php

namespace App\Controller;
use App\Repository\ReclamationRepository;
use App\Entity\Reclamation;
use App\Form\ReclamationType;
use App\Entity\Utilisateur;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Validator\Constraints as Assert;
use Knp\Component\Pager\PaginatorInterface;
use Apy\DataGridBundle\Grid\Source\Entity;
use Apy\DataGridBundle\Grid\Action\RowAction;
use Apy\DataGridBundle\Grid\Column\TextColumn;
use Chartjs\ChartJSBundle\ChartJSFactory;
use Symfony\Component\HttpFoundation\Session\SessionInterface ;
/**
 * @Route("/reclamation")
 */
class ReclamationController extends AbstractController
{

    
   /**
 * @Route("", name="app_reclamation_index", methods={"GET"})
 */
public function index(SessionInterface $session,ReclamationRepository $reclamationRepository, Request $request, PaginatorInterface $paginator): Response
{
    $user = $session->get('user');
    $user->getIduser();
    $search = $request->query->get('search');
    $queryBuilder = $reclamationRepository->createQueryBuilder('r');
    if ($search) {
        $queryBuilder->andWhere('r.titre LIKE :search')
            ->setParameter('search', '%' . $search . '%');
    }
    $queryBuilder->andWhere('r.iduser = :user_id') // Add a condition to select only reclamations of the user
        ->setParameter('user_id', $user);

    $query = $queryBuilder->getQuery();
    $reclamations = $paginator->paginate($query, $request->query->getInt('page', 1), 100);

    // Check for bad words and replace them with asterisks
    $badWords = ['bad', 'badword', 'badwordd'];
    foreach ($reclamations as $reclamation) {
        $reclamation->setTitre($this->replaceBadWords($reclamation->getTitre(), $badWords));
        $reclamation->setReclaDesc($this->replaceBadWords($reclamation->getReclaDesc(), $badWords));
    }

    return $this->render('reclamation/index.html.twig', [
        'reclamations' => $reclamations,
        'search' => $search,
    ]);
}


    /**
     * Replace bad words with asterisks
     *
     * @param string $text
     * @param array $badWords
     * @return string
     */
    private function replaceBadWords(string $text, array $badWords): string
    {
        foreach ($badWords as $badWord) {
            $text = preg_replace('/\b' . preg_quote($badWord) . '\b/i', str_repeat('*', strlen($badWord)), $text);
        }
        return $text;
    }
    

    /**
     * @Route("-new", name="app_reclamation_new", methods={"GET", "POST"})
     */
    public function new(SessionInterface $session,Request $request, ReclamationRepository $recrepository): Response
    {
        $user = $session->get('user');
        $utilisateur = $this->getDoctrine()->getRepository(Utilisateur::class)->find($user->getIduser());
        
        $reclamation = new Reclamation();
        $form = $this->createForm(ReclamationType::class, $reclamation);
        $form->handleRequest($request);
        
        if ($form->isSubmitted() && $form->isValid()) {
            $reclamation->setIduser($utilisateur);
            $recrepository->add($reclamation, true);
        
            return $this->redirectToRoute('app_reclamation_index', [], Response::HTTP_SEE_OTHER);
        }
        
        return $this->renderForm('reclamation/new.html.twig', [
            'reclamation' => $reclamation,
            'form' => $form,
        ]);
        
    }

    

    /**
     * @Route("-{idReclamation}", name="app_reclamation_show", methods={"GET"})
     */
    public function show(Reclamation $reclamation): Response
    {
        return $this->render('reclamation/show.html.twig', [
            'reclamation' => $reclamation,
        ]);
    }

    /**
     * @Route(":{idReclamation}-edit", name="app_reclamation_edit", methods={"GET", "POST"})
     */
    public function edit(Request $request, Reclamation $reclamation, ReclamationRepository $recrepository ): Response
    {
        $form = $this->createForm(ReclamationType::class, $reclamation);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $recrepository->add($reclamation, true);

            return $this->redirectToRoute('app_reclamation_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('reclamation/edit.html.twig', [
            'reclamation' => $reclamation,
            'form' => $form,
        ]);
    }

    /**
     * @Route("-{idReclamation}", name="app_reclamation_delete", methods={"POST"})
     */
    public function delete(Request $request, Reclamation $reclamation, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$reclamation->getIdReclamation(), $request->request->get('_token'))) {
            $entityManager->remove($reclamation);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_reclamation_index', [], Response::HTTP_SEE_OTHER);
    }
/**
 * @Route("/chart", name="app_reclamation_chart", methods={"GET"})
 */
public function chart(ReclamationRepository $reclamationRepository): Response
{
    $ratings = [1 => 0, 2 => 0, 3 => 0, 4 => 0, 5 => 0];

    // Count the number of reclamations for each rating
    foreach ($reclamationRepository->findAll() as $reclamation) {
        $rating = $reclamation->getRating();
        if ($rating >= 1 && $rating <= 5) {
            $ratings[$rating]++;
        }
    }

    // Prepare the chart data in the format expected by Symfony/UX-Chartjs
    $labels = array_keys($ratings);
    $data = array_values($ratings);

    $chartData = [
        'type' => 'pie',
        'data' => [
            'labels' => $labels,
            'datasets' => [
                [
                    'label' => 'Reclamation Ratings',
                    'backgroundColor' => '#4e73df',
                    'hoverBackgroundColor' => '#2e59d9',
                    'data' => $data,
                ],
            ],
        ],
        'options' => [
            'scales' => [
                'yAxes' => [
                    [
                        'ticks' => [
                            'beginAtZero' => true,
                        ],
                    ],
                ],
            ],
        ],
    ];

    return $this->json($chartData);
}

}
