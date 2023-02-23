/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userteymour;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.MyConx;
/**
 * FXML Controller class
 *
 * @author 21626
 */
public class GestionFreelancerController implements Initializable {

    @FXML
    private TextField oldpdw;
    @FXML
    private TextField newpdw;
    @FXML
    private TextField newnewpdw;
    @FXML
    private Button choisirimg;
    @FXML
    private Button changerimg;
    @FXML
    private Button changermdp;
    
    private String email = FreelancerMainController.getCurrentUserEmail();



    @FXML
    private Button returnu;
    @FXML
    private TextField imgpath;
    
    private Connection connection;
    @FXML
    private ImageView userImageView;
    @FXML
    private Label shownpwnew;
    @FXML
    private Label shownpwnewnew;
    @FXML
    private Label shownpwold;
    @FXML
    private ToggleButton showbtnold;
    @FXML
    private ToggleButton showbntnew;
    @FXML
    private ToggleButton showbtnnewnew;
        // Add a private static instance variable
    private static GestionFreelancerController instance;

    // Make the constructor public
    public GestionFreelancerController() {
    }

    // Add a public static getInstance method that returns the instance of the controller
    public static GestionFreelancerController getInstance() {
        return instance;
    }

    
    
     private boolean labelVisible = false;
  
    
    
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        shownpwold.setVisible(false);
        shownpwnewnew.setVisible(false);
        shownpwnew.setVisible(false);
    }    

    @FXML
    private void changeimgu1(ActionEvent event) {
    // Show a confirmation message
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText(null);
    alert.setContentText("Voulez-vous vraiment changer la photo du profil ?");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        // Get the path of the selected image
        String path = imgpath.getText();

        // Update the image for the current user in the database
        try {
            connection = MyConx.getInstance().getCnx();
            PreparedStatement ps = (PreparedStatement) connection.prepareStatement(
                "UPDATE utilisateur SET image = ? WHERE email = ?"
            );
            FileInputStream fis = new FileInputStream(path);
            ps.setBinaryStream(1, fis, fis.available());
            ps.setString(2, email);
            ps.executeUpdate();

        } catch (SQLException | IOException ex) {
            Logger.getLogger(FreelancerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

    @FXML
     void returnu1(ActionEvent event) {
    try {
        // Load the FreelancerMain.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FreelancerMain.fxml"));
        Parent root = loader.load();

        // Set the FreelancerMain.fxml file as the root of a new scene
        Scene scene = new Scene(root);

        // Get the current window and display the new scene in it
        Stage stage = (Stage) returnu.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } catch (IOException ex) {
        Logger.getLogger(FreelancerMainController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    @FXML
    private void browse1(ActionEvent event) {
    // Open a file chooser dialog to select an image file
    FileChooser fileChooser = new FileChooser();
    File file = fileChooser.showOpenDialog(null);

    if (file != null) {
        // Set the text field to the path of the selected file
        imgpath.setText(file.getAbsolutePath());

        // Update the image view with the selected image
        Image image = new Image(file.toURI().toString());
        userImageView.setImage(image);
    }
}

    @FXML
    private void changemdpu1(ActionEvent event) {
    String oldPassword = oldpdw.getText();
    String newPassword = newpdw.getText();
    String newNewPassword = newnewpdw.getText();
    
   // Check if the old password is correct
try {
    connection = MyConx.getInstance().getCnx();
    PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM utilisateur WHERE email = ? AND passwd = ?"
    );
    ps.setString(1, email);
    ps.setString(2, oldPassword);
    ResultSet rs = ps.executeQuery();
    
    if (!rs.next()) {
        // Old password is incorrect
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText("L'ancien mot de passe est incorrect.");
        alert.showAndWait();
        return;
    }
    
} catch (SQLException ex) {
    Logger.getLogger(FreelancerMainController.class.getName()).log(Level.SEVERE, null, ex);
    return;
}

// Check if the new passwords match
if (!newPassword.equals(newNewPassword)) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Erreur");
    alert.setHeaderText(null);
    alert.setContentText("Les nouveaux mots de passe ne correspondent pas.");
    alert.showAndWait();
    return;
}

// Check if the new password meets the complexity requirements
if (!newPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$")) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Erreur");
    alert.setHeaderText(null);
    alert.setContentText("Le nouveau mot de passe doit contenir au moins une lettre majuscule, une lettre minuscule et un chiffre.");
    alert.showAndWait();
    return;
}

// Update the password in the database
try {
    PreparedStatement ps = connection.prepareStatement(
        "UPDATE utilisateur SET passwd = ? WHERE email = ?"
    );
    ps.setString(1, newPassword);
    ps.setString(2, email);
    ps.executeUpdate();
    
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText(null);
    alert.setContentText("Le mot de passe a été changé avec succès.");
    alert.showAndWait();
    
    // Clear the password fields
    oldpdw.clear();
    newpdw.clear();
    newnewpdw.clear();
    
} catch (SQLException ex) {
    Logger.getLogger(FreelancerMainController.class.getName()).log(Level.SEVERE, null, ex);
}}


    @FXML
    private void showold1(ActionEvent event) {
    String oldPassword = oldpdw.getText();
    shownpwold.setText(oldPassword);
    labelVisible = !labelVisible; // toggle the visibility
    shownpwold.setVisible(labelVisible);
    }

    @FXML
    private void shownewnew1(ActionEvent event) {
                    String oldPassword = newnewpdw.getText();
    shownpwnewnew.setText(oldPassword);
    labelVisible = !labelVisible; // toggle the visibility
    shownpwnewnew.setVisible(labelVisible);
    }

    @FXML
    private void shownew1(ActionEvent event) {
                                String oldPassword = newpdw.getText();
    shownpwnew.setText(oldPassword);
    labelVisible = !labelVisible; // toggle the visibility
    shownpwnew.setVisible(labelVisible);
    }

    
}
