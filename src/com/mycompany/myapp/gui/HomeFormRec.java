/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;

/**
 *
 * @author bhk
 */
public class HomeFormRec extends Form {

    public HomeFormRec(Form previous) {
      super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
        setTitle("Home");
        setLayout(BoxLayout.y());
        Button btnAddTask = new Button("Add new reclamation");
        Button btnListTasks = new Button("List reclamation");

        btnAddTask.addActionListener(e -> new AddReclamationForm(this).show());
        btnListTasks.addActionListener(e -> new ListReclamation(this).show());
        addAll(btnAddTask, btnListTasks);

    }

}
