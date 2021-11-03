package Main;

import Utility.AlertMessage;
import Utility.DBConnection;
import Utility.SessionContext;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Primary application. */
public class AppointmentScheduling extends Application {

  /** Launches application, opens MYSQL database connection, and initializes SessionContext. */
  public static void main(String[] args) {
    DBConnection.startConnection();
    SessionContext.init();
    launch(args);
  }

  /** Initializes start-up JavaFX stage.
   *
   * @param primaryStage stage to show main dashboard*/
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    primaryStage.setResizable(false);
    primaryStage.setTitle("Appointment and Customer Management");
    primaryStage.setOnCloseRequest(
        event -> {
          boolean confirmExit = AlertMessage.confirmationAlert(4, null, null);
          if (confirmExit) {
            DBConnection.closeConnection();
            Platform.exit();
          } else {
            event.consume();
          }
        });
  }
}
