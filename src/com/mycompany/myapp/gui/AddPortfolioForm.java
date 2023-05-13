package com.mycompany.myapp.gui;


import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.entities.portfolio;
import com.mycompany.myapp.services.ServicePortfolio;

public class AddPortfolioForm extends Form {

    public AddPortfolioForm(Form previous) {
        setTitle("Add a new portfolio");
        setLayout(BoxLayout.y());

        TextField tfdescription = new TextField("", "Description de portfolio");
        Button btnImportImage = new Button("Import Image");
        Button btnValider = new Button("Add Portfolio");

        btnImportImage.addActionListener(e -> {
            ActionListener callback = e1 -> {
                if (e1 != null && e1.getSource() != null) {
                    String filePath = (String) e1.getSource();
                   
                }
            };
            Display.getInstance().openGallery(callback, Display.GALLERY_IMAGE);
        });

        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String desc = tfdescription.getText();
                String image = "admin.jpg"; // Set the image path here or remove this line if not needed

                portfolio p = new portfolio(desc, image);

                if (ServicePortfolio.getInstance().addTask(p)) {
                    Dialog.show("Success", "Connection accepted", new Command("OK"));
                } else {
                    Dialog.show("ERROR", "Server error", new Command("OK"));
                }
            }
        });

        addAll(tfdescription, btnImportImage, btnValider);
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }
}
