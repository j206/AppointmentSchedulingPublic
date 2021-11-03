package View_Controller;

import Model.Appointment;
import Model.User;
import Utility.AlertMessage;
import Utility.DBQuery;
import Utility.DBReport;
import Utility.SessionContext;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Controller for the "Login" scene. */
public class LoginController implements Initializable {

  @FXML private Button loginButton;
  @FXML private Button exitButton;
  @FXML private TextField userTextField;
  @FXML private PasswordField passPasswordField;
  @FXML private Label mainLabel;
  @FXML private Label localeLabel;

  /**
   * Logs user in with valid credentials.
   *
   * <p>First, confirms that both username and password are filled in.
   *
   * <p>Then, validates inputted user/pass against the database. If valid, logs to a local .txt file
   * and changes scene to main screen.
   *
   * @param event button clicked event
   */
  public void loginButtonClicked(ActionEvent event) throws IOException {
    // Check for empty fields
    String username = userTextField.getText().trim();
    String password = passPasswordField.getText().trim();

    if (username.isEmpty() || password.isEmpty()) {
      AlertMessage.loginError(1);
      return;
    }

    // Initialize logging
    String log = "src/Main/login_activity.txt";
    FileWriter fileWriter = new FileWriter(log, true);
    PrintWriter writeLoginAttempt = new PrintWriter(fileWriter);

    // Authenticate
    User currentUser = DBQuery.authenticateUser(username, password);
    if (currentUser != null) {
      SessionContext.getSessionContext().setCurrentUser(currentUser);
      SessionContext.getSessionContext().setCurrentLocale(Locale.getDefault());
      writeLoginAttempt.println(LocalDateTime.now() + " | SUCCESS | " + username);
      fileWriter.flush();

      checkNextFifteenMinutes(currentUser.getId());

      displayMainScreen(event, "/View_Controller/MainScreen.fxml");
    } else {
      writeLoginAttempt.println(LocalDateTime.now() + " | FAILURE | " + username);
      fileWriter.flush();
      passPasswordField.clear();
      AlertMessage.loginError(2);
    }
  }

  /** Exits the application. */
  public void exitButtonClicked() {
    boolean confirmExit = AlertMessage.confirmationAlert(4, null, null);
    if (confirmExit) {
      Platform.exit();
    }
  }

  /**
   * Checks if validated user has appointment within 15 minutes. Uses User object to query database
   * for relevant appointment(s).
   *
   * @param currentUserId ID of authenticated user
   */
  public void checkNextFifteenMinutes(int currentUserId) {
    Appointment imminentAppointment = DBReport.queryNextFifteenMinutes(currentUserId);
    if (imminentAppointment != null) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd 'starts at' h:mm a");
      String start = imminentAppointment.getStart().format(formatter);

      AlertMessage.informationAlert(7, String.valueOf(imminentAppointment.getId()), start);
    } else {
      AlertMessage.informationAlert(8, null, null);
    }
  }

  /**
   * Reloads main screen and closes current window. Helper method.
   *
   * @param event button clicked event
   * @param fxmlResource target FXML
   */
  public void displayMainScreen(ActionEvent event, String fxmlResource) throws IOException {
    Parent parent = FXMLLoader.load(getClass().getResource(fxmlResource));
    Scene targetScene = new Scene(parent);

    Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
    window.setScene(targetScene);
    window.show();
  }

  /** Initializes JavaFX elements. Contains localization. */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ResourceBundle messagesBundle =
        ResourceBundle.getBundle("Utility/MessagesBundle", Locale.getDefault());
    loginButton.setText(messagesBundle.getString("loginButton"));
    exitButton.setText(messagesBundle.getString("exitButton"));
    userTextField.setPromptText(messagesBundle.getString("usernamePrompt"));
    passPasswordField.setPromptText(messagesBundle.getString("passwordPrompt"));
    mainLabel.setText(messagesBundle.getString("mainLabel"));
    localeLabel.setText(
        messagesBundle.getString("localeLabel") + ZoneId.systemDefault().getDisplayName(TextStyle.FULL, Locale.getDefault()));
  }
}
