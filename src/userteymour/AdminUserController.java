package userteymour;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import entities.User;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.UserService;
import utils.MyConx;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;

public class AdminUserController {

    @FXML
    private TableView<User> utilisateur;

    @FXML
    private TableColumn<User, Integer> idusercol;

    @FXML
    private TableColumn<User, String> nomcol;

    @FXML
    private TableColumn<User, String> prenomcol;

    @FXML
    private TableColumn<User, String> emailcol;

    @FXML
    private TableColumn<User, String> telcol;

    @FXML
    private TableColumn<User, String> adrcol;

    @FXML
    private TableColumn<User, Integer> agecol;

    @FXML
    private TableColumn<User, String> passwdcol;

    @FXML
    private TableColumn<User, Integer> idrolecol;

    private ObservableList<User> data;

    private UserService userService;

    @FXML
    private Button supprimer;
    @FXML
    private Button modifier;
    
    private User selectedUser;
    @FXML
    private TextField teltf;
    private TextField passwdtf;
    @FXML
    private TextField emailtf;
    @FXML
    private DatePicker agetf;
    @FXML
    private TextField adrtf;
    @FXML
    private TextField pretf;
    @FXML
    private TextField nomtf;
    
    
      private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @FXML
    private Button btnreturn;
    @FXML
    private TableColumn<?, ?> sexecol;
    @FXML
    private TextField rech;
    

public void initialize() {
    // Initialize the data
    userService = new UserService() {};
    data = FXCollections.observableArrayList(userService.readAll());
    utilisateur.setItems(data);
    // Initialize the date picker
    agetf.setConverter(new StringConverter<LocalDate>() {
        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }

        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    });
    agetf.setPromptText("yyyy-MM-dd");
    // Initialize the columns
    idusercol.setCellValueFactory(new PropertyValueFactory<>("iduser"));
    nomcol.setCellValueFactory(new PropertyValueFactory<>("nom"));
    prenomcol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
    emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
    telcol.setCellValueFactory(new PropertyValueFactory<>("tel"));
    adrcol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
    agecol.setCellValueFactory(new PropertyValueFactory<>("age"));
    passwdcol.setCellValueFactory(new PropertyValueFactory<>("passwd"));
    idrolecol.setCellValueFactory(new PropertyValueFactory<>("id_role"));
    sexecol.setCellValueFactory(new PropertyValueFactory<>("sexe"));

    //tekhou l values mel table view
    utilisateur.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            selectedUser = newSelection;
            nomtf.setText(selectedUser.getNom());
            pretf.setText(selectedUser.getPrenom());
            emailtf.setText(selectedUser.getEmail());
            teltf.setText(String.valueOf(selectedUser.getTel()));
            adrtf.setText(selectedUser.getAdresse());
            agetf.setValue(selectedUser.getAge());
            passwdtf.setText(selectedUser.getPasswd());
        }
    });
}


@FXML


private void delete(ActionEvent event) {
    // Get the selected user from the table view
    User selectedUser = utilisateur.getSelectionModel().getSelectedItem();

    // Check that a user is actually selected
    if (selectedUser != null) {
        // Create an alert message
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Êtes-vous sûr(e) de vouloir Supprimer cet utilisateur ?");
        alert.setContentText("Cliquez sur OK pour confirmer ou sur Cancel pour revenir ");

        // Show the alert and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();

        // If the user clicks OK, delete the user from the database and remove it from the table view
        if (result.get() == ButtonType.OK){
            userService.delete(selectedUser);
            data.remove(selectedUser);
        }
    }
}


@FXML
private void modifieruser(ActionEvent event) {
    
            if (nomtf.getText().isEmpty() || pretf.getText().isEmpty() || emailtf.getText().isEmpty() ||
                teltf.getText().isEmpty() || adrtf.getText().isEmpty() || passwdtf.getText().isEmpty() ||
                agetf.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }
        
         else if (nomtf.getText().matches(".*\\d.*") || pretf.getText().matches(".*\\d.*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le nom et le prénom ne doivent pas contenir de chiffres !");
            alert.showAndWait();
            return;
            
         }
        
        else if (!emailtf.getText().matches("\\w+@\\w+\\.\\w+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("L'adresse email est invalide !");
            alert.showAndWait();
            return;
        }

         else if (!teltf.getText().matches("\\d{8}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le numéro de téléphone doit contenir 8 chiffres !");
            alert.showAndWait();
            return;
        } else if (!passwdtf.getText().matches("(?=.*[A-Z])(?=.*\\d).+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le mot de passe doit contenir au moins une lettre majuscule et un chiffre !");
            alert.showAndWait();
            return;
        }
    // Get the selected user from the table view
    User selectedUser = utilisateur.getSelectionModel().getSelectedItem();

    // Check that a user is actually selected
    if (selectedUser != null) {
        // Create an alert message
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Êtes-vous sûr(e) de vouloir modifier cet utilisateur ?");
        alert.setContentText("Cliquez sur OK pour confirmer ou sur Cancel pour revenir ");

        // Show the alert and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();

        // If the user clicks OK, update the user's properties with the values entered in the text fields
        if (result.get() == ButtonType.OK){
            selectedUser.setNom(nomtf.getText());
            selectedUser.setPrenom(pretf.getText());
            selectedUser.setEmail(emailtf.getText());
            selectedUser.setTel((teltf.getText()));
            selectedUser.setAdresse(adrtf.getText());
            selectedUser.setAge(agetf.getValue());
            selectedUser.setPasswd(passwdtf.getText());

            // Update the user in the database
            userService.update(selectedUser);

            // Refresh the table view to reflect the changes
            utilisateur.refresh();
        }
    }
}

    @FXML
    private void returnadmin(ActionEvent event) throws IOException {
        Parent connecterParent = FXMLLoader.load(getClass().getResource("Connecter.fxml"));
        Scene connecterScene = new Scene(connecterParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(connecterScene);
        window.show();
}

    @FXML
    private void rech(ActionEvent event) {
    }
}
