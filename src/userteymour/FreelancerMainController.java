

package userteymour;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mysql.jdbc.PreparedStatement;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
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
    private Label allezfreelacner;
    @FXML
    private Label ageLabel;
    @FXML
    private ImageView qrcodee;
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
              LocalDate dob = resultSet.getDate("age").toLocalDate();
              int age = Period.between(dob, LocalDate.now()).getYears();
              InputStream image = resultSet.getBinaryStream("image");
              nameLabel.setText("Name: "+nom);
              emailLabel.setText("Email: "+email);
              ageLabel.setText("Age: " + age);
              setImage(image);
               generateQRCode();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PageMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

    

private void generateQRCode() {
    try {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String nomPrenom = nameLabel.getText();
        String email = emailLabel.getText();
        String message = "Bonjour " + nomPrenom + ", vous êtes connecté à Majesty Freelance avec l'email: " + email + " en tant que Freelancer";
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

    public void setEmail(String email) {
        this.email = email;
    }

public void setName(String name) {
    nameLabel.setText(name);
}
public void setEmailLabel(String email) {
    emailLabel.setText(email);
}
public void setAgeLabel(String age) {
    ageLabel.setText(age);
}
    public void setImage(InputStream image) {
        userImageView.setImage(new Image(image));
    }

  @FXML
private void GestionFreelancer(MouseEvent event) {
    try {
        // Load the GestionUser.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GestionFreelancer.fxml"));
        Parent root = loader.load();
        
        // Get the controller of the GestionUser.fxml file
        GestionFreelancerController gestionFreelancerController = loader.getController();
        
        // Set the email of the user in the GestionUserController
        gestionFreelancerController.setEmail(email);
        
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