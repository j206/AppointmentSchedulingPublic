package Utility;

import Model.Appointment;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Database interaction methods for appointments. */
public class DBAppointment {

  private static final ObservableList<Appointment> allAppointments =
      FXCollections.observableArrayList();

  /**
   * Retrieves all appointments from the database. Populates and returns an ObservableList
   *  with all appointments.
   *
   * @return ObservableList of all appointment objects
   */
  public static ObservableList<Appointment> getAllAppointments() {
    try {
      String sql = "SELECT * FROM appointments";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      allAppointments.clear();
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
        allAppointments.add(a);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return allAppointments;
  }

  /**
   * Adds a new appointment to the database.
   *
   * @param newAppointment Appointment object to added to database
   */
  public static void addAppointment(Appointment newAppointment) {
    try {
      String sql =
          "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Created_By, "
              + "Last_Updated_By, Customer_ID, User_ID, Contact_ID) "
              + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

      ps.setString(1, newAppointment.getTitle());
      ps.setString(2, newAppointment.getDescription());
      ps.setString(3, newAppointment.getLocation());
      ps.setString(4, newAppointment.getType());
      ps.setTimestamp(5, Timestamp.valueOf(newAppointment.getStart()));
      ps.setTimestamp(6, Timestamp.valueOf(newAppointment.getEnd()));
      ps.setString(7, newAppointment.getCreatedBy());
      ps.setString(8, newAppointment.getLastUpdatedBy());
      ps.setInt(9, newAppointment.getCustomerId());
      ps.setInt(10, newAppointment.getUserId());
      ps.setInt(11, newAppointment.getContactId());

      ps.execute();
      DBQuery.checkRowsAffected(ps);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  /**
   * Updates an existing appointment in the database. Excluded are columns automatically handled by
   * the database.
   *
   * @param appointmentId ID of appointment to modify
   * @param title appointment title
   * @param description appointment description
   * @param location appointment location
   * @param type appointment type
   * @param start LocalDateTime of appointment start time
   * @param end LocalDateTime of appointment end time
   * @param lastUpdatedBy currently logged in user
   * @param customerId appointment customer ID
   * @param userId appointment user ID, not explicitly current user
   * @param contactId appointment contact ID
   */
  public static void modifyAppointment(
      int appointmentId,
      String title,
      String description,
      String location,
      String type,
      LocalDateTime start,
      LocalDateTime end,
      String lastUpdatedBy,
      int customerId,
      int userId,
      int contactId) {
    try {
      String sql =
          "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, "
              + "End = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? "
              + "WHERE Appointment_ID = ?";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

      ps.setString(1, title);
      ps.setString(2, description);
      ps.setString(3, location);
      ps.setString(4, type);
      ps.setTimestamp(5, Timestamp.valueOf(start));
      ps.setTimestamp(6, Timestamp.valueOf(end));
      ps.setString(7, lastUpdatedBy);
      ps.setInt(8, customerId);
      ps.setInt(9, userId);
      ps.setInt(10, contactId);

      ps.setInt(11, appointmentId);

      ps.execute();
      DBQuery.checkRowsAffected(ps);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  /**
   * Deletes an appointment from the database.
   *
   * @param appointmentId ID of appointment to delete
   */
  public static void deleteAppointment(int appointmentId) {
    try {
      String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
      ps.setInt(1, appointmentId);
      ps.execute();

      DBQuery.checkRowsAffected(ps);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
}
