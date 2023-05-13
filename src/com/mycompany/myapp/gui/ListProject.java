package com.mycompany.myapp.gui;

import com.codename1.components.*;
import com.codename1.ui.*;
import com.codename1.ui.layouts.*;
import com.mycompany.myapp.entities.Project;
import com.mycompany.myapp.services.ServiceProject;
import java.util.*;

public class ListProject extends Form {

    Form previous;

    public static Project currentV = null;
    Button addBtn;

    public ListProject(Form previous) {

        this.previous = previous;
             setTitle("list des projets");
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

        ArrayList<Project> listProject = ServiceProject.getInstance().getAll();
        if (listProject.size() > 0) {
            for (Project v : listProject) {
                Component model = makeProjectModel(v);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    Label titre,prixprojet,type;

    private Container makeModelWithoutButtons(Project pro) {
        Container projectModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        projectModel.setUIID("containerRounded");

       titre = new Label(" titre : " + pro.getTitreprojet());
       titre.setUIID("labelDefault");
       prixprojet = new Label("prixprojet : " + pro.getPrixprojet());
       prixprojet.setUIID("labelDefault");
       type = new Label("type : " + pro.getType());
       type.setUIID("labelDefault");
      
       
//
//        puissance = new Label("power : " + v.getPuissance());
//        puissance.setUIID("labelDefault");

        projectModel.addAll(
                titre,prixprojet,type
        );

        return projectModel;
    }

    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeProjectModel(Project pro) {

        Container projectModel = makeModelWithoutButtons(pro);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("update");
        editBtn.setUIID("buttonWhiteCenter");

        editBtn.addActionListener(action -> {
            currentV = pro;
            
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

                ServiceProject.getInstance().delete(pro.getIdprojet());
                new ListProject(previous).show();
            });

            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        projectModel.add(btnsContainer);

        return projectModel;
    }

}