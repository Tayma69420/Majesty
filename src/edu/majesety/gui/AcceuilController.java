/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.majesety.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Haith
 */
public class AcceuilController implements Initializable {

    @FXML
    private Button btn_reclamation;
    @FXML
    private Button btn_categoriedureclamation;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void reclamation(ActionEvent event) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reclamtion.fxml"));
            try {
                Parent root = loader.load();
                btn_reclamation.getScene().setRoot(root);
                

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
    }

    @FXML
    private void categoriedureclamation(ActionEvent event) { 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("categorie_rec.fxml"));
            try {
                Parent root = loader.load();
                btn_categoriedureclamation.getScene().setRoot(root);
                

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
    }
    
}
