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
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.entities.portfolio;
import static com.mycompany.myapp.gui.ListPortfolio.currentV;
import com.mycompany.myapp.services.ServicePortfolio;


public class modifierusere extends Form {

    Form previous;

    public modifierusere(Form previous) {

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

           TextField tfnom = new TextField("", "nom");
        TextField tfprenom = new TextField("", "prenom");
       
        Button btnValider = new Button("modifier ");

       btnValider.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent evt) {
        portfolio p = new portfolio();
        p.setDescription(tfnom.getText());
        p.setImage(tfprenom.getText());
        // Set other properties as needed

        boolean success = ServicePortfolio.getInstance().updateUser(p);
        if (success) {
            Dialog.show("Success", "Portfolio updated successfully", "OK", null);
            // Refresh the list view
          //  previous.refresh();
            previous.showBack();
        } else {
            Dialog.show("Error", "Failed to update Portfolio", "OK", null);
        }
    }
});


        addAll(tfnom,tfprenom,btnValider);
    getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e-> previous.showBack());

    }

}