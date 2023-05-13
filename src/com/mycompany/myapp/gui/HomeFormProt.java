/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.ui.Button;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;

/**
 *
 * @author bhk
 */
public class HomeFormProt extends Form {

    public HomeFormProt(Form previus) {

        setTitle("Home");
        setLayout(BoxLayout.y());
        Button btnAddTask = new Button("Add new Portfolio");
        Button btnListTasks = new Button("List portfolio");
        Button statisticsButton = new Button("Show Statistics");


        btnAddTask.addActionListener(e -> new AddPortfolioForm(this).show());
        btnListTasks.addActionListener(e -> new ListPortfolio(this).show());
               addAll(btnAddTask, btnListTasks,statisticsButton);

    }

}
