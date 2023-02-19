/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userteymour;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import utils.MyConx;

/**
 * FXML Controller class
 *
 * @author 21626
 */
public class ConnecterController implements Initializable {

    @FXML
    private Button inscrire_user;
    
    @FXML
    private TextField donneradresse;
    @FXML
    private TextField password;
    @FXML
    private Button connect;
    @FXML
    private Button inscrire_freelancer;
    private Connection connection;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    private void freelancer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FreelancerConnecter.fxml"));
    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
    }

    @FXML
    private void user(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ConnecterUser.fxml"));
    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
    }

    @FXML
    private void Connecter(ActionEvent event)throws SQLException, IOException {
        
      connection = MyConx.getInstance().getCnx();
    String email = donneradresse.getText();
    String mdp = password.getText();

    if (email.isEmpty() || mdp.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez saisir l'email et le mot de passe.");
        alert.showAndWait();
        return;
    } 

    try {
        String requete = "SELECT * FROM utilisateur WHERE email = ? AND passwd = ?";
          try (PreparedStatement statement = connection.prepareStatement(requete)) {
              statement.setString(1, email);
              statement.setString(2, mdp);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Utilisateur trouvé
                    int idRole = resultSet.getInt("id_role");
                    if (idRole == 1) {
                        Parent root = FXMLLoader.load(getClass().getResource("AdminUser.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Succès");
                        alert.setHeaderText(null);
                        alert.setContentText("Vous etes maintenant connecté!");
                        alert.showAndWait();
                        Parent root = FXMLLoader.load(getClass().getResource("PageMain.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    }
                } else {
                    // Utilisateur non trouvé
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Email ou mot de passe incorrect.");
                    alert.showAndWait();
                    connection.close();
                }
            }
          }
       
    } catch (SQLException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText("Impossible de se connecter à la base de données.");
        alert.showAndWait();
    }  
    
    
    }
    }