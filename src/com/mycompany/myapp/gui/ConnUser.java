/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.components.ImageViewer;
import com.codename1.ui.Button;
import com.codename1.ui.URLImage;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.SessionManager;

public class ConnUser extends Form {

    public ConnUser(Form previous) {
        setTitle("Connected User");
        setLayout(BoxLayout.y());

        // Get the user's name, email, and image URL from the session
        String name = SessionManager.getNom();
        String email = SessionManager.getEmail();
         String imageName = SessionManager.getImage();
         // Construct the image URL using the image name and base URL
        String imageUrl = "file://C:/Users/21626/Desktop/projetsymphonie/majesty/public/images/" + imageName;

      // Define the desired width and height for the resized image
        int desiredWidth = 400;
        int desiredHeight = 400;

        // Create an ImageView to display the user's image with the desired size
        ImageViewer imageViewer = new ImageViewer(URLImage.createToStorage(
                EncodedImage.createFromImage(Image.createImage(desiredWidth, desiredHeight), false),
                imageUrl,
                imageUrl,
                URLImage.RESIZE_SCALE_TO_FILL
        ));

        // Create labels to display the user's name and email
        Label nameLabel = new Label("Name: " + name);
        Label emailLabel = new Label("Email: " + email);
        
        Button panierButton = new Button("Panier");
        panierButton.addActionListener((e) -> {
            new PanierForm(this).show();
        });
       
        Button ProjetButton = new Button("Projet");
        ProjetButton.addActionListener((e) -> {
            new HomeFormPanier(this).show();
        });
        
        Button AjoutProjetBut = new Button("Ajouter Projet");
        AjoutProjetBut.addActionListener((e) -> {
        new AddProjectForm(this).show();
        });
        
        Button ReclamationButton = new Button("Reclamation");
        ReclamationButton.addActionListener((e) -> {
        new HomeFormRec(this).show();
        });
        
        // Add the ImageView and labels to the form
        addAll(imageViewer, nameLabel, emailLabel,panierButton,ProjetButton,ReclamationButton,AjoutProjetBut);
          super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }
}
