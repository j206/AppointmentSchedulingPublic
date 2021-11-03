package View_Controller;

import Model.ReportCountsResult;
import Utility.DBReport;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
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

/** Controller for the "Appointments by Type / Month" scene. */
public class ReportCountsController implements Initializable {

  @FXML private Button cancelButton;
  @FXML private TableView<ReportCountsResult> appointmentTableView;
  @FXML private TableColumn<ReportCountsResult, String> appointmentTypeColumn;
  @FXML private TableColumn<ReportCountsResult, Integer> appointmentCountColumn;
  @FXML private ComboBox<String> monthComboBox;

  /** Displays all appointments for the selected contact. */
  public void monthComboBoxSelected() {
    ObservableList<ReportCountsResult> monthlyCount = FXCollections.observableArrayList();
    monthlyCount.clear();

    String monthString = monthComboBox.getValue();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM").withLocale(Locale.ENGLISH);
    TemporalAccessor accessor = formatter.parse(monthString);
    int selectedMonth = accessor.get(ChronoField.MONTH_OF_YEAR);

    if (monthString != null) {
      monthlyCount = DBReport.getMonthlyCount(selectedMonth);
      appointmentTableView.setItems(monthlyCount);
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
    ObservableList<String> months = FXCollections.observableArrayList();
    String[] monthNames = {
      "January",
      "February",
      "March",
      "April",
      "May",
      "June",
      "July",
      "August",
      "September",
      "October",
      "November",
      "December"
    };
    months.addAll(monthNames);

    monthComboBox.setPromptText("Select Month");
    monthComboBox.setItems(months);

    appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    appointmentCountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
  }
}
