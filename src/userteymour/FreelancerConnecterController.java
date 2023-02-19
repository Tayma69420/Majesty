/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userteymour;

import entities.User;
import entities.role;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.UserService;

/**
 * FXML Controller class
 *
 * @author 21626
 */
public class FreelancerConnecterController implements Initializable {

    @FXML
    private TextField tfnom;
    @FXML
    private TextField tfprenom;
    @FXML
    private TextField tfemail;
    @FXML
    private TextField tftel;
    @FXML
    private TextField tfadresse;
    @FXML
    private TextField tfage;
    @FXML
    private TextField tfpasswd;
    @FXML
    private Button inscrirefeelancer;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void sauvgarder12(ActionEvent event) throws IOException {
        role r1;
        r1 = new role (3,"Freelancer");
        String nom = tfnom.getText();
        String prenom = tfprenom.getText();
        int tel = Integer.parseInt(tftel.getText());
        String adresse = tfadresse.getText();
        String email = tfemail.getText();
        String passwd = tfpasswd.getText();
        int age = Integer.parseInt(tfage.getText());
 
   
      

        // Créer un nouvel utilisateur avec les données du formulaire et un rôle par défaut
        User u = new User( nom,prenom,tel,adresse, r1,email,passwd,age);


        // Utiliser le service UserService pour ajouter l'utilisateur à la base de données
        UserService userService = new UserService() {};

        userService.insertFreelancer(u);

        // Afficher un message de confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Utilisateur ajouté avec succès !");
        alert.showAndWait();

        // Réinitialiser le formulaire
        tfnom.setText("");
        tfprenom.setText("");
        tftel.setText("");
        tfadresse.setText("");
        tfemail.setText("");
        tfpasswd.setText("");
        tfage.setText("");
        
    }

    
    


    
}
