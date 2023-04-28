<?php

namespace App\Controller;

use App\Entity\Facture;
use App\Entity\Panier;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Dompdf\Dompdf;
use Swift_SmtpTransport;
use Swift_Mailer;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Session\SessionInterface ;
use App\Entity\Utilisateur;


class FactureController extends AbstractController
{
   
    #[Route('/facture', name: 'app_facture_add')]
    public function app_add_facture(SessionInterface $session,Request $request): Response
    {
        $user = $session->get('user');
    $transport = (new Swift_SmtpTransport('smtp.gmail.com', 587, 'tls'))
    ->setUsername('pidevmajesty@gmail.com')
    ->setPassword('xfbyslhggajvfdjz');
    $mailer = new Swift_Mailer($transport);
    $entityManager = $this->getDoctrine()->getManager();
    $cartItems = $entityManager->getRepository(Panier::class)->findAll();

    $total = 0;
    foreach ($cartItems as $item) {
        $subtotal = $item->getQnt() * $item->getPrix();
        $total += $subtotal;
    }

    // Create a new Facture entity
    $facture = new Facture();

    // Create the form builder
    $form = $this->createFormBuilder($facture)
        ->add('cardnumber')
        ->add('expirationdate')
        ->add('securitycode', PasswordType::class)
        ->add('promo_code', null, [
            'mapped' => false, // this field is not persisted in the database
            'data' => '', // set the value of the promo code manually
        ])
        ->getForm();

    // Handle form submission
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        // Set the total from the "panier" table
        $entityManager = $this->getDoctrine()->getManager();
        $cartItems = $entityManager->getRepository(Panier::class)->findAll();

        $total = 0;
        foreach ($cartItems as $item) {
            $subtotal = $item->getQnt() * $item->getPrix();
            $total += $subtotal;
        }

        // Check if promo code is valid and set discount accordingly
        $promoCode = $form->get('promo_code')->getData();
        $discount = 0;
        if ($promoCode === 'Code10') {
            $discount = $total * 0.1;
        }

        // Set the total and discount
        $facture->setTotal($total - $discount);

        // Set the user ID, firstname, and lastname
        $facture->setIduser($user->getIduser());
        $facture->setFirstname($user->getPrenom());
        $facture->setLastname($user->getNom());

        // Persist the Facture entity
        $entityManager->persist($facture);

        // Remove the Panier table
        $connection = $entityManager->getConnection();
        $platform = $connection->getDatabasePlatform();
        $connection->executeStatement($platform->getTruncateTableSQL('panier', true /* whether to cascade */));

        // Flush changes to the database
        $entityManager->flush();

           
              
  // Send the email to the user
  $message = (new \Swift_Message('Facture'))
  ->setFrom('pidevmajesty@gmail.com')
  ->setTo($user->getEmail())
  ->setBody(
      $this->renderView(
          'facture/emailfacture.html.twig',
          ['facture' => $facture]
      ),
      'text/html'
  );

$mailer->send($message);

// Redirect to success page or do whatever you want
if ($request->query->get('download_pdf')) {
  // Redirect to the PDF generation action
  return $this->redirectToRoute('app_facture_pdf');
} else {
  return $this->redirectToRoute('confpdffac');
}
}

return $this->render('facture/index.html.twig', [
'form' => $form->createView(),
'total' => $total,
]);
}


#[Route('/facture/pdf', name: 'app_facture_pdf')]
public function generatePdf(Request $request): Response
{
    // Get the facture entity
    $facture = $this->getDoctrine()->getRepository(Facture::class)->findOneBy([], ['idfac' => 'DESC']);

    // Generate the HTML for the facture
    $html = $this->renderView('facture/pdf.html.twig', [
        'facture' => $facture,
    ]);

    // Create the PDF
    $dompdf = new Dompdf();
    $dompdf->loadHtml($html);
    $dompdf->setPaper('A4', 'portrait');
    $dompdf->render();

    // Output the PDF
    $pdfOutput = $dompdf->output();

    // Send the PDF to the user as a download
    $response = new Response($pdfOutput);
    $response->headers->set('Content-Type', 'application/pdf');
    $response->headers->set('Content-Disposition', 'attachment; filename="facture.pdf"');
    return $response;
}
 #[Route('/confpdf', name: 'confpdffac')]
    public function index(Request $request): Response
    {
       
        // Render the Twig template and pass the projects to it
        return $this->render('facture/confirmationpdf.html.twig', [
            
        ]);
    }
}
