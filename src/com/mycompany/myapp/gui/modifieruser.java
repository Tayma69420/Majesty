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
import com.mycompany.myapp.entities.User;
import static com.mycompany.myapp.gui.ListUserForm.currentV;
import com.mycompany.myapp.services.ServiceUser;

/**
 *
 * @author khmir
 */
public class modifieruser extends Form {

    Form previous;

    public modifieruser(Form previous) {

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

           TextField tfnom = new TextField("", "nom");
        TextField tfprenom = new TextField("", "prenom");
          TextField tfemail = new TextField("", "email");
              TextField tftel = new TextField("", "tel");
                   TextField tfadresse = new TextField("", "adresse");
                   TextField tfpasswd = new TextField("", "passwd");
       
        tfpasswd.setConstraint(TextField.PASSWORD);
       
        

 

       

        Button btnValider = new Button("modifier ");

       btnValider.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent evt) {
        User user = new User();
        user.setIduser(currentV.getIduser()); // Set the user ID to update
        user.setNom(tfnom.getText());
        user.setPrenom(tfprenom.getText());
        user.setEmail(tfemail.getText());
        user.setTel(tftel.getText());
        user.setAdresse(tfadresse.getText());
        user.setPasswd(tfpasswd.getText());
        // Set other properties as needed

        boolean success = ServiceUser.getInstance().updateUser(user);
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


        addAll(tfnom,tfprenom,tfemail,tftel,tfpasswd, tfadresse,btnValider);
    getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e-> previous.showBack());

    }

}
