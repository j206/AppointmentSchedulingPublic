package View_Controller;

import Model.Country;
import Model.ReportDivisionResult;
import Utility.DBQuery;
import Utility.DBReport;
import java.io.IOException;
import java.net.URL;
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

/** Controller for the "Customers by State or Province" scene. */
public class ReportDivisionController implements Initializable {

  @FXML private Button cancelButton;
  @FXML private TableView<ReportDivisionResult> divisionTableView;
  @FXML private TableColumn<ReportDivisionResult, String> divisionColumn;
  @FXML private TableColumn<ReportDivisionResult, Integer> countColumn;
  @FXML private ComboBox<Country> countryComboBox;

  /** Displays division count for the selected country. */
  public void countryComboBoxSelected() {
    ObservableList<ReportDivisionResult> divisionCount = FXCollections.observableArrayList();
    divisionCount.clear();

    Country selectedCountry = countryComboBox.getValue();

    if (selectedCountry != null) {
      divisionCount = DBReport.getDivisionCount(selectedCountry.getId());
      divisionTableView.setItems(divisionCount);
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
    ObservableList<Country> allCountries = DBQuery.getAllCountries();

    countryComboBox.setPromptText("Select Country");
    countryComboBox.setItems(allCountries);

    divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
    countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
  }

}
