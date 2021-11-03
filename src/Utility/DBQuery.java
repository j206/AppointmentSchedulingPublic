package Utility;

import Model.Contact;
import Model.Country;
import Model.Division;
import Model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** General-utility database interaction methods. */
public class DBQuery {

  private static final ObservableList<Country> allCountries = FXCollections.observableArrayList();
  private static final ObservableList<Division> allDivisions = FXCollections.observableArrayList();
  private static final ObservableList<Contact> allContacts = FXCollections.observableArrayList();
  private static final ObservableList<User> allUsers = FXCollections.observableArrayList();

  /**
   * Authenticates inputted login credentials. Returns a User object if authentication
   * against the database is successful. Otherwise, returns null.
   *
   * @param username user-inputted username
   * @param password user-inputted password
   */
  public static User authenticateUser(String username, String password) {
    try {
      String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ps.setString(1, username);
      ps.setString(2, password);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        int id = rs.getInt("User_ID");
        String user = rs.getString("User_Name");
        return new User(id, user);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  /**
   * Retrieves all contacts from the database. Populates and returns an ObservableList
   *  with all contacts.
   *
   * @return ObservableList of all contact objects
   */
  public static ObservableList<Contact> getAllContacts() {
    try {
      String sql = "SELECT * FROM contacts";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      allContacts.clear();
      while (rs.next()) {
        int id = rs.getInt("Contact_ID");
        String contactName = rs.getString("Contact_Name");
        String email = rs.getString("Email");

        Contact c = new Contact(id, contactName, email);
        allContacts.add(c);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return allContacts;
  }

  /**
   * Retrieves all countries from the database. Populates and returns an ObservableList
   *  with all countries.
   *
   * @return ObservableList of all country objects
   */
  public static ObservableList<Country> getAllCountries() {
    try {
      String sql = "SELECT * FROM countries";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      allCountries.clear();
      while (rs.next()) {
        int id = rs.getInt("Country_ID");
        String name = rs.getString("Country");

        Country c = new Country(id, name);
        allCountries.add(c);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return allCountries;
  }

  /**
   * Retrieves all first-level divisions from the database. Populates and returns an
   * ObservableList
   *  with all first-level divisions.
   *
   * @return ObservableList of all first-level division objects
   */
  public static ObservableList<Division> getAllDivisions() {
    try {
      String sql = "SELECT * FROM first_level_divisions";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      allDivisions.clear();
      while (rs.next()) {
        int id = rs.getInt("Division_ID");
        int countryId = rs.getInt("COUNTRY_ID");
        String divisionName = rs.getString("Division");
        String createDate = rs.getString("Create_Date");
        String createdBy = rs.getString("Created_By");
        String lastUpdate = rs.getString("Last_Update");
        String lastUpdatedBy = rs.getString("Last_Updated_By");

        Division d =
            new Division(
                id, countryId, divisionName, createDate, createdBy, lastUpdate, lastUpdatedBy);
        allDivisions.add(d);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return allDivisions;
  }

  /**
   * Retrieves all users from the database. Populates and returns an ObservableList
   *  with all users.
   *
   * @return ObservableList of all user objects
   */
  public static ObservableList<User> getAllUsers() {
    try {
      String sql = "SELECT User_ID, User_Name FROM users";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      allUsers.clear();
      while (rs.next()) {
        int id = rs.getInt("User_ID");
        String username = rs.getString("User_Name");

        User u = new User(id, username);
        allUsers.add(u);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return allUsers;
  }

  /**
   * Retrieves a Country object given a specific Division.
   *
   * @param divisionId user-selected first-level division ID
   * @return overarching Country object related to first-level division
   */
  public static Country getCountryFromDivision(int divisionId) {
    try {
      String sql =
          "SELECT countries.Country_ID, countries.Country FROM countries "
              + "INNER JOIN first_level_divisions "
              + "ON countries.Country_ID = first_level_divisions.COUNTRY_ID "
              + "WHERE Division_ID = ?";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ps.setInt(1, divisionId);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        int id = rs.getInt("Country_ID");
        String name = rs.getString("Country");

        return new Country(id, name);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  /**
   * Queries for conflicting appointment times for a customer. Called in
   * DateTimeUtils.confirmCustomerTimeConflict.
   *
   * @param startNew start time of appointment to be added
   * @param endNew end time of appointment to be added
   * @param customerId ID of customer to check conflicts for
   * @param selectedAppointmentId ID of selected appointment, if modifying
   * @return returns true if conflict exists, false if no conflict
   */
  public static boolean queryCustomerTimeConflict(
      String startNew, String endNew, int customerId, int selectedAppointmentId) {
    try {
      String sql =
          "SELECT c.Customer_ID, Start, End FROM appointments a "
              + "INNER JOIN customers c ON c.Customer_ID = a.Customer_ID "
              + "WHERE c.Customer_ID = ? "
              + "&& NOT (Start >= ? OR End <= ?) "
              + "&& NOT (a.Appointment_ID = ?)";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ps.setInt(1, customerId);
      ps.setString(2, endNew);
      ps.setString(3, startNew);
      ps.setInt(4, selectedAppointmentId);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        return true;
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return false;
  }

  /**
   * Checks number of rows in database affected by an operation. Helper method that prints to system
   * log.
   *
   * @param ps PreparedStatement for a given database query
   */
  public static void checkRowsAffected(PreparedStatement ps) {
    try {
      if (ps.getUpdateCount() > 0) System.out.println(ps.getUpdateCount() + " rows(s) affected");
      else System.out.println("No changes made");
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
}
