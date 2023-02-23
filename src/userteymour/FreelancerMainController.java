package userteymour;

import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import utils.MyConx;

public class FreelancerMainController implements Initializable {
    @FXML
    private ListView<String> nameLabel;
    @FXML
    private ListView<String> emailLabel;
    @FXML
    private ImageView userImageView;

    private String email;
    
     private GestionFreelancerController gestionFreelancerController;
     
      @FXML
    private void openGestionFreelancer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GestionFreelancer.fxml"));
            Parent root = loader.load();
            
            // Get the instance of the GestionFreelancerController
            gestionFreelancerController = GestionFreelancerController.getInstance();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FreelancerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void closeGestionFreelancer(ActionEvent event) {
        // Call the returnu1 method of the GestionFreelancerController to close the window
        gestionFreelancerController.returnu1(event);
    }



        private static String currentUserEmail;
        
    public static void setCurrentUserEmail(String email) {
        currentUserEmail = email;
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }
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
                InputStream image = resultSet.getBinaryStream("image");
                setName(nom);
                setEmailLabel(email);
                setImage(image);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FreelancerMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

    
    

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        nameLabel.getItems().add(name);
    }

    public void setEmailLabel(String email) {
        emailLabel.getItems().add(email);
    }

    public void setImage(InputStream image) {
        userImageView.setImage(new Image(image));
    }

  @FXML
private void GestionFreelancer(MouseEvent event) {
    try {
        // Load the GestionFreelancer.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GestionFreelancer.fxml"));
        Parent root = loader.load();
        
        // Get the controller of the GestionFreelancer.fxml file
        GestionFreelancerController gestionfreelancerController = loader.getController();
        
        // Set the email of the user in the GestionFreelancerController
        gestionfreelancerController.setEmail(email);
        
        // Create a new scene with the GestionUser.fxml file as the root
        Scene scene = new Scene(root);
        
        // Get the stage from the MouseEvent
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Set the new scene in the stage and show the stage
        stage.setScene(scene);
        stage.show();
    } catch (IOException ex) {
        Logger.getLogger(FreelancerMainController.class.getName()).log(Level.SEVERE, null, ex);
    }
}
}
