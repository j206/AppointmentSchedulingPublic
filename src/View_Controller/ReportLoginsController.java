package View_Controller;

import Model.Login;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/** Controller for the "Login Attempts" scene. */
public class ReportLoginsController implements Initializable {
  private static ObservableList<Login> allLogins = FXCollections.observableArrayList();

  @FXML private Button cancelButton;
  @FXML private TableView<Login> loginTableView;
  @FXML private TableColumn<Login, String> loginUserColumn;
  @FXML private TableColumn<Login, String> loginAuthenticatedColumn;
  @FXML private TableColumn<Login, String> loginAttemptTimeColumn;

  /**
   * Displays all login attempts and whether authentication was successful. Parses
   * login_activity.txt, one attempt per line. This method also contains a lambda expression, which
   * concisely replaces what would have been a for loop.
   */
  public static ObservableList<Login> parseLoginLogs() throws IOException {
    allLogins.clear();
    List<String> logins = Files.readAllLines(new File("src/Main/login_activity.txt").toPath());

    logins.forEach(
        (login) -> {
          String[] tokens = login.split(" \\| ");
          String username = tokens[2].trim();
          String authenticated = tokens[1].trim();
          String attemptedAt = tokens[0].trim();

          Login l = new Login(username, authenticated, attemptedAt);
          allLogins.add(l);
        });
    return allLogins;
  }

  /**
   * Changes to the main dashboard scene.
   *
   * @param event button clicked event
   */
  public void displayMainScreen(ActionEvent event) throws IOException {
    Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
    Scene targetScene = new Scene(parent);

    Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
    window.setScene(targetScene);
    window.show();
  }

  /**
   * Initializes JavaFX elements. Queries database for necessary information and populates
   * ComboBoxes.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      loginTableView.setItems(parseLoginLogs());
    } catch (IOException e) {
      e.printStackTrace();
    }

    loginUserColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    loginAuthenticatedColumn.setCellValueFactory(new PropertyValueFactory<>("authenticated"));
    loginAttemptTimeColumn.setCellValueFactory(new PropertyValueFactory<>("attemptedAt"));
  }
}
