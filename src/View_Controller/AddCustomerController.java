package View_Controller;

import Model.Country;
import Model.Customer;
import Model.Division;
import Utility.AlertMessage;
import Utility.DBCustomer;
import Utility.DBQuery;
import Utility.SessionContext;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Controller for the "Add Customer" scene. */
public class AddCustomerController implements Initializable {

  private ObservableList<Division> allDivisions = FXCollections.observableArrayList();
  private ObservableList<Country> allCountries = FXCollections.observableArrayList();
  @FXML private Button addCustomerButton;
  @FXML private Button cancelButton;
  @FXML private TextField customerNameTextField;
  @FXML private TextField customerAddressTextField;
  @FXML private TextField customerPostalCodeTextField;
  @FXML private TextField customerPhoneTextField;
  @FXML private ComboBox<Country> customerCountryComboBox;
  @FXML private ComboBox<Division> customerDivisionComboBox;

  /**
   * Saves new customer to database using inputted data.
   *
   * <p>First, elements are confirmed to be filled/selected by validateCustomerForm. If
   * filled/selected, the inputted data is parsed into variables.
   *
   * <p>If all data is validated, saves the new customer to the database, and closes the window.
   *
   * @param event button clicked event
   */
  public void addCustomerButtonClicked(ActionEvent event) throws IOException {
    if (validateCustomerForm()) {
      // Parse fields to appropriate types, assign to variables
      String name = customerNameTextField.getText().trim();
      String address = customerAddressTextField.getText().trim();
      String postalCode = customerPostalCodeTextField.getText().trim();
      String phone = customerPhoneTextField.getText().trim();
      int division = customerDivisionComboBox.getValue().getId();
      String currentUser = SessionContext.getSessionContext().getCurrentUser().getUsername();

      Customer newCustomer =
          new Customer(division, name, address, postalCode, phone, currentUser, currentUser);
      DBCustomer.addCustomer(newCustomer);
      AlertMessage.informationAlert(2, name, null);
      displayMainScreen(event);
    }
  }

  /**
   * Populates the first-level division ComboBox. Using the selected country, selects
   * the correct divisions to fill the drop-down box.
   */
  public void countryComboBoxSelected() {
    ObservableList<Division> selectedCountryDivisions = FXCollections.observableArrayList();

    Country selectedCountry = customerCountryComboBox.getValue();
    selectedCountryDivisions.clear();
    if (selectedCountry != null) {
      for (Division division : allDivisions) {
        if (division.getCountryId() == selectedCountry.getId()) {
          selectedCountryDivisions.add(division);
        }
      }
    }
    customerDivisionComboBox.setItems(selectedCountryDivisions);
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
  public boolean validateCustomerForm() {
    resetFieldsStyle();
    TextField[] textFields = {
      customerNameTextField,
      customerAddressTextField,
      customerPostalCodeTextField,
      customerPhoneTextField
    };

    // Check for empty fields
    for (TextField field : textFields) {
      if (field.getText().trim().isEmpty()) {
        AlertMessage.customerError(1, field);
        return false;
      }
    }

    // Check for empty ComboBoxes
    if (customerCountryComboBox.getValue() == null || customerDivisionComboBox.getValue() == null) {
      AlertMessage.customerError(2, null);
      return false;
    }
    return true;
  }

  /** Resets TextField borders to grey. */
  public void resetFieldsStyle() {
    customerNameTextField.setStyle("-fx-border-color: lightgrey");
    customerAddressTextField.setStyle("-fx-border-color: lightgrey");
    customerPostalCodeTextField.setStyle("-fx-border-color: lightgrey");
    customerPhoneTextField.setStyle("-fx-border-color: lightgrey");
  }

  /**
   * Initializes JavaFX elements. Queries database for necessary information and populates
   * ComboBoxes.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    resetFieldsStyle();

    allCountries = DBQuery.getAllCountries();
    allDivisions = DBQuery.getAllDivisions();

    customerCountryComboBox.setPromptText("Select Country");
    customerDivisionComboBox.setPromptText("-");
    customerCountryComboBox.setItems(allCountries);
  }
}
