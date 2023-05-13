/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.mycompany.myapp.entities.User;
import com.mycompany.myapp.services.ServiceUser;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.Random;


/**
 *
 * @author bhk
 */
public class AddUserForm extends Form {
    
    
    private void addPicture(TextField textField) {
    Display.getInstance().openGallery(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (evt == null || evt.getSource() == null) {
                return;
            }
            String filePath = (String) evt.getSource();
            if (filePath != null && !filePath.isEmpty()) {
                textField.setText(filePath);
            }
        }
    }, Display.GALLERY_IMAGE);
}

        private ComboBox<String> combo1;
          private ComboBox<String> combo2;
                    private ComboBox<String> combo3;
    private static final String TWILIO_ACCOUNT_SID = "ACa3f47735bde23cea35aac8e3b509ac97";
    private static final String TWILIO_AUTH_TOKEN = "0e1f7131a59883516ba7e4888cc5efbc";
    private static final String TWILIO_PHONE_NUMBER = "+16205088251";

    public AddUserForm(Form previous) {
        setTitle("Add new user");
        setLayout(BoxLayout.y());

        TextField tfmatricule = new TextField("", "User :");
       
        
        
  
        TextField tfnom = new TextField("", "nom");
        TextField tfprenom = new TextField("", "prenom");
          TextField tfemail = new TextField("", "email");
              TextField tftel = new TextField("", "tel");
                   TextField tfadresse = new TextField("", "adresse");
              TextField tfage = new TextField("", "Age");
                   TextField tfpasswd = new TextField("", "passwd");
        TextField tfimage = new TextField("", "Picture");
        TextField tfsexe = new TextField("", "sexe");
         TextField tfrole = new TextField("", "role");
          combo1=new ComboBox<>("Homme", "Femme");
          combo2=new ComboBox<>("2", "3");
         
       

        Button btnValider = new Button("Add task");
// Add the textfield and button for verification code
        TextField tfVerificationCode = new TextField("", "Verification code");
        Button btnSendVerificationCode = new Button("Send code");

              // Send verification code when button is pressed
        btnSendVerificationCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String phoneNumber = tftel.getText().trim();
   if (phoneNumber.length() != 8) {
        Dialog.show("Error", "Please enter a valid phone number starting with +216", new Command("OK"));
        return;
                }

                // Add +216 by default to the phone number
                phoneNumber = "+216" + phoneNumber;

                Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
              int verificationCode = new Random().nextInt(9000) + 1000;


                Message message = Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(TWILIO_PHONE_NUMBER),
                        "Your verification code is: " + verificationCode).create();

                if (message.getStatus() != Message.Status.FAILED) {
                    Dialog.show("Success", "Verification code sent to " + phoneNumber, new Command("OK"));
                    tfVerificationCode.setText(Integer.toString(verificationCode));
                } else {
                    Dialog.show("Error", "Failed to send verification code", new Command("OK"));
                }
            }
        });
        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
               if ((tfnom.getText().length() == 0)) {
                    Dialog.show("Alert", "Please fill all the fields", new Command("OK"));
                } else{
                
                      String  nom=tfnom.getText().toString();
                      String  prenom=tfprenom.getText().toString();
                      String  email=tfemail.getText().toString();
                       String  tel=tftel.getText().toString();
                        String  adresse=tfadresse.getText().toString();
                         String  age=tfage.getText().toString();
                          String  passwd=tfpasswd.getText().toString();
                           String  image=tfimage.getText().toString();
                            String  tfsexe=combo1.getSelectedItem();
                             String  tfrole=combo2.getSelectedItem();
                        
                       
                      
                      
                      
                      
               
                     
                           
                      int idrole =Integer.parseInt(tfrole) ;
                        User v = new User( nom,  prenom,  tel,  adresse,  idrole,  tfsexe,  email,  passwd,  age,  image);
                       
                        if (ServiceUser.getInstance().addTask(v)) {
                            Dialog.show("Success", "Connection accepted", new Command("OK"));
                        } else {
                            Dialog.show("ERROR", "Server error", new Command("OK"));
                        }
                   

                }

            }
        });
Button btnAddImage = new Button("Add Image");
btnAddImage.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent evt) {
        addPicture(tfimage);
    }
});
addAll(tfnom, tfprenom,tfemail,tftel,tfadresse,tfage,tfpasswd, tfimage,combo1,combo2,btnValider,tfVerificationCode,btnSendVerificationCode, btnAddImage);

        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

    }

}
