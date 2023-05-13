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
import com.mycompany.myapp.entities.Reclamation;
import com.mycompany.myapp.services.ServiceReclamation;

/**
 *
 * @author haithem
 */
public class modifieruserr extends Form {

    Form previous;

    public modifieruserr(Form previous) {

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

           TextField tfnom = new TextField("", "titre");
        TextField tfprenom = new TextField("", "description");
       
        
        Button btnValider = new Button("modifier ");

       btnValider.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent evt) {
        Reclamation user = new Reclamation();
        user.setTitre(tfnom.getText());
        user.setRecla_desc(tfprenom.getText());
        // Set other properties as needed

        boolean success = ServiceReclamation.getInstance().updateUser(user);
        if (success) {
            Dialog.show("Success", "User updated successfully", "OK", null);
            // Refresh the list view
          //  previous.refresh();
            previous.showBack();
        } else {
            Dialog.show("Error", "Failed to update user", "OK", null);
        }
    }
});


        addAll(tfnom,tfprenom,btnValider);
    getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e-> previous.showBack());

    }

}