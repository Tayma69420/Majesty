package com.mycompany.myapp.gui;

import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.mycompany.myapp.entities.Panier;
import com.mycompany.myapp.services.PanierService;
import java.util.ArrayList;

public class HomeFormPanier extends Form {

    public HomeFormPanier(Form previous) {
          super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        setTitle("Home");
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        
        // create a container to hold the project details and buttons
        Container projectsContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        // retrieve the list of projects using the PanierService
        ArrayList<Panier> projectsList = PanierService.getInstance().getAll();
        
        // iterate over the list of projects and add a container with project details and button to the container
        for (Panier project : projectsList) {
            String projectDetails = "Title: " + project.getNom() + ", Prix: " + project.getPrix();
            Container projectContainer = new Container(new FlowLayout());
            projectContainer.add(new Label(projectDetails));
            Button addButton = new Button("Add");
            addButton.addActionListener((e) -> {
                // add the project to the table in the database using PanierService
                PanierService.getInstance().addProjectToTable(project);
            });
            projectContainer.add(addButton);
            projectsContainer.add(projectContainer);
        }
        
        // add the container with project details and buttons to the form
        addComponent(projectsContainer);
        
        // create a button to go to the PanierForm
        Button panierButton = new Button("Panier");
        panierButton.addActionListener((e) -> {
            new PanierForm(this).show();
        });
        addComponent(panierButton);
    }
}
