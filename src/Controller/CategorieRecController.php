<?php

namespace App\Controller;
use App\Repository\CategorieRecRepository;
use App\Entity\CategorieRec;
use App\Entity\Utilisateur;
use App\Form\CategorieRecType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * @Route("/categorie/rec")
 */
class CategorieRecController extends AbstractController
{
    /**
     * @Route("/", name="app_categorie_rec_index", methods={"GET"})
     */
    public function index(EntityManagerInterface $entityManager): Response
    {
        $categorieRecs = $entityManager
            ->getRepository(CategorieRec::class)
            ->findAll();

        return $this->render('categorie_rec/index.html.twig', [
            'categorie_recs' => $categorieRecs,
        ]);
    }

    /**
     * @Route("/new", name="app_categorie_rec_new", methods={"GET", "POST"})
     */
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $categorieRec = new CategorieRec();
        $form = $this->createForm(CategorieRecType::class, $categorieRec);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($categorieRec);
            $entityManager->flush();

            return $this->redirectToRoute('app_categorie_rec_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('categorie_rec/new.html.twig', [
            'categorie_rec' => $categorieRec,
            'form' => $form,
        ]);
    }

    /**
     * @Route("/{id}", name="app_categorie_rec_show", methods={"GET"})
     */
    public function show(CategorieRec $categorieRec): Response
    {
        return $this->render('categorie_rec/show.html.twig', [
            'categorie_rec' => $categorieRec,
        ]);
    }

    /**
     * @Route("/{id}/edit", name="app_categorie_rec_edit", methods={"GET", "POST"})
     */
    public function edit(Request $request, CategorieRec $categorieRec, CategorieRecRepository $catrecrepo): Response
    {
        $form = $this->createForm(CategorieRecType::class, $categorieRec);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $catrecrepo->add($categorieRec, true);

            return $this->redirectToRoute('app_categorie_rec_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('categorie_rec/edit.html.twig', [
            'categorie_rec' => $categorieRec,
            'form' => $form,
        ]);
    }

    /**
     * @Route("/{id}", name="app_categorie_rec_delete", methods={"POST"})
     */
    public function delete(Request $request, CategorieRec $categorieRec, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$categorieRec->getId(), $request->request->get('_token'))) {
            $entityManager->remove($categorieRec);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_categorie_rec_index', [], Response::HTTP_SEE_OTHER);
    }
}
