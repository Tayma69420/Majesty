<?php

namespace App\Controller;

use App\Entity\Avis;
use App\Form\Avis1Type;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Repository\AvisRepository;
use Dompdf\Dompdf;
use Knp\Component\Pager\PaginatorInterface;

#[Route('/avis')]
class AvisController extends AbstractController
{
    #[Route('', name: 'app_avis_index', methods: ['GET'])]
    public function index(EntityManagerInterface $entityManager,PaginatorInterface $paginator,Request $request): Response
    {
        // Get all the Avis objects from the database using the entity manager
        $avis = $entityManager
            ->getRepository(Avis::class)
            ->findAll();
            $avis = $paginator->paginate(
                $avis, /* query NOT result */
                $request->query->getInt('page', 1),
                2
            );

            
        // Render the index.html.twig template and pass the Avis objects as a variable
        return $this->render('avis/index.html.twig', [
            'avis' => $avis,
        ]);
    }
 
    function filterDescription(string $commentaire): string {
        $badWords = file('C:\Users\Ahmed\OneDrive\Bureau\badwords.txt', FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
        foreach ($badWords as $word) {
            $replacement = $word[0] . str_repeat('*', strlen($word) - 2) . $word[-1];
            $commentaire = preg_replace('/\b' . preg_quote($word) . '\b/i', $replacement, $commentaire);
        }
        return $commentaire;
    }
    
    #[Route('/new', name: 'app_avis_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        // Create a new Avis object
        $avi = new Avis();
    
        // Create a form based on the Avis1Type form class and the $avi object
        $form = $this->createForm(Avis1Type::class, $avi);
    
        // Handle the form submission
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            // If the form was submitted and is valid, save the Avis object to the database
            $entityManager->persist($avi);
    
            // Filter the comment before saving it to the database
            $commentaire = $form->get('commentaire')->getData();
            $filteredDescription = $this->filterDescription($commentaire);
            $avi->setCommentaire($filteredDescription);
    
            $entityManager->flush();
    
            // Redirect to the index route
            return $this->redirectToRoute('app_avis_index', [], Response::HTTP_SEE_OTHER);
        }
    
        // Render the new.html.twig template and pass the $avi object and form as variables
        return $this->renderForm('avis/new.html.twig', [
            'avi' => $avi,
            'form' => $form,
        ]);
    }
    


    #[Route('/{idavis}', name: 'app_avis_show', methods: ['GET'])]
    public function show(Avis $avi): Response
    {
        // Render the show.html.twig template and pass the $avi object as a variable
        return $this->render('avis/show.html.twig', [
            'avi' => $avi,
        ]);

        
    }

    #[Route('/{idavis}/edit', name: 'app_avis_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Avis $avi, EntityManagerInterface $entityManager): Response
    {
        // Create a form based on the Avis1Type form class and the $avi object
        $form = $this->createForm(Avis1Type::class, $avi);
        
        // Handle the form submission
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // If the form was submitted and is valid, update the Avis object in the database
            $entityManager->flush();

            // Redirect to the index route
            return $this->redirectToRoute('app_avis_index', [], Response::HTTP_SEE_OTHER);
        }

        // Render the edit.html.twig template and pass the $avi object and form as variables
        return $this->renderForm('avis/edit.html.twig', [
            'avi' => $avi,
            'form' => $form,
        ]);
    }

    #[Route('/{idavis}', name: 'app_avis_delete', methods: ['POST'])]
    public function delete(Request $request, Avis $avi, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$avi->getIdavis(), $request->request->get('_token'))) {
            $entityManager->remove($avi);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_avis_index', [], Response::HTTP_SEE_OTHER);
    }



    #[Route('/dashboard/stat', name: 'stat', methods: ['POST','GET'])]
    public function VoitureStatistics( AvisRepository $repo): Response
    {
        $total = $repo->countByLibelle('a') +
            $repo->countByLibelle('bbxb') +
            $repo->countByLibelle('12');

        $BMWCount = $repo->countByLibelle('12');
        $MercedesCount = $repo->countByLibelle('bbxb');
        $AudiCount = $repo->countByLibelle('a');


        $BmwPercentage = round(($BMWCount / $total) * 100);
        $MercedesPercentage = round(($MercedesCount / $total) * 100);
        $AudiPercentage = round(($AudiCount / $total) * 100);
        

        return $this->render('Avis/stat.html.twig', [
            'BMWPercentage' => $BmwPercentage,
            'MercedesPercentage' => $MercedesPercentage,
            'AudiPercentage' => $AudiPercentage,
           

        ]);
    }
    
    


    #[Route('/avis/exportpdf', name: 'exportpdf')]
    public function exportToPdf(AvisRepository $repository): Response
    {
        // Récupérer les données de réservation depuis votre base de données
        $Avis = $repository->findAll();

        // Créer le tableau de données pour le PDF
        $tableData = [];
        foreach ($Avis as  $Avis) {
            $tableData[] = [
                'id' => $Avis->getIdavis(),
                'commentaire' => $Avis->getCommentaire(),
            ];
        }

        // Créer le PDF avec Dompdf
        $dompdf = new Dompdf();
        $html = $this->renderView('Avis/pdf.html.twig', [
            'tableData' => $tableData,
        ]);
        $dompdf->loadHtml($html);
        $dompdf->setPaper('A4', 'landscape');
        $dompdf->render();

        // Envoyer le PDF au navigateur
        $response = new Response($dompdf->output(), 200, [
            'Content-Type' => 'application/pdf',
            'Content-Disposition' => 'attachment; filename="Avis.pdf"',
        ]);
        return $response;
    }
    
}
