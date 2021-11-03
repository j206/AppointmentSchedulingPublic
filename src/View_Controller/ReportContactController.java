package View_Controller;

import Model.Appointment;
import Model.Contact;
import Utility.DBQuery;
import Utility.DBReport;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/** Controller for the "Schedules by Contact" scene. */
public class ReportContactController implements Initializable {
  private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();

  @FXML private Button cancelButton;
  @FXML private TableView<Appointment> contactScheduleTableView;
  @FXML private TableColumn<Appointment, Integer> appointmentIdColumn;
  @FXML private TableColumn<Appointment, String> appointmentTitleColumn;
  @FXML private TableColumn<Appointment, String> appointmentDescriptionColumn;
  @FXML private TableColumn<Appointment, String> appointmentTypeColumn;
  @FXML private TableColumn<Appointment, LocalDateTime> appointmentStartTimeColumn;
  @FXML private TableColumn<Appointment, LocalDateTime> appointmentEndTimeColumn;
  @FXML private TableColumn<Appointment, String> appointmentCustomerColumn;
  @FXML private ComboBox<Contact> contactComboBox;

  /** Displays all appointments for the selected contact. */
  public void contactComboBoxSelected() {
    ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
    contactAppointments.clear();

    Contact selectedContact = contactComboBox.getValue();
    if (selectedContact != null) {
      int selectedContactId = selectedContact.getId();
      contactAppointments = DBReport.getContactAppointments(selectedContactId);
      contactScheduleTableView.setItems(contactAppointments);
    }
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
    allContacts = DBQuery.getAllContacts();

    contactComboBox.setPromptText("Select Contact");
    contactComboBox.setItems(allContacts);

    appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    appointmentStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
    appointmentEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
    appointmentCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
  }
}
