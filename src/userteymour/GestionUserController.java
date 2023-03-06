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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.MyConx;
/**
 * FXML Controller class
 *
 * @author 21626
 */
public class GestionUserController implements Initializable {

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
    
    private String email = PageMainController.getCurrentUserEmail();



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
    
    
    private boolean labelVisible = false;
    
  
    
    
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
   @Override
public void initialize(URL url, ResourceBundle rb) {
    // Bind labels to corresponding text fields
    shownpwold.textProperty().bind(oldpdw.textProperty());
    shownpwnew.textProperty().bind(newpdw.textProperty());
    shownpwnewnew.textProperty().bind(newnewpdw.textProperty());

    shownpwold.setVisible(false);
    shownpwnewnew.setVisible(false);
    shownpwnew.setVisible(false);
}

@FXML
private void changeimgu(ActionEvent event) {
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
            Logger.getLogger(GestionUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

@FXML
private void returnu(ActionEvent event) {
   try {
        // Load the PageMain.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PageMain.fxml"));
        Parent root = loader.load();

        // Set the PageMain.fxml file as the root of a new scene
        Scene scene = new Scene(root);

        // Get the current window and display the new scene in it
        Stage stage = (Stage) returnu.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } catch (IOException ex) {
        Logger.getLogger(GestionUserController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

  @FXML
private void browse(ActionEvent event) {
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
private void changemdpu(ActionEvent event) throws NoSuchAlgorithmException {
       String oldPassword = oldpdw.getText();
    String newPassword = newpdw.getText();
    String newNewPassword = newnewpdw.getText();

    // Check if the old password is correct
    try {
        connection = MyConx.getInstance().getCnx();
        PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM utilisateur WHERE email = ?"
        );
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            // Get the hashed password from the database
            String hashedPassword = rs.getString("passwd");

            // Compute the hash of the old password
            String hashedOldPassword = encryptPassword(oldPassword);

            if (hashedPassword.equals(hashedOldPassword)) {
                // The old password is correct, so change the password
                String hashedNewPassword = encryptPassword(newPassword);
                String hashedNewNewPassword = encryptPassword(newNewPassword);

                if (hashedNewPassword.equals(hashedNewNewPassword)) {
                    PreparedStatement ps2 = connection.prepareStatement(
                        "UPDATE utilisateur SET passwd = ? WHERE email = ?"
                    );
                    ps2.setString(1, hashedNewPassword);
                    ps2.setString(2, email);
                    ps2.executeUpdate();

                    // Show a success message
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Le mot de passe a été modifié avec succès.");
                    alert.showAndWait();

                    // Clear the text fields
                    oldpdw.setText("");
                    newpdw.setText("");
                    newnewpdw.setText("");
                } else {
                    // The new passwords do not match
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Les nouveaux mots de passe ne correspondent pas.");
                    alert.showAndWait();
                }
            } else {
                // The old password is incorrect
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le mot de passe actuel est incorrect.");
                alert.showAndWait();
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(GestionUserController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

 private String encryptPassword(String password) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
    StringBuilder hexString = new StringBuilder();
    for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
    }
    return hexString.toString();
}


    @FXML
    private void showold(ActionEvent event) {
  //  String oldPassword = oldpdw.getText();
 //   shownpwold.setText(oldPassword);
    labelVisible = !labelVisible; // toggle the visibility
    shownpwold.setVisible(labelVisible);
    }

    @FXML
    private void shownew(ActionEvent event) {
       //     String oldPassword = newpdw.getText();
  //  shownpwnew.setText(oldPassword);
    labelVisible = !labelVisible; // toggle the visibility
    shownpwnew.setVisible(labelVisible);
    }
/*
    private void shownewnew(ActionEvent event) {
     //               String oldPassword = newnewpdw.getText();
   // shownpwnewnew.setText(oldPassword);
    labelVisible = !labelVisible; // toggle the visibility
    shownpwnewnew.setVisible(labelVisible);
        
    }*/

    @FXML
    private void shownewnew(MouseEvent event) {
            labelVisible = !labelVisible; // toggle the visibility
    shownpwnewnew.setVisible(labelVisible);
    }
    
}
