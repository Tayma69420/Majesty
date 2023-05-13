package com.mycompany.myapp.gui;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.entities.Reclamation;
import com.mycompany.myapp.services.ServiceReclamation;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
/**
 *
 * @author haithem
 */
public class AddReclamationForm extends Form {

  

    public AddReclamationForm(Form previous) {
        setTitle("Add reclamation");
        setLayout(BoxLayout.y());

      

        TextField tftitre = new TextField("", "titre");
        TextField tfdesc = new TextField("", "description");
       
        Button btnValider = new Button("Add reclamation");

btnValider.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent evt) {
        String titre = tftitre.getText();
        String recla_desc = tfdesc.getText();

        if(recla_desc.contains("badword")) {
            Dialog.show("Error", "You cannot type that word", new Command("OK"));
            return; // Cancel the operation
        }

        // Create the reclamation object
        Reclamation v = new Reclamation(titre, recla_desc);

        // Add the reclamation to the database
        if (ServiceReclamation.getInstance().addTask(v)) {
            Dialog.show("Success", "Reclamation added successfully", new Command("OK"));
            
            // Send the email
            String url = "http://127.0.0.1:8000/sendEmailRec";
            ConnectionRequest request = new ConnectionRequest(url);
            request.setPost(false);
            request.addResponseListener(response -> {
                if (response.getResponseCode() == 200) {
                    Dialog.show("Success", "Email sent", "OK", null);
                } else {
                    Dialog.show("Error", "Failed to send email", "OK", null);
                }
            });
            NetworkManager.getInstance().addToQueue(request);
        } else {
            Dialog.show("ERROR", "Server error", new Command("OK"));
        }
    }
});



        Button btnCharts = new Button("Charts");
btnValider.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent evt) {
        String titre = tftitre.getText();
        String recla_desc = tfdesc.getText();
           
        if(recla_desc.contains("badword")) {
            Dialog.show("Error", "You cannot type that word", new Command("OK"));
            return;
        }
        
        Reclamation v = new Reclamation(titre, recla_desc);

        if (ServiceReclamation.getInstance().addTask(v)) {
            Dialog.show("Success", "Connection accepted", new Command("OK"));
        } else {
            Dialog.show("ERROR", "Server error", new Command("OK"));
        }
    }
});


        addAll(tftitre, tfdesc, btnValider, btnCharts);
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

    }
 
}
