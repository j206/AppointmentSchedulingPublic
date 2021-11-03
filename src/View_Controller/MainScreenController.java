package View_Controller;

import Model.Appointment;
import Model.Customer;
import Utility.AlertMessage;
import Utility.DBAppointment;
import Utility.DBConnection;
import Utility.DBCustomer;
import Utility.SessionContext;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controller for the main dashboard scene. Displayed after successful login. Contains navigation to
 * all appointment/customer interactions, as well as all reports.
 */
public class MainScreenController implements Initializable {

  private static final ObservableList<Appointment> allAppointments =
      DBAppointment.getAllAppointments();
  @FXML private Button exitButton;
  @FXML private Button addCustomerButton;
  @FXML private Button modifyCustomerButton;
  @FXML private Button addAppointmentButton;
  @FXML private Button modifyAppointmentButton;
  @FXML private Button reportCountsButton;
  @FXML private Button reportDivisionButton;
  @FXML private Button reportContactsButton;
  @FXML private Button reportLoginsButton;

  @FXML private TableView<Appointment> appointmentTableView;
  @FXML private TableColumn<Appointment, Integer> appointmentIdColumn;
  @FXML private TableColumn<Appointment, String> appointmentTitleColumn;
  @FXML private TableColumn<Appointment, String> appointmentDescriptionColumn;
  @FXML private TableColumn<Appointment, String> appointmentLocationColumn;
  @FXML private TableColumn<Appointment, String> appointmentContactColumn;
  @FXML private TableColumn<Appointment, String> appointmentTypeColumn;
  @FXML private TableColumn<Appointment, LocalDateTime> appointmentStartTimeColumn;
  @FXML private TableColumn<Appointment, LocalDateTime> appointmentEndTimeColumn;
  @FXML private TableColumn<Appointment, String> appointmentCustomerColumn;

  @FXML private ToggleGroup filterToggleGroup;
  @FXML private RadioButton allRadioButton;
  @FXML private RadioButton weekRadioButton;
  @FXML private RadioButton monthRadioButton;

  @FXML private TableView<Customer> customerTableView;
  @FXML private TableColumn<Customer, Integer> customerIdColumn;
  @FXML private TableColumn<Customer, String> customerNameColumn;
  @FXML private TableColumn<Customer, String> customerAddressColumn;
  @FXML private TableColumn<Customer, String> customerPostalCodeColumn;
  @FXML private TableColumn<Customer, String> customerDivisionColumn;
  @FXML private TableColumn<Customer, String> customerPhoneColumn;

  /** Opens "Add Appointment" window. */
  public void addAppointmentButtonClicked() throws IOException {
    showNewWindow("/View_Controller/AddAppointment.fxml", "Add Appointment");
  }

  /** Opens "Modify/Delete Appointment" window. */
  public void modifyAppointmentButtonClicked() throws IOException {
    Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
    if (!(selectedAppointment == null)) {
      FXMLLoader fxmlLoader =
          new FXMLLoader(getClass().getResource("/View_Controller/ModifyAppointment.fxml"));
      Parent root1 = (Parent) fxmlLoader.load();
      Stage stage = new Stage();
      stage.setScene(new Scene(root1));

      ModifyAppointmentController controller = fxmlLoader.getController();
      controller.initData(selectedAppointment);

      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initStyle(StageStyle.UTILITY);
      stage.centerOnScreen();
      stage.setResizable(false);
      stage.setTitle("Modify Appointment");

      stage.setOnCloseRequest(
          confirmation -> {
            boolean confirmExit = AlertMessage.confirmationAlert(1, null, null);
            if (!confirmExit) {
              confirmation.consume();
            }
          });
      stage.show();
    }
  }

  /** Displays all appointments in appointmentTableView. */
  public void allRadioButtonClicked() {
    appointmentTableView.setItems(allAppointments);
  }

  /**
   * Displays the current week's appointments in appointmentTableView. Creates a
   * FilteredList and utilizes Predicates represented using lambda expressions
   * to filter the list of all appointments.
   */
  public void weekRadioButtonClicked() {
    FilteredList<Appointment> filteredAppointments = new FilteredList<>(allAppointments);
    LocalDate startOfWeekMarker = LocalDate.now();
    LocalDate endOfWeekMarker = LocalDate.now();

    while (startOfWeekMarker.getDayOfWeek() != DayOfWeek.SUNDAY) {
      startOfWeekMarker = startOfWeekMarker.minusDays(1);
    }
    int startOfWeek = startOfWeekMarker.getDayOfMonth();

    while (endOfWeekMarker.getDayOfWeek() != DayOfWeek.SATURDAY) {
      endOfWeekMarker = endOfWeekMarker.plusDays(1);
    }
    int endOfWeek = endOfWeekMarker.getDayOfMonth();

    Predicate<Appointment> sameWeek =
        i ->
            i.getStart().getDayOfMonth() >= startOfWeek
                && i.getStart().getDayOfMonth() <= endOfWeek;
    filteredAppointments.setPredicate(sameWeek);

    appointmentTableView.setItems(filteredAppointments);
  }

  /**
   * Displays the current month's appointments in appointmentTableView. Creates a
   *
   * FilteredList and utilizes Predicates represented using lambda expressions
   * to filter the list of all appointments.
   */
  public void monthRadioButtonClicked() {
    FilteredList<Appointment> filteredAppointments = new FilteredList<>(allAppointments);
    int currentMonth = LocalDate.now().getMonthValue();

    Predicate<Appointment> sameMonth = i -> i.getStart().getMonthValue() == currentMonth;
    filteredAppointments.setPredicate(sameMonth);

    appointmentTableView.setItems(filteredAppointments);
  }

  /** Opens "Add Customer" window. */
  public void addCustomerButtonClicked() throws IOException {
    showNewWindow("/View_Controller/AddCustomer.fxml", "Add Customer");
  }

  /** Opens "Modify/Delete Customer" window. */
  public void modifyCustomerButtonClicked() throws IOException {
    Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
    if (!(selectedCustomer == null)) {
      FXMLLoader fxmlLoader =
          new FXMLLoader(getClass().getResource("/View_Controller/ModifyCustomer.fxml"));
      Parent root1 = (Parent) fxmlLoader.load();
      Stage stage = new Stage();
      stage.setScene(new Scene(root1));

      ModifyCustomerController controller = fxmlLoader.getController();
      controller.initData(selectedCustomer);

      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initStyle(StageStyle.UTILITY);
      stage.centerOnScreen();
      stage.setResizable(false);
      stage.setTitle("Modify Customer");

      stage.setOnCloseRequest(
          confirmation -> {
            boolean confirmExit = AlertMessage.confirmationAlert(1, null, null);
            if (!confirmExit) {
              confirmation.consume();
            }
          });
      stage.show();
    }
  }

  /**
   * Opens "Appointments by Type / Month" report window.
   *
   * @param event button clicked event
   */
  public void reportCountsButtonClicked(ActionEvent event) throws IOException {
    changeSceneButton(event, "/View_Controller/ReportCounts.fxml");
  }

  /**
   * Opens "Customers by State or Province" report window.
   *
   * @param event button clicked event
   */
  public void reportDivisionButtonClicked(ActionEvent event) throws IOException {
    changeSceneButton(event, "/View_Controller/ReportDivision.fxml");
  }

  /**
   * Opens "Schedules by Contact" report window.
   *
   * @param event button clicked event
   */
  public void reportContactsButtonClicked(ActionEvent event) throws IOException {
    changeSceneButton(event, "/View_Controller/ReportContact.fxml");
  }

  /**
   * Opens "Login Attempts" report window.
   *
   * @param event button clicked event
   */
  public void reportLoginsButtonClicked(ActionEvent event) throws IOException {
    changeSceneButton(event, "/View_Controller/ReportLogins.fxml");
  }

  /**
   * Logs out the current user. Also resets SessionContext to empty.
   *
   * @param event button clicked event
   */
  public void logoutButtonClicked(ActionEvent event) throws IOException {
    boolean confirmExit = AlertMessage.confirmationAlert(5, null, null);
    if (confirmExit) {
      SessionContext.resetContext();
      changeSceneButton(event, "/View_Controller/Login.fxml");
    }
  }

  /** Exits the application. */
  public void exitButtonClicked() {
    boolean confirmExit = AlertMessage.confirmationAlert(4, null, null);
    if (confirmExit) {
      DBConnection.closeConnection();
      Platform.exit();
    }
  }

  /**
   * Changes the current scene. Contrasted from showNewWindow, which opens a new
   * window.
   *
   * @param event button clicked event
   * @param fxmlResource location of target FXML file
   */
  public void changeSceneButton(ActionEvent event, String fxmlResource) throws IOException {
    Parent parent = FXMLLoader.load(getClass().getResource(fxmlResource));
    Scene targetScene = new Scene(parent);

    Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
    window.setScene(targetScene);
    window.show();
  }

  /**
   * Opens a new window. Contrasted from changeSceneButton, which changes the scene
   * within the same window.
   *
   * @param fxmlResource location of target FXML file
   * @param title display title of the new window
   */
  public void showNewWindow(String fxmlResource, String title) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlResource));
    Parent root1 = (Parent) fxmlLoader.load();
    Stage stage = new Stage();
    stage.setScene(new Scene(root1));

    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initStyle(StageStyle.UTILITY);
    stage.centerOnScreen();
    stage.setResizable(false);
    stage.setTitle(title);

    stage.setOnCloseRequest(
        confirmation -> {
          boolean confirmExit = AlertMessage.confirmationAlert(1, null, null);
          if (!confirmExit) {
            confirmation.consume();
          }
        });
    stage.show();
  }

  /**
   * Initializes JavaFX elements on the main screen. Populates the two TableViews and
   * initializes the appointment filtering radio buttons.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Initialize appointment table
    appointmentTableView.setItems(DBAppointment.getAllAppointments());
    appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    appointmentContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
    appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    appointmentStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
    appointmentEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
    appointmentCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

    // Initialize radio buttons
    filterToggleGroup = new ToggleGroup();
    this.allRadioButton.setToggleGroup(filterToggleGroup);
    this.weekRadioButton.setToggleGroup(filterToggleGroup);
    this.monthRadioButton.setToggleGroup(filterToggleGroup);
    this.allRadioButton.setSelected(true);

    // Initialize customer table
    customerTableView.setItems(DBCustomer.getAllCustomers());
    customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
    customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
    customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
  }
}
