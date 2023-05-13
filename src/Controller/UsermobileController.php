<?php

namespace App\Controller;
use App\Entity\Portfolio;
use App\Entity\Reclamation;
use App\Entity\Utilisateur;
use App\Entity\Panier;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;
use Symfony\Component\Serializer\SerializerInterface;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\Serializer\Annotation\Groups;
use Swift_SmtpTransport;
use Swift_Mailer;
use Twilio\Rest\Client;
use Twilio\Jwt\ClientToken;
use Symfony\Component\HttpFoundation\JsonResponse;

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
          $password = $req->get('passwd');
          $encryptedPassword = $this->encryptPassword($password);
          $utilisateur->setPasswd($encryptedPassword);
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
          //$utilisateur->setAge($req->get('age'));
          $password = $req->get('passwd');
          $encryptedPassword = $this->encryptPassword($password);
          $utilisateur->setPasswd($encryptedPassword);
        //  $utilisateur->setImage($req->get('image'));
        //  $utilisateur->setSexe($req->get('sexe'));
      
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

      private function encryptPassword(string $password): string
      {
          return hash('sha256', $password);
      }
    
      
 /**
 * @Route("/sendEmail", name="sendEmail")
 */
public function SendEmail(ManagerRegistry $doctrine, Request $request, NormalizerInterface $Normalizer)
{
    $entityManager = $doctrine->getManager();
    
    $email = $request->query->get('email');
    
    $utilisateur = $this->getDoctrine()->getRepository(Utilisateur::class)->findOneBy(['email' => $email]);
    
    if ($utilisateur) {
        // Create the SwiftMailer transport
        $transport = (new Swift_SmtpTransport('smtp.gmail.com', 587, 'tls'))
            ->setUsername('pidevmajesty@gmail.com')
            ->setPassword('xfbyslhggajvfdjz');
        
        // Create the SwiftMailer instance
        $mailer = new Swift_Mailer($transport);
        
        // Generate a new password and set it for the user
        $newPassword = substr(md5(rand()), 0, 8);
        $utilisateur->setPasswd($this->encryptPassword($newPassword));

        // Update the user in the database
        $entityManager = $this->getDoctrine()->getManager();
        $entityManager->persist($utilisateur);
        $entityManager->flush();

        // Create the message to send
        $message = (new \Swift_Message('Mot de passe oublié'))
            ->setFrom('noreply@example.com')
            ->setTo($email);

        // Add the message body with a design and user's name
        $message->setBody(
            $this->renderView(
                'custom/email_template.html.twig',
                ['nom' => $utilisateur->getNom(), 'prenom' => $utilisateur->getPrenom(), 'new_password' => $newPassword]
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

/**
 * @Route("/send-code", name="send_code")
 */
public function sendCode(Request $request)
{
    $twilioAccountSid = 'ACa3f47735bde23cea35aac8e3b509ac97';
    $twilioAuthToken = '0e1f7131a59883516ba7e4888cc5efbc';
    $twilioPhoneNumber = '+16205088251';
    
    $client = new Client($twilioAccountSid, $twilioAuthToken);

    $code = rand(1000, 9999);
    $phoneNumber = '+216' . $request->get('tel');

    $message = "Your verification code is: " . $code;
    
    try {
        $client->messages->create(
            $phoneNumber,
            array(
                'from' => $twilioPhoneNumber,
                'body' => $message
            )
        );
        return new Response("Code sent successfully");
    } catch (\Exception $e) {
        return new Response("Failed to send code: " . $e->getMessage());
    }
}

/**
 * @Route("/user-json", name="user_json")
 */
public function getUserJson(Request $request, SerializerInterface $serializer): JsonResponse
{

    $email = $request->get('email');
   $mdp = $request->get('passwd');
    // Check if user exists with given email
    
    $entityManager = $this->getDoctrine()->getManager();
    $user = $entityManager->getRepository(Utilisateur::class)->findOneBy(['email' => $email,'passwd' => $this->encryptPassword($mdp)]);

    if (!$user) {
        return new JsonResponse(['error' => 'User not found with given email'], Response::HTTP_NOT_FOUND);
    }

    $json = $serializer->serialize($user, 'json', ['groups' => 'utilisateur']);
    return new JsonResponse($json, Response::HTTP_OK, [], true);
}


//affichage
#[Route("/ReclamationMobile/list", name: "liist")]
public function getReclamation(SerializerInterface $serializer)
{ 
$ReclamationRepository = $this->getDoctrine()->getRepository(Reclamation::class);
$reclamations = $ReclamationRepository->findAll();
$json = $serializer->serialize($reclamations, 'json', ['groups' => "reclamations"]);
return new Response($json);
}

//ajouter
#[Route("/addReclamationJSON/new", name: "addReclamationJSON")]
public function addReclamationJSON(ManagerRegistry $doctrine, Request $req,NormalizerInterface $Normalizer)
{

$em = $this->getDoctrine()->getManager();
$reclamation = new reclamation();
$reclamation->setReclaDesc($req->get('reclaDesc'));
$reclamation->setTitre($req->get('titre'));




$em->persist($reclamation);
$em->flush();

$jsonContent = $Normalizer->normalize($reclamation, 'json', ['groups' => 'reclamations']);
return new Response(json_encode($jsonContent));
}



//supprimer

#[Route("/deleteReclamationJSON/{idreclamation}", name: "deleteReclamationJSON")]
public function deletereclamationJSON(ManagerRegistry $doctrine, Request $req, $idreclamation, NormalizerInterface $Normalizer)
{

$em = $doctrine->getManager();
$reclamation = $em->getRepository(reclamation::class)->find($idreclamation);
$em->remove($reclamation);
$em->flush();
$jsonContent = $Normalizer->normalize($reclamation, 'json', ['groups' => 'reclamations']);
return new Response("reclamation deleted successfully " . json_encode($jsonContent));
}



//modifier

#[Route("/updatereclamationjsonn-{id}", name: "updatereclamation")]
  public function updateuserr(ManagerRegistry $doctrine, Request $req, $id, NormalizerInterface $Normalizer)
  {
      $em = $this->getDoctrine()->getManager();

      $utilisateur = $em->getRepository(Reclamation::class)->find($id);
  
      if (!$reclamation) {
          return new Response("Reclamation not found");
      }
  
      $reclamation->setReclaDesc($req->get('reclaDesc'));
      $reclamation->setTitre($req->get('titre'));
      $em->persist($reclamation);
      $em->flush();
  
      $jsonContent = $Normalizer->normalize($reclamation, 'json', ['groups' => 'reclamation']);
      return new Response("Reclamation updated successfully " . json_encode($jsonContent));
  }


//--------------------------AHMED---------------------------------------------------//
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
#[Route("/portfoliomobile/list", name: "lisst")]
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
 //-----------------------------farouk-------//

 #[Route("/clearDatabasePanierJSON", name: "deleteAllPaniersJSON")]
    public function deleteAllPaniersJSON(ManagerRegistry $doctrine, NormalizerInterface $normalizer)
    {
        $em = $doctrine->getManager();
        $paniers = $em->getRepository(Panier::class)->findAll();

        foreach ($paniers as $panier) {
            $em->remove($panier);
        }

        $em->flush();

        $jsonContent = $normalizer->normalize($paniers, 'json', ['groups' => 'paniers']);
        return new Response(count($paniers) . " paniers deleted successfully " . json_encode($jsonContent));
    }
//-------oussema-----//
    //affichage
#[Route("/projetmobile/list", name: "lissst")]
public function getProjet(SerializerInterface $serializer)
    { 
        $projetRepository = $this->getDoctrine()->getRepository(Projet::class);
        $projets = $projetRepository->findAll();
        $json = $serializer->serialize($projets, 'json', ['groups' => "projets"]);
        return new Response($json);
    }


//ajouter
    #[Route("/addProjetJSON/new", name: "addProjetJSON")]
    public function addProjetJSON(ManagerRegistry $doctrine, Request $req,NormalizerInterface $Normalizer)
    {

        $em = $this->getDoctrine()->getManager();
        $projet = new Projet();
        $projet->settitreprojet($req->get('titreprojet'));
        $projet->setprixprojet($req->get('prixprojet'));
        $projet->settype($req->get('type'));
        $projet->setidprojet($req->get('idprojet'));

        $em->persist($projet);
        $em->flush();

        $jsonContent = $Normalizer->normalize($projet, 'json', ['groups' => 'projets']);
        return new Response(json_encode($jsonContent));
    }

//supprimer

    #[Route("/deleteProjetJSON/{idprojet}", name: "deleteProjetJSON")]
    public function deleteProjetJSON(ManagerRegistry $doctrine, Request $req, $idprojet, NormalizerInterface $Normalizer)
    {

        $em = $doctrine->getManager();
        $panier = $em->getRepository(Projet::class)->find($idprojet);
        $em->remove($projet);
        $em->flush();
        $jsonContent = $Normalizer->normalize($panier, 'json', ['groups' => 'projets']);
        return new Response("projet deleted successfully " . json_encode($jsonContent));
    }

    #[Route("/sendEmailPrix", name: "sendPrixEmail")]
public function sendEmailPrix(ManagerRegistry $doctrine, Request $request, NormalizerInterface $normalizer)
{
    $entityManager = $doctrine->getManager();
    
    // Replace this email address with the desired recipient email address
    $email = "daadaa.farouk@live.fr";
    
   
    

    
     {
        // Create the SwiftMailer transport
        $transport = (new Swift_SmtpTransport('smtp.gmail.com', 587, 'tls'))
            ->setUsername('pidevmajesty@gmail.com')
            ->setPassword('xfbyslhggajvfdjz');
        
        // Create the SwiftMailer instance
        $mailer = new Swift_Mailer($transport);
        
        // Create the message to send
        $message = (new \Swift_Message('Thank you for your purchase'))
            ->setFrom('noreply@example.com')
            ->setTo($email)
            ->setBody(
                $this->renderView('emails/thank_you.html.twig', [
                
                ]),
                'text/html'
            );
        
        // Send the message
        $mailer->send($message);
        
        return new Response("Email sent successfully."); 
     
    }
}


}



