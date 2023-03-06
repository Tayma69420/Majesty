/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userteymour;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.Properties;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
import utils.MyConx;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.Statement;
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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import entities.panier;





/**
 * FXML Controller class
 *
 * @author farouk daadaa
 */

public class CheckoutController implements Initializable {

    @FXML
    private RadioButton visabutton;
    @FXML
    private RadioButton masterbutton;
    @FXML
    private RadioButton dinarbutton;
    @FXML
    private TextField cn;
    @FXML
    private TextField exdate;
    @FXML
    private TextField fname;
    @FXML
    private TextField lname;
    @FXML
    private TextField promo;
    @FXML
    private Button aply;
    @FXML
    private PasswordField scode;
    @FXML
    private Button conPay;
    @FXML
    private Label tot;
    @FXML
    private Button retour;
    @FXML
    private Button bws;
    @FXML
    private Button imp;
  
   private Connection conn;
   
   
private PreparedStatement prepare;
    private ResultSet result;
    @FXML
    private ImageView qrcode;

    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
   @Override
public void initialize(URL url, ResourceBundle rb) {
        
    try {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        StringBuilder sb = new StringBuilder();

        // Retrieve the data from the database
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projet_freelance", "root", "");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nom, prix, qnt FROM panier");

        while (rs.next()) {
            sb.append(" Nom produit "+rs.getString("nom")).append(",");
            sb.append("Prix Produit"+rs.getFloat("prix")).append(",");
            sb.append("Quantite"+rs.getInt("qnt")).append(";");
        }

        String information = sb.toString();
        int width = 300;
        int height = 300;

        BufferedImage bufferedImage = null;
        BitMatrix byteMatrix = qrCodeWriter.encode(information, BarcodeFormat.QR_CODE, width, height);
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

        qrcode.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
    } catch (WriterException | SQLException ex) {
        ex.printStackTrace();
    }

    imp.setVisible(false);

    imp.setVisible(true);
    try {
        // Call the calculateTotal() method to get the total price of all items in the cart
        float total = calculateTotal();
        // Update the text of the label tot with the total value
        tot.setText(String.format("%.2f DT", total));
    } catch (SQLException ex) {
        Logger.getLogger(CheckoutController.class.getName()).log(Level.SEVERE, null, ex);
    }
}
 
    @FXML
    private void cardnumber(ActionEvent event) {
    }

    @FXML
    private void expirationDate(ActionEvent event) {
    }

    @FXML
    private void firstname(ActionEvent event) {
    }

    @FXML
    private void lastname(ActionEvent event) {
    }

    @FXML
    private void promocode(ActionEvent event) {
    }
    
    
@FXML
private void apply(ActionEvent event) {
    String code = promo.getText();
    if (code.equals("CODE10")) {
        try {
            float total = calculateTotal();
            float newTotal = total * 0.9f;
            tot.setText(String.format("%.2f TND", newTotal));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Code promo appliqué");
            alert.setHeaderText(null);
            alert.setContentText("Le code promo a été appliqué avec succès.");
            alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(CheckoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Code promo invalide");
        alert.setHeaderText(null);
        alert.setContentText("Le code promo est invalide. Veuillez réessayer.");
        alert.showAndWait();
    }
}



    @FXML
    private void securitycode(ActionEvent event) {
    }
    
    
    
    
private float calculateTotal() throws SQLException {
    float total = 0.0f;
    Connection conn = MyConx.getInstance().getCnx();
    String selectQuery = "SELECT SUM(prix*qnt) as total FROM panier";
    PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
    ResultSet rs = selectStmt.executeQuery();
    if (rs.next()) {
        total = rs.getFloat("total");
    }
    return total;
}




@FXML
private void confirmerEtPayer(ActionEvent event) throws SQLException, AddressException, MessagingException {
    if (!visabutton.isSelected() && !masterbutton.isSelected() && !dinarbutton.isSelected()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("Aucun des boutons radio n'est sélectionné");
        alert.showAndWait();
        return;
    }
    if (cn.getText().isEmpty() || cn.getText().length() != 16) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("Le numéro de carte doit contenir 16 chiffres.");
        alert.showAndWait();
        return;
    }
    if (exdate.getText().isEmpty() || exdate.getText().length() != 5) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("La date d'expiration doit être au format MM/AA.");
        alert.showAndWait();
        return;
    }
    if (scode.getText().isEmpty() || scode.getText().length() != 3) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("Le code de sécurité doit contenir 3 chiffres.");
        alert.showAndWait();
        return;
    }
    if (fname.getText().isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez entrer votre prénom.");
        alert.showAndWait();
        return;
    }
    if (lname.getText().isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez entrer votre nom de famille.");
        alert.showAndWait();
        return ;
    }
  // Ask for confirmation before deleting the cart items
    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmAlert.setTitle("Confirmer la suppression du panier");
    confirmAlert.setHeaderText(null);
    confirmAlert.setContentText("Êtes-vous sûr de vouloir achetez les produit ?");
    Optional<ButtonType> result = confirmAlert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        Connection conn = MyConx.getInstance().getCnx();

          // Calculate the total price of all items in the cart
    float total = calculateTotal();
    float newTotal = total;
    String code = promo.getText();
    if (!code.isEmpty()) {
        if (code.equals("CODE10")) {
            newTotal = total * 0.9f;
        }
    } else {
        newTotal = total;
    }

    // Set the total price to the tot label
    tot.setText(String.format("%.2f TND", newTotal));


    String cardNumber = cn.getText();
    String expirationDate = exdate.getText();
    int securityCode = Integer.parseInt(scode.getText());
    String firstName = fname.getText();
    String lastName = lname.getText();

    String query = "INSERT INTO facture (cardnumber, expirationdate, securitycode, firstname, lastname, total) VALUES (?, ?, ?, ?, ?, ?)";
    PreparedStatement stmt = conn.prepareStatement(query);
    stmt.setString(1, cardNumber);
    stmt.setString(2, expirationDate);
    stmt.setInt(3, securityCode);
    stmt.setString(4, firstName);
    stmt.setString(5, lastName);
    stmt.setFloat(6, newTotal);

    String deleteQuery = "DELETE FROM panier";
    PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
    deleteStmt.executeUpdate();
    stmt.executeUpdate();

    // Retrieve the total value from the facture table
    String totalQuery = "SELECT total FROM facture";
    PreparedStatement totalStmt = conn.prepareStatement(totalQuery);
    ResultSet rs = totalStmt.executeQuery();
    if (rs.next()) {
        float factureTotal = rs.getFloat("total");
        // Set the facture total to the tot label
        tot.setText(String.format("%.2f TND", factureTotal));
    }

    // Send email
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("majestyjavaa@gmail.com", "fvlgrswwftqzthvz");
        }
    });

    
    try {
        
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("majestyjavaa@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("farouk.daadaa@esprit.tn"));
        message.setSubject("Order confirmation");
        
        // Get the current date and time
        Date now = new Date();
        // Format the date and time using the "EEE, MMM d, yyyy hh:mm:ss a" format
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a");
        String formattedDate = dateFormat.format(now);
        // Build the message body with the customer's name and the current date
        String messageBody = String.format("Dear %s %s,\n\nThank you for your order. Your payment of %.2f TND has been processed successfully on %s.\n\nBest regards,\nMajesty", firstName, lastName, newTotal, formattedDate);


        message.setFrom(new InternetAddress("majestyjavaa@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("farouk.daadaa@esprit.tn"));
        message.setSubject("Order confirmation");
        message.setText(messageBody);

        Transport.send(message);

        System.out.println("Email sent successfully.");


    } catch (MessagingException e) {
        System.out.println("Failed to send email. Error message: " + e.getMessage());
    }
    {
    
     // If payment is successful, generate the PDF invoice
    boolean success = true; // Replace with actual code to process payment
    if (success) {
        
        // Show confirmation message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Paiement réussi");
        alert.setHeaderText(null);
        alert.setContentText("Le paiement a été effectué avec succès.");
        alert.showAndWait();
        
        // Ask user if they want to generate a PDF invoice
        Alert pdfAlert = new Alert(Alert.AlertType.CONFIRMATION);
        pdfAlert.setTitle("Générer une facture PDF");
        pdfAlert.setHeaderText(null);
        pdfAlert.setContentText("Voulez-vous générer une facture PDF pour cette commande?");
        
        ButtonType yesButton = new ButtonType("Oui");
        ButtonType noButton = new ButtonType("Non");
        pdfAlert.getButtonTypes().setAll(yesButton, noButton);
        
        Optional<ButtonType> result1 = pdfAlert.showAndWait();
        
        if (result1.get() == yesButton) {
            handlePdfButton(event);
        }
    } else {
        // Show error message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de paiement");
        alert.setHeaderText(null);
        alert.setContentText("Une erreur s'est produite lors du traitement de votre paiement.");
        alert.showAndWait();
    }

    

        
    }
    }
}





  @FXML
private void ret(ActionEvent event) throws IOException {
    // Charger la vue Details.fxml
    FXMLLoader loader = new FXMLLoader(getClass().getResource("details.fxml"));
    Parent root = loader.load();

    // Créer une nouvelle scène
    Scene scene = new Scene(root);

    // Obtenir la fenêtre actuelle et la remplacer par la nouvelle scène
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
}

    @FXML
    private void payeravecStripe(ActionEvent event) {
         // Charger la vue Details.fxml
    FXMLLoader loader = new FXMLLoader(getClass().getResource("StripePayment.fxml"));
    Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(DetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    // Créer une nouvelle scène
    Scene scene = new Scene(root);

    // Obtenir la fenêtre actuelle et la remplacer par la nouvelle scène
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
    }
    
    
    @FXML
    private void handlePdfButton(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save PDF File");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
    File file = fileChooser.showSaveDialog(null);
    if (file != null) {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            PdfPTable table = new PdfPTable(5); // 3 columns

            // Add table headers
            table.addCell("FirstName");
            table.addCell("LastName");
            table.addCell("CardNumber");
            table.addCell("ExpirationDate");
            table.addCell("Total");
            conn = MyConx.getInstance().getCnx();
            // Add table rows from the database
            String query = "SELECT * FROM facture";
            ResultSet resultSet = conn.createStatement().executeQuery(query);
            while (resultSet.next()) {
                 String firstname = resultSet.getString("firstname");
                  String lastname = resultSet.getString("lastname");
                   String cardnum = resultSet.getString("cardnumber");
                String expirationdate = resultSet.getString("expirationdate");
                float total = resultSet.getFloat("total");
               
                table.addCell(firstname); 
                table.addCell(lastname);//ism les attribut ml base 
                table.addCell(cardnum);
                table.addCell(expirationdate);
                table.addCell(Float.toString(total));
            }
            document.add(table);
            document.close();
            JOptionPane.showMessageDialog(null, "Les données du facture ont été exportées dans le fichier " + file.getName());
        } catch (FileNotFoundException | DocumentException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'exportation des données des factures : " + e.getMessage());
        }
    }
}


    



}