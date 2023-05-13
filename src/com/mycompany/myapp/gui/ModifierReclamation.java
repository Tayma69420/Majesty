package com.mycompany.myapp.gui;

import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;

import com.mycompany.myapp.services.ServiceReclamation;

public class ModifierReclamation extends Form {

//    private Form previous;
//    private ComboBox<String> combo1;
//    private ComboBox<String> combo2;
//    private ComboBox<String> combo3;
//
//    public ModifierReclamation(Form previous, Voiture v) {

//       setTitle("update car");
//        setLayout(BoxLayout.y());
//
//        this.previous = previous;
//        combo1 = new ComboBox<>("BMW", "Mercedes", "Audi", "clio", "porshe", "peugeot", "hamer");
//        combo2 = new ComboBox<>("5ch", "6ch", "7ch", "8ch", "9ch", "10ch", "11ch", "12ch", "13ch");
//        combo3 = new ComboBox<>("essence", "gazoil", "gpl");
//
//        TextField matricule = new TextField(v.getMatricule(), "Registration number");
//        TextField prix_jours = new TextField(v.getPrix_jours(), "Price per day");
//        TextField etat = new TextField(v.getEtat(), "Status");
//        TextField id_locateur = new TextField(String.valueOf(v.getIdlocateur()), "ID locateur");
//        TextField picture = new TextField(v.getPicture(), "Picture ");
//
//        Button btnModifier = new Button("Edit");
//        Button btnAnnuler = new Button("Cancel");
//
//        btnModifier.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//                v.setMatricule(matricule.getText());
//
//                String selectedMarque = (String) combo1.getSelectedItem();
//                v.setMarque(selectedMarque);
//                String selectedpower = (String) combo2.getSelectedItem();
//                v.setMarque(selectedpower);
//                v.setPrix_jours(prix_jours.getText());
//                String selectedenergy = (String) combo3.getSelectedItem();
//                v.setMarque(selectedenergy);
//                v.setEtat(etat.getText());
//                v.setIdlocateur(Integer.parseInt(id_locateur.getText()));
//                v.setPicture(picture.getText());
//
//                try {
//                    if (ServiceVoiture.getInstance().modifier(v)) {
//                        new ListReclamation(previous).show();
//                    } else {
//                        Dialog.show("Error", "Unable to update transaction", new Command("OK"));
//                    }
//                } catch (Exception e) {
//                    Dialog.show("Error", "Unable to update transaction: " + e.getMessage(), new Command("OK"));
//                }
//
//            }
//
//        });
//
//        btnAnnuler.addActionListener(e -> {
//            new ListReclamation(previous).show();
//        });
//
//        addAll(matricule, combo1, combo2, prix_jours, combo3, etat, id_locateur, picture, btnAnnuler, btnModifier);
//        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

    //}
}
