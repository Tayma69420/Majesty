/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.entities.Project;
import com.mycompany.myapp.services.ServiceProject;
import java.util.Date;

/**
 *
 * @author haithem
 */
public class AddProjectForm extends Form {

  

    public AddProjectForm(Form previous) {
        setTitle("Add project");
        setLayout(BoxLayout.y());

      

        TextField tTitreprojet = new TextField("", "titreprojet");
        TextField tprixprojet = new TextField("", "prixprojet");
        TextField tType = new TextField("", "type");
       
  
        Button btnValider = new Button("Add task");

        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
               

                  
                    String titreprojet = tTitreprojet.getText();
                    String prixprojet = tprixprojet.getText();
                    String type = tType.getText();
      
                    
                   
                    

                    Project pr = new Project(titreprojet, prixprojet,type );

                    if (ServiceProject.getInstance().addTask(pr)) {
                        Dialog.show("Success", "Connection accepted", new Command("OK"));
                    } else {
                        Dialog.show("ERROR", "Server error", new Command("OK"));
                    }

                }

            
        });

        addAll(tTitreprojet,tprixprojet,tType,  btnValider);
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

    }
 
}