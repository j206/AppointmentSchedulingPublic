package Utility;

import Model.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Database interaction methods for customers. */
public class DBCustomer {

  private static final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

  /**
   * Retrieves all customers from the database. Populates and returns an ObservableList with all
   * customers.
   *
   * @return ObservableList of all customer objects
   */
  public static ObservableList<Customer> getAllCustomers() {
    try {
      String sql = "SELECT * FROM customers";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      allCustomers.clear();
      while (rs.next()) {
        int id = rs.getInt("Customer_ID");
        int divisionId = rs.getInt("Division_ID");
        String name = rs.getString("Customer_Name");
        String address = rs.getString("Address");
        String postalCode = rs.getString("Postal_Code");
        String phone = rs.getString("Phone");
        String createDate = rs.getString("Create_Date");
        String createdBy = rs.getString("Created_By");
        String lastUpdate = rs.getString("Last_Update");
        String lastUpdatedBy = rs.getString("Last_Updated_By");

        Customer c =
            new Customer(
                id,
                divisionId,
                name,
                address,
                postalCode,
                phone,
                createDate,
                createdBy,
                lastUpdate,
                lastUpdatedBy);
        allCustomers.add(c);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return allCustomers;
  }

  /**
   * Adds a new customer to the database.
   *
   * @param newCustomer Customer object to added to database
   */
  public static void addCustomer(Customer newCustomer) {
    try {
      String sql =
          "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Created_By, "
              + "Last_Updated_By, Division_ID) "
              + "VALUES(?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

      ps.setString(1, newCustomer.getName());
      ps.setString(2, newCustomer.getAddress());
      ps.setString(3, newCustomer.getPostalCode());
      ps.setString(4, newCustomer.getPhone());
      ps.setString(5, newCustomer.getCreatedBy());
      ps.setString(6, newCustomer.getLastUpdatedBy());
      ps.setInt(7, newCustomer.getDivisionId());

      ps.execute();
      DBQuery.checkRowsAffected(ps);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  /**
   * Updates an existing customer in the database. Excluded are columns automatically handled by the
   * database.
   *
   * @param customerId ID of customer to modify
   * @param name customer name
   * @param address customer address
   * @param postalCode customer postal code
   * @param phone customer phone number
   * @param lastUpdatedBy currently logged in user
   * @param divisionId customer first-division ID
   */
  public static void modifyCustomer(
      int customerId,
      String name,
      String address,
      String postalCode,
      String phone,
      String lastUpdatedBy,
      int divisionId) {
    try {
      String sql =
          "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, "
              + "Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

      ps.setString(1, name);
      ps.setString(2, address);
      ps.setString(3, postalCode);
      ps.setString(4, phone);
      ps.setString(5, lastUpdatedBy);
      ps.setInt(6, divisionId);

      ps.setInt(7, customerId);

      ps.execute();
      DBQuery.checkRowsAffected(ps);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  /**
   * Deletes an customer from the database. Deletes any relevant appointments first to maintain
   * referential integrity.
   *
   * @param customerId ID of customer to delete
   */
  public static void deleteCustomer(int customerId) {
    try {
      String deleteAppointmentSql = "DELETE FROM appointments WHERE Customer_ID = ?";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(deleteAppointmentSql);
      ps.setInt(1, customerId);
      ps.execute();

      String deleteCustomerSql = "DELETE FROM customers WHERE Customer_ID = ?";
      ps = DBConnection.getConnection().prepareStatement(deleteCustomerSql);
      ps.setInt(1, customerId);
      ps.execute();

      DBQuery.checkRowsAffected(ps);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
}
