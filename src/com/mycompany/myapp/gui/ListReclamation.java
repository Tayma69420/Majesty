package com.mycompany.myapp.gui;

import com.codename1.components.*;
import com.codename1.ui.*;
import com.codename1.ui.layouts.*;
import com.mycompany.myapp.entities.Reclamation;
import com.mycompany.myapp.services.ServiceReclamation;
import java.util.*;

public class ListReclamation extends Form {

    Form previous;

    public static Reclamation currentV = null;
    Button addBtn;

    public ListReclamation(Form previous) {

        this.previous = previous;
             setTitle("list des reclamation");
        setLayout(BoxLayout.y());

        addGUIs();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();

        this.refreshTheme();
    }

    private void addGUIs() {

        ArrayList<Reclamation> listReclamation = ServiceReclamation.getInstance().getAll();
        if (listReclamation.size() > 0) {
            for (Reclamation v : listReclamation) {
                Component model = makeReclamationModel(v);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    Label titre, description;

    private Container makeModelWithoutButtons(Reclamation v) {
        Container reclamationModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        reclamationModel.setUIID("containerRounded");

        titre = new Label(" titre : " + v.getTitre());
      titre.setUIID("labelDefault");
       description = new Label("description : " + v.getRecla_desc());
       description.setUIID("labelDefault");
//
//        puissance = new Label("power : " + v.getPuissance());
//        puissance.setUIID("labelDefault");

        reclamationModel.addAll(
                titre, description
        );

        return reclamationModel;
    }

    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeReclamationModel(Reclamation v) {

        Container reclamationModel = makeModelWithoutButtons(v);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("update");
        editBtn.setUIID("buttonWhiteCenter");

        editBtn.addActionListener(action -> {
            currentV = v;
            new modifieruser(this).show();
        });

        deleteBtn = new Button("Delete");
        deleteBtn.setUIID("buttonWhiteCenter");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce Transaction ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");

            btnConfirm.addActionListener(actionConf -> {

                ServiceReclamation.getInstance().delete(v.getId_reclamation());
                new ListReclamation(previous).show();
            });

            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        reclamationModel.add(btnsContainer);

        return reclamationModel;
    }

}
