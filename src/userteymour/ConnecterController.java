package userteymour;


import java.io.IOException;
import java.io.InputStream;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javax.mail.PasswordAuthentication;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;


import javax.mail.Session;
import utils.MyConx;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Transport;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.application.Application;
import javax.mail.MessagingException;

import javafx.scene.web.WebView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import javafx.application.HostServices;
import javafx.scene.control.CheckBox;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;




public class ConnecterController  implements Initializable {

    @FXML
    private Button inscrire_user;
    @FXML
    private TextField donneradresse;
    @FXML
    private TextField password;
    @FXML
    private Button inscrire_freelancer;
    private Connection connection;
    @FXML
    private JFXButton show;
    @FXML
    private Label mpd;
private boolean labelVisible = false;
    @FXML
    private Button mdpoub;
     private Application application;
    private String captchaResponse;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXButton Connecter;
private Timeline timeline;
    @FXML
    private ImageView imageView;

    private void sendRequest(String captchaResponse) throws IOException {
        String secret = "6Le8eL4kAAAAAL_Xn9fmTNiDjeuJ-gu4TkzF5Zu0";
        String url = "https://www.google.com/recaptcha/api/siteverify";
        String parameters = "secret=" + URLEncoder.encode(secret, "UTF-8") + "&response=" + URLEncoder.encode(captchaResponse, "UTF-8");
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.writeBytes(parameters);
            outputStream.flush();
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            JsonObject json = new JsonParser().parse(response.toString()).getAsJsonObject();
            boolean success = json.get("success").getAsBoolean();
            if (success) {
                // reCAPTCHA was successful, continue with your code
            } else {
                // reCAPTCHA failed, show an error message
            }
        }
    }

    
     public void setApplication(Application application) {
        this.application = application;
    }
    
    // other methods in your controller
    
    public void someMethod() {
        HostServices hostServices = application.getHostServices();
        // use hostServices as needed
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
    
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
               mpd.textProperty().bind(password.textProperty());
               mpd.setVisible(false);
               

         
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
private void Connecter(ActionEvent event) throws SQLException, IOException, NoSuchAlgorithmException {

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
    String encryptedPassword = encryptPassword(mdp);
    String requete = "SELECT * FROM utilisateur WHERE email = ? AND passwd = ?";
    try (PreparedStatement statement = connection.prepareStatement(requete)) {
        statement.setString(1, email);
        statement.setString(2, encryptedPassword);
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
    String image = null;
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
            image = resultSet2.getString("image");
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
    int age = 0;
    try {
        PreparedStatement freelancerStatement = connection.prepareStatement("SELECT nom, prenom, image, age FROM utilisateur WHERE email = ?");
        freelancerStatement.setString(1, email);
        ResultSet freelancerResultSet = freelancerStatement.executeQuery();
        if (freelancerResultSet.next()) {
            String nom = freelancerResultSet.getString("nom");
            String prenom = freelancerResultSet.getString("prenom");
            if (nom != null && !nom.isEmpty() && prenom != null && !prenom.isEmpty()) {
                freelancerName = nom + " " + prenom;
            }
            freelancerImage = freelancerResultSet.getBinaryStream("image");
            LocalDate birthdate = freelancerResultSet.getDate("age").toLocalDate();
            LocalDate now = LocalDate.now();
            Period period = Period.between(birthdate, now);
            age = period.getYears();
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
            freelancerController.setName("Name: "+freelancerName);
        } else {
            // handle empty name
            freelancerController.setName("Unknown");
        }
        freelancerController.setEmailLabel("Mail: "+email);
        freelancerController.setImage(freelancerImage);
        freelancerController.setCurrentUserEmail(email);
        freelancerController.setAgeLabel("Age: "+age);
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

}

    @FXML
    private void showmdp(ActionEvent event) {
                    labelVisible = !labelVisible; // toggle the visibility
    mpd.setVisible(labelVisible);
    }


   @FXML
private void mdpoublier(ActionEvent event) throws SQLException, IOException, NoSuchAlgorithmException, MessagingException {

    connection = MyConx.getInstance().getCnx();
    String email = donneradresse.getText();
    if (email.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez saisir l'email.");
        alert.showAndWait();
        return;
    }
    // Generate a new password
    String newPassword = generateRandomPassword();
    // Encrypt the new password
    String encryptedPassword = encryptPassword(newPassword);
    // Update the user's password in the database
    String updateQuery = "UPDATE utilisateur SET passwd = ? WHERE email = ?";
    try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
        statement.setString(1, encryptedPassword);
        statement.setString(2, email);
        statement.executeUpdate();
    }
    // Send the new password to the user's email
    sendEmail(email, "Nouveau mot de passe", "Votre nouveau mot de passe est: " + newPassword);

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Succès");
    alert.setHeaderText(null);
    alert.setContentText("Un nouveau mot de passe a été envoyé à votre adresse email!");
    alert.showAndWait();
}


private String generateRandomPassword() {
    // Generate a new password with 8 characters
    String passwordChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder passwordBuilder = new StringBuilder();
    for (int i = 0; i < 8; i++) {
        int index = (int) (Math.random() * passwordChars.length());
        passwordBuilder.append(passwordChars.charAt(index));
    }
    return passwordBuilder.toString();
}
private void sendEmail(String to, String subject, String body) throws MessagingException {
    // Send an email
    // ...
    String from = "pidevmajesty@gmail.com";
    String password = "xfbyslhggajvfdjz";

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(from, password);
        }
    });

    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(from));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
    message.setSubject(subject);
    message.setText(body);

    Transport.send(message);
}





private void sendPasswordEmail(String email, String newPassword) throws Exception {
    // Set up the properties of the email
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    // Authenticate with the email server
    String username = "majestyjavaa@gmail.com"; // replace with your email address
    String password = "fvlgrswwftqzthvz"; // replace with your email password
    Session session = Session.getInstance(props, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    // Create a new email message
    MimeMessage message = new MimeMessage(session);
    message.setFrom(new InternetAddress(username));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
    message.setSubject("Nouveau mot de passe");
    message.setText("Votre nouveau mot de passe est : " + newPassword);

    // Send the email
    Transport.send(message);
}

    @FXML
    private void imgani(MouseEvent event) {
    }

@FXML
private void imganiEntered(MouseEvent event) {

}

@FXML
private void imganiExited(MouseEvent event) {

}






   
    




}






