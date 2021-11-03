package Utility;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

/** Utility class for pop-up dialog boxes. */
public class AlertMessage {

  /**
   * Alert box with non-critical information.
   *
   * @param code content reference code
   * @param info1 first information string
   * @param info2 second information string
   */
  public static void informationAlert(int code, String info1, String info2) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("");
    alert.setHeaderText("");
    switch (code) {
      case 1:
        {
          alert.setContentText("Appointment successfully added!");
          break;
        }
      case 2:
        {
          alert.setContentText("Customer successfully added!");
          break;
        }
      case 3:
        {
          alert.setContentText("Appointment successfully modified!");
          break;
        }
      case 4:
        {
          alert.setContentText("Customer successfully modified!");
          break;
        }
      case 5:
        {
          alert.setContentText(info1 + "appointment (ID: " + info2 + ") successfully deleted.");
          break;
        }
      case 6:
        {
          alert.setContentText("Customer (" + info1 + ") successfully deleted.");
          break;
        }
      case 7:
        {
          alert.setContentText(
              "You have an appointment within the next 15 minutes.\n"
                  + "Appointment #"
                  + info1
                  + " on "
                  + info2
                  + ".");
          break;
        }
      case 8:
        {
          alert.setContentText("You have no appointments in the next 15 minutes.");
          break;
        }
      default:
        {
          alert.setContentText("An unknown error has occurred.");
          break;
        }
    }
    alert.showAndWait();
  }

  /**
   * Alert box for login errors. Contains localization.
   *
   * @param code content reference code
   */
  public static void loginError(int code) {
    ResourceBundle messagesBundle =
        ResourceBundle.getBundle("Utility/MessagesBundle", Locale.getDefault());
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("");
    alert.setHeaderText(messagesBundle.getString("loginErrorHeader"));
    switch (code) {
      case 1:
        {
          alert.setContentText(messagesBundle.getString("emptyFieldErrorContent"));
          break;
        }
      case 2:
        {
          alert.setContentText(messagesBundle.getString("incorrectCredentialsContent"));
          break;
        }
      default:
        {
          alert.setContentText("An unknown error has occurred.");
          break;
        }
    }
    alert.showAndWait();
  }

  /**
   * Alert box for confirmation. Requires user input. Contains localization.
   *
   * @param code content reference code
   * @param info1 first information string
   * @param info2 second information string
   */
  public static boolean confirmationAlert(int code, String info1, String info2) {
    ResourceBundle messagesBundle =
        ResourceBundle.getBundle("Utility/MessagesBundle", Locale.getDefault());
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("");
    switch (code) {
      case 1:
        {
          alert.setHeaderText("Are you sure you want to cancel?");
          alert.setContentText("Any unsaved changes will be lost.");
          break;
        }
      case 2:
        {
          alert.setHeaderText("Are you sure you want to delete the selected customer?");
          alert.setContentText(info1 + " will be permanently deleted.");
          break;
        }
      case 3:
        {
          alert.setHeaderText("Are you sure you want to delete the selected appointment?");
          alert.setContentText(info1 + " appointment (ID: " + info2 + ") will be permanently deleted.");
          break;
        }
      case 4:
        {
          alert.setHeaderText(messagesBundle.getString("exitConfirmHeader"));
          alert.setContentText(messagesBundle.getString("exitConfirmContent"));
          break;
        }
      case 5:
        {
          alert.setHeaderText("Are you sure you want to log out?");
          alert.setContentText("Any unsaved changes will be lost.");
          break;
        }
    }
    Optional<ButtonType> result = alert.showAndWait();
    return result.get() == ButtonType.OK;
  }

  /**
   * Alert box for customer errors. Marks field with error in red.
   *
   * @param code content reference code
   * @param field field containing error
   */
  public static void customerError(int code, TextField field) {
    fieldError(field);

    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("");
    alert.setHeaderText("Error");
    switch (code) {
      case 1:
        {
          alert.setContentText("Please enter customer information into all fields.");
          break;
        }
      case 2:
        {
          alert.setContentText("Please select a country and a state or province.");
          break;
        }
      default:
        {
          alert.setContentText("An unknown error has occurred.");
          break;
        }
    }
    alert.showAndWait();
  }

  /**
   * Alert box for appointment errors. Marks field with error in red.
   *
   * @param code content reference code
   * @param field field containing error
   */
  public static void appointmentError(int code, TextField field) {
    fieldError(field);

    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("");
    alert.setHeaderText("Error");
    switch (code) {
      case 1:
        {
          alert.setContentText("Please enter appointment information into all fields.");
          break;
        }
      case 2:
        {
          alert.setContentText("Please select a customer for the appointment.");
          break;
        }
      case 3:
        {
          alert.setContentText("Please select the user requesting the appointment.");
          break;
        }
      case 4:
        {
          alert.setContentText("Please select a contact for the appointment.");
          break;
        }
      case 5:
        {
          alert.setContentText("Please select a date for the appointment.");
          break;
        }
      case 6:
        {
          alert.setContentText("Please select a start time for the appointment.");
          break;
        }
      case 7:
        {
          alert.setContentText("Please select an end time for the appointment.");
          break;
        }
      case 8:
        {
          alert.setContentText("Start time must be before end time.");
          break;
        }
      case 9:
        {
          alert.setContentText("Appointment must be entire within business hours.");
          break;
        }
      case 10:
        {
          alert.setContentText(
              "Entered time conflicts with another appointment for that customer.");
          break;
        }
      default:
        {
          alert.setContentText("An unknown error has occurred.");
          break;
        }
    }
    alert.showAndWait();
  }

  /**
   * Changes TextField border to red.
   *
   * @param field field containing error
   */
  public static void fieldError(TextField field) {
    if (field != null) {
      field.setStyle("-fx-border-color: red");
    }
  }
}
