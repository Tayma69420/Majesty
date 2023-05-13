package com.mycompany.myapp.gui;

import com.codename1.components.*;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.*;
import com.mycompany.myapp.entities.User;
import com.mycompany.myapp.services.ServiceUser;
import java.text.SimpleDateFormat;
import java.util.*;

public class ListUserForm extends Form {

    Form previous;

    public static User currentV = null;
    Button addBtn;

    public ListUserForm(Form previous) {

        this.previous = previous;

        addGUIs();
        addActions();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    ListUserForm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {

        ArrayList<User> listUser = ServiceUser.getInstance().getAll();
        if (listUser.size() > 0) {
            for (User v : listUser) {
                Component model = makeUserModel(v);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        /*  addBtn.addActionListener(action -> {
            currentReclamation = null;
            new Manage(this).show();
        });
         */

    }
    Label  nom, prenom,email;

    private Container makeModelWithoutButtons(User v) {
        Container usermodel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        usermodel.setUIID("containerRounded");

 

        nom = new Label("Nom : " + v.getNom());
        nom.setUIID("labelDefault");

        prenom = new Label("prenom : " + v.getPrenom());
        prenom.setUIID("labelDefault");
        
          email = new Label("email : " + v.getEmail());
        email.setUIID("labelDefault");
        
       usermodel.addAll(
                nom, prenom, email
        );


        

        return usermodel;
    }

    Button editBtn, deleteBtn;
    Container btnsContainer;

   private Component makeUserModel(User v) {

    Container userModel = makeModelWithoutButtons(v);

    btnsContainer = new Container(new BorderLayout());
    btnsContainer.setUIID("containerButtons");

    editBtn = new Button("update");
    editBtn.setUIID("buttonWhiteCenter");

    editBtn.addActionListener(action -> {
          currentV = v;
       // new Edit(previous, v).show();
       new modifieruser(this).show();  
    });

    deleteBtn = new Button("Delete");
    deleteBtn.setUIID("buttonWhiteCenter");
    deleteBtn.addActionListener(action -> {
        InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
        dlg.setLayout(new BorderLayout());
        dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce utilisateur ?"));
        Button btnClose = new Button("Annuler");
        btnClose.addActionListener((ee) -> dlg.dispose());
        Button btnConfirm = new Button("Confirmer");

        btnConfirm.addActionListener(actionConf -> {

                ServiceUser.getInstance().delete(v.getIduser());         
              new ListUserForm(previous).show();
        });

        Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
        btnContainer.addAll(btnClose, btnConfirm);
        dlg.addComponent(BorderLayout.SOUTH, btnContainer);
        dlg.show(800, 800, 0, 0);
    });

    btnsContainer.add(BorderLayout.WEST, editBtn);
    btnsContainer.add(BorderLayout.EAST, deleteBtn);

    Container userContainer = new Container(new BorderLayout());
    userContainer.add(BorderLayout.CENTER, userModel);
    userContainer.add(BorderLayout.EAST, btnsContainer);

    return userContainer;
}


}
