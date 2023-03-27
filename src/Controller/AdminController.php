<?php

// src/Controller/AdminController.php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Routing\Annotation\Route;

class AdminController extends AbstractController
{
    /**
     * @Route("/admin_page", name="admin_page")
     */
    public function adminPage()
    {
        return $this->render('custom/admin.html.twig');
    }
}
