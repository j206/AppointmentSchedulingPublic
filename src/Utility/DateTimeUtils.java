package Utility;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.ComboBox;

/** Utility class for date and time related operations. */
public class DateTimeUtils {

  /**
   * Parses separate hour, minute, and AM/PM values into a LocalTime object. Adjusts values for
   * AM/PM discrepancies.
   *
   * @param hour user-selected hour value
   * @param minute user-selected minute value
   * @param amPm user-selected AM/PM value
   * @return LocalTime of parsed values
   */
  public static LocalTime parseTimeInput(
      ComboBox<Integer> hour, ComboBox<String> minute, ComboBox<String> amPm) {
    String paddedHour = String.format("%02d", hour.getValue());
    String combinedHourMinute = paddedHour + minute.getValue();
    LocalTime parsedTime = LocalTime.parse(combinedHourMinute);

    // Adjust for 12AM/12PM
    if (parsedTime.getHour() == 12) {
      if (amPm.getValue().equals("A.M.")) {
        LocalTime amPmParsedTime = parsedTime.plusHours(12);
        return amPmParsedTime;
      } else return parsedTime;
    }

    // Adjust for all other values
    if (amPm.getValue().equals("P.M.")) {
      LocalTime amPmParsedTime = parsedTime.plusHours(12);
      return amPmParsedTime;
    }
    return parsedTime;
  }

  /**
   * Calls appointment time validation methods. Returns true if all appointment time validation
   * checks pass. Also validates that start time is before end time.
   *
   * @param start user-selected start of appointment time
   * @param end user-selected end of appointment time
   * @param customerId ID of user-selected customer
   * @param selectedAppointmentId ID of user-selected appointment, if modifying appointment
   * @return true if valid appointment time, false if invalid
   */
  public static boolean validateAppointmentDateTime(
      LocalDateTime start, LocalDateTime end, int customerId, int selectedAppointmentId) {
    if (!start.isBefore(end)) {
      AlertMessage.appointmentError(8, null);
      return false;
    }

    if (!confirmWithinBusinessHours(start, end)) {
      return false;
    }

    if (!confirmCustomerTimeConflict(start, end, customerId, selectedAppointmentId)) {
      return false;
    }
    return true;
  }

  /**
   * Confirms appointment time is within business hours. Business hours are defined as 08:00 to
   * 22:00 EST, including weekends. Converts inputted times to America/New_York timezone for
   * comparison.
   *
   * @param start user-selected start of appointment time
   * @param end user-selected end of appointment time
   * @return true if appointment time is within business hours, false if not
   */
  public static boolean confirmWithinBusinessHours(LocalDateTime start, LocalDateTime end) {
    ZonedDateTime easternStart = convertLocalToZoned(start, "America/New_York");
    ZonedDateTime easternEnd = convertLocalToZoned(end, "America/New_York");

    if (easternStart.getHour() < 8 || easternEnd.getHour() > 21) {
      AlertMessage.appointmentError(9, null);
      return false;
    }
    return true;
  }

  /**
   * Confirms appointment time does not conflict with another for the same customer. Converts
   * inputted times to UTC, queries DB for other appointments that would overlap, and returns false
   * if result set is not 0.
   *
   * @param start user-selected start of appointment time
   * @param end user-selected end of appointment time
   * @param customerId ID of user-selected customer
   * @param selectedAppointmentId ID of user-selected appointment, if modifying appointment
   * @return true if time does not conflict, false if there is a conflict
   */
  public static boolean confirmCustomerTimeConflict(
      LocalDateTime start, LocalDateTime end, int customerId, int selectedAppointmentId) {
    ZonedDateTime utcStartZonedDateTime = convertLocalToZoned(start, "UTC");
    ZonedDateTime utcEndZonedDateTime = convertLocalToZoned(end, "UTC");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String utcStart = utcStartZonedDateTime.format(formatter);
    String utcEnd = utcEndZonedDateTime.format(formatter);

    if (DBQuery.queryCustomerTimeConflict(utcStart, utcEnd, customerId, selectedAppointmentId)) {
      AlertMessage.appointmentError(10, null);
      return false;
    }
    return true;
  }

  /**
   * Helper method to convert LocalDateTime to ZonedDateTime.
   *
   * @param localDateTime time to be converted
   * @param zoneId target timezone ID
   */
  public static ZonedDateTime convertLocalToZoned(LocalDateTime localDateTime, String zoneId) {
    ZonedDateTime zonedDateTime =
        localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(zoneId));

    return zonedDateTime;
  }
}
