package View_Controller;

import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Model.User;
import Utility.AlertMessage;
import Utility.DBAppointment;
import Utility.DBCustomer;
import Utility.DBQuery;
import Utility.DateTimeUtils;
import Utility.SessionContext;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Controller for the "Add Appointment" scene. */
public class AddAppointmentController implements Initializable {

  private final ObservableList<Integer> hours = FXCollections.observableArrayList();
  private final ObservableList<String> minutes = FXCollections.observableArrayList();
  private final ObservableList<String> amPm = FXCollections.observableArrayList();
  private ObservableList<Contact> allContacts = FXCollections.observableArrayList();
  private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
  private ObservableList<User> allUsers = FXCollections.observableArrayList();
  @FXML private Button addAppointmentButton;
  @FXML private Button cancelButton;
  @FXML private TextField appointmentTitleField;
  @FXML private TextField appointmentDescriptionField;
  @FXML private TextField appointmentLocationField;
  @FXML private TextField appointmentTypeField;
  @FXML private ComboBox<Customer> appointmentCustomerComboBox;
  @FXML private ComboBox<User> appointmentUserComboBox;
  @FXML private ComboBox<Contact> appointmentContactComboBox;
  @FXML private DatePicker appointmentDatePicker;
  @FXML private ComboBox<Integer> appointmentStartHourComboBox;
  @FXML private ComboBox<String> appointmentStartMinuteComboBox;
  @FXML private ComboBox<String> appointmentStartAmPmComboBox;
  @FXML private ComboBox<Integer> appointmentEndHourComboBox;
  @FXML private ComboBox<String> appointmentEndMinuteComboBox;
  @FXML private ComboBox<String> appointmentEndAmPmComboBox;

  /**
   * Saves new appointment to database using inputted data.
   *
   * <p>First, elements are confirmed to be filled/selected by validateAppointmentForm.
   * If filled/selected, the inputted data is parsed into variables.
   *
   * <p>Secondly, runs a set of checks, DateTimeUtils.validateAppointmentDateTime, to
   * validate appointment time to project specification.
   *
   * <p>If all data is validated, saves the new appointment to the database, and closes the window.
   *
   * @param event button clicked event
   */
  public void addAppointmentButtonClicked(ActionEvent event) throws IOException {
    // Validate fields are not empty
    if (validateAppointmentForm()) {
      // Parse elements necessary for scheduling validation first
      LocalDate date = appointmentDatePicker.getValue();
      LocalTime startTime =
          DateTimeUtils.parseTimeInput(
              appointmentStartHourComboBox,
              appointmentStartMinuteComboBox,
              appointmentStartAmPmComboBox);
      LocalTime endTime =
          DateTimeUtils.parseTimeInput(
              appointmentEndHourComboBox, appointmentEndMinuteComboBox, appointmentEndAmPmComboBox);
      LocalDateTime start = LocalDateTime.of(date, startTime);
      LocalDateTime end = LocalDateTime.of(date, endTime);
      int customerId = appointmentCustomerComboBox.getValue().getId();

      // Validate for logical mistakes and adherence to project spec
      boolean dateTimeValidated =
          DateTimeUtils.validateAppointmentDateTime(start, end, customerId, 0);

      // Parse remaining elements, add to DB
      if (dateTimeValidated) {
        String title = appointmentTitleField.getText().trim();
        String description = appointmentDescriptionField.getText().trim();
        String location = appointmentLocationField.getText().trim();
        String type = appointmentTypeField.getText().trim();

        int userId = appointmentUserComboBox.getValue().getId();
        int contactId = appointmentContactComboBox.getValue().getId();
        String currentUser = SessionContext.getSessionContext().getCurrentUser().getUsername();

        Appointment newAppointment =
            new Appointment(
                customerId,
                userId,
                contactId,
                title,
                description,
                location,
                type,
                start,
                end,
                currentUser,
                currentUser);
        DBAppointment.addAppointment(newAppointment);
        AlertMessage.informationAlert(1, null, null);
        displayMainScreen(event);
      }
    }
  }

  /**
   * Closes the current window. Returns to main screen.
   *
   * @param event button clicked event
   */
  public void cancelButtonClicked(ActionEvent event) throws IOException {
    boolean confirmCancel = AlertMessage.confirmationAlert(1, null, null);
    if (confirmCancel) {
      displayMainScreen(event);
    }
  }

  /**
   * Reloads main screen and closes current window. Helper method.
   *
   * @param event button clicked event
   */
  public void displayMainScreen(ActionEvent event) throws IOException {
    Parent parent = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
    Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
    window.close();
  }

  /**
   * Validates input fields are not empty or unselected. Empty TextField borders are
   * changed to red.
   *
   * @return returns true if all elements are filled/selected
   */
  public boolean validateAppointmentForm() {
    resetFieldsStyle();
    TextField[] textFields = {
      appointmentTitleField,
      appointmentDescriptionField,
      appointmentLocationField,
      appointmentTypeField
    };

    // Check for empty fields
    for (TextField field : textFields) {
      if (field.getText().trim().isEmpty()) {
        AlertMessage.appointmentError(1, field);
        return false;
      }
    }

    // Check for empty ComboBoxes
    if (appointmentCustomerComboBox.getValue() == null) {
      AlertMessage.appointmentError(2, null);
      return false;
    } else if (appointmentUserComboBox.getValue() == null) {
      AlertMessage.appointmentError(3, null);
      return false;
    } else if (appointmentContactComboBox.getValue() == null) {
      AlertMessage.appointmentError(4, null);
      return false;
    } else if (appointmentDatePicker.getValue() == null) {
      AlertMessage.appointmentError(5, null);
      return false;
    } else if (appointmentStartHourComboBox.getValue() == null
        || appointmentStartMinuteComboBox.getValue() == null
        || appointmentStartAmPmComboBox.getValue() == null) {
      AlertMessage.appointmentError(6, null);
      return false;
    } else if (appointmentEndHourComboBox.getValue() == null
        || appointmentEndMinuteComboBox.getValue() == null
        || appointmentEndAmPmComboBox.getValue() == null) {
      AlertMessage.appointmentError(7, null);
      return false;
    }
    return true;
  }

  /** Populates date/time selection elements. Formatting method. */
  public void initializeDateTimeBoxes() {
    hours.clear();
    for (int i = 1; i < 13; i++) {
      hours.add(i);
    }
    appointmentStartHourComboBox.setItems(hours);
    appointmentEndHourComboBox.setItems(hours);

    minutes.clear();
    minutes.addAll(":00", ":15", ":30", ":45");
    appointmentStartMinuteComboBox.setItems(minutes);
    appointmentEndMinuteComboBox.setItems(minutes);

    amPm.clear();
    amPm.addAll("A.M.", "P.M.");
    appointmentStartAmPmComboBox.setItems(amPm);
    appointmentEndAmPmComboBox.setItems(amPm);
  }

  /** Resets TextField borders to grey. */
  public void resetFieldsStyle() {
    appointmentTitleField.setStyle("-fx-border-color: lightgrey");
    appointmentDescriptionField.setStyle("-fx-border-color: lightgrey");
    appointmentLocationField.setStyle("-fx-border-color: lightgrey");
    appointmentTypeField.setStyle("-fx-border-color: lightgrey");
  }

  /**
   * Initializes JavaFX elements. Queries database for necessary information and populates
   * ComboBoxes.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    resetFieldsStyle();

    allContacts = DBQuery.getAllContacts();
    allCustomers = DBCustomer.getAllCustomers();
    allUsers = DBQuery.getAllUsers();

    appointmentUserComboBox.setPromptText("-");
    appointmentCustomerComboBox.setPromptText("-");
    appointmentContactComboBox.setPromptText("-");
    appointmentContactComboBox.setItems(allContacts);
    appointmentCustomerComboBox.setItems(allCustomers);
    appointmentUserComboBox.setItems(allUsers);
    appointmentUserComboBox.setValue(SessionContext.getSessionContext().getCurrentUser());
    initializeDateTimeBoxes();
  }
}
