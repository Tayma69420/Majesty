/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfaihi;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MessageController {
    // Replace with your Twilio Account SID and Auth Token
    public static final String ACCOUNT_SID = "ACd63aafd60878d863e38288d64f4cccca";
    public static final String AUTH_TOKEN = "cc28e9da325c996b40a3a40928e2f8f6";
    public static final String TWILIO_NUMBER = "+12763889587";

    private Label statusLabel;

    @FXML
    private TextField textfield;
    @FXML
    private Label alert;

    @FXML
    private void sendSMS() {
    String toPhoneNumber = textfield.getText();
    if (toPhoneNumber == null || toPhoneNumber.trim().isEmpty()) {
        alert.setText("Please enter a phone number.");
        return;
    }

   
Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String messageText = "portfolio added le " + formatter.format(currentDate);
    Message message = Message.creator(new PhoneNumber(toPhoneNumber),
            new PhoneNumber(TWILIO_NUMBER),
            messageText).create();

    if (message.getSid() != null) {
        statusLabel.setText("SMS sent successfully to " + toPhoneNumber + "!");
    } else {
        statusLabel.setText("Error sending SMS to " + toPhoneNumber + ".");
    }
}

}
