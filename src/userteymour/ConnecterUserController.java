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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.UserService;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;

//import javax.mail.Message;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.Transport;
import java.security.MessageDigest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

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
    @FXML
    private ToggleButton showbtnnewnew;
    @FXML
    private Label mdpshow;
    private boolean labelVisible = false;
    @FXML
    private TextField vertel;
    private String code;
    private String codeemail;
    
    public static final String ACCOUNT_SID = "AC888b21cc1072373d1fb728a2315dc79f";
public static final String AUTH_TOKEN = "e84b2bb96edad35f14b680389a128a28";
    @FXML
    private Button sendcode;
    @FXML
    private Button codeee;
    @FXML
    private TextField codee;
public String encryptPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return null;
    }
}

    
private String generateCode() {
    // Generate a 6-digit random code
    Random random = new Random();
    int code = 100000 + random.nextInt(900000);
    return Integer.toString(code);
}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
                toggleGroup = new ToggleGroup();
        hommeb.setToggleGroup(toggleGroup);
        femmeb.setToggleGroup(toggleGroup);
            mdpshow.textProperty().bind(tfpasswd.textProperty());

    mdpshow.setVisible(false);
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
        
        else if (!tfemail.getText().matches("\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w+([\\.-]?\\w+)*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("L'adresse email est invalide !");
            alert.showAndWait();
            return;
        }

         else if (!tftel.getText().matches("\\+216\\d{8}")) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Erreur de saisie");
    alert.setHeaderText(null);
    alert.setContentText("Le numéro de téléphone doit commencer par +216 et contenir 8 chiffres après !");
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
        if (!code.equals(vertel.getText())) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Erreur de saisie");
    alert.setHeaderText(null);
    alert.setContentText("Le code de vérification est incorrect !");
    alert.showAndWait();
    return;
}
        String encryptedPassword = encryptPassword(tfpasswd.getText());

        role r1 = new role (2, "User");
        String nom = tfnom.getText();
        String prenom = tfprenom.getText();
        String tel = tftel.getText();
        String adresse = tfadresse.getText();
        String email = tfemail.getText();
       // String passwd = tfpasswd.getText();
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
        User u = new User(nom, prenom, tel, adresse, r1, email, encryptedPassword, birthdate, sexe, image);

         // check the verification code
    if (!codee.getText().equals(codeemail)) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Code de vérification incorrect");
        alert.setHeaderText(null);
        alert.setContentText("Le code de vérification que vous avez saisi est incorrect !");
        alert.showAndWait();
        return;}
    
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

    @FXML
    private void showuserpw(ActionEvent event) {
                            labelVisible = !labelVisible; // toggle the visibility
    mdpshow.setVisible(labelVisible);
    }
    
    

int verificationCode = (int) (Math.random() * 900000) + 100000;

    @FXML
private void sendcode(ActionEvent event) {
    Random rand = new Random();
    code = String.format("%04d", rand.nextInt(10000));
    System.out.println("Code: " + code); // for testing purposes

    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    com.twilio.rest.api.v2010.account.Message twilioMessage = 
        com.twilio.rest.api.v2010.account.Message.creator(new PhoneNumber(tftel.getText()), 
        new PhoneNumber("+12706481625"), "Votre code de vérification est: " + code).create();

    // notify the user
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Verification code sent");
    alert.setHeaderText(null);
    alert.setContentText("A verification code has been sent to your phone number.");
    alert.showAndWait();
}

@FXML
private void CodeMail(ActionEvent event) throws MessagingException {
    // generate a verification code
    codeemail = generateCode();
    
    // create properties for the email session
    Properties properties = new Properties();
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.port", "587");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");

    // create a session with authentication
    Session session = Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("pidevmajesty@gmail.com", "xfbyslhggajvfdjz");
        }
    });

    // Create a Mail message
javax.mail.Message mailMessage = new MimeMessage(session);
mailMessage.setFrom(new InternetAddress("your-email@gmail.com"));
mailMessage.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(tfemail.getText()));
mailMessage.setSubject("Verification code");
mailMessage.setText("Your verification code is: " + codeemail);

    // send the email
    Transport.send(mailMessage);
    
    // notify the user
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Verification code sent");
    alert.setHeaderText(null);
    alert.setContentText("A verification code has been sent to your email address.");
    alert.showAndWait();
}


  

}



