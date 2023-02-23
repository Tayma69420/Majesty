
package userteymour;

import java.io.IOException;
import java.io.InputStream;
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
private void Connecter(ActionEvent event) throws SQLException, IOException {

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
                    int idRole = resultSet.getInt("id_role");
                    switch (idRole) {
                        case 1:
                            // Admin user
                            Parent adminRoot = FXMLLoader.load(getClass().getResource("AdminUser.fxml"));
                            Scene adminScene = new Scene(adminRoot);
                            Stage adminStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            adminStage.setScene(adminScene);
                            adminStage.show();
                            break;
case 2:
    // Normal user
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Succès");
    alert.setHeaderText(null);
    alert.setContentText("Vous êtes maintenant connecté!");
    alert.showAndWait();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("PageMain.fxml"));
    Parent normalUserRoot = loader.load();
    PageMainController controller = loader.getController();
    controller.setEmail(email); // set the email parameter in the existing PageMainController instance
    
    String name = "";
    InputStream image = null;
    try {
        PreparedStatement statement2 = connection.prepareStatement("SELECT nom, prenom, image FROM utilisateur WHERE email = ?");
        statement2.setString(1, email);
        ResultSet resultSet2 = statement2.executeQuery();
        if (resultSet2.next()) {
            String nom = resultSet2.getString("nom");
            String prenom = resultSet2.getString("prenom");
            if (nom != null && !nom.isEmpty() && prenom != null && !prenom.isEmpty()) {
                name = nom + " " + prenom;
            }
            image = resultSet2.getBinaryStream("image");
        }
    } catch (SQLException e) {
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText("Impossible de récupérer les informations de l'utilisateur.");
        alert.showAndWait();
    }

    if (controller != null) {
        if (!name.isEmpty()) {
            controller.setName(name);
        } else {
            // handle empty name
            controller.setName("Unknown");
        }
        controller.setEmailLabel(email);
        controller.setImage(image);
        PageMainController.setCurrentUserEmail(email);
    }

    loader.setController(controller); // set the existing PageMainController instance as the controller
    Scene normalUserScene = new Scene(normalUserRoot);
    Stage normalUserStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    normalUserStage.setScene(normalUserScene);
    normalUserStage.show();
    break;


case 3:
    // Freelancer user
    FXMLLoader freelancerLoader = new FXMLLoader(getClass().getResource("FreelancerMain.fxml"));
    Parent freelancerRoot = freelancerLoader.load();
    FreelancerMainController freelancerController = freelancerLoader.getController();
    freelancerController.setEmail(email);

    String freelancerName = "";
    InputStream freelancerImage = null;
    try {
        PreparedStatement freelancerStatement = connection.prepareStatement("SELECT nom, prenom, image FROM utilisateur WHERE email = ?");
        freelancerStatement.setString(1, email);
        ResultSet freelancerResultSet = freelancerStatement.executeQuery();
        if (freelancerResultSet.next()) {
            String nom = freelancerResultSet.getString("nom");
            String prenom = freelancerResultSet.getString("prenom");
            if (nom != null && !nom.isEmpty() && prenom != null && !prenom.isEmpty()) {
                freelancerName = nom + " " + prenom;
            }
            freelancerImage = freelancerResultSet.getBinaryStream("image");
        }
    } catch (SQLException e) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText("Impossible de récupérer les informations de l'utilisateur.");
        alert.showAndWait();
    }

    if (freelancerController != null) {
        if (!freelancerName.isEmpty()) {
            freelancerController.setName(freelancerName);
        } else {
            // handle empty name
            freelancerController.setName("Unknown");
        }
        freelancerController.setEmailLabel(email);
        freelancerController.setImage(freelancerImage);
    }

    freelancerLoader.setController(freelancerController);
    Scene freelancerScene = new Scene(freelancerRoot);
    Stage freelancerStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    freelancerStage.setScene(freelancerScene);
    freelancerStage.show();
    break;

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

}}