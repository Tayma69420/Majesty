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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.MyConx;

public class AuthenticationController implements Initializable {
 private Connection connection;
    private DetailsController detailsController;
    @FXML
    private ImageView notif;
    @FXML
    private Label notiflab;

    private FXMLLoader fxmlLoader;

    
    public void refresh() throws IOException {
    // Load the FXML file again
    FXMLLoader loader = new FXMLLoader(getClass().getResource("details.fxml"));
    Parent root = loader.load();
    // Get the current scene and replace it with the new one
    Scene scene = pan.getScene();
    scene.setRoot(root);
}

    
@Override
public void initialize(URL url, ResourceBundle rb) {
    // create an instance of MyConnexion to connect to the database
    MyConx myConnexion = new MyConx();
    connection = myConnexion.getCnx();
    // create an instance of DetailsController to handle project details and panier
    detailsController = new DetailsController();
    fxmlLoader = new FXMLLoader(getClass().getResource("Authentication.fxml"));


    try {
        // get the quantity from the panier table
        String query = "SELECT SUM(qnt) as total_quantity FROM panier";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int quantity = rs.getInt("total_quantity");
            if (quantity == 0) {
                // if the quantity is zero, hide the notif label and image view
                notiflab.setVisible(false);
                notif.setVisible(false);
            } else {
                // if the quantity is not zero, show the quantity in the notif label and show the notif label and image view
                notiflab.setText(Integer.toString(quantity));
                notiflab.setVisible(true);
                notif.setVisible(true);
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}


    @FXML
    private Button ajouter1;
    @FXML
    private Button ajouter2;
    @FXML
    private Button ajouter3;
    @FXML
    private ImageView pan;

   

   @FXML
private void addToCart1(ActionEvent event) {
    int idProjet = 1;
    int quantity = 1;
    detailsController.addProjectToPanier(idProjet, quantity);
    try {
        Parent root = fxmlLoader.load();
        Scene scene = ajouter1.getScene();
        scene.setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

@FXML
private void addToCart2(ActionEvent event) {
    int idProjet = 2;
    int quantity = 1;
    detailsController.addProjectToPanier(idProjet, quantity);
    try {
        Parent root = fxmlLoader.load();
        Scene scene = ajouter2.getScene();
        scene.setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

@FXML
private void addToCart3(ActionEvent event) {
    int idProjet = 3;
    int quantity = 1;
    detailsController.addProjectToPanier(idProjet, quantity);
    try {
        Parent root = fxmlLoader.load();
        Scene scene = ajouter3.getScene();
        scene.setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    private void pagechange(MouseEvent event) {
            try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("details.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
    }
    }

}
