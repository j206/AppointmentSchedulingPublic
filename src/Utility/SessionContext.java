package Utility;

import Model.User;
import java.util.Locale;

/** Class containing information about the current local application session. */
public class SessionContext {
  private static SessionContext sessionContext;

  private User currentUser;
  private Locale currentLocale;

  /** Default constructor. */
  public SessionContext(User currentUser, Locale currentLocale) {
    this.currentUser = currentUser;
    this.currentLocale = currentLocale;
  }

  /** Initializes an empty context. */
  public static void init() {
    sessionContext = new SessionContext(null, null);
  }

  /** Resets the current context to empty. This is identical to init in this implementation. */
  public static void resetContext() {
    sessionContext = new SessionContext(null, null);
  }

  /** Retrieves the current session context. */
  public static SessionContext getSessionContext() {
    return sessionContext;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
  }

  public Locale getCurrentLocale() {
    return currentLocale;
  }

  public void setCurrentLocale(Locale currentLocale) {
    this.currentLocale = currentLocale;
  }
}
