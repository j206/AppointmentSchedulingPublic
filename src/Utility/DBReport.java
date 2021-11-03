package Utility;

import Model.Appointment;
import Model.ReportCountsResult;
import Model.ReportDivisionResult;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Database interaction methods for reports. */
public class DBReport {

  private static final ObservableList<Appointment> contactAppointments =
      FXCollections.observableArrayList();
  private static final ObservableList<ReportCountsResult> monthlyCount =
      FXCollections.observableArrayList();
  private static final ObservableList<ReportDivisionResult> divisionCount =
      FXCollections.observableArrayList();

  /**
   * Retrieves all appointments for a given contact.
   *
   * @param selectedContactId ID of selected contact
   * @return ObservableList of appointment objects
   */
  public static ObservableList<Appointment> getContactAppointments(int selectedContactId) {
    try {
      String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ps.setInt(1, selectedContactId);

      ResultSet rs = ps.executeQuery();
      contactAppointments.clear();
      while (rs.next()) {
        int id = rs.getInt("Appointment_ID");
        int customerId = rs.getInt("Customer_ID");
        int userId = rs.getInt("User_ID");
        int contactId = rs.getInt("Contact_ID");
        String title = rs.getString("Title");
        String description = rs.getString("Description");
        String location = rs.getString("Location");
        String type = rs.getString("Type");
        LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
        LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
        LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
        String createdBy = rs.getString("Created_By");
        LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
        String lastUpdatedBy = rs.getString("Last_Updated_By");

        Appointment a =
            new Appointment(
                id,
                customerId,
                userId,
                contactId,
                title,
                description,
                location,
                type,
                start,
                end,
                createDate,
                createdBy,
                lastUpdate,
                lastUpdatedBy);
        contactAppointments.add(a);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return contactAppointments;
  }

  /**
   * Retrieves the number of appointments per type for a given month.
   *
   * @param selectedMonth month to retrieve appointments for
   * @return ReportCountsResult object with query results
   */
  public static ObservableList<ReportCountsResult> getMonthlyCount(int selectedMonth) {
    try {
      String sql =
          "SELECT Type, COUNT(*) AS 'Count' FROM appointments "
              + "WHERE MONTH(Start) = ? "
              + "GROUP BY Type";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ps.setInt(1, selectedMonth);

      ResultSet rs = ps.executeQuery();
      monthlyCount.clear();
      while (rs.next()) {
        String type = rs.getString("Type");
        int count = rs.getInt("Count");

        ReportCountsResult r = new ReportCountsResult(type, count);
        monthlyCount.add(r);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return monthlyCount;
  }

  /** Retrieves the number of customers per country for a given first-level division.
   *
   * @param selectedCountryId ID of country to retrieve first-level division data for*/
  public static ObservableList<ReportDivisionResult> getDivisionCount(int selectedCountryId) {
    try {
      String sql =
          "SELECT f.Division, COUNT(*) AS 'Count' FROM customers c "
              + "INNER JOIN first_level_divisions f ON c.Division_ID = f.Division_ID "
              + "WHERE f.COUNTRY_ID = ? "
              + "GROUP BY f.Division";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ps.setInt(1, selectedCountryId);

      ResultSet rs = ps.executeQuery();
      divisionCount.clear();
      while (rs.next()) {
        String division = rs.getString("Division");
        int count = rs.getInt("Count");

        ReportDivisionResult r = new ReportDivisionResult(division, count);
        divisionCount.add(r);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return divisionCount;
  }

  /**
   * Retrieves the next appointment within the next 15 minutes for the current user.
   *
   * @param currentUserId the currently logged in user
   * @return the next appointment within 15 minutes of LocalDateTime.now()
   */
  public static Appointment queryNextFifteenMinutes(int currentUserId) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      ZonedDateTime zonedNow = DateTimeUtils.convertLocalToZoned(LocalDateTime.now(), "UTC");
      String plusFifteenMinutes = zonedNow.plusMinutes(15).format(formatter);
      String now = zonedNow.format(formatter);

      String sql =
          "SELECT * FROM appointments " + "WHERE (Start >= ? && Start <= ?) " + "&& (User_ID = ?)";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ps.setString(1, now);
      ps.setString(2, plusFifteenMinutes);
      ps.setInt(3, currentUserId);

      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        int id = rs.getInt("Appointment_ID");
        int customerId = rs.getInt("Customer_ID");
        int userId = rs.getInt("User_ID");
        int contactId = rs.getInt("Contact_ID");
        String title = rs.getString("Title");
        String description = rs.getString("Description");
        String location = rs.getString("Location");
        String type = rs.getString("Type");
        LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
        LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
        LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
        String createdBy = rs.getString("Created_By");
        LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
        String lastUpdatedBy = rs.getString("Last_Updated_By");

        return new Appointment(
            id,
            customerId,
            userId,
            contactId,
            title,
            description,
            location,
            type,
            start,
            end,
            createDate,
            createdBy,
            lastUpdate,
            lastUpdatedBy);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }
}
