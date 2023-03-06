package userteymour;

import com.mysql.jdbc.PreparedStatement;

import java.io.FileInputStream;

import java.io.InputStream;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.input.MouseEvent;

import utils.MyConx;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.stage.Stage;


public class PageMainController implements Initializable {
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private ImageView userImageView;
    

    private String email;

        private static String currentUserEmail;
        
    public static void setCurrentUserEmail(String email) {
        currentUserEmail = email;
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }
    @FXML
    private ImageView qrcodee;
    @FXML
    private Button logout;
    
   

    
@Override
public void initialize(URL url, ResourceBundle rb) {   
    // Check if there is a current user email
    if (currentUserEmail != null) {
        // Use the current user email to display the user's details
        try (Connection connection = MyConx.getInstance().getCnx()) {
            String query = "SELECT * FROM utilisateur WHERE email=?";
            PreparedStatement statement = (PreparedStatement) connection.prepareStatement(query);
            statement.setString(1, currentUserEmail);
            ResultSet resultSet = statement.executeQuery();
           if (resultSet.next()) {
    String nom = resultSet.getString("nom");
    String prenom = resultSet.getString("prenom");
    if (nom != null && !nom.isEmpty() && prenom != null && !prenom.isEmpty()) {
        nom = nom + " " + prenom;
    }
    String email = resultSet.getString("email");
    String imagePath = resultSet.getString("image");
    setName(nom);
    setEmailLabel(email);
    setImage(imagePath);
      // Generate the QR code
                generateQRCode();
     
}
        } catch (SQLException ex) {
            Logger.getLogger(PageMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }
}

    
    

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        nameLabel.setText(name);
    }

    public void setEmailLabel(String email) {
        emailLabel.setText(email);
    }

public void setImage(String imagePath) {
    try {
        InputStream image = new FileInputStream(imagePath);
        userImageView.setImage(new Image(image));
    } catch (IOException ex) {
        Logger.getLogger(PageMainController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

private void generateQRCode() {
    try {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String nomPrenom = nameLabel.getText();
        String email = emailLabel.getText();
        String message = "Bonjour " + nomPrenom + ", vous êtes connecté à Majesty Freelance avec l'email: " + email + " en tant que client";
        int width = 300;
        int height = 300;

        BufferedImage bufferedImage = null;
        BitMatrix byteMatrix = qrCodeWriter.encode(message, BarcodeFormat.QR_CODE, width, height);
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedImage.createGraphics();

        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }

        System.out.println("Success...");

        qrcodee.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
    } catch (WriterException ex) {
        Logger.getLogger(PageMainController.class.getName()).log(Level.SEVERE, null, ex);
    }
}



  @FXML
private void gestionuser(MouseEvent event) {
    try {
        // Load the GestionUser.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GestionUser.fxml"));
        Parent root = loader.load();
        
        // Get the controller of the GestionUser.fxml file
        GestionUserController gestionUserController = loader.getController();
        
        // Set the email of the user in the GestionUserController
        gestionUserController.setEmail(email);
        
        // Create a new scene with the GestionUser.fxml file as the root
        Scene scene = new Scene(root);
        
        // Get the stage from the MouseEvent
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Set the new scene in the stage and show the stage
        stage.setScene(scene);
        stage.show();
    } catch (IOException ex) {
        Logger.getLogger(PageMainController.class.getName()).log(Level.SEVERE, null, ex);
    }

}

    @FXML
    private void logout(ActionEvent event) {
       
    try {
        // Load the connecter.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Connecter.fxml"));
        Parent root = loader.load();
        
        // Create a new scene with the Connecter.fxml file as the root
        Scene scene = new Scene(root);
        
        // Get the stage from the MouseEvent
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Set the new scene in the stage and show the stage
        stage.setScene(scene);
        stage.show();
        
        // Clear the currentUserEmail variable to log the user out
        setCurrentUserEmail(null);
    } catch (IOException ex) {
        Logger.getLogger(PageMainController.class.getName()).log(Level.SEVERE, null, ex);
    }
    }


}
