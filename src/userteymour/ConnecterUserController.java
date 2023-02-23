
package userteymour;

import entities.User;
import entities.role;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.UserService;

public class ConnecterUserController implements Initializable {

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
    private TextField tfpasswd;
    @FXML
    private Button inscrire;
    @FXML
    private DatePicker ftage;
    @FXML
    private Button btnreturn;
    @FXML
    private RadioButton hommeb;
    @FXML
    private RadioButton femmeb;
    @FXML
    private Button imagebtn;
    @FXML
    private TextField imgurltf;
    @FXML
    private ImageView imgusr;
    
     private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
                toggleGroup = new ToggleGroup();
        hommeb.setToggleGroup(toggleGroup);
        femmeb.setToggleGroup(toggleGroup);
    }

    @FXML
    private void sauvgarder(ActionEvent event) throws IOException {
        if (tfnom.getText().isEmpty() || tfprenom.getText().isEmpty() || tfemail.getText().isEmpty() ||
                tftel.getText().isEmpty() || tfadresse.getText().isEmpty() || tfpasswd.getText().isEmpty() ||
                ftage.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }
        
         else if (tfnom.getText().matches(".*\\d.*") || tfprenom.getText().matches(".*\\d.*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le nom et le prénom ne doivent pas contenir de chiffres !");
            alert.showAndWait();
            return;
            
         }
        
        else if (!tfemail.getText().matches("\\w+@\\w+\\.\\w+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("L'adresse email est invalide !");
            alert.showAndWait();
            return;
        }

         else if (!tftel.getText().matches("\\d{8}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le numéro de téléphone doit contenir 8 chiffres !");
            alert.showAndWait();
            return;
        } else if (!tfpasswd.getText().matches("(?=.*[A-Z])(?=.*\\d).+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le mot de passe doit contenir au moins une lettre majuscule et un chiffre !");
            alert.showAndWait();
            return;
        }
        role r1 = new role (2, "User");
        String nom = tfnom.getText();
        String prenom = tfprenom.getText();
        int tel = Integer.parseInt(tftel.getText());
        String adresse = tfadresse.getText();
        String email = tfemail.getText();
        String passwd = tfpasswd.getText();
         String sexe;
        if (hommeb.isSelected()) {
            sexe = "Homme";
        } else if (femmeb.isSelected()) {
            sexe = "Femme";
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner le sexe !");
            alert.showAndWait();
            return;
        }
       FileInputStream image = new FileInputStream(imgurltf.getText());
  
LocalDate birthdate = LocalDate.of(ftage.getValue().getYear(), ftage.getValue().getMonthValue(), ftage.getValue().getDayOfMonth());
        int age = Period.between(birthdate, LocalDate.now()).getYears();    
        User u = new User(nom, prenom, tel, adresse, r1, email, passwd, birthdate, sexe, image);

      
           // check if email already exists
    UserService userService = new UserService() {};
    List<User> users = userService.readAll();
    for (User user : users) {
        if (user.getEmail().equals(email)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("L'adresse email existe déjà !");
            alert.showAndWait();
            return;
        }
    }

    // email doesn't exist, continue with saving the user
  //  role r1 = new role(2, "User");
        
        userService.insert(u);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Utilisateur ajouté avec succès !");
        alert.showAndWait();

        tfnom.setText("");
        tfprenom.setText("");
        tftel.setText("");
        tfadresse.setText("");
        tfemail.setText("");
        tfpasswd.setText("");
        ftage.setValue(null);
    }

    @FXML
    private void return1(ActionEvent event) throws IOException {
        Parent connecterParent = FXMLLoader.load(getClass().getResource("Connecter.fxml"));
        Scene connecterScene = new Scene(connecterParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(connecterScene);
        window.show();
    }

 @FXML
private void importimage(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choisir une image");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Images", ".png", ".jpg", "*.gif");
    fileChooser.getExtensionFilters().add(imageFilter);
    File selectedFile = fileChooser.showOpenDialog(null);
    if (selectedFile != null) {
        try {
            FileInputStream fileInputStream = new FileInputStream(selectedFile);
            Image image = new Image(fileInputStream);
            imgusr.setImage(image);
            imgurltf.setText(selectedFile.getAbsolutePath());
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'importation de l'image !");
            alert.showAndWait();
        }
    }
}

@FXML
private void importimg(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choisir une image");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Images", ".png", ".jpg", "*.gif");
    fileChooser.getExtensionFilters().add(imageFilter);
    File selectedFile = fileChooser.showOpenDialog(null);
    
    if (selectedFile != null) {
        imgurltf.setText(selectedFile.getAbsolutePath()); // Set the path of the selected file to the text field
        imgusr.setImage(new Image(selectedFile.toURI().toString())); // Set the image of the selected file to the image view
    }
}
}



