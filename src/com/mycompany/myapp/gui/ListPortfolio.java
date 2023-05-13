package com.mycompany.myapp.gui;

import com.codename1.components.*;
import com.codename1.ui.*;
import com.codename1.ui.layouts.*;
import com.mycompany.myapp.entities.*;
import com.mycompany.myapp.services.ServicePortfolio;
import java.util.*;

public class ListPortfolio extends Form {

    Form previous;

    public static portfolio currentV = null;
    Button addBtn;

    public ListPortfolio(Form previous) {

        this.previous = previous;
           setTitle("list portfolio");
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

        ArrayList<portfolio> ListPortfolio = ServicePortfolio.getInstance().getAll();
        if (ListPortfolio.size() > 0) {
            for (portfolio v : ListPortfolio) {
                Component model = makeportfolioModel(v);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    Label image, description;

    private Container makeModelWithoutButtons(portfolio v) {
        Container portfolioModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        portfolioModel.setUIID("containerRounded");

        description = new Label(" description : " + v.getDescription());
      description.setUIID("labelDefault");
       image = new Label("image : " + v.getImage());
       image.setUIID("labelDefault");
//
//        puissance = new Label("power : " + v.getPuissance());
//        puissance.setUIID("labelDefault");

        portfolioModel.addAll(
                image, description
        );

        return portfolioModel;
    }

    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeportfolioModel(portfolio v) {

        Container portfolioModel = makeModelWithoutButtons(v);

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

                ServicePortfolio.getInstance().delete(v.getIdportfolio());
                new ListPortfolio(previous).show();
            });

            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        portfolioModel.add(btnsContainer);

        return portfolioModel;
    }

}