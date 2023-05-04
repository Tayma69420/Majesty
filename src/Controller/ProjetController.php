<?php

namespace App\Controller;

use App\Entity\Categorie;
use App\Entity\Projet;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\RangeType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Serializer\SerializerInterface;
use Symfony\Component\HttpFoundation\JsonResponse;
use Swift_SmtpTransport;
use Swift_Mailer;
use Swift_Message;
use App\Entity\Utilisateur;
use Symfony\Component\Form\Extension\Core\Type\TextType;



class ProjetController extends AbstractController
{
    #[Route('/projet', name: 'app_projet')]
    public function index(Request $request): Response
    {
        // Get all projects from the database
        $projetRepository = $this->getDoctrine()->getRepository(Projet::class);
        $projets = $projetRepository->findAll();

        // Render the Twig template and pass the projects to it
        return $this->render('projet/index.html.twig', [
            'projets' => $projets,
        ]);
    }


    #[Route('/projet-admin', name: 'app_projet_admin')]
    public function indexadmin(Request $request): Response
    {
        // Get all projects from the database
        $projetRepository = $this->getDoctrine()->getRepository(Projet::class);
        $projets = $projetRepository->findAll();

        // Render the Twig template and pass the projects to it
        return $this->render('projet/backprojet.html.twig', [
            'projets' => $projets,
        ]);
    }
    #[Route('/projet-new', name: 'app_projet_new')]
    public function new(Request $request,\Swift_Mailer $mailer): Response
    {
        $transport = (new Swift_SmtpTransport('smtp.gmail.com', 587, 'tls'))
        ->setUsername('pidevmajesty@gmail.com')
        ->setPassword('xfbyslhggajvfdjz');
    $mailer = new Swift_Mailer($transport);

        // Create a new project entity
        $projet = new Projet();

        // Get all categories from the database
        $categorieRepository = $this->getDoctrine()->getRepository(Categorie::class);
        $categories = $categorieRepository->findAll();


      // Create a form for the project with a dropdown field for categories
      $form = $this->createFormBuilder($projet)
        ->add('titreprojet', TextType::class, [
            'label' => 'Titre du projet:'
        ])
        ->add('prixprojet', RangeType::class, [
            'attr' => [
                'min' => 100,
                'max' => 800,
                'step' => 10,
            ],
            'label' => 'Prix du projet:'
        ])
        ->add('imageproj', FileType::class, [
            'label' => 'Image:',
            'mapped' => false,
            'required' => false,
        ])
        ->add('type', ChoiceType::class, [
            'choices' => array_combine(
                array_map(fn(Categorie $c) => $c->getNomcat(), $categories),
                array_map(fn(Categorie $c) => $c->getIdcat(), $categories)
            ),
            'label' => 'Type de projet:'
        ])
        ->getForm();

  
  $form->handleRequest($request);
  
  if ($form->isSubmitted() && $form->isValid()) {
      // Get the selected category id
      $selectedCategoryId = $form->get('type')->getData();
  
      // Get the category entity by its id
      $selectedCategory = $categorieRepository->find($selectedCategoryId);
  
      // Set the project's type to the selected category
      $projet->setType($selectedCategory->getNomcat());
 // handle image upload
 $image = $form['imageproj']->getData();
 $imagePath = $request->request->get('imagePath');

 if ($image && is_uploaded_file($image)) {
     $imageFileName = uniqid().'.'.$image->guessExtension();
     move_uploaded_file($image, $this->getParameter('images_directory').'/'.$imageFileName);
     $projet->setImageproj($imageFileName);
 }
  
      // Save the project to the database
      $entityManager = $this->getDoctrine()->getManager();
      $entityManager->persist($projet);
      $entityManager->flush();
   // Save the project to the database
   $entityManager = $this->getDoctrine()->getManager();
   $entityManager->persist($projet);
   $entityManager->flush();

   // Send an email to all users with idrole3
   $userRepository = $this->getDoctrine()->getRepository(Utilisateur::class);
   $users = $userRepository->findBy(['idRole' => 3]);

   // Create the message
   $message = (new Swift_Message('New project added'))
       ->setFrom('your-email@example.com')
       ->setTo(array_map(fn(Utilisateur $u) => $u->getEmail(), $users))
       ->setBody(
           $this->renderView(
               'emails/new_project_added.html.twig',
               ['projet' => $projet]
           ),
           'text/html'
       );

   // Send the message
   $mailer->send($message);


      // Redirect back to the index page
      return $this->redirectToRoute('app_projet');
  }
  

        // Render the form template
        return $this->render('projet/create.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/projet-{id}-edit', name: 'app_projet_edit')]
    public function edit(Request $request, Projet $projet): Response
    {
        // Get all categories from the database
        $categorieRepository = $this->getDoctrine()->getRepository(Categorie::class);
        $categories = $categorieRepository->findAll();
    
        // Create a form for the project with a dropdown field for categories
        $form = $this->createFormBuilder($projet)
            ->add('titreprojet')
            ->add('prixprojet')
            ->add('imageproj', FileType::class, [
                'label' => 'Image',
                'mapped' => false,
                'required' => false,
            ])
            ->add('type', ChoiceType::class, [
                'choices' => array_combine(
                    array_map(fn(Categorie $c) => $c->getNomcat(), $categories),
                    array_map(fn(Categorie $c) => $c->getIdcat(), $categories)
                ),
            ])
            ->getForm();
    
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            // Get the selected category id
            $selectedCategoryId = $form->get('type')->getData();
    
            // Get the category entity by its id
            $selectedCategory = $categorieRepository->find($selectedCategoryId);
    
            // Set the project's type to the selected category
            $projet->setType($selectedCategory->getNomcat());
    
           // handle image upload
        $image = $form['imageproj']->getData();
        $imagePath = $request->request->get('imagePath');

        if ($image && is_uploaded_file($image)) {
            $imageFileName = uniqid().'.'.$image->guessExtension();
            move_uploaded_file($image, $this->getParameter('images_directory').'/'.$imageFileName);
            $user->setImage($imageFileName);
        }
            // Save the project to the database
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->flush();
    
            // Redirect back to the index page
            return $this->redirectToRoute('app_projet_admin');
        }
    
        // Render the form template
        return $this->render('projet/edit.html.twig', [
            'form' => $form->createView(),
        ]);
    }
    #[Route('/projet-{id}-delete', name: 'app_projet_delete')]
public function delete(Request $request, Projet $projet): Response
{
    $entityManager = $this->getDoctrine()->getManager();
    $entityManager->remove($projet);
    $entityManager->flush();

    $this->addFlash('success', 'Projet supprimé avec succès.');

    return $this->redirectToRoute('app_projet_admin');

}
#[Route('/searchpro', name:'searchSponsor')]
    public function search(Request $request, SerializerInterface $serializer)
    {
        $entityManager = $this->getDoctrine()->getManager();
        $query = $request->query->get('query');
        $users =  $entityManager->getRepository(Projet::class)->createQueryBuilder('projet')
            ->where('projet.titreprojet LIKE :query')
            ->setParameter('query', '%' . $query . '%')
            ->getQuery()
            ->getResult();
        $json = $serializer->serialize($users, 'json');
        return new JsonResponse($json);
    }
}