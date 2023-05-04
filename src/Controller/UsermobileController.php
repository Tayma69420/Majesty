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
use Swift_SmtpTransport;
use Swift_Mailer;
use Twilio\Rest\Client;
use Twilio\Jwt\ClientToken;

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
          $utilisateur->setAge($req->get('age'));
          $utilisateur->setPasswd(encryptPassword($req->get('passwd')));
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



}
