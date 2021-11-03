package Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Class containing database connection methods. */
public class DBConnection {
  // JDBC URL parts
  private static final String PROTOCOL = "jdbc";
  private static final String VENDOR = ":mysql:";
  private static final String HOSTNAME = "hostname";
  private static final String DBNAME = "dbname";

  // JDBC URL
  private static final String JDBCURL = PROTOCOL + VENDOR + HOSTNAME + DBNAME;

  // Driver and connection interface reference
  private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

  // Credentials
  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private static Connection CONN = null;

  /** Starts persistent database connection. */
  public static void startConnection() {
    try {
      Class.forName(DRIVER);
      CONN = DriverManager.getConnection(JDBCURL, USERNAME, PASSWORD);
      System.out.println("Database connection successful!");
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns active connection without reconnecting.
   *
   * @return Connection currently in use
   */
  public static Connection getConnection() {
    return CONN;
  }

  /** Ends database connection. Called upon platform exit. */
  public static void closeConnection() {
    try {
      CONN.close();
      System.out.println("Database connection closed!");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
