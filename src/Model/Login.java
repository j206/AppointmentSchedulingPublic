package Model;

/** Object model for user login attempts. */
public class Login {
  private String username, authenticated, attemptedAt;

  public Login(String username, String authenticated, String attemptedAt) {
    this.username = username;
    this.authenticated = authenticated;
    this.attemptedAt = attemptedAt;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getAuthenticated() {
    return authenticated;
  }

  public void setAuthenticated(String authenticated) {
    this.authenticated = authenticated;
  }

  public String getAttemptedAt() {
    return attemptedAt;
  }

  public void setAttemptedAt(String attemptedAt) {
    this.attemptedAt = attemptedAt;
  }
}
